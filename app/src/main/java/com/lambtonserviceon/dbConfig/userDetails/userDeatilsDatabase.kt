package com.lambtonserviceon.dbConfig.userDetails

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(UserDetails::class), version = 7, exportSchema = false)

abstract  class  userDeatilsDatabase :RoomDatabase () {


    abstract fun userDeatilsDao(): userDetailsDao


    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)

            INSTANCE?.let { database ->
                scope.launch {

                    var user = database.userDeatilsDao()
                    // Delete all content here.
                     // fee.deleteAll()


                }
            }
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: userDeatilsDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): userDeatilsDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE
                ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        userDeatilsDatabase::class.java,
                        "User_info_database"
                    )

                        .addCallback(
                            WordDatabaseCallback(
                                scope
                            )
                        )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                    // return instance
                    instance
                }
        }


    }
}