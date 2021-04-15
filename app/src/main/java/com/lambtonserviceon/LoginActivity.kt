package com.lambtonserviceon

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.lambtonserviceon.dbConfig.userDetails.UserDetails
import com.lambtonserviceon.dbConfig.userDetails.userDeatailsViewModel
import kotlinx.android.synthetic.main.activity_profile_details.*


class LoginActivity : AppCompatActivity() {


    lateinit var email : EditText
    lateinit var  password :EditText
    lateinit var loginBtn :Button
    lateinit var createAccount : TextView



    private lateinit var UserDetailsViewModel: userDeatailsViewModel
    private lateinit var  currentUsers :  List<UserDetails>
    private lateinit var  currentUser :  UserDetails



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        email = findViewById(R.id.Email)
        password =  findViewById(R.id.password)
        loginBtn = findViewById(R.id.loginbtn)
        createAccount = findViewById(R.id.CreateAcc)
        createAccount.linksClickable = true


        UserDetailsViewModel = ViewModelProvider(this).get(userDeatailsViewModel::class.java)



        createAccount.setOnClickListener {

            val goto = Intent(this, RegisterActivity::class.java)
            startActivity(goto)


        }


        UserDetailsViewModel.alldata.observe(this, Observer { words ->
            // Update the cached copy of the words in the adapter.
            words?.let {


                currentUsers = it

                println("helllllllooooworrllldd")
                println("Size "+ currentUsers.size)

            }

        })

        loginBtn.setOnClickListener {


            var emailtxt = email.text.toString()
            var  passtxt = password.text.toString()

            this.login(emailtxt , passtxt )


        }






    }

     private fun login(email:String, password:String){


        if( email== "" ) {

            Toast.makeText(this,"please enter email" ,Toast.LENGTH_SHORT).show()
            return

        }else if (password == ""){
            Toast.makeText(this,"please enter password" ,Toast.LENGTH_SHORT).show()
            return
        }else

        {

            currentUsers.map {
               if ( it.Email.equals(email)  && it.Password.equals(password) ){

                   this.currentUser  = it


                   finish()

                   val goto = Intent(this , MainActivity::class.java)
                   goto.putExtra("userdetails" , this.currentUser )
                   startActivity(goto)


                   Toast.makeText(this,"Login in ..." ,Toast.LENGTH_SHORT).show()


               }else {


                   Toast.makeText(this,"User does not exist , please Sign up!!" ,Toast.LENGTH_SHORT).show()


               }


            }

        }



    }

}
