package app.testapplication.gihan.com.weatherapp;

import android.app.Activity;
import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.UUID;

/**
 * This class will initialize Weather and Forecast object.
 */
public class AllWeather  {

    // This variable help to check class is created or not.
    private static AllWeather allWeather;

    // Declare other variable.
    private Context allApplicationContext;
    private ArrayList<Weather> weatherList;
    private LocationFinder finder;
    private WOEIDPicker picker;

    /**
     * Assign running context.
     * @param allApplicationContext
     */
    private AllWeather(Context allApplicationContext){
        this.allApplicationContext = allApplicationContext;
        weatherList = new ArrayList<Weather>();
        Boolean internet = false;
        String WOEIDNO = null ;
        Weather s1 = new Weather();

        //Check the internet connection availability
        InternetDetector detect = new InternetDetector(allApplicationContext);
        internet = detect.isConnectionAvailable();

        if(internet) {
            finder = new LocationFinder(allApplicationContext);
            try {

                //Get the latitude and longitude.
                if (finder.canGetLocation()) {
                    String latitude = Double.toString(finder.getLatitude());
                    String longitude = Double.toString(finder.getLongitude());
                    picker = new WOEIDPicker(latitude, longitude);
                    WOEIDNO = picker.getWOEIDNo();
                    String finalUrl = "http://weather.yahooapis.com/forecastrss?w=" + WOEIDNO + "&u=c";
                    XMLYahoo yahoo = new XMLYahoo(finalUrl);
                    yahoo.fetchXML();

                    //Add weather details to the list.
                    while (yahoo.parsingComplete) ;
                    yahoo.getCityWeather().setImage("Yes");
                    weatherList.add(yahoo.getCityWeather());
                }

                //Create empty forecast object list.
                else {
                    ArrayList<Forecast> newf = new ArrayList<Forecast>();
                    Forecast f1 = new Forecast();
                    f1.setMax("");
                    f1.setDate("");
                    f1.setMin("");
                    f1.setStatus("");
                    for(int i =0; i<5;i++){
                        newf.add(f1);
                    }

                    //Add empty forecast.
                    s1.setForecast(newf);
                    s1.setImage("Yes");
                    weatherList.add(s1);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(allApplicationContext, "Network Error", Toast.LENGTH_LONG).show();
            ArrayList<Forecast> newf = new ArrayList<Forecast>();
            Forecast f1 = new Forecast();
            f1.setMax("");
            f1.setDate("");
            f1.setMin("");
            f1.setStatus("");

            newf.add(f1);
            newf.add(f1);
            newf.add(f1);
            newf.add(f1);
            newf.add(f1);
            s1.setForecast(newf);
            weatherList.add(s1);
        }

        FileManager fileManager = new FileManager(allApplicationContext);
        ArrayList<Weather> readlist = fileManager.Read();
        ArrayList<Weather> updateed = new ArrayList<Weather>();

        if(readlist.size()>1){
            for(int i =1;i<readlist.size();i++){
                Weather w = readlist.get(i);
                if(internet){
                    //w = Update(w);
                }
                weatherList.add(w);
            }
        }else {
            //Do Nothing.
        }
    }

    /**
     * Get current weather list.
     * @return
     */
    public ArrayList<Weather> getWeatherList(){
        return weatherList;
    }

    /**
     * This method check this class is created or not. If not
     * create new one.
     * @param context
     * @return
     */
    public static AllWeather get(Context context){
        if(allWeather==null){
            allWeather = new AllWeather(context.getApplicationContext());
        }

        return allWeather;
    }

    /**
     * Get selected city
     * //Todo
     * @param id
     * @return
     */
    public Weather getCityDetail(UUID id){
        for(Weather cityDetail:weatherList){
            if(cityDetail.getId().equals(id)){
                return cityDetail;
            }
        }
        return null;
    }

    /**
     * Update weather.
     * @param weather
     * @return
     */
    public Weather Update(Weather weather){
        Weather update = weather;
        Weather updated = new Weather();
        String cityName = weather.getCityName();
        CityPicker cityPicker = new CityPicker();
        int WOEID =cityPicker.WOEIDSelection(cityName);
        XMLYahoo yahoo;

        //Read Yahoo api.
        try{
            String finalUrl = "http://weather.yahooapis.com/forecastrss?w="+WOEID+"&u=c";
            yahoo = new XMLYahoo(finalUrl);
            yahoo.fetchXML();
            while (yahoo.parsingComplete);
            updated =(yahoo.getCityWeather());
        }catch (Exception e){
            e.printStackTrace();
        }
        return updated;
    }

    /**
     * Update current geo location.
     * @return
     */
    public Weather LocationUpdate(){
        Weather location = new Weather();
        finder = new LocationFinder(allApplicationContext);

        //Check the geo location settings.
        try {
            if (finder.canGetLocation()) {
                String latitude = Double.toString(finder.getLatitude());
                String longitude = Double.toString(finder.getLongitude());
                picker = new WOEIDPicker(latitude, longitude);
                String WOEIDNO = picker.getWOEIDNo();

                //Read Yahoo API (XML)
                String finalUrl = "http://weather.yahooapis.com/forecastrss?w=" + WOEIDNO + "&u=c";
                XMLYahoo yahoo = new XMLYahoo(finalUrl);
                yahoo.fetchXML();

                while (yahoo.parsingComplete) ;
                yahoo.getCityWeather().setImage("Yes");
                location=(yahoo.getCityWeather());

            } else {

                // Create empty forecast.
                Toast.makeText(allApplicationContext, "Please Allow to use Location Service", Toast.LENGTH_LONG).show();
                ArrayList<Forecast> newf = new ArrayList<Forecast>();
                Forecast f1 = new Forecast();
                f1.setMax("");
                f1.setDate("");
                f1.setMin("");
                f1.setStatus("");

                for(int i =0; i<5;i++){
                    newf.add(f1);
                }
                location.setForecast(newf);
                location.setImage("Yes");
            }
        }catch (Exception e){
            //Do nothing.
        }
        return location;
    }

}
