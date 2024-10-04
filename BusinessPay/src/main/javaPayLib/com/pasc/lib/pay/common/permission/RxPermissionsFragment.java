package com.pasc.lib.pay.common.permission;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.v4.app.AppOpsManagerCompat;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.subjects.PublishSubject;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

public class RxPermissionsFragment extends Fragment {

    private static final int PERMISSIONS_REQUEST_CODE = 42;

    // Contains all the current permission requests.
    // Once granted or denied, they are removed from it.
    private Map<String, PublishSubject<Permission>> mSubjects = new HashMap<>();
    private boolean mLogging;

    public RxPermissionsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @TargetApi(Build.VERSION_CODES.M)
    void requestPermissions(@NonNull String[] permissions) {
        requestPermissions(permissions, PERMISSIONS_REQUEST_CODE);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != PERMISSIONS_REQUEST_CODE) return;

        boolean[] shouldShowRequestPermissionRationale = new boolean[permissions.length];

        for (int i = 0; i < permissions.length; i++) {
            shouldShowRequestPermissionRationale[i] = shouldShowRequestPermissionRationale(permissions[i]);
        }

        onRequestPermissionsResult(permissions, grantResults, shouldShowRequestPermissionRationale);
    }

    void onRequestPermissionsResult(String permissions[], int[] grantResults, boolean[] shouldShowRequestPermissionRationale) {
        for (int i = 0, size = permissions.length; i < size; i++) {
            log("onRequestPermissionsResult  " + permissions[i]);
            // Find the corresponding subject
            PublishSubject<Permission> subject = mSubjects.get(permissions[i]);
            if (subject == null) {
                // No subject found
                Log.d(RxPermissions.TAG, "RxPermissions.onRequestPermissionsResult invoked but didn't find the corresponding permission request.");
                return;
            }
            mSubjects.remove(permissions[i]);
            boolean granted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
            if (permissions[i].equals(Manifest.permission.CAMERA) && isMeizuMobilePhone()) {// 拒绝后，以后的询问魅族默认调用拒绝
                granted = hasCameraPermissionForMeizu();
            }
            subject.onNext(new Permission(permissions[i], granted, shouldShowRequestPermissionRationale[i]));
            subject.onComplete();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    boolean isGranted(String permission) {
        if (!isMarshmallow()) return true;
        if (permission.equals(Manifest.permission.CAMERA) && isMeizuMobilePhone()) {
            return hasCameraPermissionForMeizu();
        }
        if (isXiaomiMobilePhone()
                || isHuaweiMobilePhone()
                || isMeizuMobilePhone()
                || isVivoMobilePhone()) {
            return isGrantedForLocalization(permission);
        }
        try {
            return checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_GRANTED;
        } catch (RuntimeException t) {
            return false;
        }
    }

    private boolean isXiaomiMobilePhone() {
        return "Xiaomi".equalsIgnoreCase(Build.MANUFACTURER);
    }

    private boolean isVivoMobilePhone() {
        return "vivo".equalsIgnoreCase(Build.MANUFACTURER);
    }

    private boolean isHuaweiMobilePhone() {
        return "huawei".equalsIgnoreCase(Build.MANUFACTURER);
    }

    private boolean isMeizuMobilePhone() {
        return "meizu".equalsIgnoreCase(Build.MANUFACTURER);
    }

    private boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    private boolean isGrantedForLocalization(String permission) {
        String permissionToOp = AppOpsManagerCompat.permissionToOp(permission);
        if (permissionToOp == null) {
            // in case of normal permissions(e.g. INTERNET)
            return true;
        }
        int noteOp = AppOpsManagerCompat.noteOp(getActivity(), permissionToOp, Process.myUid(), getActivity().getPackageName());
        return noteOp == AppOpsManagerCompat.MODE_ALLOWED && checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean hasCameraPermissionForMeizu() {
        Camera camera = null;
        boolean granted = false;

        try {
            camera = Camera.open();
            Camera.Parameters mParameters = camera.getParameters();
            camera.setParameters(mParameters);
            granted = true;
        } catch (RuntimeException e) {
            granted = false;
        }

        if (camera != null) {
            camera.release();
            camera = null;
        }
        return granted;
    }

    @TargetApi(Build.VERSION_CODES.M)
    boolean isRevoked(String permission) {
        return getActivity().getPackageManager().isPermissionRevokedByPolicy(permission, getActivity().getPackageName());
    }

    public void setLogging(boolean logging) {
        mLogging = logging;
    }

    public PublishSubject<Permission> getSubjectByPermission(@NonNull String permission) {
        return mSubjects.get(permission);
    }

    public boolean containsByPermission(@NonNull String permission) {
        return mSubjects.containsKey(permission);
    }

    public PublishSubject<Permission> setSubjectForPermission(@NonNull String permission, @NonNull PublishSubject<Permission> subject) {
        return mSubjects.put(permission, subject);
    }

    void log(String message) {
        if (mLogging) {
            Log.d(RxPermissions.TAG, message);
        }
    }

}
