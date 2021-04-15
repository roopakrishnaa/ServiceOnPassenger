package com.lambtonserviceon.dbConfig.userDetails

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface userDetailsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend  fun insert(user: UserDetails )


    @Query("SELECT * from userDetailsTables")
    fun getalldata(): LiveData<List<UserDetails>>

    @Update
    suspend fun update(user : UserDetails)

    //Delete all data
    @Query("DELETE FROM userDetailsTables")
    suspend fun deleteAll()



}