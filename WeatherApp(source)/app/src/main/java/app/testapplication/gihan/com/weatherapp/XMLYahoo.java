package app.testapplication.gihan.com.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Yahoo API reader.
 */
public class XMLYahoo {

    private String urlString = null;
    private ArrayList<Weather> cityList = null;
    private Weather cityWeather;
    private Forecast cityForecast;
    private ArrayList<Forecast> forecastList;
    private XmlPullParserFactory xmlFactoryObject;
    public volatile boolean parsingComplete = true;

    public XMLYahoo(String url){

        //Initialise variable.
        this.urlString = url;
        cityList = new ArrayList<Weather>();
        forecastList = new ArrayList<Forecast>();
    }

    public ArrayList<Weather> getTime() {
        return cityList;
    }
    public Weather getCityWeather(){
        return cityWeather;
    }

    /**
     * Read the weather details.
     * @param newParser
     */
    public void getXmlStore(XmlPullParser newParser){
        int event;
        cityWeather = new Weather();
        try{
            event = newParser.getEventType();
            while (event != XmlPullParser .END_DOCUMENT){
                String name = newParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if(name.equals("item")) {
                            //do nothing.
                        }
                        break;
                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.END_TAG:
                        //Set the city name.
                         if(name.equals("yweather:location")){
                              cityWeather.setCityName(newParser.getAttributeValue(null, "city"));
                            }
                         //Get the temperature and condition.
                        else if (name.equals("yweather:condition")) {
                            cityWeather.setTemperature(newParser.getAttributeValue(null, "temp"));
                            cityWeather.setStatus(newParser.getAttributeValue(null, "text"));


                        //Get the five day forecast.
                        }else if(name.equals("yweather:forecast")){
                            cityForecast = new Forecast();
                            forecastList.add(cityForecast);
                            cityWeather.setForecast(forecastList);
                            cityForecast.setStatus(newParser.getAttributeValue(null, "text"));
                            cityForecast.setMin(newParser.getAttributeValue(null, "low"));
                            cityForecast.setMax(newParser.getAttributeValue(null, "high"));
                            cityForecast.setDate(newParser.getAttributeValue(null, "date"));
                        }
                        break;
                }
                event = newParser.next();
            }
            parsingComplete = false;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void fetchXML(){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    URL Geturl = new URL(urlString);
                    HttpURLConnection CheckConnection = (HttpURLConnection)Geturl.openConnection();
                    //Set the timer (millisecond)
                    CheckConnection.setReadTimeout(10000);
                    CheckConnection.setConnectTimeout(50000);
                    CheckConnection.setRequestMethod("GET");
                    CheckConnection.setDoInput(true);
                    CheckConnection.connect();
                    InputStream stream = CheckConnection.getInputStream();

                    xmlFactoryObject = XmlPullParserFactory.newInstance();
                    XmlPullParser myparser = xmlFactoryObject.newPullParser();

                    myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES
                            , false);
                    myparser.setInput(stream, null);
                    getXmlStore(myparser);
                    stream.close();
                } catch (Exception e) {

                }
            }
        });

        thread.start();
    }


}
