package com.lambton

import com.google.gson.annotations.SerializedName

   
data class GetZipCode (

   @SerializedName("success") var success : Boolean,
   @SerializedName("location") var location : List<Location>

)