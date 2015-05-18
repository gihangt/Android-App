package app.testapplication.gihan.com.weatherapp;


import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.*;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;
/**
 * This Main class. Create fragment to viewpager.
 */

public class MyActivity extends FragmentActivity {

    public  static ViewPager vp;
    public  static ArrayList<Fragment> list;
    public static ControlModuleFragment cf;
    public  ArrayList<Weather> newlist;

    //Create Fragment list.
    private ArrayList<Fragment> getFragments(){
        list = new ArrayList<Fragment>();
        newlist = new ArrayList<Weather>();
        String Status=ReadStatus();

        //Read city list
        if (Status !=null&&Status.equals("Off")) {
            ArrayList<Weather> Slist = AllWeather.get(getApplicationContext()).getWeatherList();

            //Remove old geo location
            if(Slist.size()>1) {
                for (int i = 1; i < Slist.size(); i++) {
                    Weather w = Slist.get(i);
                    newlist.add(w);
                }
            }else {
                newlist = AllWeather.get(getApplicationContext()).getWeatherList();
            }
        } else {
            newlist = AllWeather.get(getApplicationContext()).getWeatherList();

        }

        //Save current selected city.
        Save(newlist);

        //Add the city to the fragment list.
        for(Weather weather:newlist){
            list.add(ModuleFragment.newInstance(weather.getCityName(), weather.getStatus()
                    , weather.getTemperature(),weather.getForecast(),weather.getImage()));
        }
        return list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Hide action bar and notification bar.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my);

        //Select the viewpager
        vp = (ViewPager)findViewById(R.id.view_pager);
        //changePagerScroller();
        cf = new ControlModuleFragment(getSupportFragmentManager(),getFragments());
        vp.setAdapter(cf);

        //Set the viewPager action.
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            //Change default page selection settings.
            @Override
            public void onPageSelected(int position) {
                InternetDetector detect = new InternetDetector(getApplicationContext());
                Boolean internet = detect.isConnectionAvailable();

                //Set the first page select action of viewpager.
                if(position==0){

                    //Check the internet connection.
                    if(internet){
                        LocationFinder finder = new LocationFinder(getApplicationContext());

                        //Check the GPS settings.
                        if(finder.canGetLocation){
                            //Do nothing.
                        }else{
                            Toast.makeText(getBaseContext(),"Please Allow to use Location Service",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getBaseContext(),"Network Error",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    //Check the connection when other page select.
                    if(internet){
                        //Do Nothing.
                    }else{
                        Toast.makeText(getBaseContext(),"Network Error",Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //Do Nothing.
            }
        });

        //Check the GPS settings when the application load.
        LocationFinder finder = new LocationFinder(getApplicationContext());
        if(finder.canGetLocation){
            //Do Nothing.
        }else{

            //Set the setting alert and the direction.
            Toast.makeText(getBaseContext(),"Please Allow to use Location Service",Toast.LENGTH_SHORT).show();
            showSettingsAlert();
        }

    }

    //This application dose not use action bar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu;
        // this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    //This application does not use menu bar.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Set back button action.
    @Override
    public void onBackPressed()
    {
        //Save city list when back button press.
        Save(newlist);
        moveTaskToBack(true);
    }

    //Save city object list on .dat file.
    public void Save(ArrayList<Weather> newList) {
        PrintWriter writer = null;
        try{
            FileOutputStream fileOut = openFileOutput("weather.dat", MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream (fileOut);
            for(Weather w :newList){
                oos.writeObject (w);
            }
            oos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //Read city list.
    public ArrayList<Weather> Read(){
        ArrayList<Weather> newlist1 = new ArrayList<Weather>();
        FileInputStream fis = null;
        ObjectInputStream in = null;
        Objects objects=null;
        try {
            fis = openFileInput("weather.dat");
            in = new ObjectInputStream(fis);
            int line = 0;
            while (fis.available()>0) {
                newlist1.add((Weather) in.readObject());
                line++;
            }

            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return newlist1;
    }

    //Save status of Location service on .dat file.
    public void Save(String switchStatus) {
        PrintWriter writer = null;
        try{
            FileOutputStream fileOut = openFileOutput("switchStatus.dat", MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream (fileOut);
            oos.writeObject (switchStatus);
            oos.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //Read status of Location service from .dat file.
    public String ReadStatus(){
        String status =null;
        FileInputStream fis = null;
        ObjectInputStream in = null;
        Objects objects=null;
        try {
            fis = openFileInput("switchStatus.dat");
            in = new ObjectInputStream(fis);
            status =((String) in.readObject());
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return status ;
    }

    //Change the viewpager speed.
    public class ViewPagerScroller extends Scroller {
        private int scrollDuration = 500;
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
    }

    //show setting dialog alert.
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MyActivity.this);
        // Title
        alertDialog.setTitle("GPS is settings");
        //Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        // Settings button action.
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                MyActivity.this.startActivity(intent);
            }
        });
        //cancel button action.
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        Toast.makeText(MyActivity.this,"Please Allow to use Location Service",Toast.LENGTH_LONG ).show();
        // Call Alert
        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }
}


