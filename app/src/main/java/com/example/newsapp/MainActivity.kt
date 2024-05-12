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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.MainViewModelFactory
import com.example.newsapp.ui.theme.NewsAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainViewModel = ViewModelProvider(this, MainViewModelFactory(this))[MainViewModel::class.java]
        setContent {
            NewsAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen(mainViewModel)
                }
            }
        }
    }
}

@Composable
fun HomeScreen(mainViewModel: MainViewModel) {

    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomNavigationBar(mainViewModel, mainViewModel.selectedTabForNavigationBar.intValue) { tab -> mainViewModel.selectedTabForNavigationBar.intValue = tab } }
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ){
            HorizontalScrollableTabRow(mainViewModel, state = mainViewModel.selectedTabForTabRow.intValue , onStateSelected = { tab -> mainViewModel.selectedTabForTabRow.intValue = tab})

            LazyColumn() {
                for(i in 0..<mainViewModel.listItemsHomeScreen.value.size){
                    item {
                        Item_single(
                            items = Items(
                                mainViewModel.listItemsHomeScreen.value[i].img,
                                mainViewModel.listItemsHomeScreen.value[i].headline,
                                mainViewModel.listItemsHomeScreen.value[i].source,
                                mainViewModel.listItemsHomeScreen.value[i].date,
                            )
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun Item_single(items:Items, modifier:Modifier = Modifier){

    Card (
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = Modifier
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
fun BottomNavigationBar(mainViewModel: MainViewModel, selectedTab: Int, onTabSelected: (Int) -> Unit) {
    NavigationBar(
        contentColor = Color.Black,
        modifier = Modifier.fillMaxWidth()
    ) {
        for(i in 0..<mainViewModel.bottomNavigationCategories.size){
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = mainViewModel.bottomNavigationImages[i],
                        contentDescription = mainViewModel.bottomNavigationCategories[i]
                    )
                },
                label = { Text(mainViewModel.bottomNavigationCategories[i]) },
                selected = selectedTab == i,
                onClick = { onTabSelected(i) }
            )
        }
    }
}

@Composable
fun HorizontalScrollableTabRow(mainViewModel: MainViewModel, state: Int, onStateSelected: (Int) -> Unit){
    ScrollableTabRow(selectedTabIndex = state) {

        for(i in 0..<mainViewModel.categories.size){
            Tab(
                selected = state == i,
                onClick = { onStateSelected(i)
                    mainViewModel.changeTabs(i)},
                text = { Text(text = mainViewModel.categories[i], maxLines = 2, overflow = TextOverflow.Ellipsis) }
            )
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun GreetingPreview() {
//    NewsAppTheme {
//        HomeScreen()
//    }
//}