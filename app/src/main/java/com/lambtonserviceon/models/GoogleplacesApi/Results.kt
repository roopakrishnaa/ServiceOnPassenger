package com.example.example

import com.google.gson.annotations.SerializedName

   
data class Results (

   @SerializedName("business_status") var businessStatus : String,
   @SerializedName("formatted_address") var formattedAddress : String,
   @SerializedName("geometry") var geometry : Geometry,
   @SerializedName("icon") var icon : String,
   @SerializedName("name") var name : String,
   @SerializedName("opening_hours") var openingHours : OpeningHours,
   @SerializedName("photos") var photos : List<Photos>,
   @SerializedName("place_id") var placeId : String,
   @SerializedName("plus_code") var plusCode : PlusCode,
   @SerializedName("rating") var rating : Double,
   @SerializedName("reference") var reference : String,
   @SerializedName("types") var types : List<String>,
   @SerializedName("user_ratings_total") var userRatingsTotal : Int

)