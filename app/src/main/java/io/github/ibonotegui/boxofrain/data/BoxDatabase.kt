package io.github.ibonotegui.boxofrain.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//can have multiple entities
@Database(entities = [Location::class], version = 1)
abstract class BoxDatabase : RoomDatabase() {

    //can have multiple daos here
    abstract fun locationDao(): LocationDao

    //singleton prevents multiple instances of database opening at the same time
    companion object {

        @Volatile
        private var INSTANCE: BoxDatabase? = null

        fun getDatabase(context: Context): BoxDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BoxDatabase::class.java,
                    "location_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}
