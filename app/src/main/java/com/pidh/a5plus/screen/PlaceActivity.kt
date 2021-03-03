package com.pidh.a5plus.screen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.Navigation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.*
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.pidh.a5plus.R
import com.pidh.a5plus.databinding.ActivityPlaceBinding
import com.pidh.a5plus.helper.PermissionUtils.PermissionDeniedDialog.Companion.newInstance
import com.pidh.a5plus.helper.PermissionUtils.isPermissionGranted
import com.pidh.a5plus.helper.PermissionUtils.requestPermission
import com.pidh.a5plus.helper.events.OnAppClickListener
import com.pidh.a5plus.provider.maps.entities.PlaceInfo
import com.pidh.a5plus.screen.adapter.PlacesAdapter

class PlaceActivity : AppCompatActivity(), OnMapReadyCallback, OnAppClickListener {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private var permissionDenied = false
    private lateinit var mLocationServices: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap
    private lateinit var mPlacesClient: PlacesClient
    private lateinit var mToken: AutocompleteSessionToken
    private lateinit var mUserLocation: Location
    private lateinit var mAdapter: PlacesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAdapter = PlacesAdapter(this)

        ActivityPlaceBinding.inflate(layoutInflater).apply {
            setContentView(root)

            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            rvPlaces.adapter = mAdapter
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mLocationServices = LocationServices.getFusedLocationProviderClient(this)
        Places.initialize(this, getString(R.string.google_api_key))
        mPlacesClient = Places.createClient(this)
        mToken = AutocompleteSessionToken.newInstance()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setMinZoomPreference(10.0f)
        mMap.setMaxZoomPreference(20.0f)

        enableMyLocation()
    }

    override fun onItemClick(place: PlaceInfo) {
        val uri =
            "http://maps.google.com/maps?saddr=${place.userLatitude},${place.userLongitude}&daddr=${place.latitude},${place.longitude}"

        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
    }

    private fun enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION, true
            )
        } else {
            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = true

            mLocationServices.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    mUserLocation = it

                    val latLngLocation = LatLng(location.latitude, location.longitude)

                    mMap.addMarker(
                        MarkerOptions()
                            .position(latLngLocation)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_user))
                            .title(getString(R.string.i_am_here))
                    )
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngLocation, 13.0f))
                    mMap.setOnCameraIdleListener {
                        placesOnTheMaps()
                    }
                }
            }.addOnFailureListener { e ->
                Log.d("place@lastLocationUser", e.message, e)
            }
        }
    }

    private fun placesOnTheMaps() {
        val predictionsRequest = FindAutocompletePredictionsRequest.builder()
            .setTypeFilter(TypeFilter.ESTABLISHMENT)
            .setSessionToken(mToken)
            .setLocationRestriction(
                RectangularBounds.newInstance(
                    mMap.projection.visibleRegion.latLngBounds
                )
            )
            .setQuery("cinema")
            .build()

        mPlacesClient.findAutocompletePredictions(predictionsRequest)
            .addOnSuccessListener { response ->
                val predictions = response.autocompletePredictions
                for (prediction in predictions) {
                    if (prediction.placeTypes.contains(Place.Type.MOVIE_THEATER))
                        placeAddMarker(prediction)
                }
            }
            .addOnFailureListener { error ->
                Log.d("Place@placesClient", error.message, error)
            }
    }

    private fun placeAddMarker(prediction: AutocompletePrediction) {
        val fields: List<Place.Field> =
            listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)

        val request = FetchPlaceRequest.builder(prediction.placeId, fields)
            .setSessionToken(mToken)
            .build()

        mPlacesClient.fetchPlace(request)
            .addOnSuccessListener { response ->
                val place = response.place
                val latLng = place.latLng

                mMap.addMarker(MarkerOptions().position(latLng!!).title(place.name))

                addItemPlaceList(place)
            }
    }

    private fun addItemPlaceList(place: Place) {
        val latLng = place.latLng

        val placeLocation = Location("placeLocation")
        latLng?.let {
            placeLocation.latitude = it.latitude
            placeLocation.longitude = it.longitude
        }

        val distanceKm = mUserLocation.distanceTo(placeLocation) / 1000

        val placeInfo = PlaceInfo(
            place.id!!,
            place.name!!,
            place.address!!,
            placeLocation.latitude,
            placeLocation.longitude,
            mUserLocation.latitude,
            mUserLocation.longitude,
            distanceKm
        )

        mAdapter.addPlace(placeInfo)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return
        }
        if (isPermissionGranted(
                permissions,
                grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            enableMyLocation()
        } else {
            permissionDenied = true
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        if (permissionDenied) {
            newInstance(true).show(supportFragmentManager, "dialog")
            permissionDenied = false
        }
    }
}