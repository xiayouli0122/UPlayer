package com.yuri.uplayer;

public class Log {
    private static boolean DEBUG = true;
    private static String APP_TAG = "Yuri/";

    public static void d(String tag, String msg) {
        if (DEBUG) {
            android.util.Log.d(APP_TAG + tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (DEBUG) {
            android.util.Log.i(APP_TAG + tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (DEBUG) {
            android.util.Log.v(APP_TAG + tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        android.util.Log.e(APP_TAG + tag, msg);
    }
}
