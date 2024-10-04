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

####################  公共部分  ################################
-optimizationpasses 5
-useuniqueclassmembernames
-keepattributes SourceFile,LineNumberTable
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-dontoptimize
#自定义混淆字典，替代 abcd
-obfuscationdictionary dictionary_rules.txt
#-classobfuscationdictionary dictionary_rules.txt
-verbose
-ignorewarnings
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#关闭压缩  默认开启，用以减小应用体积，移除未被使用的类和成员，
#并且会在优化动作执行之后再次执行（因为优化后可能会再次暴露一些未被使用的类和成员）。
-dontshrink

#apk 包内所有 class 的内部结构
-dump class_files.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt
#混淆前后的映射
-printmapping mapping.txt

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-renamesourcefileattribute SourceFile
-keepattributes *JavascriptInterface*

#------------------  下方是android平台自带的排除项，这里不要动         ----------------

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclasseswithmembernames class *{
	native <methods>;
}

-keepclasseswithmembers class * {
    ... *JNI*(...);
}

-keepclasseswithmembernames class * {
	... *JRI*(...);
}

-keep class **JNI* {*;}

#tenddata 天眼
-dontwarn com.tendcloud.tenddata.**
-keep class com.tendcloud.** {*;}
-keep public class com.tendcloud.tenddata.** { public protected *;}
-keepclassmembers class com.tendcloud.tenddata.**{
public void *(***);
}
-keep class com.talkingdata.sdk.TalkingDataSDK {public *;}
-keep class com.apptalkingdata.** {*;}
-keep class dice.** {*; }
-dontwarn dice.**
#tenddata

#对外提供使用的方法,不混淆的话就加上注释
# keep annotated by NotProguard
-keep class com.pasc.business.ewallet.NotProguard {*;}
-keep @com.pasc.business.ewallet.NotProguard class * {*;}
-keepclassmembers class * {
    @com.pasc.business.ewallet.NotProguard <fields>;
}
-keepclassmembers class * {
    @com.pasc.business.ewallet.NotProguard <methods>;
}
#对外提供使用的方法
-assumenosideeffects class android.util.Log{
    public static *** v(...);
    public static *** i(...);
    public static *** d(...);
    public static *** w(...);
    public static *** e(...);
}

-keep class com.snpay.android.ISnPay{*;}
-keep class com.snpay.android.ISnPay$Stub{*;}
-keep class com.snpay.android.IRemoteServiceCallback{*;}
-keep class com.snpay.android.IRemoteServiceCallback$Stub{*;}
-keep class com.snpay.sdk.app.PayTask{ public *;}
-keep class com.snpay.sdk.app.CashierContentTask{ public *;}
-keep class com.snpay.sdk.app.PayResultStatus{*;}
-keep class com.snpay.sdk.model.PayResult{*;}
#第三方包不混淆
-keep class com.android.volley.**{*;}
-keep interface com.android.volley.**{*;}

#威富通
-dontwarn com.swiftfintech.pay.**
-keep class com.swiftfintech.pay.** { *;}

-keep class com.chinaums.pppay.unify.** {*;}

-keep class com.pasc.business.ewallet.business.pay.net.** {*;}