package com.lambtonserviceon.AppActivity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.lambtonserviceon.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.maps.android.PolyUtil
import com.lambtonserviceon.models.directions.Direction
import com.lambtonserviceon.models.directions.Step
import okhttp3.*
import java.io.IOException

private lateinit var status :TextView
private lateinit var Drivername :TextView
private lateinit var Board :Button
//Google map initialization
private lateinit var mMap: GoogleMap
private lateinit var myMarker: Marker
var polylines: MutableList<Polyline> = mutableListOf<Polyline>()
private val client = OkHttpClient()
private lateinit var  decodedPolyLine : List <LatLng>
private lateinit var  riderstatus : String
private lateinit var url:String
private lateinit var url2:String

class ConfirmRide : AppCompatActivity(), OnMapReadyCallback,GoogleMap.OnMarkerClickListener {

    private var driverLocation = LatLng(0.0 , 0.0)
    var destinationlocation = LatLng(0.0, 0.0)
    //Current location
    private var riderlocation = LatLng(0.0 , 0.0)





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_ride)

        this.setupActionBarBtn()

        //setting up Googlemap
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        status = findViewById(R.id.status)
        Drivername  =  findViewById(R.id.dname)
        Board = findViewById(R.id.board)



    }
    

    //function to setup Actionbar back button and title
    fun setupActionBarBtn(){

        this.title = "Confirm Ride"
        val actionbar = supportActionBar
        //set back button
        actionbar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onMapReady(googleMap: GoogleMap) {


        val db = Firebase.firestore

        val docRef = db.collection("ridedetails").document("ride").collection("driverDetails").document("details" )
        db.collection("ridedetails").document("ride")
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                println("DRIVER DETAILS ERR")
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {

                println("DRIVER DETAILS SUCCESS")
                riderstatus = snapshot.get("rideStatus").toString()


                status.setText("Drive Found")
                Drivername.setText(snapshot.get("firstName").toString() )
                println("Current data: ${snapshot.get("firstName") }")

                val lati  =  snapshot.get("ridersLatititue").toString()
                val longi =  snapshot.get("ridersLongitude").toString()

                riderlocation = LatLng(lati.toDouble() , longi.toDouble())

                val Driverlati  =  snapshot.get("currentLatititue").toString()
                val Driverlongi =  snapshot.get("currentLongitude").toString()
                driverLocation = LatLng(Driverlati.toDouble() , Driverlongi.toDouble())


                println("you guys !! its locations........")
                //println("riderloc"+ riderlocation)
                println("driverloc"+driverLocation)


                val destilati = snapshot.get("destinationLatititue").toString()
                val destilongi = snapshot.get("destinationLongitude").toString()

                destinationlocation = LatLng(destilati.toDouble(), destilongi.toDouble())



                mMap = googleMap
                mMap.clear()
                myMarker = mMap.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)) .position(driverLocation).title("yourDriver"))
                myMarker.showInfoWindow()


                myMarker =
                    mMap.addMarker(MarkerOptions() .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        .position(riderlocation).title("hey you are here"))


                myMarker =
                    mMap.addMarker(
                        MarkerOptions().icon(
                            BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_YELLOW
                            )
                        )
                            .position(destinationlocation).title("Destination!!")
                    )



                mMap?.animateCamera(CameraUpdateFactory.newLatLng(driverLocation))
                mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(driverLocation, 10f))

                url = getURL(driverLocation, riderlocation)
                println(url)


                 url2 = getURL(riderlocation, destinationlocation)



                Board.visibility = View.INVISIBLE

                val driverreachedcheck =  snapshot.get("rideStatus").toString()

                if(driverreachedcheck == "riderlocation" ){

                    Board.visibility = View.VISIBLE


                }



                if (riderstatus == "") {

                   // removepoly()

                    run(url, "GREEN")
                    run(url2, "RED")
                    println("riderlocation empty")

                } else if (riderstatus == "riderlocation") {
                 //   removepoly()
                    run(url2, "RED")
                } else if (riderstatus == "destinationlocation") {
                 //   removepoly()
                    println("riders reached at destination with pay to the rider ")

                    var goto = Intent(this , enRouteDriver::class.java)

                    startActivity(goto)

                }


            } else {


            }

        }

        Board.setOnClickListener {

            Board.isVisible = false
            val docRef = db.collection("ridedetails").document("ride").collection("driverDetails").document("details" )

            docRef.update("riderborded","true")

        }


    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        TODO("Not yet implemented")
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



    fun run(url: String, color: String) {

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {


            override fun onFailure(call: Call, e: IOException) {

                println("hey its failed..!")
            }

            override fun onResponse(call: Call, response: Response) {

                val gson = Gson()
                var Direction2 = gson.fromJson(response.body?.string(), Direction::class.java)

                //function to fetch steps and pass to ADD polyline
                addPolyLines(Direction2.routes[0].legs[0].steps, color)
            }

        })


    }


    private fun addPolyLines(steps: List<Step>, color: String) {

        val path: MutableList<List<LatLng>> = ArrayList()

        if(steps !== null){

            for (step in steps) {
                decodedPolyLine = PolyUtil.decode(step.polyline.points);
                path.add(decodedPolyLine)

            }
        }

        runOnUiThread {

            val polyLineOption = PolylineOptions()
            if (color == "RED") {

                val col1 = Color.RED
                polyLineOption.color(col1)

            } else {

                val color2 = Color.YELLOW
                polyLineOption.color(color2)
            }


            for (p in path)

                polyLineOption.addAll(p)

            polylines.add(mMap.addPolyline(polyLineOption));

        }

    }




    fun removepoly(){

        for (line in polylines) {

            line.remove()
        }
        polylines.clear()
    }
}
