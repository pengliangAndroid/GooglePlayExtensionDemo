package com.hdgame.extensiondmeo;

import android.content.Context;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import com.google.android.vending.licensing.AESObfuscator;
import com.google.android.vending.licensing.APKExpansionPolicy;
import com.google.android.vending.licensing.LicenseChecker;
import com.google.android.vending.licensing.LicenseCheckerCallback;
import com.hdgame.extensiondmeo.multithreaddownloader.DownloadManager;
import com.hdgame.extensiondmeo.multithreaddownloader.FileDownloader;

/**
 * @author pengl
 */
public class GoogleApkExpandHelper {

    //随机byte数组，随便填就好
    private static final byte[] salt = new byte[]{18, 22, -31, -11, -54, 18, -101, -32, 43, 2, -8, -4, 9, 5, -106, -17, 33, 44, 3, 1};

    private static final String TAG = "GoogleApkExpandHelper";

    public static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhFrPlD710d15WQkloHuOXBxjtW8tyfp0JbtnFBsagK2yaap7PpfiFptzoECnCr5x0zN7tBhi3dD9n4mpFQ1Ph3C0rQIs494RJooRw2vFfPPFfYOOPczI8KpZSgcqdAEfeUL4WBdocv5GCThWmLENl8j3s3N5ZSxmwjdF1/RdhbsrHYZvcSEoPJvPbJlg2S7k/FJrJWAy2kbRLB7rpHwdU55z1ajC/bF6LBolUX+WIHAvLTUEB67CByE/i9sx9wljmlQVFXf2GyOLm1U5Tu+Vxh463zsTsb9YKFb8apc+mEsEvO6WXUQXBTQ6dQfyiMoyVkSfJqay8nPgFVHmsA/CfwIDAQAB";

    public static void getObbUrl(final Context context) {
        final APKExpansionPolicy aep = new APKExpansionPolicy(
                context,
                new AESObfuscator(salt,
                        context.getPackageName(),
                        Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID)
                ));
        aep.resetPolicy();

        final LicenseChecker checker = new LicenseChecker(context, aep, PUBLIC_KEY);

        checker.checkAccess(new LicenseCheckerCallback() {
            @Override
            public void allow(int reason) {
                Log.i(TAG, "allow:" + reason);
                if (aep.getExpansionURLCount() > 0) {
                    //获取到拓展文件的地址
                    String url = aep.getExpansionURL(0);

                    Log.i(TAG, "url:" + url);
                    startDownloadFile(context,url);
                }
            }

            @Override
            public void dontAllow(int reason) {
                Log.i(TAG, "dontAllow:" + reason);
            }

            @Override
            public void applicationError(int errorCode) {
                Log.i(TAG, "applicationError:" + errorCode);
            }
        });
    }

    public static void startDownloadFile(Context context,String url){
        DownloadManager instance = DownloadManager.
                getInstance(context.getApplicationContext());
        instance.download("extensionsFile", url, Environment.getExternalStorageDirectory().getAbsolutePath(), 1, new FileDownloader.OnDownloadListener() {
            @Override
            public void onUpdate(int totalSize, int currentSize, int speed, int percent) {
                System.out.println("1 percent = " + percent + "%");
                System.out.println("1 speed = " + speed + "kb/s");
                //updateDownloadProgress(percent, 1);
            }

            @Override
            public void onFinish(String downloadUrl, String filepath) {

            }
        });
    }

}
