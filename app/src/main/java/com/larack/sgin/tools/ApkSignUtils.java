package com.larack.sgin.tools;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ApkSignUtils {

    private static final char[] HEX_CHAR = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    /**
     * 获取签名
     *
     * @param activity
     * @param pkgName
     * @return
     */
    public static String getSign(Activity activity, String pkgName) {
        String name = pkgName.trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(activity, "包名不能为空", Toast.LENGTH_LONG).show();
            return "";
        }

        try {
            PackageInfo info = activity.getPackageManager().getPackageInfo(name, PackageManager.GET_SIGNATURES);
            if (info.signatures != null && info.signatures.length > 0) {
                Signature signature = info.signatures[0];
                MessageDigest md5 = null;
                try {
                    md5 = MessageDigest.getInstance("MD5");
                    byte[] digest = md5.digest(signature.toByteArray());
                    return toHexString(digest);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    // Should not occur
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(activity, "包名不存在", Toast.LENGTH_LONG).show();
        }
        return "";
    }

    /**
     * 将字节数组转化为对应的十六进制字符串
     */
    private static String toHexString(byte[] rawByteArray) {
        char[] chars = new char[rawByteArray.length * 2];
        for (int i = 0; i < rawByteArray.length; ++i) {
            byte b = rawByteArray[i];
            chars[i * 2] = HEX_CHAR[(b >>> 4 & 0x0F)];
            chars[i * 2 + 1] = HEX_CHAR[(b & 0x0F)];
        }
        return new String(chars);
    }
}
