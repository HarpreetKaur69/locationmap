package info.android.location;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private GpsTracker gpsTracker;
    private TextView tvLatitude,tvLongitude;
    TextView city,address,state,country,postalCode, altitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLatitude = (TextView)findViewById(R.id.latitude);
        tvLongitude = (TextView)findViewById(R.id.longitude);
        address = (TextView)findViewById(R.id.address);
        city = (TextView)findViewById(R.id.city);
        state = (TextView)findViewById(R.id.state);
        country = (TextView)findViewById(R.id.country);
        postalCode = (TextView)findViewById(R.id.postalCode);
        altitude = (TextView)findViewById(R.id.altitude);

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }


    }

    public void getCompleteAddressString(double latitude, double longitude){
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5


        String addres = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String citys = addresses.get(0).getLocality();
        String getState = addresses.get(0).getAdminArea();
        String getCountry = addresses.get(0).getCountryName();
        String getPostalCode = addresses.get(0).getPostalCode();
       // String knownName = addresses.get(0).getFeatureName();

        address.setText(addres);
        city.setText(citys);
        state.setText(getState);
        country.setText(getCountry);
        postalCode.setText(getPostalCode);
        //tvLongitude.setText(String.valueOf(longitude));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void getLocation(View view){
        gpsTracker = new GpsTracker(MainActivity.this);
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            tvLatitude.setText(String.valueOf(latitude));
            tvLongitude.setText(String.valueOf(longitude));
            getCompleteAddressString(latitude,longitude);
            if (Contacts.location !=null){
                altitude.setText(""+Contacts.location.getAltitude());
            }

        }else{
            gpsTracker.showSettingsAlert();
        }
    }

}