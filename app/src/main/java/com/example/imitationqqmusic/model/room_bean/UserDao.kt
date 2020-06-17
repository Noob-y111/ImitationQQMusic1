package com.example.imitationqqmusic.model.room_bean

import androidx.room.*

@Dao
interface UserDao {
    @Insert
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Query("DELETE FROM User WHERE User.userId = :id")
    fun delete(id: String)

    @Query("SELECT User.userId FROM User WHERE User.userId = :id")
    fun selectByUserId(id: String): String

    @Query("SELECT * FROM User WHERE User.userId = :id")
    fun getUserById(id: String): User?

    @Query("SELECT * FROM User")
    fun getUserList(): Array<User>

    @Query("DELETE FROM User")
    fun deleteAll()
}