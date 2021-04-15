package com.lambtonserviceon.AppActivity
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import com.google.maps.android.PolyUtil
import com.lambtonserviceon.R
import com.lambtonserviceon.dbConfig.userDetails.UserDetails
import com.lambtonserviceon.dbConfig.userDetails.userDeatailsViewModel
import com.lambtonserviceon.models.User
import com.lambtonserviceon.models.directions.Direction
import com.lambtonserviceon.models.directions.Step
import okhttp3.*
import okhttp3.internal.notify
import okhttp3.internal.notifyAll
import java.io.IOException



//User


//Okhttp client
private val client = OkHttpClient()
//polyLine from google
private lateinit var  decodedPolyLine : List <LatLng>

//Google map initialization
private lateinit var mMap: GoogleMap


private lateinit var myMarker: Marker

private lateinit var cu : UserDetails
private lateinit var UserDetailsViewModel: userDeatailsViewModel
private lateinit var  currentUsers :  List<UserDetails>

//dummy
var DestinationAnontation = LatLng(0.0 , 0.0)

//Current location
var currentLocation = LatLng(0.0 , 0.0)

class MapAct : AppCompatActivity() , OnMapReadyCallback ,GoogleMap.OnMarkerClickListener {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        //setting up current user from Main activity

        UserDetailsViewModel = ViewModelProvider(this).get(userDeatailsViewModel::class.java)

        cu = intent.getParcelableExtra("UserDetails")


       // hello()



        //setting up Googlemap
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //back btn setup
        this.setupActionBarBtn()


    }

    //backPressSupporting function
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }



    fun setupActionBarBtn(){

        this.title = "Map"
        val actionbar = supportActionBar
        //set back button
        actionbar!!.setDisplayHomeAsUpEnabled(true)
    }


    override fun onMapReady(googleMap: GoogleMap) {


        UserDetailsViewModel.alldata.observe(this, Observer { words ->
            // Update the cached copy of the words in the adapter.
            words?.let {

                currentUsers = it
                println("Size "+ currentUsers.size)

                currentUsers.map {



                    if(cu.UserId == it.UserId ){

                        cu = it


                        currentLocation = LatLng(   it.CurrentLatititue , it.currentLongitude)
                        DestinationAnontation = LatLng(it.DestinationLatititue ,it.DestinationLongitude )
                        val url = getURL(currentLocation, DestinationAnontation)
                        println(url)
                        this.run(url)



                        mMap = googleMap
                        mMap.clear()




                        mMap?.animateCamera(CameraUpdateFactory.newLatLng(currentLocation))
                        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 10f))
                        myMarker = mMap.addMarker(MarkerOptions().position(currentLocation).title("hey you are here"))
                        myMarker.showInfoWindow()


                        myMarker =
                            mMap.addMarker(MarkerOptions().position(DestinationAnontation).title("hey you want to go here"))





                    }


                }

            }

        })








        //build Url to fetch google api


        //okHttp fetch  to get polyLine between destination and current user


        //



    }


    override fun onMarkerClick(p0: Marker?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    //build Url to fetch google api
    private fun getURL(from: LatLng, to: LatLng): String {

        val origin = "origin=" + from.latitude + "," + from.longitude
        val dest = "destination=" + to.latitude + "," + to.longitude
        val sensor = "sensor=false"
        val params = "$origin&$dest"
        val key = "&key=AIzaSyDfitQFZjRn76sFCbB4dXzjf7r1i3GU-Lc"

        return "https://maps.googleapis.com/maps/api/directions/json?$params$key"

    }


    fun run(url: String) {

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {


            override fun onFailure(call: Call, e: IOException) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

            }

            override fun onResponse(call: Call, response: Response) {

                val gson = Gson()
                var Direction2 = gson.fromJson(response.body?.string(), Direction::class.java)

                //function to fetch steps and pass to ADD polyline
                addPolyLines(Direction2.routes[0].legs[0].steps)
            }

        })
    }

    private fun addPolyLines(steps: List<Step>) {

        val path: MutableList<List<LatLng>> = ArrayList()

        for (step in steps) {
             decodedPolyLine = PolyUtil.decode(step.polyline.points);
            path.add(decodedPolyLine)

        }

        runOnUiThread {

           val  polyLineOption = PolylineOptions()

            polyLineOption.color(Color.GREEN)

            for(p in path)
            polyLineOption.addAll(p)

            val polyline = mMap.addPolyline(polyLineOption)

        }


    }




}




