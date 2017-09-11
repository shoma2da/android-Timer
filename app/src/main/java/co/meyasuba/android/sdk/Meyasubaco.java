package co.meyasuba.android.sdk;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Meyasubaco {

    private static Meyasubaco INSTANCE;

    public static Meyasubaco getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new Meyasubaco(context);
        }
        return INSTANCE;
    }

    private Context mContext;
    private ApiClient mApiClient;

    private Meyasubaco(Context context) {
        mContext = context;
        mApiClient = new ApiClient(context);
    }

    public void initialize(String apiKey) {
        SharedPreferences pref = mContext.getSharedPreferences("meyasubaco", Context.MODE_PRIVATE);
        pref.edit().putString("apiKey", apiKey).apply();
        mApiClient.sendEvent(mContext, "launch", apiKey);
    }

    public void sendEvent(String name, JSONObject params) {
        SharedPreferences pref = mContext.getSharedPreferences("meyasubaco", Context.MODE_PRIVATE);
        String apiKey = pref.getString("apiKey", null);
        try {
            JSONObject parameter = new JSONObject();
            parameter.put("name", name);
            parameter.put("params", params);
            mApiClient.sendEvent(mContext, "event", parameter, apiKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendNps(int value, String comment, int launch) {
        SharedPreferences pref = mContext.getSharedPreferences("meyasubaco", Context.MODE_PRIVATE);
        String apiKey = pref.getString("apiKey", null);
        try {
            JSONObject parameter = new JSONObject();
            parameter.put("value", value);
            parameter.put("comment", comment);
            parameter.put("launch", launch);
            mApiClient.sendEvent(mContext, "nps", parameter, apiKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
