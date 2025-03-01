package com.example.newsapp

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf



data class Items(val img: Int,
                 val headline: String,
                 val source: String,
                 val date: String,
                 val content: String = "Content...",
                 var bookmark:MutableIntState = mutableIntStateOf(0))