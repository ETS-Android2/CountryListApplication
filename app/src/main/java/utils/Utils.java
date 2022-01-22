package utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Utils {

    /**
     * This method checks if the Network available on the device or not.
     *
     * @param context
     * @return true if network available, false otherwise
     */
    public static Boolean isNetworkAvailable(Context context) {
        boolean connected = false;
        try {
            final ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                connected = true;
            } else if (netInfo != null && netInfo.isConnected()
                    && cm.getActiveNetworkInfo().isAvailable()) {
                connected = true;
            } else if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection urlc = (HttpURLConnection) url
                            .openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        connected = true;
                    }
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (cm != null) {
                final NetworkInfo[] netInfoAll = cm.getAllNetworkInfo();
                for (NetworkInfo ni : netInfoAll) {
                    System.out.println("get network type :::" + ni.getTypeName());
                    if ((ni.getTypeName().equalsIgnoreCase("WIFI") || ni
                            .getTypeName().equalsIgnoreCase("MOBILE"))
                            && ni.isConnected() && ni.isAvailable()) {
                        connected = true;
                        if (connected) {
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connected;
    }

    /**
     * Json to Pojo to parse the response into our model class.
     *
     * @param jsonString
     * @param pojoClass
     * @return
     */
    public static Object jsonToPojo(String jsonString, Class<?> pojoClass) {
        return new Gson().fromJson(jsonString, pojoClass);
    }
}
