package app.testapplication.gihan.com.weatherapp;

import android.content.Intent;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * WOEID API Reader.
 */
public class XMLGeoWOEID {

    private String urlString = null;
    private String WOEID;
    private XmlPullParserFactory xmlFactoryObject;
    public volatile boolean parsingComplete = true;

    public XMLGeoWOEID(String url){
        this.urlString = url;
    }

    public String getWOEID() {
        return WOEID;
    }

    /**
     * Read XML out put from API.
     * @param newParser
     */
    public void getXmlStore(XmlPullParser newParser){
        int event;
        String text = null;
        try{
            event = newParser.getEventType();
            while (event != XmlPullParser .END_DOCUMENT){
                String name = newParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        text = newParser.getText();
                        break;
                    case XmlPullParser.END_TAG:

                        //Get WOEID.
                        if (name.equals("woeid")) {
                            WOEID =text;
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

    /**
     * Read the url connection on separate thread.
     */
    public void fetchXML(){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    URL Geturl = new URL(urlString);
                    HttpURLConnection checkConnection = (HttpURLConnection)Geturl.openConnection();

                    //Set timer.
                    checkConnection.setReadTimeout(10000);
                    checkConnection.setConnectTimeout(50000);
                    checkConnection.setRequestMethod("GET");
                    checkConnection.setDoInput(true);
                    checkConnection.connect();
                    InputStream stream = checkConnection.getInputStream();
                    xmlFactoryObject = XmlPullParserFactory.newInstance();
                    XmlPullParser myparser = xmlFactoryObject.newPullParser();

                    myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES
                            , false);
                    myparser.setInput(stream, null);
                    getXmlStore(myparser);
                    stream.close();
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });
        thread.start();
    }

}
