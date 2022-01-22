package utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {

    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private static SimpleDateFormat DATE_FORMAT_YEAR = new SimpleDateFormat("yyyy", Locale.US);

    private static Date toDate(String date) throws ParseException {
        return DATE_FORMAT.parse(date);
    }


    public static String getCurrentYear() {
        return Utils.getCurrentime("yyyy");
    }

    /**
     * get Current Time with format parameter
     *
     * @param format
     * @return string time
     */
    public static String getCurrentime(String format) {
        Calendar cal = Calendar.getInstance();
        cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(cal.getTime());

    }

    /**
     * this method get resource from any object who need it.
     *
     * @param idResource
     */
    public static String getStringFromResource(int idResource, Context _context) {
        // TODO Auto-generated method stub
        try {
            return _context.getResources().getString(idResource);
        } catch (NullPointerException e) {
            // TODO: handle exception
            return "";
        }
    }

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
}
