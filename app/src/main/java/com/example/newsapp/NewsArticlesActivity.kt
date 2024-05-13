package com.example.newsapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.newsapp.ui.theme.NewsAppTheme

class NewsArticlesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val headline = intent.getStringExtra("headline")
        val image = intent.getIntExtra("image", 0)
//        val content = intent.getStringExtra("content")
        val bookmark = intent.getIntExtra("bookmark", 0)

        val content =
            "If you are a android developer or just a casual guy developing some android apps. I am sure you had come in a situation where you have to share some data from one activity to another. (Hope you know what is activity :-) ).\n" +
                    "\n" +
                    "I will be writing code in java, if you want code or example in kotlin use some online converter and paste the java code and you will get kotlin code. Otherwise comment or mail me I will write another article for kotlin also.\n" +
                    "\n" +
                    "There are many different ways to pass the data from one activity to another activity.\n" +
                    "\n" +
                    "Intents/Bundles (custom model also)\n" +
                    "Shared Preferences\n" +
                    "Static classes\n" +
                    "Database\n" +
                    "Shared Preferences and database are valid when we need to pass data and at the same time make this information persistent. This is the case of configuration parameter for example where we have to persist this information to not ask again it. We will cover this topic in another post, by now we are more interested on sharing data between activities without making them persistent. So we are in static classes and Intents.\n" +
                    "\n" +
                    "Static class\n" +
                    "This is the easiest way to achieve our goal. Exploiting the Android feature that an app runs in a single process we can use a static class to exchange data between activities. In this case we can create several static method to hold the data we want to share.\n" +
                    "Otherwise, we can use the singleton pattern that ensures we have only one instance of this class in all our JVM. In this case we create several method to hold the data.\n" +
                    "\n" +
                    "Intents\n" +
                    "This is the reason you are here, this is most versatile and used way. I can even say one of the most powerful when we don’t have to save the data and just send it.\n" +
                    "\n" +
                    "There are 3 way we can send data using intents.\n" +
                    "\n" +
                    "> Direct :- Put our data into intents directly\n" +
                    "\n" +
                    "> Bundle :- create a bundle and set the data here\n" +
                    "\n" +
                    "> Parcelable :- It is a way of “serializing” our object.\n" +
                    "\n" +
                    "To see how to use them let take a example we have two activities with name firstActivity and secondActivity."

        setContent {
            NewsAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ArticleScreen(articleTitle = headline!!, image = image, content = content!!) {
                        // intent for the back page goes here
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        this.startActivity(intent)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleScreen(
        articleTitle: String,
        image: Int,
        content: String,
        onBackClicked: () -> Unit
) {

    val scrollState = rememberScrollState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Your App Name") },
                navigationIcon = {
                    IconButton(onClick = { onBackClicked() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Save action */ }) {
                        Icon(Icons.Filled.FavoriteBorder, contentDescription = "Save")
                    }
                    IconButton(onClick = { /* Share action */ }) {
                        Icon(Icons.Filled.Share, contentDescription = "Share")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),

                )
        },
        bottomBar = { }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // News content goes here
//            Text(text = articleTitle,
//                 style = MaterialTheme.typography.headlineMedium.copy(
//                     fontWeight = FontWeight.Bold
//                 ),
//                 modifier = Modifier.padding(21.dp))

            OutlinedCard(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .graphicsLayer {
                        shadowElevation = 2.dp.toPx()
                    }
                    .clickable(
                        onClick = { /* Handle click action here */ },
//                        indication = rememberRipple()
                    ),
            ) {
                Text(
                    text = articleTitle,
                    modifier = Modifier
                        .padding(16.dp),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center,
                )
                Image(
                    painter = painterResource(id = image),
                    contentDescription = "Heart",
                    modifier = Modifier.fillMaxWidth()
                )
                Text(text = content, modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    NewsAppTheme {
        ArticleScreen(
            articleTitle = "\"The article title goes here\"",
            image = R.drawable.ic_launcher_background,
            content = "contnet"
        ) {

        }
    }
}