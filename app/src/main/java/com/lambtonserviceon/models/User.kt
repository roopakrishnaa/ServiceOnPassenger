package com.lambtonserviceon.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User (

    var CurrentLongi : String = "Defaultvalue",
    var CurrentLati: String = "Defaultvalue",


    var Name : String = "Defaultvalue",
    var Destination :String = "Defaultvalue",

    var Destinationlongi : String = "Defaultvalue",
    var DestinationLati : String = "Defaultvalue"


): Parcelable


