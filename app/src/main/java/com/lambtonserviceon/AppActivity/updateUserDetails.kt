package com.lambtonserviceon.AppActivity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.lambtonserviceon.R
import com.lambtonserviceon.dbConfig.userDetails.UserDetails
import com.lambtonserviceon.dbConfig.userDetails.userDeatailsViewModel
import kotlinx.android.synthetic.main.activity_update_user_details.*
import java.io.ByteArrayOutputStream

class updateUserDetails : AppCompatActivity() {



    private lateinit var UserDetailsViewModel: userDeatailsViewModel
    private  lateinit var firstName  : EditText
    private  lateinit  var lastname : EditText
    private  lateinit  var update : Button
    private  lateinit  var changeBtn : Button
    private  lateinit  var Img : ImageView

    var imgData : String = ""

    var Id :Int = 0

    private lateinit var  currentUsers :  List<UserDetails>
    private lateinit var cu : UserDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user_details)

        cu = intent.getParcelableExtra("userdetails")


        this.title = "update user details "

        firstName = findViewById(R.id.firstname)
        lastname = findViewById(R.id.LastName)
        changeBtn = findViewById(R.id.changebtn)
        UserDetailsViewModel = ViewModelProvider(this).get(userDeatailsViewModel::class.java)
        Img = findViewById(R.id.img)
        update = findViewById(R.id.updatebtn)




        updatebtn.setOnClickListener {


            var firstname = firstname.text.toString()
            var lastname = LastName.text.toString()

            if (imgData == ""){


                val userDetails = UserDetails(cu.UserId,firstname,lastname,cu.Email , cu.Password ,imgData , cu.CurrentLatititue , cu.currentLongitude  , cu.DestinationLatititue , cu.DestinationLongitude , "" , "")

                UserDetailsViewModel.update(userDetails)
                finish()

            }else {

                val userDetails = UserDetails(cu.UserId,firstname,lastname,cu.Email , cu.Password , cu.UserImg ,  cu.CurrentLatititue , cu.currentLongitude  , cu.DestinationLatititue , cu.DestinationLongitude , "" , "")

                UserDetailsViewModel.update(userDetails)
                finish()


            }




        }






        changeBtn.setOnClickListener {


            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            startActivityForResult(cameraIntent  , 1)
        }



        this.setupUpdaterElements()

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {

            val photo: Bitmap = data?.extras?.get("data") as Bitmap
            this.Img.setImageBitmap(photo)

            val byteArrayOutputStream =
                ByteArrayOutputStream()
            photo.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            val encoded: String = Base64.encodeToString(byteArray, Base64.DEFAULT)
            imgData = encoded

        }
    }






    private fun setupUpdaterElements() {


        UserDetailsViewModel.alldata.observe(this, Observer { words ->
            // Update the cached copy of the words in the adapter.
            words?.let {

                currentUsers = it

                currentUsers.map {

                    if (cu.UserId == it.UserId) {

                        firstName.setText(it.FirstName)
                        lastname.setText(it.LastNmae)


                        val imgData = it.UserImg

                        val k = Base64.decode(imgData, Base64.DEFAULT)
                        val image = BitmapFactory.decodeByteArray(k, 0, k.size)
                        Img.setImageBitmap(image)

                    } else {

                        println("user database is empty ")

                    }

                }
            }
        })


    }
}
