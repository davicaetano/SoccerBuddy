package com.davicaetano.soccerbuddy.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import com.davicaetano.soccerbuddy.R;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by davicaetano on 1/21/16.
 */
public class Utils {

    private static final String TAG = "Utils";
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private static final String UTF_8 = "UTF-8";

    public static Context context;

    public static String password(String name)  {
        String password = "";
        String key = context.getString(R.string.signin_key);
        String salt = context.getString(R.string.sal);
        try {
            name = name + salt;
            byte[] keyBytes = key.getBytes();
            SecretKeySpec signingKey = new SecretKeySpec(keyBytes,HMAC_SHA1_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(name.getBytes());
            byte[] hexBytes = new Hex().encode(rawHmac);
            password = new String(hexBytes,UTF_8);
        }catch (Exception e){
            Log.v(TAG, "Password error.");
        }
        return password;
    }

    public static boolean IsInternetConnected(Context context) {
        boolean output = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isConnected() || mobile.isConnected()) {
            output = true;
        }
        return output;
    }
}
