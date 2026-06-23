# Nisha Transport - ProGuard Rules

# Keep Room entities
-keep class com.nishatransport.data.local.entity.** { *; }
-keep class com.nishatransport.data.local.dao.** { *; }

# Keep ViewModels
-keep class com.nishatransport.ui.**.** extends androidx.lifecycle.ViewModel { *; }

# MPAndroidChart
-keep class com.github.mikephil.charting.** { *; }

# Apache POI
-keep class org.apache.poi.** { *; }
-keep class org.openxmlformats.** { *; }
-dontwarn org.apache.xmlbeans.**
-dontwarn javax.xml.**
-dontwarn org.apache.poi.**

# iTextPDF
-keep class com.itextpdf.** { *; }
-dontwarn com.itextpdf.**

# Google Drive
-keep class com.google.api.** { *; }
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.**

# Kotlin coroutines
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
