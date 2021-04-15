package com.lambtonserviceon.AppActivity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.lambtonserviceon.R
import com.lambtonserviceon.dbConfig.userDetails.UserDetails
import com.lambtonserviceon.dbConfig.userDetails.userDeatailsViewModel
import kotlinx.android.synthetic.main.activity_profile_details.*
import org.w3c.dom.Text
import java.io.ByteArrayOutputStream


class ProfileDetails : AppCompatActivity() {


    lateinit var imageView: ImageView
    lateinit var  firstName : TextView
    lateinit var lastName : TextView
    var imgData : String = ""
    lateinit var Updatebtn :Button

    private lateinit var UserDetailsViewModel: userDeatailsViewModel

    private lateinit var cu : UserDetails

    private lateinit var  currentUsers :  List<UserDetails>
    private lateinit var  currentUser :  UserDetails




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_details)


        cu = intent.getParcelableExtra("userdetails")

       if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)
           == PackageManager.PERMISSION_DENIED)

           ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)



        imageView = findViewById(R.id.Dispic)
        UserDetailsViewModel = ViewModelProvider(this).get(userDeatailsViewModel::class.java)







        //initialization of view buttons

        firstName = findViewById(R.id.firstName)
        lastName = findViewById(R.id.lastName)
        Updatebtn = findViewById(R.id.updatebtn)



        updatebtn.setOnClickListener {
            val intent = Intent(this, updateUserDetails::class.java)

            intent.putExtra("userdetails" , this.cu )

            startActivity(intent)

        }


        this.setUpUserDetails()
        this.setupActionBarBtn()




    }



    fun setupActionBarBtn(){

        this.title = "Profile Details"
        val actionbar = supportActionBar
        //set back button
        actionbar!!.setDisplayHomeAsUpEnabled(true)

    }



    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }



    private  fun setUpUserDetails(){


        UserDetailsViewModel.alldata.observe(this, Observer { words ->
            // Update the cached copy of the words in the adapter.
            words?.let {



                currentUsers = it



                currentUsers.map {

                    if(cu.UserId == it.UserId ){

                        firstName.text = it.FirstName
                        lastName.text = it.LastNmae



                        val imgData = it.UserImg
                        val k =  Base64.decode(imgData, Base64.DEFAULT)
                        val image = BitmapFactory.decodeByteArray(k, 0, k.size)
                        imageView.setImageBitmap(image)

                    }


                }

            }




        })





    }

}
