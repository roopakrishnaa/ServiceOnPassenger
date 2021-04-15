package com.lambtonserviceon.models.directions


import com.google.gson.annotations.SerializedName

data class Step(
    @SerializedName("distance")
    val distance: DistanceX,
    @SerializedName("duration")
    val duration: DurationX,
    @SerializedName("end_location")
    val endLocation: EndLocationX,
    @SerializedName("html_instructions")
    val htmlInstructions: String,
    @SerializedName("maneuver")
    val maneuver: String,
    @SerializedName("polyline")
    val polyline: Polyline,
    @SerializedName("start_location")
    val startLocation: StartLocationX,
    @SerializedName("travel_mode")
    val travelMode: String
)