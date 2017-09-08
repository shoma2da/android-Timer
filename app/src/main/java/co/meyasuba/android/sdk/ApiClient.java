package co.meyasuba.android.sdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;

class ApiClient {

    private static final String PLATFORM = "Android";

    private Context mContext;

    ApiClient(Context context) {
        mContext = context;
    }

    void sendEvent(Context context, String type, String apiKey) {
        this.sendEvent(context, type, new JSONObject(), apiKey);
    }

    void sendEvent(Context context, String type, JSONObject parameter, String apiKey) {
        final JSONObject json = new JSONObject();
        try {
            json.put("api_key", apiKey);
            json.put("type", type);
            json.put("parameter", parameter);
            json.put("platform", "Android");
            json.put("app_version", getAppVersion(context));
            json.put("os_version", getOsVersion());
            json.put("device_name", getDeviceName());
            json.put("sdk_version", getSdkVersion());
            json.put("device_id", getDeviceId(context));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    String urlSt = "https://api.meyasuba.co/v1/appevents";
                    URL url = new URL(urlSt);
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.setRequestProperty("X-API-KEY", "vg436Q9HWm1RSAFSWPC8A9rOed16aPhradnlSUa2");

                    con.connect();

                    OutputStreamWriter outputStream = new OutputStreamWriter(con.getOutputStream());
                    outputStream.write(json.toString(4));
                    outputStream.close();

                    int statusCode = con.getResponseCode();
                    MeyasubacoLog.d("response : " + statusCode);
                    Scanner scanner = new Scanner(con.getInputStream());
                    while (scanner.hasNextLine()) {
                        MeyasubacoLog.d(scanner.nextLine());
                    }

                    con.disconnect();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    private String getAppVersion(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            String packageName = context.getPackageName();
            PackageInfo info = packageManager.getPackageInfo(packageName, 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "unknown";
        }
    }

    private String getOsVersion() {
        return Build.VERSION.RELEASE;
    }

    private String getDeviceName() {
        return android.os.Build.MODEL;
    }

    private String getSdkVersion() {
        //TODO: ファイル読込？要検討
        return "0.0.1";
    }

    private String getDeviceId(Context context) {
        SharedPreferences pref = context.getSharedPreferences("meyasubaco", Context.MODE_PRIVATE);
        String id = pref.getString("uuid", null);
        if (id == null) {
            id = UUID.randomUUID().toString();
            pref.edit().putString("uuid", id).apply();
        }
        return id;
    }

}
