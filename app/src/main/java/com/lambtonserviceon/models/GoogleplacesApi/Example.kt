package com.example.example

import com.google.gson.annotations.SerializedName

   
data class Example (

   @SerializedName("html_attributions") var htmlAttributions : List<String>,
   @SerializedName("results") var results : List<Results>,
   @SerializedName("status") var status : String

)