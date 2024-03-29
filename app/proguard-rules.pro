# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\B.E.L\Documents\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}



# Google Play Services library
-keep class * extends java.util.ListResourceBundle {
    protected Object[] getContent();
}


-keep class com.ironsource.mobilcore.*{ *; }

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *

-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}


-dontwarn android.webkit.JavascriptInterface
-dontwarn com.flurry.**
-dontwarn com.squareup.okhttp.**




-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keep class com.flurry.** { *; }
-dontwarn com.revmob.**
-dontwarn com.flurry.**

-keep class com.startapp.** {
      *;
}

-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}


-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}



-renamesourcefileattribute SourceFile
-keepattributes Exceptions, InnerClasses, Signature, Deprecated, SourceFile, LineNumberTable, *Annotation*, EnclosingMethod
-printmapping out.map
-optimizationpasses 25
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*