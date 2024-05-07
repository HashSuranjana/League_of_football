package com.example.application

import android.os.Bundle
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.application.ui.theme.ApplicationTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

lateinit var database2 :ClubsDataBase
lateinit var clubsDao: ClubsDao

class ClubsByLeague : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database2 = Room.databaseBuilder(
            this, ClubsDataBase::class.java,
            "ClubsDataBase"
        ).fallbackToDestructiveMigration()
            .build()

        clubsDao = database2.clubsDao()

        setContent {
            ApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LeagueSearch()

                }
            }
        }
    }


    @Composable
    fun LeagueSearch() {

        var clubinfoDisplay by rememberSaveable { mutableStateOf(" ") }

        var keyword by remember { mutableStateOf("") }  // the league title keyword that searching

        val scope = rememberCoroutineScope()  // Creates a CoroutineScope bound to the GUI composable lifecycle

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = "Search for Clubs",
                modifier = Modifier.padding(top = 30.dp))

            Column {

                TextField(value = keyword, onValueChange = { keyword = it })
            }

            Row {
                Button(onClick = {
                    scope.launch {

                        clubinfoDisplay = fetchClubs(keyword)
                    }
                }, modifier =Modifier.padding(top = 10.dp)) {
                    Text("Retrieve Clubs")
                }

                Button(onClick = {
                    scope.launch {
                        if (clubinfoDisplay.isNotEmpty()) { // Check if clubs have been fetched

                            println(clubinfoDisplay)
                            scope.launch {
                                save(clubinfoDisplay) // Save clubs
                            }
                        }
                    }
                }, modifier =Modifier.padding(top = 10.dp)) {
                    Text("Save clubs")
                }
            }

            Text(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                text = clubinfoDisplay
            )
        }
    }

    private suspend fun fetchClubs(keyword: String): String {

        val newKeyword = keyword.split(" ").joinToString("%20") //if there are spaces inside the keyword replaces them with %20

        val urlString = "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=$newKeyword" //new url to get the data

        val url = URL(urlString)

        val con: HttpURLConnection = url.openConnection() as HttpURLConnection


        val strb = StringBuilder() // collecting all the JSON string

        // runs the code of the launched coroutine in a new thread
        withContext(Dispatchers.IO) {

            val buffreader = BufferedReader(InputStreamReader(con.inputStream))
            var phrase: String? = buffreader.readLine()
            while (phrase != null) { // keep reading until no more lines of text
                strb.append(phrase + "\n")
                phrase = buffreader.readLine()
            }
        }

        val allClubs = parseJSON(strb)

        return allClubs //Returning all clubs
    }


    private fun parseJSON(strb: StringBuilder): String {


        val json = JSONObject(strb.toString()) // this contains the full JSON returned by the Web Service

        val allClubs = StringBuilder() // Information about all the clubs extracted by this function

        val jsonArray: JSONArray = json.getJSONArray("teams")

        // extract all the clubs from the JSON array
        for (i in 0..jsonArray.length() - 1) {

            val club: JSONObject = jsonArray[i] as JSONObject // this is a json object

            // extract the title
            allClubs.append(club.getString("idTeam") + "\n")
            allClubs.append(club.getString("strTeam") + "\n")
            allClubs.append(club.getString( "strTeamShort") + "\n")
            allClubs.append(club.getString("strAlternate") + "\n")
            allClubs.append(club.getString("intFormedYear") + "\n")
            allClubs.append(club.getString("strLeague") + "\n")
            allClubs.append(club.getString("idLeague") + "\n")
            allClubs.append(club.getString("strStadium") + "\n")
            allClubs.append(club.getString("strKeywords") + "\n")
            allClubs.append(club.getString("strStadiumThumb") + "\n")
            allClubs.append(club.getString("strStadiumLocation") + "\n")
            allClubs.append(club.getString("intStadiumCapacity") + "\n")
            allClubs.append(club.getString("strWebsite") + "\n")
            allClubs.append(club.getString("strTeamJersey") + "\n")
            allClubs.append(club.getString("strTeamLogo") + "\n\n\n")
        }

        return allClubs.toString()
    }

    private suspend fun save(clubsData: String) {
        val json = JSONObject(clubsData) // Convert string to JSONObject
        val jsonArray: JSONArray = json.getJSONArray("teams")


        for (i in 0..jsonArray.length() ) {
            val club: JSONObject = jsonArray[i] as JSONObject
            // Save each club to the database
            clubsDao.insertAll(
                Clubs(i,
                    club.getString("strTeam"),
                    club.getString("idTeam"),
                    club.getString("strTeamShort"),
                    club.getString("strAlternate"),
                    club.getString("intFormedYear"),
                    club.getString("strLeague"),
                    club.getString("idLeague"),
                    club.getString("strStadium"),
                    club.getString("strKeywords"),
                    club.getString("strStadiumThumb"),
                    club.getString("strStadiumLocation"),
                    club.getString("intStadiumCapacity"),
                    club.getString("strWebsite"),
                    club.getString("strTeamJersey"),
                    club.getString("strTeamLogo")
                )
            )
        }
    }
}




