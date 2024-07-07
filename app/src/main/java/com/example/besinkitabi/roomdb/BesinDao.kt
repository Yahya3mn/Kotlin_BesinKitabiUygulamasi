package com.example.besinkitabi.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.besinkitabi.model.Besin

@Dao
interface BesinDao {

    @Insert
    suspend fun insertAll(vararg besin : Besin) : List<Long>
    //eklediğimiz besinlerin id'sini long olarak geri veriyor

    @Query("SELECT * FROM besin")
    suspend fun getAllBesin() : List<Besin>

    @Query("SELECT * FROM besin WHERE uuid = :besinId")
    suspend fun getBesinId(besinId : Int) : Besin

    @Query("DELETE FROM besin")
    suspend fun deleteAllBesin()
}