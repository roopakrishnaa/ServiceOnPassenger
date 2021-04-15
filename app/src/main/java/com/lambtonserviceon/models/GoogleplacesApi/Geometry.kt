package com.example.example

import com.google.gson.annotations.SerializedName


data class Geometry (

   @SerializedName("location") var location : Location,
   @SerializedName("viewport") var viewport : Viewport

)