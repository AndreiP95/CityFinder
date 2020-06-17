package com.example.cityfinder.repo.model

import android.os.Parcel
import android.os.Parcelable

class City() : Parcelable {

    var name: String? = null
    var country: String? = null
    var _id: String? = null
    var coord: CityCoordinate? = null

    constructor(
        name: String?,
        country: String?,
        _id: String?,
        coord: CityCoordinate?
    ) : this() {
        this.name = name
        this._id = _id
        this.country = country
        this.coord = coord
    }

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(CityCoordinate::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(country)
        parcel.writeString(_id)
        parcel.writeParcelable(coord, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<City> {
        override fun createFromParcel(parcel: Parcel): City {
            return City(parcel)
        }

        override fun newArray(size: Int): Array<City?> {
            return arrayOfNulls(size)
        }
    }
}