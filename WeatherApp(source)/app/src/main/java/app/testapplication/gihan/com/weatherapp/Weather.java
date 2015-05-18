package app.testapplication.gihan.com.weatherapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 * This class object hold all the weather details.
 */
public class Weather implements Serializable{

    //Declare variables
    private String cityName="";
    private String temperature="";
    private String status="";
    private ArrayList<Forecast> forecast=null;
    private String image ="NO";
    private UUID id;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    //Set unique ID for each city.
    public Weather(){
        id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public ArrayList<Forecast> getForecast() {
        return forecast;
    }

    public void setForecast(ArrayList<Forecast> forecast) {
        this.forecast = forecast;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
