package com.lambtonserviceon

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.lifecycle.Observer
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.lambtonserviceon.dbConfig.userDetails.UserDetails
import com.lambtonserviceon.dbConfig.userDetails.userDeatailsViewModel
import kotlinx.android.synthetic.main.activity_register.*
import java.io.ByteArrayOutputStream
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    lateinit var  login : TextView
    lateinit var FirstName :EditText
    lateinit var LastName :EditText
    lateinit var Email :EditText
    lateinit var Password :EditText
    lateinit var SetImageBtn :Button
    lateinit var RegisterBtn :Button
    lateinit var imageView: ImageView
    var imgData = ""



    private lateinit var  currentUsers :  List<UserDetails>
    private lateinit var  currentUser :  UserDetails
    private lateinit var UserDetailsViewModel: userDeatailsViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)



        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED)

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)

        UserDetailsViewModel = ViewModelProvider(this).get(userDeatailsViewModel::class.java)



        UserDetailsViewModel.alldata.observe(this, Observer { words ->
            // Update the cached copy of the words in the adapter.
            words?.let {


                currentUsers = it

                println("helllllllooooworrllldd")
                println("Size "+ currentUsers.size)

            }

        })


        login  = findViewById(R.id.login)
        FirstName = findViewById(R.id.firstname)
        LastName = findViewById(R.id.lastname)
        Email = findViewById(R.id.Email)
        Password = findViewById(R.id.password)
        SetImageBtn = findViewById(R.id.SetImg)
        RegisterBtn = findViewById(R.id.Registerbtn)
        imageView  = findViewById(R.id.Dispic)


        login.setOnClickListener {


            finish()

        }


        SetImageBtn.setOnClickListener {

            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent  , 1)

        }



        RegisterBtn.setOnClickListener {


            val  firstname = this.FirstName.text.toString()
            val lastname = this.LastName.text.toString()
            val email = this.Email.text.toString()
            val password = this.Password.text.toString()

         save(firstname , lastname , password ,email , imgData)


        }







    }


    private fun  save(firstname :String  , lastname:String  ,  password : String  ,  email : String ,  imgData:  String  ){

        if (firstname==""){

            Toast.makeText(this , "Enter name" ,Toast.LENGTH_LONG).show()


             return
        }else if (lastname == ""){

            Toast.makeText(this , "Enter last name" ,Toast.LENGTH_LONG).show()


            return

        }
        else if  (password == "") {

            Toast.makeText(this , "Enter password" ,Toast.LENGTH_LONG).show()


            return

        }
        else if ( email == "") {

            Toast.makeText(this , "Enter Email" ,Toast.LENGTH_LONG).show()

            return

        }
        else if ( imgData == "") {

            Toast.makeText(this , " please select image" ,Toast.LENGTH_LONG).show()


            return

        }else {


            var result = validEmail(email)
            var  exist = ifUserExist(email)


            if (result) {


               if ( !exist){

                   val userDetails = UserDetails(0,firstname,lastname,email , password, imgData , 0.0 , 0.0 , 0.0,0.0, "", "")
                   UserDetailsViewModel.insert(userDetails)
                   Toast.makeText(this , "YAYA! You are finally registered ..!!" ,Toast.LENGTH_LONG).show()
                   finish()

               }
                else{

                   Toast.makeText(this , "User already Exists!!" ,Toast.LENGTH_LONG).show()

               }

            }

            else{

                Toast.makeText(this , "Not a well formed email" ,Toast.LENGTH_LONG).show()
            }


        }



    }


    //Starting camera activity on result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {



            val photo: Bitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(photo)


            val byteArrayOutputStream =
                ByteArrayOutputStream()
            photo.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()


            val encoded: String = Base64.encodeToString(byteArray, Base64.DEFAULT)

            this.imgData = encoded

        }
    }


    private fun   validEmail( email: String): Boolean {
         val pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }


    private fun ifUserExist(email: String) : Boolean {

        var exits = false
        currentUsers.map {

            if(it.Email.equals(email) ) {

                exits = true
            }

        }

        return  exits

    }



}
