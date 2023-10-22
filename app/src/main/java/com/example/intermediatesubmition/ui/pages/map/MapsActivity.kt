package com.example.intermediatesubmition.ui.pages.map

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.intermediatesubmition.R
import com.example.intermediatesubmition.data.remote.response.ListStoryItem
import com.example.intermediatesubmition.data.remote.response.StoryResponse
import com.example.intermediatesubmition.data.remote.retrofit.ApiConfig
import com.example.intermediatesubmition.data.database.UserPreferences
import com.example.intermediatesubmition.data.database.dataStore
import com.example.intermediatesubmition.ui.viewmodel.UserViewModel

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.intermediatesubmition.databinding.ActivityMapsBinding
import com.example.intermediatesubmition.helper.UserModelFactory
import com.example.intermediatesubmition.ui.pages.login.LoginActivity
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = UserPreferences.getInstance(application.dataStore)
        val mainViewModel = ViewModelProvider(this, UserModelFactory(pref)).get(
            UserViewModel::class.java
        )

        mainViewModel.getToken().observe(this){
                token ->
            if (token == "null"){
                toLogin()
            }
            else{
                getStoryWithLocationApi(token)
            }
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(0.143136, 118.7371783)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker Indonesia"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        mMap.setOnPoiClickListener { pointOfInterest ->
            val poiMarker = mMap.addMarker(
                MarkerOptions()
                    .position(pointOfInterest.latLng)
                    .title(pointOfInterest.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            )
            poiMarker?.showInfoWindow()
        }
    }

    private fun getStoryWithLocationApi(token: String){
        val client = ApiConfig.getApiService().getStoriesWithLocation("Bearer "+token)
        client.enqueue(object : Callback<StoryResponse>{
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                if (response.isSuccessful){
                    val responseBody = response.body()!!.listStory
                    addManyMarker(responseBody)
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun toLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun addManyMarker(storyList : List<ListStoryItem>){
        val list = storyList

        list.forEach{ storyPosition ->
            val latLng = LatLng(storyPosition.lat.toString().toDouble(), storyPosition.lon.toString().toDouble())
            mMap.addMarker(MarkerOptions().position(latLng).title(storyPosition.name))
        }
    }
}