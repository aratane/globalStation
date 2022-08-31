package info.appteve.radioelectro;
import android.app.Application;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.onesignal.OneSignal;


public class AppController extends Application {
    public static final String TAG = AppController.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private static AppController mInstance;
    SharedPreferences prefs = null;

    @Override
    public void onCreate() {
        super.onCreate();

        OneSignal.startInit(this).init();

        FontsOverride.setDefaultFont(this, "DEFAULT", "appteve/Oswaldesque-Regular.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "appteve/Oswaldesque-Regular.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "appteve/Oswaldesque-Regular.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "appteve/Oswaldesque-Regular.ttf");

        mInstance = this;
      //  startService(new Intent(this, RegistrationIntentService.class));

        prefs = getSharedPreferences("info.com.castio", MODE_PRIVATE);

        if (prefs.getBoolean("firstrun", true)) {
            prefs.edit().putBoolean("firstrun", false).commit();

        }

    }


    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }



    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }




}
