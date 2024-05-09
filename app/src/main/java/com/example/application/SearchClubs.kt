package com.example.application

import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.application.ui.theme.ApplicationTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class SearchClubs : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ClubsSearch()
                }
            }
        }
    }

    private val flagList = mutableListOf<Bitmap>()


    @Composable
    fun ClubsSearch(){

        LocalContext.current

        var searchTerm by rememberSaveable { mutableStateOf("") }
        
        var clubsFound by rememberSaveable { mutableStateOf("") }

        val scope = rememberCoroutineScope()  // Creates a CoroutineScope bound to the GUI composable lifecycle

        val orientation = LocalConfiguration.current.orientation // getting the orientation of the phone

        if(orientation == Configuration.ORIENTATION_PORTRAIT) {

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(text = "Find Your Club .......",
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .padding(10.dp),
                    style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.ExtraBold)
                )

                Spacer(modifier = Modifier.size(20.dp))

                Column(modifier= Modifier
                    .fillMaxWidth()
                    .padding(10.dp)) {

                    TextField(value = searchTerm, onValueChange = { searchTerm = it }, singleLine = true,
                        modifier = Modifier.width(400.dp))
                }

                Spacer(modifier = Modifier.size(20.dp))

                Row {
                    Button(onClick = {
                        scope.launch {
                            clubsFound = clubsFinding(searchTerm, leaguesDao)

                        }
                    }, modifier = Modifier
                        .padding(top = 10.dp)
                        .width(150.dp)) {
                        Text("Search")
                    }
                }

                Spacer(modifier = Modifier.size(20.dp))

                Text(text = clubsFound,
                    modifier =  Modifier.verticalScroll(rememberScrollState())
                )

                for (i in flagList) {

                    Image(bitmap = i.asImageBitmap(), contentDescription =null,modifier = Modifier.size(100.dp) )
                }

            }
        }else {
            Row (modifier = Modifier.fillMaxSize()){

                Column(
                    modifier = Modifier
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.Start
                ){

                    Text(text = "Find Your Club .......",
                        style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.ExtraBold),
                        modifier = Modifier.padding(top = 30.dp, start = 20.dp)
                    )

                    Spacer(modifier = Modifier.size(60.dp))

                    Column(modifier = Modifier.padding(start = 20.dp)) {

                        TextField(value = searchTerm, onValueChange = { searchTerm = it }, singleLine = true)
                    }

                    Spacer(modifier = Modifier.size(30.dp))

                    Row(modifier = Modifier
                        .padding(start = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,) {
                        Button(onClick = {
                            scope.launch {
                                clubsFound = clubsFinding(searchTerm, leaguesDao)

                            }
                        }, modifier = Modifier
                            .padding(top = 10.dp)
                            .width(150.dp)) {
                            Text("Search")
                        }


                    }
                }

                Column(modifier= Modifier
                    .fillMaxHeight()
                    .width(545.dp)
                    .padding(start = 10.dp)) {

                    Text(text = clubsFound,
                        modifier = Modifier.verticalScroll(rememberScrollState()),
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    )

                    for (i in flagList) {

                        Image(bitmap = i.asImageBitmap(), contentDescription =null,modifier = Modifier.size(100.dp) )
                    }
                }

            }
        }

    }

    //Retrieving the logo of the club
    private fun loadImageFromUrl(url: String): Bitmap? {
        var bitmap: Bitmap? = null  //check whether image can be got
        val connection = URL(url).openConnection() as? HttpURLConnection
        connection?.let {
            runBlocking {
                launch {
                    withContext(Dispatchers.IO) {
                        val inputStream = it.inputStream
                        bitmap = BitmapFactory.decodeStream(inputStream)
                    }
                }
            }
        }
        return bitmap // returning the image
    }

    //function to check the club's letters
    private suspend fun clubsFinding(searchTerm:String, leaguesDao: LeaguesDao): String {

        var allLeagues = "" //initialize the display message

        var count = 0 // initialize a count variable

        val leagues: List<Leagues> = leaguesDao.getAll()    // read the data

        runBlocking {
            launch {

                //check whether flag list is empty or not
                if (flagList.isNotEmpty()) {

                    flagList.clear()  //clear the flag list
                }

                //run through the league list
                for(i in leagues) {

                    //checking the search term is in the strTeam or in the strLeague Name

                    if(i.strTeam?.contains(searchTerm,ignoreCase = true) == true || i.strLeague?.contains(searchTerm,ignoreCase = true)==true){

                        allLeagues += " ${ i.strTeam } \n\n " //append the team name if condition is true

                        i.strTeamLogo?.let { loadImageFromUrl(it)?.let { flagList.add(count,it) } } //get the teams logo if it's available and add it to flagList
                        count++   //increment the count

                    }else{
                        println("No Such Letters !")
                    }
                }
            }
        }

        return allLeagues  //return displaying content
    }

}

