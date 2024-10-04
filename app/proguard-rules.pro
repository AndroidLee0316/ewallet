# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-optimizationpasses 5
-useuniqueclassmembernames
-keepattributes SourceFile,LineNumberTable
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-dontoptimize
#自定义混淆字典，替代 abcd
-obfuscationdictionary dictionary_rules.txt
-classobfuscationdictionary dictionary_rules.txt
-verbose
-ignorewarnings
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*


#apk 包内所有 class 的内部结构
-dump class_files.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt
#混淆前后的映射
-printmapping mapping.txt

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep class * extends android.preference.Preference
-keep class * extends android.os.Bundle

-keepclasseswithmembernames class * {
    native <methods>;
}

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
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

-keepclasseswithmembers class * {
    void onEvent*(...);
}

-keepnames class * implements java.io.Serializable

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-renamesourcefileattribute SourceFile
-keepattributes *JavascriptInterface*

#Bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

-assumenosideeffects class android.util.Log{
    public static *** v(...);
    public static *** i(...);
    public static *** d(...);
    public static *** w(...);
    public static *** e(...);
}

#important  爱加密不能混淆 加固aar 所依赖的第三方库
# 给别人使用的时候 ，也要提供下面的 混淆给对方
-keep class com.google.** {*;}
-keep class com.squareup.** {*;}
-keep class okhttp3.** {*;}
-keep class okio.** {*;}
-keep class retrofit2.** {*;}
-keep class com.tencent.mm.opensdk.** {*;}
-keep class com.tendcloud.tenddata.** {*;}
-keep class com.trello.rxlifecycle2.** {*;}
-keep class org.bouncycastle.** {*;}
-keep class org.greenrobot.eventbus.** {*;}
-keep class org.reactivestreams.** {*;}
-keep class com.pasc.business.ewallet.** {*;}
-keep class com.pasc.lib.** {*;}
-keep class android.support.**{*;}