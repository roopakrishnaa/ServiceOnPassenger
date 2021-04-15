package com.lambtonserviceon.dbConfig.userDetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.lambtonserviceon.dbConfig.CardDetails.CardDetails
import com.lambtonserviceon.dbConfig.CardDetails.cardDatabase
import com.lambtonserviceon.dbConfig.CardDetails.cardRepo
import kotlinx.coroutines.launch

class userDeatailsViewModel(application: Application) : AndroidViewModel(application) {

    // The ViewModel maintains a reference to the repository to get data.
    private val repository: userDetailsRepo


    val alldata: LiveData<List<UserDetails>>



    init {
        val FeeDao =  userDeatilsDatabase.getDatabase(application, viewModelScope).userDeatilsDao()

        repository = userDetailsRepo(FeeDao)

        alldata = repository.allData
    }


    fun insert(User: UserDetails) = viewModelScope.launch {

        repository.insert(User)
    }



    fun update(User: UserDetails) = viewModelScope.launch {

        repository.update(User)
    }





}