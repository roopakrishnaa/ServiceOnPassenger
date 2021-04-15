package com.lambtonserviceon.dbConfig.CardDetails

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Database(entities = arrayOf(CardDetails::class), version = 4, exportSchema = false)

abstract  class cardDatabase : RoomDatabase () {


    abstract fun cardDetailsDao() : CardDetailsDao


    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)

            INSTANCE?.let { database ->
                scope.launch {

                    var fee = database.cardDetailsDao()
                    // Delete all content here.
                   //  fee.deleteAll()



                }
            }
        }
    }



    companion object {
        @Volatile
        private var INSTANCE: cardDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): cardDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE
                ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    cardDatabase::class.java,
                    "Fee_info_database"
                )

                    .addCallback(
                        WordDatabaseCallback(
                            scope
                        )
                    )
                    . fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }


    }




}






