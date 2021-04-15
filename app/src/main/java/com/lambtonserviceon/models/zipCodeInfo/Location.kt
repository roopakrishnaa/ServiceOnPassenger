package com.lambton

import com.google.gson.annotations.SerializedName


data class Location (

   @SerializedName("zipCode") var zipCode : String,
   @SerializedName("City") var City : String,
   @SerializedName("timeZone") var timeZone : String,
   @SerializedName("latitude") var latitude : String,
   @SerializedName("longitude") var longitude : String,
   @SerializedName("state") var state : String,
   @SerializedName("stateCode2") var stateCode2 : String,
   @SerializedName("country") var country : String,
   @SerializedName("countryCode2") var countryCode2 : String,
   @SerializedName("countryCode3") var countryCode3 : String

)