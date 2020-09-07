package com.example.imitationqqmusic.model.room_bean

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
        @PrimaryKey
        var userId: String,

        @ColumnInfo(name = "userName")
        var name: String?,

        @ColumnInfo(name = "headUrl")
        var userHead: String?,

        @ColumnInfo(name = "password")
        var passWord: String
) : Parcelable {
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(userId)
        dest?.writeString(name)
        dest?.writeString(userHead)
        dest?.writeString(passWord)
    }

    override fun describeContents(): Int {
        return 0
    }

    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString(),
            parcel.readString(),
            parcel.readString()!!) {
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }

    fun toStringArray(): ArrayList<String> {
        val arr = ArrayList<String>()
        val head = if (userHead == null) "" else userHead!!
        arr.add(head)
        val uName = if (name == null) "" else name!!
        arr.add(uName)
        arr.add(userId)
        return arr
    }
}