package app.testapplication.gihan.com.weatherapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.security.UnresolvedPermission;
import java.util.ArrayList;

/**
 * Display current list of selected city except geo location.
 */
public class ListofCity_setting extends ListFragment {

    private ArrayList<Weather> CityList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the ArrayList from AllContacts
        CityList = AllWeather.get(getActivity()).getWeatherList();
        WeatherAdapter contactAdapter = new WeatherAdapter(CityList);
        // Provides the data for the ListView by setting the Adapter
        setListAdapter(contactAdapter);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ((WeatherAdapter)getListAdapter()).notifyDataSetChanged();
    }

    private class WeatherAdapter extends ArrayAdapter<Weather> {
        public WeatherAdapter(ArrayList<Weather> weathers) {
            super(getActivity(), android.R.layout.simple_list_item_1, weathers);
        }


        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {

            // Check if this is a recycled list item and if not we inflate it
            if (convertView == null) {

                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.city_list_view_template, null);

            }

            //Display city list if it is more than one.
            if(position>0){
                final Weather weather = getItem(position);
                TextView cityName =
                        (TextView) convertView.findViewById(R.id.list_city_name);

                cityName.setText(weather.getCityName());

                //Set the delete button action of the city.
                Button delete = (Button)convertView.findViewById(R.id.delete_city);

                //Remove city from the list.
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CityList.remove(position);

                        //Update current list.
                        ((WeatherAdapter)getListAdapter()).notifyDataSetChanged();
                    }
                });

            }
            //Hide delete button when the city list empty.
            else {
                TextView cityName =
                        (TextView) convertView.findViewById(R.id.list_city_name);

                cityName.setText("");
                Button delete = (Button)convertView.findViewById(R.id.delete_city);
                delete.setVisibility(View.GONE);

            }

            // Return the finished list item for display
            return convertView;

        }


    }

}
