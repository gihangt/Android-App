package app.testapplication.gihan.com.weatherapp;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Create file manager to read and write .dat file.
 */
public class FileManager {
    private Context context;
    public FileManager(Context getContext){
        context = getContext;
    }

    /**
     * Save city list.
     * @param newList
     */
    public void Save(ArrayList<Weather> newList) {
        PrintWriter writer = null;
        try{
            FileOutputStream fileOut = context.openFileOutput("weather.dat", context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream (fileOut);
            for(Weather w :newList){
                oos.writeObject (w);
            }
            oos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Read city list from .dat file.
     * @return
     */
    public ArrayList<Weather> Read(){
        ArrayList<Weather> newlist1 = new ArrayList<Weather>();
        FileInputStream fis = null;
        ObjectInputStream in = null;
        Objects objects=null;
        try {
            fis = context.openFileInput("weather.dat");
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

}
