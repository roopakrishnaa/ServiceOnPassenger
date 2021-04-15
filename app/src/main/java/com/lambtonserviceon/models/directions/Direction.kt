package com.lambtonserviceon.models.directions


import com.google.gson.annotations.SerializedName

data class Direction(
    @SerializedName("geocoded_waypoints")
    val geocodedWaypoints: List<GeocodedWaypoint>,
    @SerializedName("routes")
    val routes: List<Route>,
    @SerializedName("status")
    val status: String
)