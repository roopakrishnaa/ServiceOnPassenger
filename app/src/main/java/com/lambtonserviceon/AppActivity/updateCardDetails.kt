package com.lambtonserviceon.AppActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.labtest1.feeskeeper.nimit.dbConfig.cardDetailsViewMOdel
import androidx.lifecycle.Observer
import com.lambtonserviceon.R
import com.lambtonserviceon.dbConfig.CardDetails.CardDetails

class updateCardDetails : AppCompatActivity() {
    private lateinit var CardDetailsViewMOdel: cardDetailsViewMOdel
    var Id :Int = 0

    //setting up  view Elements
    private lateinit var cardnumber : EditText
    private lateinit var cvv : EditText
    private lateinit var updatebtn :Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_card_details)


        this.title = "updateDetails"

//ViewModel initialization
        CardDetailsViewMOdel = ViewModelProvider(this).get(cardDetailsViewMOdel::class.java)

        //finding view by ID
        cardnumber = findViewById(R.id.cardno)
        cvv = findViewById(R.id.cvv)
        updatebtn = findViewById(R.id.updatebtn)







        this.setupUpdateElements()


        updatebtn.setOnClickListener {


            val cardNumber = cardnumber.text.toString().toDouble()
            val expiryNumber =  cvv.text.toString().toInt()
            var cardDetails  = CardDetails(this.Id, cardNumber, expiryNumber)

            CardDetailsViewMOdel.update(cardDetails)

            println("dataupdated")

             finish()

        }



    }


    private fun setupUpdateElements(){

        CardDetailsViewMOdel.alldata.observe(this, Observer { card ->
            // Update the cached copy of the words in the adapter.
            card?.let {




                if(it.size !=0){

                    cardnumber.setText(it[0].cardNumber.toString())


                    cvv.setText(it[0].cardId.toString()

                    )

                    Id = it[0].cardId

                }else{
                    println("user database is empty ")

                }

            }
        })


    }


}
