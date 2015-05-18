package app.testapplication.gihan.com.weatherapp;

import java.util.ArrayList;

/**
 * Pick Where on Earth ID from Yahoo API.
 */
public class WOEIDPicker {
    private XMLGeoWOEID Geo;
    String WOEIDNo;

    /**
     * Return id.
     * @return
     */
    public String getWOEIDNo() {
        return WOEIDNo;
    }

    /**
     * Get WOEID according to the given latitude and longitude.
     * @param latitude
     * @param longitude
     */
    public WOEIDPicker(String latitude,String longitude){
        try{
            String finalUrl = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20geo.placefinder%20where%20text%3D%22"+latitude+"%2C"+longitude+"%22%20and%20gflags%3D%22R%22&diagnostics=true";
            Geo = new XMLGeoWOEID(finalUrl);
            Geo.fetchXML();

            while (Geo.parsingComplete);
            WOEIDNo = Geo.getWOEID();

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
