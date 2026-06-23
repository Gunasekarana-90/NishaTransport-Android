# 🚛 Nisha Transport — Android App

A complete offline-first Android application for tracking lorry transport loads, expenses, profits,
and business performance. Built with Kotlin, MVVM, Room Database, and Material Design 3.

---

## 📦 Technology Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| Architecture | MVVM (Model-View-ViewModel) |
| Database | Room (SQLite) |
| UI | Material Design 3 |
| Charts | MPAndroidChart |
| PDF Export | iTextPDF |
| Excel Export | Apache POI |
| Cloud Backup | Google Drive API |
| Backup Encryption | AES-256 (PBKDF2WithHmacSHA256) |
| Scheduled Backup | WorkManager |
| Navigation | Fragment-based with BottomNavigationView |
| DI | Manual (App-level singletons) |

---

## 🗂 Project Structure

```
app/src/main/java/com/nishatransport/
├── NishaTransportApp.kt            ← Application class
├── data/
│   ├── local/
│   │   ├── NishaDatabase.kt        ← Room database
│   │   ├── entity/
│   │   │   └── Load.kt             ← Load entity
│   │   └── dao/
│   │       └── LoadDao.kt          ← All DB queries
│   └── repository/
│       └── LoadRepository.kt       ← Data repository
├── ui/
│   ├── MainActivity.kt             ← Bottom nav host
│   ├── dashboard/
│   │   ├── DashboardFragment.kt
│   │   ├── DashboardViewModel.kt
│   │   └── RecentLoadsAdapter.kt
│   ├── loads/
│   │   ├── add/
│   │   │   ├── AddLoadActivity.kt
│   │   │   └── AddLoadViewModel.kt
│   │   ├── history/
│   │   │   ├── LoadHistoryFragment.kt
│   │   │   ├── LoadHistoryViewModel.kt
│   │   │   └── LoadsAdapter.kt
│   │   └── detail/
│   │       └── LoadDetailActivity.kt
│   ├── analytics/
│   │   ├── AnalyticsFragment.kt
│   │   └── AnalyticsViewModel.kt
│   ├── reports/
│   │   ├── ReportsFragment.kt
│   │   └── ReportsViewModel.kt
│   └── backup/
│       └── BackupFragment.kt
└── utils/
    ├── Utils.kt                    ← DateUtils, CurrencyUtils
    ├── PreferenceManager.kt        ← App preferences/settings
    ├── ViewModelFactory.kt         ← ViewModel factory
    ├── PdfExporter.kt              ← PDF generation
    ├── ExcelExporter.kt            ← Excel (.xlsx) generation
    ├── DriveBackupManager.kt       ← Google Drive backup
    ├── BackupWorker.kt             ← WorkManager background backup
    └── Extensions.kt              ← Kotlin extension functions
```

---

## 🚀 Setup Instructions

### Step 1 — Prerequisites

- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17
- Android SDK 34
- Google account (for Drive backup)

### Step 2 — Open the Project

1. Open Android Studio
2. File → Open → Select the `NishaTransport` folder
3. Wait for Gradle sync to complete

### Step 3 — Configure Google Drive Backup

1. Go to [Google Cloud Console](https://console.cloud.google.com)
2. Create a new project named "NishaTransport"
3. Enable **Google Drive API**
4. Create **OAuth 2.0 credentials** → Android app
5. Add your app's SHA-1 fingerprint:
   ```
   keytool -keystore ~/.android/debug.keystore -list -v
   ```
6. Download `google-services.json`
7. Place it at: `NishaTransport/app/google-services.json`

### Step 4 — Build & Run

```bash
./gradlew assembleDebug
```

Or press **Run ▶** in Android Studio.

---

## 📱 App Screens

### 1. Dashboard
- Today's Revenue & Profit cards
- This Month Revenue & Profit cards
- This Year Profit card
- Total Loads count
- Recent 5 loads
- Quick action buttons: Add Load, Analytics, Export

### 2. Load History
- Search by location / vehicle number
- Filter chips: All, This Month, This Year, Pick Month
- Each entry shows: Date, Route, Revenue, Expense, Profit
- Swipe or tap Edit/Delete buttons

### 3. Add / Edit Load
- Date picker
- From & To location (required)
- Vehicle number & Notes (optional)
- Load Price (required)
- 7 expense fields (all default to ₹0)
- Live calculation: Total Expense & Net Profit

### 4. Load Detail
- Full breakdown of revenue and each expense
- Net Profit displayed prominently
- Edit and Delete buttons

### 5. Analytics
- Select Month and Year
- Monthly Summary: Revenue, Expense, Profit, Load Count
- Yearly Summary: Revenue, Expense, Profit, Load Count
- 4 Charts:
  - Revenue Line Chart (12 months)
  - Expense Line Chart (12 months)
  - Profit Line Chart (12 months)
  - Grouped Bar Chart (Monthly Comparison)

### 6. Reports / Export
- Select report type: Monthly, Yearly, Date Range, All Time
- Export as PDF (professional report with iTextPDF)
- Export as Excel (.xlsx with Apache POI)
- Share via WhatsApp, Email, Drive, etc.

### 7. Backup & Restore
- Sign in with Google
- Auto backup: Daily / Weekly / Manual
- Backup Now button
- View all backups on Drive
- One-tap restore from any previous backup
- AES-256 encryption on all backup files

---

## 🔐 Data Protection

All backup files are encrypted before upload:

```
Encryption: AES/CBC/PKCS5Padding
Key Derivation: PBKDF2WithHmacSHA256 (65536 iterations, 256-bit key)
Salt: 16 bytes (random per backup)
IV: 16 bytes (random per backup)
File format: salt(16) + iv(16) + encrypted_data
```

---

## 💾 Database Schema

### `loads` table

| Column | Type | Description |
|---|---|---|
| id | INTEGER PK | Auto-generated |
| date | INTEGER | Epoch milliseconds |
| fromLocation | TEXT | Origin |
| toLocation | TEXT | Destination |
| vehicleNumber | TEXT | Optional |
| notes | TEXT | Optional |
| loadPrice | REAL | Total revenue |
| loadingCharge | REAL | Loading expense |
| dieselCost | REAL | Diesel expense |
| policeExpense | REAL | Police expense |
| tollFee | REAL | Toll expense |
| driverCharge | REAL | Driver expense |
| unloadingCharge | REAL | Unloading expense |
| otherExpense | REAL | Other expenses |
| totalExpense | REAL | Sum of all expenses |
| profit | REAL | loadPrice - totalExpense |
| createdAt | INTEGER | Record creation time |
| updatedAt | INTEGER | Last update time |

---

## ⚡ Performance

- Supports 10,000+ load records
- Efficient Room queries with indexes
- Flow-based reactive UI updates
- Coroutines for all async operations
- RecyclerView with DiffUtil for smooth lists

---

## 🌙 Dark Mode

Toggle from system settings or app preferences.

---

## 📞 Support

This app is built for personal use by a single lorry owner.
All data stays on the device. Google Drive backup is optional but recommended.

---

*Built with ❤️ for Nisha Transport*
