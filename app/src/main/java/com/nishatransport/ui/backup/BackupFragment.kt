package com.nishatransport.ui.backup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.DriveScopes
import com.nishatransport.NishaTransportApp
import com.nishatransport.databinding.FragmentBackupBinding
import com.nishatransport.utils.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class BackupFragment : Fragment() {

    private var _binding: FragmentBackupBinding? = null
    private val binding get() = _binding!!

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var prefManager: PreferenceManager

    private var backupFiles: List<BackupFile> = emptyList()

    companion object {
        private const val RC_SIGN_IN = 1001
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBackupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefManager = (requireActivity().application as NishaTransportApp).preferenceManager

        setupGoogleSignIn()
        setupBackupFrequencySelector()
        setupClickListeners()
        updateUI()
    }

    private fun setupGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(Scope(DriveScopes.DRIVE_FILE), Scope(DriveScopes.DRIVE_APPDATA))
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun setupBackupFrequencySelector() {
        // Set current selection
        when (prefManager.backupFrequency) {
            PreferenceManager.BACKUP_DAILY -> binding.rbDaily.isChecked = true
            PreferenceManager.BACKUP_WEEKLY -> binding.rbWeekly.isChecked = true
            PreferenceManager.BACKUP_MANUAL -> binding.rbManual.isChecked = true
        }

        binding.rgBackupFrequency.setOnCheckedChangeListener { _, checkedId ->
            val freq = when (checkedId) {
                binding.rbDaily.id -> PreferenceManager.BACKUP_DAILY
                binding.rbWeekly.id -> PreferenceManager.BACKUP_WEEKLY
                else -> PreferenceManager.BACKUP_MANUAL
            }
            prefManager.backupFrequency = freq

            if (freq == PreferenceManager.BACKUP_MANUAL) {
                BackupWorker.cancelBackup(requireContext())
            } else {
                BackupWorker.scheduleBackup(requireContext(), freq)
            }

            Toast.makeText(requireContext(),
                "Backup schedule updated: ${freq.replaceFirstChar { it.uppercase() }}",
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupClickListeners() {
        // Sign in/out
        binding.btnSignIn.setOnClickListener {
            if (GoogleSignIn.getLastSignedInAccount(requireContext()) != null) {
                signOut()
            } else {
                signIn()
            }
        }

        // Manual backup now
        binding.btnBackupNow.setOnClickListener {
            if (GoogleSignIn.getLastSignedInAccount(requireContext()) == null) {
                Toast.makeText(requireContext(), "Please sign in to Google first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            performBackup()
        }

        // View & restore backups
        binding.btnViewBackups.setOnClickListener {
            loadAndShowBackups()
        }

        // Auto backup toggle
        binding.switchAutoBackup.isChecked = prefManager.isAutoBackupEnabled
        binding.switchAutoBackup.setOnCheckedChangeListener { _, isChecked ->
            prefManager.isAutoBackupEnabled = isChecked
            if (isChecked) {
                BackupWorker.scheduleBackup(requireContext(), prefManager.backupFrequency)
            } else {
                BackupWorker.cancelBackup(requireContext())
            }
        }
    }

    private fun signIn() {
        startActivityForResult(googleSignInClient.signInIntent, RC_SIGN_IN)
    }

    private fun signOut() {
        googleSignInClient.signOut().addOnCompleteListener {
            prefManager.googleAccountEmail = null
            updateUI()
            Toast.makeText(requireContext(), "Signed out from Google", Toast.LENGTH_SHORT).show()
        }
    }

    private fun performBackup() {
        binding.btnBackupNow.isEnabled = false
        binding.progressBackup.visibility = View.VISIBLE
        binding.tvBackupStatus.text = "Backing up..."

        lifecycleScope.launch {
            val result = DriveBackupManager.backupToGoogleDrive(requireContext())
            binding.progressBackup.visibility = View.GONE
            binding.btnBackupNow.isEnabled = true

            when (result) {
                is BackupResult.Success -> {
                    prefManager.lastBackupTime = System.currentTimeMillis()
                    prefManager.lastBackupStatus = "✅ ${result.fileName}"
                    prefManager.driveFileId = result.fileId
                    updateUI()
                    Toast.makeText(requireContext(), "Backup successful! ✅", Toast.LENGTH_LONG).show()
                }
                is BackupResult.Failure -> {
                    prefManager.lastBackupStatus = "❌ Failed"
                    binding.tvBackupStatus.text = "❌ ${result.message}"
                    Toast.makeText(requireContext(), "Backup failed: ${result.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun loadAndShowBackups() {
        binding.progressBackup.visibility = View.VISIBLE

        lifecycleScope.launch {
            backupFiles = DriveBackupManager.listBackups(requireContext())
            binding.progressBackup.visibility = View.GONE

            if (backupFiles.isEmpty()) {
                Toast.makeText(requireContext(), "No backups found on Google Drive", Toast.LENGTH_SHORT).show()
                return@launch
            }

            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val items = backupFiles.map { file ->
                "📦 ${dateFormat.format(Date(file.createdTime))}\n   ${file.name} (${file.sizeBytes / 1024} KB)"
            }.toTypedArray()

            AlertDialog.Builder(requireContext())
                .setTitle("Restore from Google Drive")
                .setMessage("Select a backup to restore. This will replace all current data!")
                .setItems(items) { _, index ->
                    confirmRestore(backupFiles[index])
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun confirmRestore(backup: BackupFile) {
        AlertDialog.Builder(requireContext())
            .setTitle("⚠️ Confirm Restore")
            .setMessage("This will REPLACE all current data with the backup from:\n\n${backup.name}\n\nThis cannot be undone. Are you sure?")
            .setPositiveButton("Yes, Restore") { _, _ ->
                performRestore(backup.id)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun performRestore(fileId: String) {
        binding.progressBackup.visibility = View.VISIBLE
        binding.tvBackupStatus.text = "Restoring..."

        lifecycleScope.launch {
            val result = DriveBackupManager.restoreFromGoogleDrive(requireContext(), fileId)
            binding.progressBackup.visibility = View.GONE

            when (result) {
                is RestoreResult.Success -> {
                    updateUI()
                    AlertDialog.Builder(requireContext())
                        .setTitle("✅ Restore Complete")
                        .setMessage("Data restored successfully. The app will restart to apply changes.")
                        .setPositiveButton("Restart") { _, _ ->
                            // Restart app
                            val intent = requireActivity().packageManager
                                .getLaunchIntentForPackage(requireActivity().packageName)
                            intent?.let {
                                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(it)
                                requireActivity().finish()
                            }
                        }
                        .setCancelable(false)
                        .show()
                }
                is RestoreResult.Failure -> {
                    Toast.makeText(requireContext(), "Restore failed: ${result.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun updateUI() {
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())

        if (account != null) {
            binding.tvAccountStatus.text = "✅ Signed in as: ${account.email}"
            binding.btnSignIn.text = "Sign Out"
            binding.btnBackupNow.isEnabled = true
            binding.btnViewBackups.isEnabled = true
            prefManager.googleAccountEmail = account.email
        } else {
            binding.tvAccountStatus.text = "❌ Not signed in"
            binding.btnSignIn.text = "Sign in with Google"
            binding.btnBackupNow.isEnabled = false
            binding.btnViewBackups.isEnabled = false
        }

        // Last backup info
        if (prefManager.lastBackupTime > 0) {
            val dateStr = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                .format(Date(prefManager.lastBackupTime))
            binding.tvLastBackupDate.text = "Last backup: $dateStr"
            binding.tvBackupStatus.text = prefManager.lastBackupStatus
        } else {
            binding.tvLastBackupDate.text = "Last backup: Never"
            binding.tvBackupStatus.text = "No backup found"
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            try {
                val account: GoogleSignInAccount = GoogleSignIn.getSignedInAccountFromIntent(data)
                    .getResult(ApiException::class.java)
                prefManager.googleAccountEmail = account.email
                updateUI()
                Toast.makeText(requireContext(), "Signed in as ${account.email}", Toast.LENGTH_SHORT).show()

                // Schedule backup after sign-in if auto backup enabled
                if (prefManager.isAutoBackupEnabled && prefManager.backupFrequency != PreferenceManager.BACKUP_MANUAL) {
                    BackupWorker.scheduleBackup(requireContext(), prefManager.backupFrequency)
                }
            } catch (e: ApiException) {
                Toast.makeText(requireContext(), "Sign-in failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
