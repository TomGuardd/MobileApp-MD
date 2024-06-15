package com.capstone.tomguard.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface PredictionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(prediction: Prediction)

    //    @Query("SELECT * from prediction ORDER BY id ASC")
    //    fun getAllPredictions(): LiveData<List<Prediction>>
}