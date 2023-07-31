package com.deepak.currencyexchangeapp.databse

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity

class RateEntity(@PrimaryKey val symbol: String, val rate: Double)

@Dao
abstract class RateEntityDao {
    @Query("select * from RateEntity")
    abstract fun getAll(): Flow<List<RateEntity>>

    @Query("select * from RateEntity where symbol like :symbolName")
    abstract fun getRate(symbolName: String): RateEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(rateData: List<RateEntity>)

    @Query("delete from RateEntity")
    abstract fun deleteAll()
}