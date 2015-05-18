package app.testapplication.gihan.com.weatherapp;

import java.io.Serializable;

/**
 * This class object hold all forecast details.
 */
public class Forecast implements Serializable{

    //Declare variable
    private String status;
    private String max;
    private String min;
    private String date;

    //Set default values.
    public Forecast(){
        status ="";
        max ="";
        min ="";
        date = "";
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }
}
