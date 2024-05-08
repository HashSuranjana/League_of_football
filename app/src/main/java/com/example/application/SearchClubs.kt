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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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

        var searchTerm by remember { mutableStateOf("") }
        
        var clubsFound by rememberSaveable { mutableStateOf("") }

        val scope = rememberCoroutineScope()  // Creates a CoroutineScope bound to the GUI composable lifecycle

        val orientation = LocalConfiguration.current.orientation // getting the orientation of the phone

        if(orientation == Configuration.ORIENTATION_PORTRAIT) {

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(text = "Search for Clubs",
                    modifier = Modifier.padding(top = 30.dp))

                Column {

                    TextField(value = searchTerm, onValueChange = { searchTerm = it }, singleLine = true)
                }

                Row {
                    Button(onClick = {
                        scope.launch {
                            clubsFound = clubsFinding(searchTerm, leaguesDao)

                        }
                    }, modifier =Modifier.padding(top = 10.dp)) {
                        Text("Search")
                    }


                }
                Text(text = clubsFound,
                    modifier =  Modifier.verticalScroll(rememberScrollState())
                )

                for (i in flagList) {

                    Image(bitmap = i.asImageBitmap(), contentDescription =null,modifier = Modifier.size(100.dp) )
                }

            }
        }else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(text = "Search for Clubs",
                    modifier = Modifier.padding(top = 30.dp))

                Column {

                    TextField(value = searchTerm, onValueChange = { searchTerm = it }, singleLine = true)
                }

                Row {
                    Button(onClick = {
                        scope.launch {
                            clubsFound = clubsFinding(searchTerm, leaguesDao)

                        }
                    }, modifier =Modifier.padding(top = 10.dp)) {
                        Text("Search")
                    }


                }

                Text(text = clubsFound,
                    modifier =  Modifier.verticalScroll(rememberScrollState())
                )

                println(flagList)

                for (i in flagList) {

                    Image(bitmap = i.asImageBitmap(), contentDescription =null,modifier = Modifier.size(100.dp) )
                }

            }
        }

    }

    private fun loadImageFromUrl(url: String): Bitmap? {
        var bitmap: Bitmap? = null
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
        return bitmap
    }

    private suspend fun clubsFinding(searchTerm:String, leaguesDao: LeaguesDao): String {
        var allLeagues = ""
        var count = 0
        // read the data
        val leagues: List<Leagues> = leaguesDao.getAll()

        runBlocking {
            launch {

                if (flagList.isNotEmpty()) {
                    flagList.clear()
                }

                for(i in leagues) {

                    if(i.strTeam?.contains(searchTerm,ignoreCase = true) == true || i.strLeague?.contains(searchTerm,ignoreCase = true)==true){

                        allLeagues += " ${ i.strTeam } \n\n "

                        i.strTeamLogo?.let { loadImageFromUrl(it)?.let { flagList.add(count,it) } }
                        count++


                    }else{
                        println("No Such Letters !")
                    }
                }
            }
        }

        return allLeagues
    }

}

