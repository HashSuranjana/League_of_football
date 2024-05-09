package com.example.application

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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


class ClubsByLeague : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


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

    private var newList = mutableListOf<Leagues>() //List for save clubs data getting from the web service

    @Composable
    fun LeagueSearch() {

        var clubinfoDisplay by rememberSaveable { mutableStateOf(" ") }

        var keyword by rememberSaveable { mutableStateOf("") }  // the league title keyword that searching

        val scope = rememberCoroutineScope()  // Creates a CoroutineScope bound to the GUI composable lifecycle

        val orientation = LocalConfiguration.current.orientation // getting the orientation of the phone

        val context = LocalContext.current

        if(orientation == Configuration.ORIENTATION_PORTRAIT) {

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start
            ) {

                Text(text = "Search for Leagues ......",
                    modifier = Modifier.padding(top = 30.dp).padding(10.dp),
                    style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.ExtraBold)
                )

                Spacer(modifier = Modifier.size(20.dp))

                Column(modifier= Modifier.fillMaxWidth().padding(10.dp)) {

                    TextField(value = keyword, onValueChange = { keyword = it },
                        modifier = Modifier.width(400.dp))
                }

                Spacer(modifier = Modifier.size(20.dp))

                Row(modifier= Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center) {
                    Button(onClick = {

                        if(keyword !=""){
                            scope.launch {

                                clubinfoDisplay = fetchClubs(keyword)
                            }
                        }else{
                            Toast.makeText(context,"Please Enter an League Name to Search", Toast.LENGTH_SHORT).show()
                        }
                    }, modifier =Modifier.padding(top = 10.dp).width(150.dp)) {
                        Text("Retrieve Clubs")
                    }

                    Spacer(modifier = Modifier.size(20.dp))

                    Button(onClick = {
                        scope.launch {
                            leaguesDao.deleteAll()
                            for (clubs in newList) {
                                leaguesDao.insertAll(clubs)
                            }
                        }
                    }, modifier =Modifier.padding(top = 10.dp).width(150.dp)) {
                        Text("Save clubs")
                    }
                }

                Spacer(modifier = Modifier.size(20.dp))

                Text(
                    modifier = Modifier.verticalScroll(rememberScrollState()).padding(start = 10.dp),
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp),
                    text = clubinfoDisplay
                )
            }

        } else {

            Row (modifier = Modifier.fillMaxSize()){

                Column(
                    modifier = Modifier
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.Start
                ) {

                    Text(
                        text = "Search for Leagues ......",
                        style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.ExtraBold),
                        modifier = Modifier.padding(top = 30.dp, start = 20.dp)
                    )

                    Spacer(modifier = Modifier.size(100.dp))

                    Column(modifier = Modifier.padding(start = 20.dp)) {

                        TextField(value = keyword, onValueChange = { keyword = it })
                    }

                    Spacer(modifier = Modifier.size(30.dp))

                    Row(
                        modifier = Modifier
                            .padding(start = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                        Button(onClick = {
                            if (keyword != "") {
                                scope.launch {

                                    clubinfoDisplay = fetchClubs(keyword)
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Please Enter an League Name to Search",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }, modifier = Modifier
                            .padding(top = 10.dp)
                            .width(150.dp)) {
                            Text("Retrieve Clubs")
                        }

                        Spacer(modifier = Modifier.size(10.dp))

                        Button(onClick = {
                            scope.launch {
                                leaguesDao.deleteAll()
                                for (clubs in newList) {
                                    leaguesDao.insertAll(clubs)
                                }
                            }
                        }, modifier = Modifier
                            .padding(top = 10.dp)
                            .width(150.dp)) {
                            Text("Save clubs")
                        }
                    }
                }

                Column(modifier= Modifier.fillMaxHeight().width(545.dp).padding(start=10.dp)) {
                    Text(
                        modifier = Modifier.verticalScroll(rememberScrollState()),
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp),
                        text = clubinfoDisplay
                    )
                }


            }
        }
    }

    private suspend fun fetchClubs(keyword: String): String {

        val newKeyword = keyword.split(" ")
            .joinToString("%20") //if there are spaces inside the keyword replaces them with %20

        val urlString =
            "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=$newKeyword" //new url to get the data

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

        return parseJSON(strb) //Returning all clubs
    }

    private fun parseJSON(strb: StringBuilder): String {


        val json = JSONObject(strb.toString()) // this contains the full JSON returned by the Web Service

        val allClubs = StringBuilder() // Information about all the clubs extracted by this function

        val jsonArray: JSONArray = json.getJSONArray("teams")

        // extract all the clubs from the JSON array
        for (i in 0..jsonArray.length() - 1) {

            val club: JSONObject = jsonArray[i] as JSONObject // this is a json object

            // extract the details
            allClubs.append("Name :" + club.getString("strTeam") + "\n")
            allClubs.append("Team Id :" + club.getString("idTeam") + "\n")
            allClubs.append("Short Name :" + club.getString( "strTeamShort") + "\n")
            allClubs.append("Alternate :" + club.getString("strAlternate") + "\n")
            allClubs.append("Year :" + club.getString("intFormedYear") + "\n")
            allClubs.append("League :" + club.getString("strLeague") + "\n")
            allClubs.append("League Id :" + club.getString("idLeague") + "\n")
            allClubs.append("Keywords :" + club.getString("strKeywords") + "\n")
            allClubs.append("Stadium :" + club.getString("strStadium") + "\n")
            allClubs.append("Stadium Logo :" + club.getString("strStadiumThumb") + "\n")
            allClubs.append("Stadium Loc :" + club.getString("strStadiumLocation") + "\n")
            allClubs.append("Stadium Capacity :" + club.getString("intStadiumCapacity") + "\n")
            allClubs.append("Web :" + club.getString("strWebsite") + "\n")
            allClubs.append("Jersey :" + club.getString("strTeamJersey") + "\n")
            allClubs.append("Team Logo :" + club.getString("strTeamLogo") + "\n\n\n")

            val newClub = Leagues(i+1,club.getString("idTeam"),
                                        club.getString("strTeam") ,
                                        club.getString( "strTeamShort"),
                                        club.getString( "strAlternate"),
                                        club.getString( "intFormedYear"),
                                        club.getString( "strLeague"),
                                        club.getString( "idLeague"),
                                        club.getString( "strStadium"),
                                        club.getString( "strKeywords"),
                                        club.getString( "strStadiumThumb"),
                                        club.getString( "strStadiumLocation"),
                                        club.getString( "intStadiumCapacity"),
                                        club.getString( "strWebsite"),
                                        club.getString( "strTeamJersey"),
                                        club.getString( "strTeamLogo")

            )

            newList.add(i,newClub) // adding items to the newList

        }

        return allClubs.toString()
    }


}




