package app.testapplication.gihan.com.weatherapp;

import android.content.Intent;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

/**
 * Set fragment layout.
 */
public class ModuleFragment extends Fragment {
    public static final String _cityName = "cityName";
    public static final String _cityStatus = "cityStatus";
    public static final String _temp = "temp";
    public static final String _forecast = null;
    public static final String _image ="image";

    public static final ModuleFragment newInstance(String city,String status,String tep,ArrayList<Forecast> forecasts,String image){
        ModuleFragment mf = new ModuleFragment();
        Bundle bundle = new Bundle();

        // Set the bundle from given status.
        bundle.putString(_cityName,city);
        bundle.putString(_cityStatus,status);
        bundle.putString(_temp,tep);
        bundle.putSerializable(_forecast, forecasts);
        bundle.putString(_image,image);

        //Attached bundle to moduler.
        mf.setArguments(bundle);
        return mf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weather_fragment,container,false);

        //Get the details form bundle.
        String city = getArguments() .getString(_cityName);
        String status = getArguments() .getString(_cityStatus);
        String temp = getArguments() .getString(_temp);
        ArrayList<Forecast> day1_date = (ArrayList<Forecast>)getArguments().getSerializable(_forecast);

        //Assign text field.
        TextView city_name = (TextView) view.findViewById(R.id.city_name);
        TextView city_status = (TextView) view.findViewById(R.id.status);
        TextView city_temp = (TextView) view.findViewById(R.id.temp);

        //set the Forecast day1
        TextView day1_day =(TextView)view.findViewById(R.id.f_day1);
        TextView day1_pho =(TextView)view.findViewById(R.id.f_day1_pho);
        TextView day1_max_min =(TextView)view.findViewById(R.id.f_day1_max_min);

        //Set the Forecast day2
        TextView day2_day =(TextView)view.findViewById(R.id.f_day2);
        TextView day2_pho =(TextView)view.findViewById(R.id.f_day2_pho);
        TextView day2_max_min =(TextView)view.findViewById(R.id.f_day2_max_min);

        //Set the Forecast day3
        TextView day3_day =(TextView)view.findViewById(R.id.f_day3);
        TextView day3_pho =(TextView)view.findViewById(R.id.f_day3_pho);
        TextView day3_max_min =(TextView)view.findViewById(R.id.f_day3_max_min);

        //Set the Forecast day4
        TextView day4_day =(TextView)view.findViewById(R.id.f_day4);
        TextView day4_pho =(TextView)view.findViewById(R.id.f_day4_pho);
        TextView day4_max_min =(TextView)view.findViewById(R.id.f_day4_max_min);

        //Set the Forecast day4
        TextView day5_day =(TextView)view.findViewById(R.id.f_day5);
        TextView day5_pho =(TextView)view.findViewById(R.id.f_day5_pho);
        TextView day5_max_min =(TextView)view.findViewById(R.id.f_day5_max_min);

        //Set the add button click action.
        TextView add = (TextView) view.findViewById(R.id.add_view);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Run separate thread on main thread.
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        //Open city selection view.
                        Intent intent = new Intent(getActivity(),CityPicker.class);
                        startActivity(intent);
                    }
                });
                thread.start();
            }
        });

        //Set the setting button click action.
        TextView setting = (TextView) view.findViewById(R.id.setting_view);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Settings.class);
                startActivity(intent);
            }
        });

        //Set city name.
        city_name.setText(city);

        //Check the city status and set forecast state.
        if(status.equals("Unknown")){
            city_status.setText(day1_date.get(0).getStatus());
        }else {
            city_status.setText(status);
        }

        //Display temperature value with symbol.
        city_temp.setText(temp+"℃");

        //Set forecast dates with given format.
        Date now = new Date();
        DateFormat dateFormatter1 = new SimpleDateFormat("d MMM");
        dateFormatter1.setTimeZone(TimeZone.getTimeZone("NZ"));
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DATE,1);

        //Forecast day1 view
        day1_day.setText(dateFormatter1.format(cal.getTime()));
        String day1Status = day1_date.get(0).getStatus();
        boolean statusmatch = false;
        String Statusset = "";
        if(!statusmatch) {
            for (int i = 0; i < sunnyCondition().size(); i++) {
                if (sunnyCondition().get(i).contains(day1Status)) {
                    Statusset = "Sunny";
                    statusmatch = true;
                    break;
                }
            }
        }else if(!statusmatch){
            for(int i = 0; i<cloudycondition().size();i++){
                if(cloudycondition().get(i).contains(day1Status)){
                    Statusset = "Cloudy";
                    statusmatch= true;
                    break;
                }
            }
        }else if(!statusmatch){
            for(int i = 0; i<halecondition().size();i++){
                if(halecondition().get(i).contains(day1Status)){
                    Statusset = "Hail";
                    statusmatch= true;
                    break;
                }
            }

        }else if(!statusmatch){
            for(int i = 0; i<lightningcondition().size();i++){
                if(lightningcondition().get(i).contains(day1Status)){
                    Statusset = "Lightning";
                    statusmatch= true;
                    break;
                }
            }
        }else if(!statusmatch){
            for(int i = 0; i<partycloudyconditon().size();i++){
                if(partycloudyconditon().get(i).contains(day1Status)){
                    Statusset = "PartlyCloudy";
                    statusmatch= true;
                    break;
                }
            }
        }else if(!statusmatch){
            for(int i = 0; i<raincondtioton().size();i++){
                if(raincondtioton().get(i).contains(day1Status)){
                    Statusset = "Rain";
                    statusmatch= true;
                    break;
                }
            }
        }else if(!statusmatch){
            for(int i = 0; i<wincondtioton().size();i++){
                if(wincondtioton().get(i).contains(day1Status)){
                    Statusset = "Windy";
                    statusmatch= true;
                    break;
                }
            }
        }

        if(Statusset.equals("Sunny")){
            day1_pho.setBackgroundResource(R.drawable.sunny);
        }else if(Statusset.equals("Windy")){
            //day1_pho.setText(day1_date.get(0).getStatus());
            day1_pho.setBackgroundResource(R.drawable.cloudy);
        }else if(Statusset.equals("Rain")){
            //day1_pho.setText(day1_date.get(0).getStatus());
            day1_pho.setBackgroundResource(R.drawable.rain);
        }else if(Statusset.equals("PartlyCloudy")){
            //day1_pho.setText(day1_date.get(0).getStatus());
            day1_pho.setBackgroundResource(R.drawable.partlycloudy);
        }else if(Statusset.equals("Lightning")){
            //day1_pho.setText(day1_date.get(0).getStatus());
            day1_pho.setBackgroundResource(R.drawable.lightning);
        }else if(Statusset.equals("Hail")){
            //day1_pho.setText(day1_date.get(0).getStatus());
            day1_pho.setBackgroundResource(R.drawable.hale);
        }else{
            //day1_pho.setText(day1_date.get(0).getStatus());
            day1_pho.setBackgroundResource(R.drawable.cloudy);
        }
        day1_max_min.setText(day1_date.get(0).getMin()+"° /"+day1_date.get(0).getMax()+" °");

        //Forecast day2 view
        cal.add(Calendar.DATE,1);
        day2_day.setText(dateFormatter1.format(cal.getTime()));
        String day1Status1 = day1_date.get(1).getStatus();
        boolean statusmatch1 = false;
        String Statusset1 = "";
        if(!statusmatch1) {
            for (int i = 0; i < sunnyCondition().size(); i++) {
                if (sunnyCondition().get(i).contains(day1Status1)) {
                    Statusset1 = "Sunny";
                    statusmatch1=true;
                    break;
                }
            }
        }else if(!statusmatch1){
            for(int i = 0; i<halecondition().size();i++){
                if(halecondition().get(i).contains(day1Status1)){
                    Statusset1 = "Hail";
                    statusmatch1=true;
                    break;
                }
            }
        }else if(!statusmatch1){
            for(int i = 0; i<cloudycondition().size();i++){
                if(cloudycondition().get(i).contains(day1Status1)){
                    Statusset1 = "Cloudy";
                    statusmatch1=true;
                    break;
                }
            }
        }else if(!statusmatch1){
            for(int i = 0; i<lightningcondition().size();i++){
                if(lightningcondition().get(i).contains(day1Status1)){
                    Statusset1 = "Lightning";
                    statusmatch1=true;
                    break;
                }
            }
        }else if(!statusmatch1){
            for(int i = 0; i<partycloudyconditon().size();i++){
                if(partycloudyconditon().get(i).contains(day1Status1)){
                    Statusset1 = "PartlyCloudy";
                    statusmatch1=true;
                    break;
                }
            }
        }else if(!statusmatch1){
            for(int i = 0; i<raincondtioton().size();i++){
                if(raincondtioton().get(i).contains(day1Status1)){
                    Statusset1 = "Rain";
                    statusmatch1=true;
                    break;
                }
            }
        }else if(!statusmatch1){
            for(int i = 0; i<wincondtioton().size();i++){
                if(wincondtioton().get(i).contains(day1Status1)){
                    Statusset1 = "Windy";
                    statusmatch1=true;
                    break;
                }
            }
        }
        if(Statusset1.equals("Sunny")){
            day2_pho.setBackgroundResource(R.drawable.sunny);
        }else if(Statusset1.equals("Windy")){
            //day1_pho.setText(day1_date.get(0).getStatus());
            day2_pho.setBackgroundResource(R.drawable.cloudy);
        }else if(Statusset1.equals("Rain")){
            //day1_pho.setText(day1_date.get(0).getStatus());
            day1_pho.setBackgroundResource(R.drawable.rain);
        }else if(Statusset1.equals("PartlyCloudy")){
            //day1_pho.setText(day1_date.get(0).getStatus());
            day2_pho.setBackgroundResource(R.drawable.partlycloudy);
        }else if(Statusset1.equals("Lightning")){
            //day1_pho.setText(day1_date.get(0).getStatus());
            day2_pho.setBackgroundResource(R.drawable.lightning);
        }else if(Statusset1.equals("Hail")){
            //day1_pho.setText(day1_date.get(0).getStatus());
            day2_pho.setBackgroundResource(R.drawable.hale);
        }else{
            //day1_pho.setText(day1_date.get(0).getStatus());
            day2_pho.setBackgroundResource(R.drawable.cloudy);
        }

        day2_max_min.setText(day1_date.get(1).getMin()+"° /"+day1_date.get(1).getMax()+" °");

        //Forecast day3 view
        cal.add(Calendar.DATE,1);
        day3_day.setText(dateFormatter1.format(cal.getTime()));
        String day1Status2 = day1_date.get(2).getStatus();
        boolean statusmatch2 = false;
        String Statusset2 = "";
        if(!statusmatch2) {
            for (int i = 0; i < sunnyCondition().size(); i++) {
                if (sunnyCondition().get(i).contains(day1Status2)) {
                    Statusset2 = "Sunny";
                    statusmatch2 = true;
                    break;
                }
            }
        }else if(!statusmatch2){
            for(int i = 0; i<cloudycondition().size();i++){
                if(cloudycondition().get(i).contains(day1Status2)){
                    Statusset2 = "Cloudy";
                    statusmatch2 = true;
                    break;
                }
            }
        }else if(!statusmatch2){
            for(int i = 0; i<halecondition().size();i++){
                if(halecondition().get(i).contains(day1Status2)){
                    Statusset2 = "Hail";
                    statusmatch2 = true;
                    break;
                }
            }
        }else if(!statusmatch2){
            for(int i = 0; i<lightningcondition().size();i++){
                if(lightningcondition().get(i).contains(day1Status2)){
                    Statusset2 = "Lightning";
                    statusmatch2 = true;
                    break;
                }
            }
        }else if(!statusmatch2){
            for(int i = 0; i<partycloudyconditon().size();i++){
                if(partycloudyconditon().get(i).contains(day1Status2)){
                    Statusset2 = "PartlyCloudy";
                    statusmatch2 = true;
                    break;
                }
            }
        }else if(!statusmatch2){
            for(int i = 0; i<raincondtioton().size();i++){
                if(raincondtioton().get(i).contains(day1Status2)){
                    Statusset2 = "Rain";
                    statusmatch2 = true;
                    break;
                }
            }
        }else if(!statusmatch2){
            for(int i = 0; i<wincondtioton().size();i++){
                if(wincondtioton().get(i).contains(day1Status2)){
                    Statusset2 = "Windy";
                    statusmatch2 = true;
                    break;
                }
            }
        }
        if(Statusset2.equals("Sunny")){
            day3_pho.setBackgroundResource(R.drawable.sunny);
        }else if(Statusset2.equals("Windy")){
            //day1_pho.setText(day1_date.get(0).getStatus());
            day3_pho.setBackgroundResource(R.drawable.cloudy);
        }else if(Statusset2.equals("Rain")){
            //day1_pho.setText(day1_date.get(0).getStatus());
            day3_pho.setBackgroundResource(R.drawable.rain);
        }else if(Statusset2.equals("PartlyCloudy")){
            //day1_pho.setText(day1_date.get(0).getStatus());
            day3_pho.setBackgroundResource(R.drawable.partlycloudy);
        }else if(Statusset2.equals("Lightning")){
            //day1_pho.setText(day1_date.get(0).getStatus());
            day3_pho.setBackgroundResource(R.drawable.lightning);
        }else if(Statusset2.equals("Hail")){
            //day1_pho.setText(day1_date.get(0).getStatus());
            day3_pho.setBackgroundResource(R.drawable.hale);
        }else{
            //day1_pho.setText(day1_date.get(0).getStatus());
            day3_pho.setBackgroundResource(R.drawable.cloudy);
        }

        day3_max_min.setText(day1_date.get(2).getMin()+"° /"+day1_date.get(2).getMax()+" °");

        //Forecast day4 view
        cal.add(Calendar.DATE,1);
        day4_day.setText(dateFormatter1.format(cal.getTime()));
        String day1Status3 = day1_date.get(3).getStatus();
        boolean statusmatch3 = false;
        String Statusset3 = "";
        if(!statusmatch3) {
            for (int i = 0; i < sunnyCondition().size(); i++) {
                if (sunnyCondition().get(i).contains(day1Status3)) {
                    Statusset3 = "Sunny";
                    break;
                }
            }
        }else if(!statusmatch3){
            for(int i = 0; i<cloudycondition().size();i++){
                if(cloudycondition().get(i).contains(day1Status3)){
                    Statusset3 = "Cloudy";
                    break;
                }
            }
        }else if(!statusmatch3){
            for(int i = 0; i<halecondition().size();i++){
                if(halecondition().get(i).contains(day1Status3)){
                    Statusset3 = "Hail";
                    break;
                }
            }
        }else if(!statusmatch3){
            for(int i = 0; i<lightningcondition().size();i++){
                if(lightningcondition().get(i).contains(day1Status3)){
                    Statusset3 = "Lightning";
                    break;
                }
            }
        }else if(!statusmatch3){
            for(int i = 0; i<partycloudyconditon().size();i++){
                if(partycloudyconditon().get(i).contains(day1Status3)){
                    Statusset3 = "PartlyCloudy";
                    break;
                }
            }
        }else if(!statusmatch3){
            for(int i = 0; i<raincondtioton().size();i++){
                if(raincondtioton().get(i).contains(day1Status3)){
                    Statusset3 = "Rain";
                    break;
                }
            }
        }else if(!statusmatch3){
            for(int i = 0; i<wincondtioton().size();i++){
                if(wincondtioton().get(i).contains(day1Status3)){
                    Statusset3 = "Windy";
                    break;
                }
            }
        }
        if(Statusset3.equals("Sunny")){
            day4_pho.setBackgroundResource(R.drawable.sunny);
        }else if(Statusset3.equals("Windy")){
            //day1_pho.setText(day1_date.get(0).getStatus());
            day4_pho.setBackgroundResource(R.drawable.cloudy);
        }else if(Statusset3.equals("Rain")){
            //day1_pho.setText(day1_date.get(0).getStatus());
            day4_pho.setBackgroundResource(R.drawable.rain);
        }else if(Statusset3.equals("PartlyCloudy")){
            //day1_pho.setText(day1_date.get(0).getStatus());
            day4_pho.setBackgroundResource(R.drawable.partlycloudy);
        }else if(Statusset3.equals("Lightning")){
            //day1_pho.setText(day1_date.get(0).getStatus());
            day4_pho.setBackgroundResource(R.drawable.lightning);
        }else if(Statusset3.equals("Hail")){
            //day1_pho.setText(day1_date.get(0).getStatus());
            day4_pho.setBackgroundResource(R.drawable.hale);
        }else{
            //day1_pho.setText(day1_date.get(0).getStatus());
            day4_pho.setBackgroundResource(R.drawable.cloudy);
        }


        day4_max_min.setText(day1_date.get(3).getMin()+"° /"+day1_date.get(3).getMax()+" °");

        //Forecast day5 view
        cal.add(Calendar.DATE,1);
        day5_day.setText(dateFormatter1.format(cal.getTime()));
        String day1Status4 = day1_date.get(4).getStatus();
        boolean statusmatch4 = false;
        String Statusset4 = "";
        if(!statusmatch4){
            for(int i = 0; i<sunnyCondition().size();i++){
                if(sunnyCondition().get(i).contains(day1Status4)){
                    Statusset4 = "Sunny";

                    break;
                }
            }

            for(int i = 0; i<cloudycondition().size();i++){
                if(cloudycondition().get(i).contains(day1Status4)){
                    Statusset4 = "Cloudy";
                    break;
                }
            }


            for(int i = 0; i<halecondition().size();i++){
                if(halecondition().get(i).contains(day1Status4)){
                    Statusset4 = "Hail";
                    break;
                }
            }

            for(int i = 0; i<lightningcondition().size();i++){
                if(lightningcondition().get(i).contains(day1Status4)){
                    Statusset4 = "Lightning";
                    break;
                }
            }

            for(int i = 0; i<partycloudyconditon().size();i++){
                if(partycloudyconditon().get(i).contains(day1Status4)){
                    Statusset4 = "PartlyCloudy";
                    break;
                }
            }

            for(int i = 0; i<raincondtioton().size();i++){
                if(raincondtioton().get(i).contains(day1Status4)){
                    Statusset4 = "Rain";
                    break;
                }
            }

            for(int i = 0; i<wincondtioton().size();i++){
                if(wincondtioton().get(i).contains(day1Status4)){
                    Statusset4 = "Windy";
                    break;
                }
            }
        }


        if(Statusset4.equals("Sunny")){
            day5_pho.setBackgroundResource(R.drawable.sunny);
        }else if(Statusset4.equals("Windy")){
            //day1_pho.setText(day1_date.get(0).getStatus());
            day5_pho.setBackgroundResource(R.drawable.cloudy);
        }else if(Statusset4.equals("Rain")){
            //day1_pho.setText(day1_date.get(0).getStatus());
            day5_pho.setBackgroundResource(R.drawable.rain);
        }else if(Statusset4.equals("PartlyCloudy")){
            //day1_pho.setText(day1_date.get(0).getStatus());
            day5_pho.setBackgroundResource(R.drawable.partlycloudy);
        }else if(Statusset4.equals("Lightning")){
            //day1_pho.setText(day1_date.get(0).getStatus());
            day5_pho.setBackgroundResource(R.drawable.lightning);
        }else if(Statusset4.equals("Hail")){
            //day1_pho.setText(day1_date.get(0).getStatus());
            day5_pho.setBackgroundResource(R.drawable.hale);
        }else{
            //day1_pho.setText(day1_date.get(0).getStatus());
            day5_pho.setBackgroundResource(R.drawable.cloudy);
        }


        day5_max_min.setText(day1_date.get(4).getMin()+"° /"+day1_date.get(4).getMax()+" °");

        //Set background photos.
        RelativeLayout relativeLayout =(RelativeLayout)view.findViewById(R.id.weather_detail_fragment);
        if(city.equals("Auckland")){
            relativeLayout.setBackgroundResource(R.drawable.autumn);
        }else if(city.equals("Palmerston North")){
            relativeLayout.setBackgroundResource(R.drawable.weather);
        }else if(city.equals("Wellington")){
            relativeLayout.setBackgroundResource(R.drawable.dark);
        }else if(city.equals("Hamilton")){
            relativeLayout.setBackgroundResource(R.drawable.dirty);
        }else if(city.equals("Dunedin")){
            relativeLayout.setBackgroundResource(R.drawable.ginkgo);
        }else if(city.equals("New Plymouth")){
            relativeLayout.setBackgroundResource(R.drawable.landscape);
        }else if(city.equals("Nelson")){
            relativeLayout.setBackgroundResource(R.drawable.mountains);
        }else if(city.equals("Whangarei")){
            relativeLayout.setBackgroundResource(R.drawable.mountain);
        }else if(city.equals("Christchurch")){
            relativeLayout.setBackgroundResource(R.drawable.stormy);
        }else if(city.equals("Greymouth")){
            relativeLayout.setBackgroundResource(R.drawable.sakura);
        }else if(city.equals("Napier")){
            relativeLayout.setBackgroundResource(R.drawable.sunset);
        }else if(city.equals("Invercargill")){
            relativeLayout.setBackgroundResource(R.drawable.waterb);
        }else{
            relativeLayout.setBackgroundResource(R.drawable.land);
        }

        // Set gps icon display only on location page.
        if(getArguments().getString(_image).equals("Yes")){
            ImageView imageView =(ImageView)view.findViewById(R.id.gps);
            imageView.setPadding(0,0,55,0);
            imageView.setImageResource(R.drawable.location_icon);
        }
        return view;
    }

    public ArrayList<String> sunnyCondition(){

        ArrayList<String> sunnycon = new ArrayList<String>();
        sunnycon.add("Sunny");
        sunnycon.add("Fair");
        sunnycon.add("Hot");
        sunnycon.add("Clear");
        return sunnycon;
    }

    public ArrayList<String> cloudycondition(){

        ArrayList<String> cloudycon = new ArrayList<String>();
        cloudycon.add("Cloudy");
        return cloudycon;
    }

    public ArrayList<String> halecondition(){

        ArrayList<String> halecon = new ArrayList<String>();
        halecon.add("Hail");
        halecon.add("Cold");
        halecon.add("Foggy");
        halecon.add("Snow");
        return halecon;
    }

    public ArrayList<String> lightningcondition(){

        ArrayList<String> lightcon = new ArrayList<String>();
        lightcon.add("Storm");
        lightcon.add("Thunderstorms");
        return lightcon;
    }

    public ArrayList<String> partycloudyconditon(){

        ArrayList<String> partlycoludycon = new ArrayList<String>();
        partlycoludycon.add("Partly Cloudy");
        partlycoludycon.add("Drizzle");
        return partlycoludycon;
    }

    public ArrayList<String> raincondtioton(){

        ArrayList<String> raincon = new ArrayList<String>();
        raincon.add("Rain");
        raincon.add("Showers");
        return raincon;
    }

    public ArrayList<String> wincondtioton(){

        ArrayList<String> wincon = new ArrayList<String>();
        wincon.add("Windy");
        return wincon;
    }


}
