package com.deepak.currencyexchangeapp

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.deepak.currencyexchangeapp.databse.AppDatabase
import com.deepak.currencyexchangeapp.databse.RateEntity
import com.deepak.currencyexchangeapp.databse.RateEntityDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDataBaseTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var rateEntityDao: RateEntityDao

    @Before
    fun setUp() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        appDatabase = Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java).build()
        rateEntityDao = appDatabase.rateEntityDao()
    }

    @Test
    fun getRateTest() {
        val dataList = mutableListOf<RateEntity>().apply {
            add(RateEntity(symbol = "USD", 1.0))
            add(RateEntity(symbol = "INR", 82.0))
            add(RateEntity(symbol = "AED", 3.4))
        }
        rateEntityDao.insertAll(dataList)
        val data = rateEntityDao.getRate("USD")
        Assert.assertTrue(data != null)
    }

    @Test
    fun getAllTest() = runTest {
        val dataList = mutableListOf<RateEntity>().apply {
            add(RateEntity(symbol = "USD", 1.0))
            add(RateEntity(symbol = "INR", 82.0))
            add(RateEntity(symbol = "AED", 3.4))
        }
        rateEntityDao.insertAll(dataList)
        val data = rateEntityDao.getAll().first()
        Assert.assertTrue(data != null)
    }

    @Test
    fun deleteAllTest() = runTest {
        val dataList = mutableListOf<RateEntity>().apply {
            add(RateEntity(symbol = "USD", 1.0))
            add(RateEntity(symbol = "INR", 82.0))
            add(RateEntity(symbol = "AED", 3.4))
        }
        rateEntityDao.insertAll(dataList)
        val data = rateEntityDao.getAll().first()
        Assert.assertTrue(data != null)
        rateEntityDao.deleteAll()
        val afterDeleteData = rateEntityDao.getAll().first()
        Assert.assertTrue(afterDeleteData.isEmpty())

    }
}