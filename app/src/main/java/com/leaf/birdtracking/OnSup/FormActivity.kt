package com.leaf.birdtracking.OnSup

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import kotlinx.android.synthetic.main.activity_form.*
import android.widget.ArrayAdapter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.provider.MediaStore
import android.content.Intent
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import android.app.ProgressDialog
import android.content.IntentSender
import android.os.AsyncTask
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionRequest
import android.graphics.Bitmap
import android.location.Location
import android.os.Looper
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.ebizzApps.sevenminuteworkout.OnSup.CNF
import com.ebizzApps.sevenminuteworkout.OnSup.CSup
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
import com.leaf.birdtracking.R
import com.leaf.birdtracking.database.AllTables
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.lang.Exception


class FormActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener, View.OnClickListener, OnMapReadyCallback, Callback<String> {
    lateinit var allTables: AllTables


    override fun onFailure(call: Call<String>?, t: Throwable?) {
        Toast.makeText(this, "Something went wrong please try again later!!", Toast.LENGTH_LONG).show()
        Log.e("failllll", "" + call?.request()?.url())
        Log.e("failllll", "" + t?.message)
        progress_info.visibility = View.GONE
        submit.visibility = View.VISIBLE
    }

    override fun onResponse(call: Call<String>?, response: Response<String>?) {
        progress_info.visibility = View.GONE
        submit.visibility = View.VISIBLE
        if (response != null) {
            if (response.isSuccessful) {
                if (response.body().toString().trim().equals("success")) {
                    Toast.makeText(this, "Information saved successfully!!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Something went wrong please try again later!!", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Something went wrong please try again later!!", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "Something went wrong please try again later!!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!;
        mapReady = true

    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.capture_image -> {
                selectOne()

            }
            R.id.submit -> {
                progress_info.visibility = View.VISIBLE
                submit.visibility = View.GONE

                var date = date.text.toString()
                var userName = username.text.toString()
                lat
                lon
                var iC = radio_grp.checkedRadioButtonId
                var s: String=""
                if (iC == R.id.rare) {
                    s = "rare species"
                } else if (iC == R.id.known) {
                    s = "known species"
                } else if (iC == R.id.unknown) {
                    s = "unkonwn species"
                }
                baseImage
                birdname
                val iName = "Bird" + System.currentTimeMillis();


                var success: Long? = 0

                success = allTables.birdInsert(date, userName, "" + lat, "" + lon, birdname, s, baseImage, "", iName)

                if (success!! > 0) {
                    Toast.makeText(this, "Data Inserted Successfully", Toast.LENGTH_LONG).show()
                    progress_info.visibility = View.GONE
                    submit.visibility = View.VISIBLE
                } else {
                    Toast.makeText(this, "Something went wrong please try again later!!", Toast.LENGTH_LONG).show()
                    progress_info.visibility = View.GONE
                    submit.visibility = View.VISIBLE
                }
            }


        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        birdname = categories.get(position)
    }


    val items = arrayOf("Open Camera", "Choose from Gallery ");
    private val REQUEST_CAPTURE_IMAGE = 1001
    private val PICK_FROM_GALLERY = 100

    var lat: Double = 0.0
    var lon: Double = 0.0
    var birdname: String = ""

    var mapReady = false
    lateinit var mLastUpdateTime: String;

    private val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10000;
    private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = 5000;

    private val REQUEST_CHECK_SETTINGS = 1002;

    lateinit var mMap: GoogleMap

    // bunch of location related apis
    private lateinit var mFusedLocationClient: FusedLocationProviderClient;
    private lateinit var mSettingsClient: SettingsClient;
    private lateinit var mLocationRequest: LocationRequest;
    private lateinit var mLocationSettingsRequest: LocationSettingsRequest;
    private lateinit var mLocationCallback: LocationCallback;
    private lateinit var mCurrentLocation: Location;
    private var mRequestingLocationUpdates: Boolean = false;
    lateinit var cnf: CNF



    var baseImage: String=""
    lateinit var categories: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        supportActionBar?.setTitle("Form")

        allTables = AllTables(this, null)

        categories = ArrayList<String>()
        categories.add("Wren")
        categories.add("Firecrest")
        categories.add("Goldcrest")
        categories.add("Great Tit")
        categories.add("Blue Tit")
        categories.add("Marsh Tit")
        categories.add("Willow Tit")
        categories.add("Coal Tit")
        categories.add("Crested Tit")
        categories.add("Long Tailed Tit")
        categories.add("Dunnock")
        categories.add("Robin")
        categories.add("Tree Sparrow")
        categories.add("House Sparrow")
        categories.add("Linnet Twite")
        categories.add("Redpolls")
        categories.add("Greenfinch")
        categories.add("Serin")
        categories.add("Siskin")
        categories.add("Brambling")
        categories.add("Hawfinch")
        categories.add("Bullfinch")
        categories.add("Crossbill")
        categories.add("Irruption")
        categories.add("Fan-tailored Warbler")
        categories.add("Bearded Tit")
        categories.add("Grasshopper Warbler")
        categories.add("Sedge Warbler")
        categories.add("Reed Warbler")
        categories.add("Marsh Warbler")
        categories.add("Savi’s Warbler")
        categories.add("Getti’s Warbler")
        categories.add("Great Reed Warbler")
        categories.add("Barred Warbler")
        categories.add("Wood Warbler")
        categories.add("Bonelli’s Warbler")
        categories.add("Chiffchaff")
        categories.add("Willow Warbler")
        categories.add("Icterine warbler")
        categories.add("Melodious Warbler")
        categories.add("Blackcap")
        categories.add("Garden Warbler")
        categories.add("Whitethroat")
        categories.add("Lesser Whitethroat")
        categories.add("Dartford Warbler")
        categories.add("Sardinian Warbler")
        categories.add("Subalpine Warbler")
        categories.add("Stonechat")

        bird_spinner.setOnItemSelectedListener(this);
        capture_image.setOnClickListener(this)
        submit.setOnClickListener(this)



        cnf = CSup.gN().create(CNF::class.java)


        val dataAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bird_spinner.setAdapter(dataAdapter)
        date.setText(currentDataTime())


        val SharedPreferences = getSharedPreferences("MainPref", 0);
        val userName = SharedPreferences.getString("username", "DFBT20989");
        username.setText(userName);
        locationEnable()
        PermissionCheckLocation()

        var map: SupportMapFragment? = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        map?.getMapAsync(this)
    }

    fun currentDataTime(): String {
        val sdf = SimpleDateFormat("dd/MM/yy  HH:mm:ss ")
        val currentDateandTime = sdf.format(Date())
        return currentDateandTime
    }


    fun selectOne() {
        AlertDialog.Builder(this)
                .setSingleChoiceItems(items, 0, object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        dialog?.dismiss()
                        PermissionCheck(which);

                    }

                })
                .show()
    }


    fun openCameraIntent() {
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(pictureIntent, REQUEST_CAPTURE_IMAGE);
        }
    }


    fun updateUI() = if (mCurrentLocation != null) {

        lat = mCurrentLocation.latitude
        lon = mCurrentLocation.longitude
        if (mapReady) {
            var latlong = LatLng(lat, lon)
            mMap.addMarker(MarkerOptions().position(latlong).title("current position"))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latlong))
            mMap.animateCamera(CameraUpdateFactory.zoomTo(18F))

            map_latlong.setText("Latitude : " + lat + " , " + "Longitude : " + lon)
        } else {
            tD("map is not ready")
        }
    } else {
        tD("unable to fetch location data!!")
    }

    @SuppressLint("RestrictedApi")
    fun locationEnable() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                mCurrentLocation = locationResult!!.getLastLocation();
                updateUI()

            }
        }

        mRequestingLocationUpdates = false;

        mLocationRequest = LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        var builder = LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    fun PermissionCheckLocation() {
        Dexter.withActivity(this@FormActivity)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION

                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            locationEnable()
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

    fun PermissionCheck(i: Int) {
        Dexter.withActivity(this@FormActivity)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            if (i == 0) {
                                openCameraIntent()
                            } else if (i == 1) {
                                openGallery()
                            }
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


    private fun openGallery() {
        val i = Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(i, PICK_FROM_GALLERY)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CAPTURE_IMAGE &&
                resultCode == RESULT_OK) {
            if (data != null) {
                val imageBitmap = data?.getExtras()?.get("data") as Bitmap
                convertToBase64(this, imageBitmap).execute()
            } else {
                tD("null data camera")
            }
        } else if (requestCode == PICK_FROM_GALLERY &&
                resultCode == RESULT_OK) {
            if (data != null) {
                val uri = data.data
                if (uri != null) {
                    var bitmap: Bitmap;
                    bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                    bitmap = Bitmap.createScaledBitmap(bitmap, (bitmap.getWidth() * 0.8).toInt(), (bitmap.getHeight() * 0.8).toInt(), true)

                    convertToBase64(this, bitmap).execute()

                } else {
                    tD("null uri")
                }

            } else {
                tD("null data")
            }

        } else if (requestCode == REQUEST_CHECK_SETTINGS) {
            locationEnable()
        }
    }


    inner class convertToBase64(val c: Activity, val b: Bitmap) : AsyncTask<Void, Void, Bitmap>() {

        lateinit var progressDial: ProgressDialog
        override fun onPreExecute() {
            super.onPreExecute()
            progressDial = ProgressDialog(c)
            progressDial.setMessage("Preparing Image...")
            progressDial.show()

        }

        override fun onPostExecute(result: Bitmap) {
            super.onPostExecute(result)
            progressDial.dismiss()
            Image_image.visibility = View.VISIBLE
            Image_image.setImageBitmap(result)
        }

        override fun doInBackground(vararg params: Void?): Bitmap {
            encodeTobase64(b)
            return b

        }


        fun encodeTobase64(image: Bitmap): String {
            val baos = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val b = baos.toByteArray()
            baseImage = Base64.encodeToString(b, Base64.DEFAULT)

            return baseImage!!
        }

    }

    fun tD(m: String) {
        Toast.makeText(this@FormActivity, m, Toast.LENGTH_LONG).show()
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
                                p0.startResolutionForResult(this@FormActivity,
                                        REQUEST_CHECK_SETTINGS)
                            } catch (sendEx: IntentSender.SendIntentException) {

                            }
                        }

                    }

                })
    }


}