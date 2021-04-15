package com.lambtonserviceon.AppActivity


import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.example.Example
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.lambtonserviceon.R
import com.lambtonserviceon.dbConfig.userDetails.UserDetails
import com.lambtonserviceon.dbConfig.userDetails.userDeatailsViewModel
import okhttp3.*
import java.io.IOException
import kotlin.math.roundToInt


lateinit var  mapbtn :Button
lateinit var  confirmRidebtn :Button
lateinit var  Destination : EditText
lateinit var  Distance:  EditText
lateinit var EstimatedPrice : EditText
private lateinit var cu : UserDetails
private lateinit var formattedCurrentaddress : String
private lateinit var formattedaddress : String
private lateinit var UserDetailsViewModel: userDeatailsViewModel
//initalizing of OKHttp Client
private val client = OkHttpClient()


private lateinit var  currentUsers :  List<UserDetails>




class rideDetails : AppCompatActivity() {


    var lati  = 0.0
    var longi = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ride_details)

        //setup back button
        this.setupActionBarBtn()

        //Initializing of user data from MainActivity
        cu = intent.getParcelableExtra("userDetailsS")

        UserDetailsViewModel = ViewModelProvider(this).get(userDeatailsViewModel::class.java)



//matching View with there ID
        mapbtn = findViewById(R.id.Map)
        Destination = findViewById(R.id.destination)
        Distance = findViewById(R.id.distance)
        EstimatedPrice = findViewById(R.id.EstimatedPrice)
        confirmRidebtn = findViewById(R.id.ConfirmRide)


        mapbtn.setOnClickListener {



            val userDetails = UserDetails(cu.UserId,cu.FirstName,cu.LastNmae,cu.Email , cu.Password ,cu.UserImg, cu.CurrentLatititue,cu.currentLongitude ,lati,longi ,formattedaddress , formattedCurrentaddress )

            UserDetailsViewModel.update(userDetails)

            var intent = Intent( this , MapAct::class.java)
            intent.putExtra( "UserDetails"  , cu)
            startActivity(intent)

        }



        confirmRidebtn.setOnClickListener {




            val userDetails = UserDetails(cu.UserId,cu.FirstName,cu.LastNmae,cu.Email , cu.Password ,cu.UserImg, cu.CurrentLatititue,cu.currentLongitude , lati , longi , formattedaddress , formattedCurrentaddress)

            UserDetailsViewModel.update(userDetails)


            val db = Firebase.firestore

            db.collection("ridedetails").document("ride").set(userDetails , SetOptions.merge() )

            //db.collection("ridedetails").document()
            var intent = Intent( this , ConfirmRide::class.java)



            startActivity(intent)


        }


        UserDetailsViewModel.alldata.observe(this, Observer { words ->
            // Update the cached copy of the words in the adapter.
            words?.let {

                currentUsers = it

//                println("Size "+ currentUsers.size)

                currentUsers.map {


                    if(cu.UserId == it.UserId ){



                        println("hey this got updated")
                        println( it.CurrentLatititue)
                        println(it.currentLongitude)

                        //google Places api to fetch data of the nearest Service Ontario
                        this.run("https://maps.googleapis.com/maps/api/place/textsearch/json?query=Service+Ontario+in+Toronto&location=${it.CurrentLatititue},${it.currentLongitude}&rankby=distance&key=AIzaSyDfitQFZjRn76sFCbB4dXzjf7r1i3GU-Lc")
                        cu = it

                        this.run2("https://maps.googleapis.com/maps/api/geocode/json?latlng=${it.CurrentLatititue},${it.currentLongitude}&sensor=true&key=AIzaSyDfitQFZjRn76sFCbB4dXzjf7r1i3GU-Lc")

                    }


                }

            }

        })




    }


//function to setup Actionbar back button and title
    fun setupActionBarBtn(){

        this.title = "Ride Details "
        val actionbar = supportActionBar
        //set back button
        actionbar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }




//calling Rest Api performed asyc
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
                var places =  gson.fromJson(response.body?.string(), Example::class.java)

                 lati =  places.results[0].geometry.location.lat
                 longi = places.results[0].geometry.location.lng


                //passing Destination location and the current location of the user
                val locationA = Location("point A")
                locationA.setLatitude(lati);
                locationA.setLongitude(longi);
//
//
                val locationB = Location("point B")
                locationB.setLatitude(cu.CurrentLatititue);
                locationB.setLongitude(cu.currentLongitude);
//

                var str =  places.results[0].formattedAddress

                val spt =  str.split(",")

                println{
                    spt.map {
                       print(it)
                    }
                }
//
//
                //setting up lon & lat for the current user to send



                val distance  = locationB.distanceTo(locationA) / 1000;
                val roundeddist = distance.roundToInt()

                //funtion to calculate fare
                var fare  =  roundeddist * 10


              //  Distance.setText("Lorem Ipsum is simply dummy text of the printing and typesetting industry.Lorem Ipsum has been the industry's standard dummy text ever since the");

               // Destination.layoutParams




                formattedaddress =  places.results[0].formattedAddress
                runOnUiThread{
                    Destination.setText(formattedaddress)
                    Distance.setText(roundeddist.toString() + "   KM")
                    EstimatedPrice.setText("$ ${fare.toString()}")
                }
            }
        })
    }


    //calling Rest Api
    fun run2(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }
            override fun onResponse(call: Call, response: Response) {
                val gson = Gson()
                var places =  gson.fromJson(response.body?.string(), Example::class.java)
                formattedCurrentaddress =  places.results[0].formattedAddress

                println(formattedCurrentaddress)

            }


        })

    }

}
