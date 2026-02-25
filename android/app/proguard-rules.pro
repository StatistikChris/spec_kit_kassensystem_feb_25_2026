# Bar-Kassensystem — ProGuard / R8 Rules
# GoBD: domain logic must not be obfuscated to the point of being unaudit-able.
# Keep all domain model classes fully named for crash-log readability.

# ---------- Domain layer: keep class names + members ----------
-keep class de.barpos.kassensystem.domain.** { *; }

# ---------- Room entities & DAOs ----------
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao interface * { *; }
-keepclassmembers class * extends androidx.room.RoomDatabase { *; }

# ---------- Hilt / Dagger ----------
-keep class dagger.** { *; }
-keep class javax.inject.** { *; }
-keep @dagger.hilt.android.HiltAndroidApp class *
-keep @dagger.hilt.android.AndroidEntryPoint class *

# ---------- Retrofit + Gson ----------
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
-keep class retrofit2.** { *; }
-keep,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# ---------- WorkManager ----------
-keep class * extends androidx.work.Worker
-keep class * extends androidx.work.CoroutineWorker
-keep class * extends androidx.work.ListenableWorker {
    public <init>(android.content.Context, androidx.work.WorkerParameters);
}

# ---------- Strip verbose logging in release ----------
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
}

# ---------- Keep DataStore / EncryptedSharedPreferences ----------
-keep class androidx.datastore.** { *; }
-keep class androidx.security.crypto.** { *; }

# ---------- ThreeTenBP ----------
-keep class org.threeten.bp.** { *; }
