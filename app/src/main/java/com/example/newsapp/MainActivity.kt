package com.example.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newsapp.ui.theme.NewsAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen()
                }
            }
        }
    }
}

@Composable
fun HomeScreen() {
    var selectedTabForNavigationBar by remember {
        mutableIntStateOf(0)
    }

    var selectedTabForTabRow by remember {
        mutableIntStateOf(0)
    }

    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomNavigationBar(selectedTabForNavigationBar) { tab -> selectedTabForNavigationBar = tab } }
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ){
            HorizontalScrollableTabRow(state = selectedTabForTabRow , onStateSelected = { tab -> selectedTabForTabRow = tab})

            LazyColumn() {
                //cards list
                for (i in 1..5) {
                    item {
                        Item_single(
                            items = Items(
                                R.drawable.ic_launcher_background,
                                "Brazil judge orders probe of Elon Musk over censorship charge",
                                "Tech Xplore",
                                "2024-04-08T09:10:04Z"
                            )
                        )
                    }
                }
            }
        }
    }
//    BottomNavigationBar(selectedTab = 0, onTabSelected = {tab -> selectedTab - tab})
}


data class Items(val img: Int, val headline: String, val source: String, val date: String)

@Composable
fun Item_single(items:Items, modifier:Modifier = Modifier){

    Card (
//        elevation = CardDefaults.cardElevation(
//            defaultElevation = 8.dp
//        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = Modifier
//            .padding(2.dp)
            .background(color = Color.White)
    ){
        Row {
            Column(Modifier.weight(0.7F)) {
                Text(items.source, Modifier.padding(start = 10.dp), color=Color.DarkGray, fontSize = 14.sp)
                Text(items.headline, Modifier.padding(start = 10.dp, top=6.dp, end=8.dp), fontSize = 20.sp, color=Color.Black)
                Text(items.date, Modifier.padding(start = 10.dp, top=12.dp), color=Color.Gray, fontSize = 10.sp)
            }
            Column(Modifier.weight(0.3F)){
                Image(painter = painterResource(id = items.img),
                      contentDescription = "Heart",
                      modifier.padding(end=20.dp))
            }
        }
        Divider(
            color = Color.Gray,
            thickness = 0.5.dp,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth(0.8f)
//                .padding(start=8.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(){
    TopAppBar(
        windowInsets = WindowInsets.navigationBars,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text("News App")
        }
    )
}


@Composable
fun BottomNavigationBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    NavigationBar(
        contentColor = Color.Black,
        modifier = Modifier.fillMaxWidth()
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Home"
                )
            },
            label = { Text("HOME") },
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Saved"
                )
            },
            label = { Text("Saved") },
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Settings"
                )
            },
            label = { Text("Settings") },
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) }
        )
    }
}

@Composable
fun HorizontalScrollableTabRow(state: Int, onStateSelected: (Int) -> Unit){
    ScrollableTabRow(selectedTabIndex = state) {

        Tab(
            selected = state == 0,
            onClick = { onStateSelected(0) },
            text = { Text(text = "Item 0", maxLines = 2, overflow = TextOverflow.Ellipsis) }
        )
        Tab(
            selected = state == 1,
            onClick = { onStateSelected(1) },
            text = { Text(text = "Item 1", maxLines = 2, overflow = TextOverflow.Ellipsis) }
        )
        Tab(
            selected = state == 2,
            onClick = { onStateSelected(2) },
            text = { Text(text = "Item 2", maxLines = 2, overflow = TextOverflow.Ellipsis) }
        )
        Tab(
            selected = state == 3,
            onClick = { onStateSelected(3) },
            text = { Text(text = "Item 3", maxLines = 2, overflow = TextOverflow.Ellipsis) }
        )
        Tab(
            selected = state == 4,
            onClick = { onStateSelected(4) },
            text = { Text(text = "Item 4", maxLines = 2, overflow = TextOverflow.Ellipsis) }
        )
        Tab(
            selected = state == 5,
            onClick = { onStateSelected(5) },
            text = { Text(text = "Item 5", maxLines = 2, overflow = TextOverflow.Ellipsis) }
        )
        Tab(
            selected = state == 6,
            onClick = { onStateSelected(6) },
            text = { Text(text = "Item 5", maxLines = 2, overflow = TextOverflow.Ellipsis) }
        )

    }

}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    NewsAppTheme {
        HomeScreen()
    }
}