# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/Samuel/Library/Android/sdk/tools/proguard/proguard-android.txt
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
-assumenosideeffects class android.util.Log{
    public static int v(...);
    public static int i(...);
    public static int d(...);
    public static int w(...);
    public static int e(...);
}
-ignorewarnings

#-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
#public static java.lang.String TABLENAME;
#}

#-keepclassmembers class my.dao.package.** {
#    public static final <fields>;
#    }

#-keep class **$Properties

#-keep public class my.dao.package.models.** {
#     public static <fields>;
#}

#-keep class my.dao.package.*$Properties {
#    public static <fields>;
#}

#-keepclassmembers class my.dao.package.** {
#    public static final <fields>;
#    }
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
    public static java.lang.String TABLENAME;
}
-keep class **$Properties

-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
-keep class android.support.v7.widget.SearchView { *; }

# 这里根据具体的SDK版本修改
#-libraryjars libs/bmob_v3.4.5.jar

-keepattributes Signature
-keep class cn.bmob.v3.** {*;}

# 保证继承自BmobObject、BmobUser类的JavaBean不被混淆
#com.example.samuel.expensemanager.model.MyUser
#-keep class com.example.bmobexample.bean.BankCard{*;}
-keep class com.example.samuel.expensemanager.model.CloudTypeInfo{*;}
-keep class com.example.samuel.expensemanager.model.CloudExpense{*;}
-keep class com.example.samuel.expensemanager.model.MyUser{*;}
#-keep class com.example.bmobexample.bean.Person{*;}

#-keep class com.example.bmobexample.file.Movie{*;}
#-keep class com.example.bmobexample.file.Song{*;}

#-keep class com.example.bmobexample.relation.Post{*;}
#-keep class com.example.bmobexample.relation.Comment{*;}

# 如果你使用了okhttp、okio的包，请添加以下混淆代码
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *;}
-keep interface com.squareup.okhttp.** { *; }
-dontwarn okio.**

-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}

-dontwarn android.net.http.**
-keep public class android.webkit.WebView {*;}
-keep public class android.webkit.WebViewClient {*;}
-keep class com.weibo.net.** {*;}


-keep class android.net.http.SslError
-keep class android.webkit.**{*;}
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class m.framework.**{*;}
