package app.testapplication.gihan.com.weatherapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Check the internet connection availability.
 */
public class InternetDetector {

    private Context setContext;

    public InternetDetector(Context getContext){
        this.setContext = getContext;
    }

    /**
     * Check the network connection.
     * @return
     */
    public boolean isConnectionAvailable(){

        ConnectivityManager connect = (ConnectivityManager) setContext.getSystemService(Context .CONNECTIVITY_SERVICE);
        if(connect != null){
            NetworkInfo[] check = connect.getAllNetworkInfo();
            if(check != null){
                for(int i = 0; i<check.length;i++){
                    if(check[i].getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
