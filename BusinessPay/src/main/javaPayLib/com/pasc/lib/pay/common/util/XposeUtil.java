package com.pasc.lib.pay.common.util;

import android.util.Log;
import com.pasc.business.ewallet.common.utils.LogUtil;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

/**
 * @date 2019-09-24
 * @des
 * @modify
 **/
public class XposeUtil {

    public static void checkHookTip() {
        Disposable disposable = Single.create (new SingleOnSubscribe<Boolean> () {
            @Override
            public void subscribe(SingleEmitter<Boolean> emitter) throws Exception {
                boolean isHook = isDeviceRooted () && !isHooked ();
                emitter.onSuccess (isHook);
            }
        }).subscribeOn (Schedulers.io ()).observeOn (AndroidSchedulers.mainThread ())
                .subscribe (new Consumer<Boolean> () {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        LogUtil.loge ("device is " +(aBoolean?"root":"not root"));
                        if (aBoolean) {
                            ToastUtils.toastMsg ("当前设备已root，请注意安全风险");
                        }
                    }
                }, new Consumer<Throwable> () {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    public static boolean isHooked() {
        return detectHook () || detectHook2 ();
    }

    private static boolean detectHook() {
        boolean hooked = false;
        BufferedReader reader = null;
        try {
            Set<String> libraries = new HashSet<> ();
            String mapsFilename = "/proc/" + android.os.Process.myPid () + "/maps";
            reader = new BufferedReader (new FileReader (mapsFilename));
            String line;
            while ((line = reader.readLine ()) != null) {
                if (line.endsWith (".so") || line.endsWith (".jar")) {
                    int n = line.lastIndexOf (" ");
                    libraries.add (line.substring (n + 1));
                }
            }
            for (String library : libraries) {
                if (library.contains ("com.saurik.substrate")) {
                    hooked = true;
                    Log.wtf ("HookDetection", "Substrate shared object found: " + library);
                    break;
                } else if (library.contains ("XposedBridge.jar")) {
                    hooked = true;
                    Log.wtf ("HookDetection", "Xposed JAR found: " + library);
                    break;
                }
            }
            reader.close ();
        } catch (Exception e) {
            Log.wtf ("HookDetection", e.toString ());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return hooked;
    }

    private static boolean detectHook2() {
        try {
            throw new Exception ("blah");
        } catch (Exception e) {
            int zygoteInitCallCount = 0;
            for (StackTraceElement stackTraceElement : e.getStackTrace ()) {
                if (stackTraceElement.getClassName ().equals ("com.android.internal.os.ZygoteInit")) {
                    zygoteInitCallCount++;
                    if (zygoteInitCallCount == 2) {
                        Log.wtf ("HookDetection", "Substrate is active on the device.");
                        return true;
                    }
                }
                if (stackTraceElement.getClassName ().equals ("com.saurik.substrate.MS$2")
                        && stackTraceElement.getMethodName ().equals ("invoked")) {
                    Log.wtf ("HookDetection", "A method on the stack trace has been hooked using Substrate.");
                    return true;
                }
                if (stackTraceElement.getClassName ().equals ("de.robv.android.xposed.XposedBridge")
                        && stackTraceElement.getMethodName ().equals ("main")) {
                    Log.wtf ("HookDetection", "Xposed is active on the device.");
                    return true;
                }
                if (stackTraceElement.getClassName ().equals ("de.robv.android.xposed.XposedBridge")
                        && stackTraceElement.getMethodName ().equals ("handleHookedMethod")) {
                    Log.wtf ("HookDetection", "A method on the stack trace has been hooked using Xposed.");
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isDeviceRooted() {
        return checkRootMethod1 () || checkRootMethod2 () || checkRootMethod3 ();
    }

    private static boolean checkRootMethod1() {
        String buildTags = android.os.Build.TAGS;
        return buildTags != null && buildTags.contains ("test-keys");
    }

    private static boolean checkRootMethod2() {
        String[] paths = {
                "/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su",
                "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
                "/system/bin/failsafe/su", "/data/local/su"
        };
        for (String path : paths) {
            if (new File (path).exists ()) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkRootMethod3() {
        java.lang.Process process = null;
        try {
            process = Runtime.getRuntime ().exec (new String[]{"/system/xbin/which", "su"});
            BufferedReader in = new BufferedReader (new InputStreamReader (process.getInputStream ()));
            return in.readLine () != null;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) {
                process.destroy ();
            }
        }
    }
}
