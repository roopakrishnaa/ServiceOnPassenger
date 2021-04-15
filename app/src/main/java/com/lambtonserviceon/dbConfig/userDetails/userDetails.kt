package com.lambtonserviceon.dbConfig.userDetails

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "userDetailsTables") @Parcelize
class UserDetails(

    @PrimaryKey(autoGenerate = true) @ColumnInfo(name="UserId") val UserId:Int,
    @ColumnInfo(name="FirstName") val FirstName:String,
    @ColumnInfo(name="LastName") val LastNmae:String,
    @ColumnInfo(name="Email") val Email:String,
    @ColumnInfo(name="Password") val Password:String,
    @ColumnInfo(name="UserImg") val UserImg:String ,
    @ColumnInfo(name="CurrentLatititue ") val CurrentLatititue:Double,
    @ColumnInfo(name="currentLongitude ") val currentLongitude:Double,
    @ColumnInfo(name="DestinationLatititue ") val DestinationLatititue:Double,
    @ColumnInfo(name="DestinationLongitude ") val DestinationLongitude:Double,
    @ColumnInfo(name="FormattedDestination") val formattedDestination:String,
    @ColumnInfo(name="FormattedCurrentLocation") val FormattedCurrentLocation:String
 ) : Parcelable
