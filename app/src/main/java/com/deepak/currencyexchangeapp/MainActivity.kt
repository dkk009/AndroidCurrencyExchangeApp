package com.deepak.currencyexchangeapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.deepak.currencyexchangeapp.ui.HomeScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : androidx.activity.ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
           HomeScreen()
        }
    }
}

@Composable
fun HelloWorld() {
    Text(text = "Hello world")
}