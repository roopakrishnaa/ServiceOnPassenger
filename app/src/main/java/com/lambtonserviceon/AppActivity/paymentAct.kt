package com.lambtonserviceon.AppActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.labtest1.feeskeeper.nimit.dbConfig.cardDetailsViewMOdel
import com.lambtonserviceon.dbConfig.CardDetails.CardDetails
import androidx.lifecycle.Observer
import com.lambtonserviceon.R


class paymentAct : AppCompatActivity() {


    private lateinit var CardDetailsViewModel: cardDetailsViewMOdel

    //View Elements
    private lateinit var saveBtn : Button
    private lateinit var cardno :TextView
    private lateinit var expiryno :TextView
    private lateinit var update :Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)




        CardDetailsViewModel = ViewModelProvider(this).get(cardDetailsViewMOdel::class.java)


        //initializing view model with respective ids
        cardno = findViewById(R.id.cardno)
        expiryno = findViewById(R.id.expiryDate)
        update = findViewById(R.id.updateCardBtn)
        saveBtn = findViewById(R.id.savebtn)


        //initialize  user data
        this.SetPaymentInfo()

       this.setupActionBarBtn()



       //saves User details if the user setting is not setup

        saveBtn.setOnClickListener {


            if  (cardno.text.toString() == "" || expiryno.text.toString() == "" ) {


                Toast.makeText(this, "please enter card details", Toast.LENGTH_SHORT).show()
            }else {
                val cardNumber = cardno.text.toString().toDouble()
                val expiryNumber =  expiryno.text.toString().toInt()

                var cardDetails  = CardDetails(0, cardNumber, expiryNumber)

                CardDetailsViewModel.insert(cardDetails)

                println("dataSaved")

            }

        }

        //updated user card details if the card details is not setuped

        update.setOnClickListener {

            val intent = Intent(this, updateCardDetails::class.java)
            startActivity(intent)

        }



    }



    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    fun setupActionBarBtn(){

        this.title = "Payment"
        val actionbar = supportActionBar
        //set back button
        actionbar!!.setDisplayHomeAsUpEnabled(true)


    }

    //function to initialize user
    private  fun SetPaymentInfo(){

        CardDetailsViewModel.alldata.observe(this, Observer { words ->
            // Update the cached copy of the words in the adapter.
            words?.let {


                if(it.size !=0){

                    cardno.setText(it[0].cardNumber.toString())
                    expiryno.setText(it[0].cvv.toString())

                    saveBtn.visibility = View.INVISIBLE

                }else{

                    update.visibility = View.INVISIBLE

                }
            }
        })

    }

}
