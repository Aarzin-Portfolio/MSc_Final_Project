package com.leaf.birdtracking.OnSup

import android.Manifest
import android.annotation.SuppressLint
import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import com.leaf.birdtracking.Adapter.SearchAdapter
import com.leaf.birdtracking.database.AllTables
import kotlinx.android.synthetic.main.activity_bird_serch.*
import android.widget.Toast
import android.app.SearchManager
import android.content.Context
import android.content.IntentSender
import android.location.Location
import android.os.*
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.SearchView
import com.ebizzApps.sevenminuteworkout.OnSup.CNF
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.leaf.birdtracking.R
import kotlinx.android.synthetic.main.activity_birds_detail.*
import java.lang.Exception


class BirdSerch : AppCompatActivity(), OnMapReadyCallback {


    private var customAdapter: SearchAdapter? = null
    var cursor: Cursor? = null
    lateinit var allTables: AllTables

    //private val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10000;
    private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = 5000;

    private val REQUEST_CHECK_SETTINGS = 1002;

    lateinit var mMap: GoogleMap
    var mapReady = false
    var lat: Double = 0.0
    var lon: Double = 0.0

    private lateinit var mFusedLocationClient: FusedLocationProviderClient;
    private lateinit var mSettingsClient: SettingsClient;
    private lateinit var mLocationRequest: LocationRequest;
    private lateinit var mLocationSettingsRequest: LocationSettingsRequest;
    private lateinit var mLocationCallback: LocationCallback;
    private lateinit var mCurrentLocation: Location;
    private var mRequestingLocationUpdates: Boolean = false;
    lateinit var cnf: CNF

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bird_serch)

        someTask(this@BirdSerch).execute()

    }

    private fun searchData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
            searchView.setSearchableInfo(manager.getSearchableInfo(componentName))
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(s: String?): Boolean {
                    cursor = allTables.getBirdListByKeyword(s!!)
                    if (cursor == null) {
                        Toast.makeText(this@BirdSerch, "No records found!", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@BirdSerch, cursor!!.getCount().toString() + " records found!", Toast.LENGTH_LONG).show()
                    }
                    customAdapter!!.swapCursor(cursor)

                    return false
                }

                override fun onQueryTextChange(s: String?): Boolean {
                    if(s!!.length!=0) {
                        lay_map.visibility = View.GONE
                        listView_search.visibility = View.VISIBLE
                        cursor = allTables.getBirdListByKeyword(s!!)
                        if (cursor != null) {
                            customAdapter!!.swapCursor(cursor)
                        }
                    }else
                    {
                        lay_map.visibility = View.GONE
                        listView_search.visibility = View.GONE
                    }
                    return false
                }
            })

        }
    }


    @SuppressLint("RestrictedApi")
    fun locationEnable(lat: Double, lon: Double) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                mCurrentLocation = locationResult!!.getLastLocation();
                updateUI(lat, lon)

            }
        }

        mRequestingLocationUpdates = false
        mLocationRequest = LocationRequest()
        //mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        var builder = LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    fun updateUI(lat: Double, lon: Double) {
        if (mapReady) {
            mMap.clear()
            var latlong = LatLng(lat, lon)
            mMap.mapType = com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latlong))
            mMap.animateCamera(CameraUpdateFactory.zoomTo(2F))
            if (cursor != null && cursor!!.moveToFirst()) {
                do {
                    val lat1 = cursor!!.getString(cursor!!.getColumnIndexOrThrow(AllTables.LATITUDE)).toDouble()
                    val lon1 = cursor!!.getString(cursor!!.getColumnIndexOrThrow(AllTables.LONGITUDE)).toDouble()
                    var latlong = LatLng(lat1, lon1)
                    mMap.addMarker(MarkerOptions().position(latlong).title(""))

                } while (cursor!!.moveToNext())
            }
        } else {
            tD("map is not ready")
        }

    }

    fun PermissionCheckLocation(lat: Double, lon: Double) {
        Dexter.withActivity(this@BirdSerch)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION

                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            locationEnable(lat, lon)
                            startLocationUpdates()

                        }

                        if (report.isAnyPermissionPermanentlyDenied) {
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                })
                .onSameThread()
                .check()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!;
        mapReady = true
    }


    fun startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, object : OnSuccessListener<LocationSettingsResponse> {
                    @SuppressLint("MissingPermission")
                    override fun onSuccess(p0: LocationSettingsResponse?) {
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());
                    }

                })
                .addOnFailureListener(this, object : OnFailureListener {
                    override fun onFailure(p0: Exception) {

                        if (p0 is ResolvableApiException) {
                            try {
                                p0.startResolutionForResult(this@BirdSerch,
                                        REQUEST_CHECK_SETTINGS)
                            } catch (sendEx: IntentSender.SendIntentException) {

                            }
                        }

                    }

                })

    }


    fun tD(m: String) {
        Toast.makeText(this@BirdSerch, m, Toast.LENGTH_LONG).show()
    }


    inner class someTask(val context: Context) : AsyncTask<Cursor, Cursor, Cursor>() {
        override fun onPreExecute() {
            super.onPreExecute()
            search_progress.visibility = View.VISIBLE
            searchView.visibility = View.GONE
            allTables = AllTables(context, null)

        }

        override fun doInBackground(vararg params: Cursor?): Cursor {
            cursor = allTables.getBirdCursor()
            runOnUiThread {
                customAdapter = SearchAdapter(context, cursor!!, 0)
                listView_search.adapter = customAdapter
            }
            return cursor!!
        }


        override fun onPostExecute(cursor: Cursor?) {
            super.onPostExecute(cursor)
            if (cursor != null) {
                search_progress.visibility = View.GONE
                searchView.visibility = View.VISIBLE
                searchData()

                listView_search.onItemClickListener = object : AdapterView.OnItemClickListener {
                    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        if (listView_search.visibility == View.VISIBLE) {
                            lay_map.visibility = View.VISIBLE
                            lat = cursor!!.getString(cursor!!.getColumnIndexOrThrow(AllTables.LATITUDE)).toDouble()
                            lon = cursor!!.getString(cursor!!.getColumnIndexOrThrow(AllTables.LONGITUDE)).toDouble()

                            locationEnable(lat, lon)
                            PermissionCheckLocation(lat, lon)
                            var map: SupportMapFragment? = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                            map?.getMapAsync(this@BirdSerch)

                            hideSoftKeyBoard(context, view!!)
                        }
                    }
                }

            } else {
                Toast.makeText(this@BirdSerch, "Please insert bird data first..!!", Toast.LENGTH_LONG).show()
            }

        }
    }

    fun hideSoftKeyBoard(context: Context, view: View) {
        try {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        } catch (e: Exception) {
            // TODO: handle exception
            e.printStackTrace()
        }

    }

}
