package com.deepak.currencyexchangeapp.databse

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RateEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun rateEntityDao(): RateEntityDao
}