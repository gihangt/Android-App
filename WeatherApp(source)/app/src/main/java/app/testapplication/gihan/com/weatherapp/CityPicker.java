package app.testapplication.gihan.com.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * This class direct select_city layout.
 * Show the available city on drop down menu.
 */
public class CityPicker extends ActionBarActivity {

    private XMLYahoo yahoo;
    String[] Cities = new String[] {
            "Auckland", "Wellington", "Dunedin", "Whangarei", "Napier"
            ,"Christchurch","Hamilton","New Plymouth","Greymouth","Invercargill"
            ,"Queenstown","Palmerston North","Nelson"
    };

    InternetDetector checkInternet;
    Boolean internet = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide actionBars
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.select_city);


        //Initialize auto complete text view.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, Cities);
        final AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.autoCompleteTextView);
        textView.setAdapter(adapter);

        //Set the text view click listener and pass city to API reader.
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checkInternet = new InternetDetector(getApplicationContext());
                internet = checkInternet.isConnectionAvailable();
                if(internet){
                    String name = String.valueOf(parent.getItemAtPosition(position));
                    int WOEID = WOEIDSelection(name);

                    //Read weather details related to the selected city.
                    try{
                        String finalUrl = "http://weather.yahooapis.com/forecastrss?w="+WOEID+"&u=c";
                        ArrayList<Weather> weatherList = AllWeather.get(getBaseContext()).getWeatherList();
                        yahoo = new XMLYahoo(finalUrl);
                        yahoo.fetchXML();

                        while (yahoo.parsingComplete);
                        weatherList.add(yahoo.getCityWeather());
                        MyActivity.list.add(ModuleFragment.newInstance(yahoo.getCityWeather().getCityName(), yahoo.getCityWeather().getStatus()
                                , yahoo.getCityWeather().getTemperature(), yahoo.getCityWeather().getForecast(), yahoo.getCityWeather().getImage()));

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    //Update view pager.
                    MyActivity.cf.notifyDataSetChanged();

                    //Set viewpager display page to newly added city.
                    MyActivity.vp.setCurrentItem(MyActivity.list.size()-1);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"Network Error",Toast.LENGTH_LONG).show();

                }

            }
        });

        //Set display back button action.
        //Close the current activity.
        TextView back = (TextView)findViewById(R.id.back_text_view);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    /**
     * Set the physical back button action to close
     * current view.
     */
    @Override
    public void onBackPressed()
    {
        finish();
    }

    /**
     * Set the Where on Earth ID to selected city.
     * @param cityName
     * @return
     */
    public int WOEIDSelection(String cityName){
        int WOEID;
        if(cityName.equalsIgnoreCase("Auckland")){
            WOEID =2348079;
        }else if(cityName.equalsIgnoreCase("Wellington")){
            WOEID=2351310;
        }else if(cityName.equalsIgnoreCase("Dunedin")){
            WOEID=2348444;
        }else if(cityName.equalsIgnoreCase("Whangarei")){
            WOEID=2351368;
        }else if(cityName.equalsIgnoreCase("Napier")){
            WOEID=2349663;
        }else if(cityName.equalsIgnoreCase("Christchurch")){
            WOEID=2348327;
        }else if(cityName.equalsIgnoreCase("Hamilton")){
            WOEID=2348696;
        }else if(cityName.equalsIgnoreCase("New Plymouth")){
            WOEID=2349680;
        }else if(cityName.equalsIgnoreCase("Greymouth")){
            WOEID=2348669;
        }else if(cityName.equalsIgnoreCase("Invercargill")){
            WOEID=2348887;
        }else if(cityName.equalsIgnoreCase("Queenstown")){
            WOEID=2350303;
        }else if(cityName.equalsIgnoreCase("Palmerston North")){
            WOEID=2350025;
        }else if(cityName.equalsIgnoreCase("Nelson")){
            WOEID=2349669;
        }else{
            WOEID=2350025;
        }
        return WOEID;
    }

    /**
     * Set scroll speed of viewpager.
     */
    public class ViewPagerScroller extends Scroller {
        private int scrollDuration = 0;
        public ViewPagerScroller(Context context) {
            super(context);
        }
        public ViewPagerScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }
        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, scrollDuration);

        }
        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, scrollDuration);
        }

        public  void setScrollDuration(int duration){
            this.scrollDuration = duration;
        }

    }

}
