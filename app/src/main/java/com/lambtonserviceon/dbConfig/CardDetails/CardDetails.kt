package com.lambtonserviceon.dbConfig.CardDetails


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "CardDetailsTable")
 class CardDetails (

    @PrimaryKey(autoGenerate = true) @ColumnInfo(name="cardId") val cardId:Int,
    @ColumnInfo(name="Number") val cardNumber:Double,
    @ColumnInfo(name="cvv") val cvv:Int
)