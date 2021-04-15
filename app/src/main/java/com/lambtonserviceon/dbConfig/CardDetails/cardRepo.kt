package com.lambtonserviceon.dbConfig.CardDetails

import androidx.lifecycle.LiveData


class cardRepo(private val cardDetailsDao: CardDetailsDao) {




    suspend fun insert(card : CardDetails) {

        cardDetailsDao.insert(card)
    }

    val allData: LiveData<List<CardDetails>> = cardDetailsDao.getalldata()


    suspend fun update(card: CardDetails) {

        cardDetailsDao.update(card)

    }


}



