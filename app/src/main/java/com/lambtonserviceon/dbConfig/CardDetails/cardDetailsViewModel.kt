package com.example.labtest1.feeskeeper.nimit.dbConfig

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.lambtonserviceon.dbConfig.CardDetails.CardDetails
import com.lambtonserviceon.dbConfig.CardDetails.cardDatabase
import com.lambtonserviceon.dbConfig.CardDetails.cardRepo
import kotlinx.coroutines.launch

// Class extends AndroidViewModel and requires application as a parameter.

class cardDetailsViewMOdel(application: Application) : AndroidViewModel(application) {

    // The ViewModel maintains a reference to the repository to get data.
    private val repository: cardRepo


    val alldata: LiveData<List<CardDetails>>


    init {




        val FeeDao = cardDatabase.getDatabase(application, viewModelScope).cardDetailsDao()

        repository = cardRepo(FeeDao)

        alldata = repository.allData


    }

    fun insert(card: CardDetails) = viewModelScope.launch {

        repository.insert(card)
    }



    fun update(card: CardDetails) = viewModelScope.launch {

        repository.update(card)
    }







}