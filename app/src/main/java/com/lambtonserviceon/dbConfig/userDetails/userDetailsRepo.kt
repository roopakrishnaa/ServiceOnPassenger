package com.lambtonserviceon.dbConfig.userDetails

import androidx.lifecycle.LiveData
import com.example.labtest1.feeskeeper.nimit.dbConfig.cardDetailsViewMOdel
import com.lambtonserviceon.dbConfig.CardDetails.CardDetails


class userDetailsRepo(private val userDetailsDao: userDetailsDao ) {



    suspend fun insert(user : UserDetails) {

        userDetailsDao.insert(user)
    }

    suspend fun update(user : UserDetails) {

        userDetailsDao.update(user)

    }


    val allData: LiveData<List<UserDetails>> = userDetailsDao.getalldata()




}