package io.github.ibonotegui.boxofrain.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LocationDao {

//    @Query("SELECT * FROM Location LIMIT :limit")
//    fun getLocation(limit : Int): LiveData<Location>

    // LiveData notifies its active observers when the data has changed.
    @Query("SELECT * FROM Location LIMIT 1")
    fun getLocation(): LiveData<Location>

    @Query("SELECT * FROM Location WHERE name = :name")
    fun getLocationByName(name: String): LiveData<Location>

    //not allowed from main thread, need to return LiveData
    @Query("SELECT * FROM Location")
    fun getLocations(): LiveData<List<Location>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(vararg locations: Location)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateLocation(vararg locations: Location)

}
