package com.example.application

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.application.ui.theme.ApplicationTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

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

    @Composable
    fun ClubsSearch(){

        val context = LocalContext.current

        var searchTerm by remember { mutableStateOf("") }
        
        var clubsFound by rememberSaveable { mutableStateOf("") }

        val scope = rememberCoroutineScope()  // Creates a CoroutineScope bound to the GUI composable lifecycle

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
                            clubsFound = retrieveData(searchTerm, leaguesDao)
                        
                    }
                }, modifier =Modifier.padding(top = 10.dp)) {
                    Text("Search")
                }


            }
            
            Text(text = clubsFound)
            
        }

    }

    @Composable
    fun ClubItem(club :Leagues) {
        Text(text = club.strTeam?:"")

    }

    suspend fun retrieveData(searchTerm:String, leaguesDao: LeaguesDao): String {
        var allLeagues = ""
        // read the data
        val leagues: List<Leagues> = leaguesDao.getAll()

        for(i in leagues) {

            if(i.strTeam?.contains(searchTerm,ignoreCase = true) == true || i.strLeague?.contains(searchTerm,ignoreCase = true)==true){

                allLeagues += "${i.strTeam}"

            }
        }

        return allLeagues
    }

//    fun clubFindings(searchTerm:TextFieldValue, context: Context, clubsFound:MutableState<List<Leagues>>):List<Leagues>{
//        runBlocking {
//            launch {
//                withContext(Dispatchers.IO){
//                    val clubsDao = FootBallDataBase.getDatabase(context).leaguesDao()
//                    val clubList = clubsDao.getAll()
//                    val filteredClubs = clubList.filter { club->
//                        club.strLeague?.contains(searchTerm.toString(), ignoreCase = true)?:false ||
//                                club.strTeam?.contains(searchTerm.toString(),ignoreCase = true)?:false
//                    }
//
//                    clubsFound.value = filteredClubs
//                }
//            }
//        }
//        return clubsFound.value
//    }
}

