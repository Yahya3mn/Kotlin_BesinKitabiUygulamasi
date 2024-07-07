package com.example.besinkitabi.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.besinkitabi.model.Besin
import kotlin.concurrent.Volatile

@Database(entities = [Besin::class], version = 1)
abstract class BesinDatabase : RoomDatabase() {
    abstract fun BesinDAO(): BesinDao

    //Data Race engellemek için kod
    companion object{
        @Volatile
        private var instance : BesinDatabase? = null

        private val lock = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(lock){
            instance ?: databaseOlustur(context).also {
                instance = it
            }
        }

        private fun databaseOlustur(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            BesinDatabase::class.java,
            "BesinDatabase"
        ).build()
    }
}