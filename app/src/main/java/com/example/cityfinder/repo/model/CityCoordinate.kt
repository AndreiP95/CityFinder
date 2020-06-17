package com.example.cityfinder.repo.model

import android.os.Parcel
import android.os.Parcelable

class CityCoordinate() : Parcelable {

    var lat: Double = 0.0
    var lon: Double = 0.0

    constructor(
        lon: Double,
        lat: Double
    ) : this() {
        this.lat = lat
        this.lon = lon
    }

    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(lon)
        parcel.writeDouble(lat)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CityCoordinate> {
        override fun createFromParcel(parcel: Parcel): CityCoordinate {
            return CityCoordinate(parcel)
        }

        override fun newArray(size: Int): Array<CityCoordinate?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "lat ${lat}, lon ${lon}"
    }
}