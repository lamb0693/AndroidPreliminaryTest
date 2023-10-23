package com.example.first

import android.os.Parcel
import android.os.Parcelable

data class MemberDTO(
    var id : Int,
    var name : String?,
    var age :Int,
    var mobile : String?
) : Parcelable {
    constructor(name:String?, age:Int, mobile:String?):this(0, name, age, mobile)
    companion object CREATOR : Parcelable.Creator<MemberDTO>{
        override fun createFromParcel(source: Parcel): MemberDTO {
            return MemberDTO(source)
        }

        override fun newArray(size: Int): Array<MemberDTO?> {
            return arrayOfNulls<MemberDTO>(size)
        }

    }

    constructor(parcel : Parcel) :
            this(parcel.readInt(), parcel.readString(), parcel.readInt(), parcel.readString())

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(name)
        dest.writeInt(age)
        dest.writeString(mobile)
    }

}
