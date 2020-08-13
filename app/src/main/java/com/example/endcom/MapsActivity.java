package com.example.endcom;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.endcom.Retrofit.Api;
import com.example.endcom.Retrofit.RetrofitSingleton;
import com.example.endcom.modelo.ApiCityBik;
import com.example.endcom.modelo.Station;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ApiCityBik apiCityBik = new ApiCityBik();
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
               .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        listApiCity();

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }



    private void listApiCity(){
        Api api = RetrofitSingleton.getInstance().getApiService();
        Call<ApiCityBik> call = api.getListCityBik();
        call.enqueue(new Callback<ApiCityBik>() {
            @Override
            public void onResponse(Call<ApiCityBik> call, Response<ApiCityBik> response) {
                apiCityBik = response.body();
                insertarMarcador();
            }
            @Override
            public void onFailure(Call<ApiCityBik> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
            }
        });

    }
     //metodo para insertar e marcador dependiento la latitus y longitud
    private void insertarMarcador() {

        for(Station station : apiCityBik.getNetwork().getStations()){

            if(station.getEmptySlots() < station.getFreeBikes()) { //condicion para bicis disponobles
                LatLng estacion = new LatLng(station.getLatitude(), station.getLongitude()); //
                mMap.addMarker(new MarkerOptions().position(estacion).title(station.getName()  + "/ Bicis disponibles:" + station.getFreeBikes().toString()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(estacion));
            }
             }


    }
}