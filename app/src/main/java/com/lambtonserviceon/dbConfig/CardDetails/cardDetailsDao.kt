package com.lambtonserviceon.dbConfig.CardDetails


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface CardDetailsDao {


   @Insert()
   suspend  fun insert(card: CardDetails)


   @Query("SELECT * from carddetailstable")
   fun getalldata(): LiveData<List<CardDetails>>



   //Delete all data
   @Query("DELETE FROM carddetailstable")
   suspend fun deleteAll()




   @Update
   suspend fun update(card : CardDetails)



}