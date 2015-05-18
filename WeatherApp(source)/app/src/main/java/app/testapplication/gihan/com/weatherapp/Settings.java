package app.testapplication.gihan.com.weatherapp;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Create setting page view.
 */
public class Settings extends FragmentActivity {

    Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide actionBars
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.setting_page);
        aSwitch = (Switch)findViewById(R.id.switch_location);

        //Check .dat file.
        try{
            FileInputStream fis = openFileInput("switchStatus.dat");
        }catch (Exception e){
            Save("On");
        }

        String status = Read();
        if(status.equals("On")){
            aSwitch.setChecked(true);
        }else if(status.equals("Off")){
            aSwitch.setChecked(false);
        }else {
            aSwitch.setChecked(true);
        }

        //Set the Back button click action.
        TextView back = (TextView)findViewById(R.id.setting_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check location service option.
                aSwitch = (Switch)findViewById(R.id.switch_location);
                boolean swichStatues = aSwitch.isChecked();
                String statusOfSwitch;

                if(MyActivity.list.size()<2){
                    if(swichStatues){
                        statusOfSwitch = "On";
                        //Save current location service option.
                        Save(statusOfSwitch);

                        //Call main thread and close current one.
                        Intent intent = new Intent(Settings.this,MyActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        statusOfSwitch = "Off";
                        Toast.makeText(getApplicationContext(),"Not Allow to turn off location service,Add a city first",Toast.LENGTH_LONG).show();
                    }

                }else{
                    if(swichStatues){
                        statusOfSwitch = "On";
                    }else {
                        statusOfSwitch = "Off";
                        Toast.makeText(getApplicationContext(),"Location Service Turn Off",Toast.LENGTH_LONG).show();
                    }

                    //Save current location service option.
                    Save(statusOfSwitch);

                    //Call main thread and close current one.
                    Intent intent = new Intent(Settings.this,MyActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

        //Display current city list.
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.city_list_fragment);
        if(fragment ==null){
            fragment = new ListofCity_setting();
            fragmentManager.beginTransaction()
                    .add(R.id.city_list_fragment, fragment)
                    .commit();
        }
    }

    /**
     * Set physical back button action.
     */
    @Override
    public void onBackPressed()
    {
        //Call main Thread.
        Intent intent = new Intent(Settings.this,MyActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Save current location service status on .dat file.
     * @param switchStatus
     */
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

    /**
     * Read location service status.
     * @return
     */
    public String Read(){
        String status =null;
        FileInputStream fis = null;
        ObjectInputStream in = null;
        Objects objects=null;
        try {
            fis = openFileInput("switchStatus.dat");
            in = new ObjectInputStream(fis);
            int line = 0;
            while (fis.available()>0) {
                status =((String) in.readObject());
                line++;
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return status ;
    }

}
