package com.example.imitationqqmusic.model.room_bean

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class UserDataBase : RoomDatabase(){

    companion object{
        private var userDataBase: UserDataBase? = null

        fun newInstance(context: Context) = synchronized(this){
            userDataBase ?: kotlin.run {
                userDataBase = Room.databaseBuilder(context, UserDataBase::class.java, "user")
                        .build()
            }
            userDataBase
        }
    }

    abstract fun getUserDao(): UserDao
}