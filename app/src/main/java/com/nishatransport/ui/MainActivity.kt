package com.nishatransport.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nishatransport.NishaTransportApp
import com.nishatransport.R
import com.nishatransport.databinding.ActivityMainBinding
import com.nishatransport.ui.analytics.AnalyticsFragment
import com.nishatransport.ui.backup.BackupFragment
import com.nishatransport.ui.dashboard.DashboardFragment
import com.nishatransport.ui.loads.history.LoadHistoryFragment
import com.nishatransport.ui.reports.ReportsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val dashboardFragment = DashboardFragment()
    private val loadHistoryFragment = LoadHistoryFragment()
    private val analyticsFragment = AnalyticsFragment()
    private val reportsFragment = ReportsFragment()
    private val backupFragment = BackupFragment()

    private var activeFragment: Fragment = dashboardFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Apply saved theme
        (application as NishaTransportApp).preferenceManager.applyCurrentTheme()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupFragments()
        setupBottomNavigation()
    }

    private fun setupFragments() {
        supportFragmentManager.beginTransaction().apply {
            add(R.id.fragment_container, dashboardFragment, "dashboard")
            add(R.id.fragment_container, loadHistoryFragment, "loads").hide(loadHistoryFragment)
            add(R.id.fragment_container, analyticsFragment, "analytics").hide(analyticsFragment)
            add(R.id.fragment_container, reportsFragment, "reports").hide(reportsFragment)
            add(R.id.fragment_container, backupFragment, "backup").hide(backupFragment)
        }.commit()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.nav_dashboard -> dashboardFragment
                R.id.nav_loads -> loadHistoryFragment
                R.id.nav_analytics -> analyticsFragment
                R.id.nav_reports -> reportsFragment
                R.id.nav_backup -> backupFragment
                else -> return@setOnItemSelectedListener false
            }
            switchFragment(fragment)
            true
        }
    }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .hide(activeFragment)
            .show(fragment)
            .commit()
        activeFragment = fragment
    }

    fun navigateTo(index: Int) {
        val itemId = when (index) {
            0 -> R.id.nav_dashboard
            1 -> R.id.nav_loads
            2 -> R.id.nav_analytics
            3 -> R.id.nav_reports
            4 -> R.id.nav_backup
            else -> R.id.nav_dashboard
        }
        binding.bottomNavigation.selectedItemId = itemId
    }
}
