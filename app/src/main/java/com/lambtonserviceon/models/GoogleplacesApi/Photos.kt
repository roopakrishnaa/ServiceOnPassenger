package com.example.example

import com.google.gson.annotations.SerializedName

   
data class Photos (

   @SerializedName("height") var height : Int,
   @SerializedName("html_attributions") var htmlAttributions : List<String>,
   @SerializedName("photo_reference") var photoReference : String,
   @SerializedName("width") var width : Int

)