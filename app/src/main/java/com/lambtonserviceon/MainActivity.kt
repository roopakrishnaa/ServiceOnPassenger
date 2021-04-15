package com.lambtonserviceon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.lambton.GetZipCode
import com.lambtonserviceon.AppActivity.ProfileDetails
import com.lambtonserviceon.AppActivity.paymentAct
import com.lambtonserviceon.AppActivity.rideDetails
import com.lambtonserviceon.dbConfig.userDetails.UserDetails
import com.lambtonserviceon.dbConfig.userDetails.userDeatailsViewModel
import com.lambtonserviceon.models.User
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException
import androidx.lifecycle.Observer


class MainActivity : AppCompatActivity()  {


    //dummy
    lateinit  var toggle : ActionBarDrawerToggle
    lateinit var Searchbtn : Button
    lateinit var  postalCode : EditText
    lateinit var  title  : TextView
    private lateinit var cu : UserDetails

    //Ok hhtp client , used to Fetch and retrieve Rest api data
    private val client = OkHttpClient()


    private lateinit var UserDetailsViewModel: userDeatailsViewModel
    private lateinit var  currentUsers :  List<UserDetails>




    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        cu = intent.getParcelableExtra("userdetails")



        //View button Initialization
        Searchbtn = findViewById(R.id.Searchbtn)
        postalCode = findViewById(R.id.Postal)


        title = findViewById(R.id.maint)

        //Toggle btn for main activity
        toggle = ActionBarDrawerToggle(this , drawerlayout , R.string.open , R.string.close)
        drawerlayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        UserDetailsViewModel = ViewModelProvider(this).get(userDeatailsViewModel::class.java)



        UserDetailsViewModel.alldata.observe(this, Observer { words ->
            // Update the cached copy of the words in the adapter.
            words?.let {

                currentUsers = it
                println("Size "+ currentUsers.size)

                currentUsers.map {



                    if(cu.UserId == it.UserId ){

                        title.text = "welcome    " + it.FirstName
                        cu = it


                    }

                    Searchbtn.setOnClickListener {
                        //calling zipLocation Api
                        this.run("  https://thezipcodes.com/api/v1/search?zipCode=${postalCode.text}&countryCode=CA&apiKey=82c04d7a7ad16e63a925ed39dd44b975")

                    }


                }

            }

        })


        //hamburger menu button listener
        navView.setNavigationItemSelectedListener {

            when(it.itemId){

                R.id.miItem1 -> {
                    val intent = Intent(this, ProfileDetails::class.java)
                    intent.putExtra("userdetails" , this.cu )
                    startActivity(intent)
                }

                R.id.miItem2 -> {

                    val intent = Intent(this, paymentAct::class.java)

                    startActivity(intent)

                }

                R.id.miItem3 -> {
                    Toast.makeText(this, "Dummy", Toast.LENGTH_SHORT).show()
                }

            }


            true
        }




        postalCode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

               checkPostalCode( postalCode.text.toString())

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

    }


    //backbtn
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(toggle.onOptionsItemSelected(item)){

            return true
        }
        return super.onOptionsItemSelected(item)
    }



//function to check valid postal code
    fun checkPostalCode(postalCode:String){

        if ( postalCode == "" ) {
            return
        }
            var    pattern  =  Regex  ("^\\d{5}-\\d{4}|\\d{5}|[A-Z]\\d[A-Z] \\d[A-Z]\\d\$")

            var result = pattern.matches(postalCode)

        if (result) {

            Searchbtn.visibility = View.VISIBLE
        }else if (result == false )  {

            Searchbtn.visibility = View.INVISIBLE
        }
    }



    //to fetch api zipcodes
    fun run(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call, response: Response) {


                //to Convert json to anroid or kotlin readable data we have to use Gson
                val gson = Gson()

                val mUser =  gson.fromJson(response.body?.string(), GetZipCode::class.java)


                val userDetails = UserDetails(cu.UserId,cu.FirstName,cu.LastNmae,cu.Email , cu.Password ,cu.UserImg, mUser.location[0].latitude.toDouble(), mUser.location[0].longitude.toDouble(),0.0,0.0, "" , "")
                UserDetailsViewModel.update(userDetails)


                val intent = Intent(this@MainActivity, rideDetails::class.java)
                intent.putExtra("userDetailsS" , cu )
                startActivity(intent)




            }


        }


        )

    }



//    private fun hello(){
//
//
//
//        UserDetailsViewModel.alldata.observe(this, Observer { words ->
//            // Update the cached copy of the words in the adapter.
//            words?.let {
//
//                currentUsers = it
//                println("Size "+ currentUsers.size)
//
//
//
//
//                currentUsers.map {
//
//
//
//                    if(cu.UserId == it.UserId ){
//
//                        println( it.CurrentLatititue)
//                        println(it.currentLongitude)
//
//
//                    }
//
//
//                }
//
//            }
//
//        })
//
//
//
//    }
}
