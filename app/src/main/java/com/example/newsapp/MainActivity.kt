package com.example.newsapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.MainViewModelFactory
import com.example.newsapp.ui.theme.NewsAppTheme
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import retrofit2.Call
import java.util.Locale
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {

    private val PERMISSION_REQUEST_LOCATION = 1
    lateinit var mainViewModel:MainViewModel
    private val apiKey = "5ab5c1b91b1d4b59802c9bddb33aab02"

    private fun fetchNews() {
        val call: Call<NewsResponse> = mainViewModel.getNews(mainViewModel.city, "2024-05-12", "popularity", apiKey)

        call.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    val newsResponse: NewsResponse? = response.body()
                    if(newsResponse != null){
                        for(news in newsResponse.articles){
                            val sourceName = news.source?.name ?: "Unknown source"
                            mainViewModel.tab0.add(Items(R.drawable.ic_launcher_background, news.title, sourceName, news.publishedAt!!, news.content))
                        }
                    }
                } else {
                    Log.d("API", "API response unsuccessful")
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Log.d("API", "API Failure")
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(this, MainViewModelFactory(this))[MainViewModel::class.java]
        val pm = packageManager
        if (pm.checkPermission(
                Manifest.permission.ACCESS_FINE_LOCATION,
                packageName
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getLocation(mainViewModel)
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_LOCATION
            )
        }

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

    private fun getLocation(mainViewModel: MainViewModel) {
        val pm = packageManager
        if (pm.checkPermission(
                Manifest.permission.ACCESS_FINE_LOCATION,
                packageName
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val fusedLocation = LocationServices.getFusedLocationProviderClient(this)
            fusedLocation.lastLocation.addOnSuccessListener { location: Location? ->
                val geocoder: Geocoder = Geocoder(this, Locale.getDefault())
                val addresses: MutableList<Address>? = location?.let { geocoder.getFromLocation(it.latitude, location.longitude, 1)}
                mainViewModel.city = addresses?.get(0)?.locality!!
                mainViewModel.country = addresses[0].countryName!!

                mainViewModel.categories[0] = mainViewModel.city
                fetchNews()
            }
        }
        else {
            Log.d("Location", "Permission Denied")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation(mainViewModel)
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
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
                            mainViewModel,
                            item = mainViewModel.listItemsHomeScreen.value[i]
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun Item_single(mainViewModel: MainViewModel, item:Items, modifier:Modifier = Modifier){

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Card (
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = Modifier
            .background(color = Color.White)
            .clickable {
                val intent = Intent(context, NewsArticlesActivity::class.java)
                intent.putExtra("headline", item.headline)
                intent.putExtra("image", item.img)
                intent.putExtra("content", item.content)
                intent.putExtra("bookmark", item.bookmark.intValue)
                context.startActivity(intent)
            }

    ){
        Row {
            Column(Modifier.weight(0.7F)) {
                Text(item.source, Modifier.padding(start = 10.dp), color=Color.DarkGray, fontSize = 14.sp)
                Text(item.headline, Modifier.padding(start = 10.dp, top=6.dp, end=8.dp), fontSize = 20.sp, color=Color.Black)
                Text(item.date, Modifier.padding(start = 10.dp, top=12.dp), color=Color.Gray, fontSize = 10.sp)
            }
            Column(Modifier.weight(0.3F)){
                Image(painter = painterResource(id = item.img),
                      contentDescription = "News Image",
                      modifier.padding(end=20.dp))
                Row {
                    if (mainViewModel.selectedTabForNavigationBar.intValue == 0){
                        Image(imageVector = if (item.bookmark.intValue == 0) Icons.Filled.FavoriteBorder else Icons.Filled.Favorite,
                              contentDescription = "Save Button",
                              modifier.clickable {
                                item.bookmark.intValue = 1 - item.bookmark.intValue

                                coroutineScope.launch {
                                    if(item.bookmark.intValue == 1) {
                                        mainViewModel.addToDatabase(item)
                                    }
                                    else{
                                        mainViewModel.deleteFromDatabase(item)
                                    }
                                }

                            })
                    }
                    Image(imageVector = Icons.Filled.Share, contentDescription = "Share Button")
                }
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
                onClick = {
                    onTabSelected(i)
                    if(i == 0){
                        mainViewModel.homeButtonClicked()
                    }
                    else if(i == 1){
                        mainViewModel.savedButtonClicked()
                    }
                }
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