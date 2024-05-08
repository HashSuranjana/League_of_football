package com.example.application

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.application.ui.theme.ApplicationTheme
import kotlinx.coroutines.launch

lateinit var database :FootBallDataBase
lateinit var leaguesDao: LeaguesDao

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        database = Room.databaseBuilder(
            this, FootBallDataBase::class.java,
            "FootBallDataBase"
        ).fallbackToDestructiveMigration()
            .build()

        leaguesDao = database.leaguesDao()

        setContent {
            ApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainScreen() // calling the composable function
                }
            }
        }
    }

    @Composable
    fun MainScreen(){

        val orientation = LocalConfiguration.current.orientation // getting the orientation of the phone
        val context = LocalContext.current

        if (orientation == Configuration.ORIENTATION_PORTRAIT){

            val scope = rememberCoroutineScope() //remember the scope of the GUI

            val leaguesData by rememberSaveable { mutableStateOf("") } //defining a variable called LeaguesData
            LaunchedEffect(leaguesData) {

                leaguesDao.deleteAll() // delete if there any previously saved data in the database

                //adding all the details of the leagues
                leaguesDao.insertAll(

                    Leagues(1,"English Premier League","4328","Soccer","Premier League, EPL","","","","","","","","","","",""),
                    Leagues(2,"English League Championship","4329","Soccer","Championship","","","","","","","","","","",""),
                    Leagues(3,"Scottish Premier League","4330","Soccer","Scottish Premiership, SPL","","","","","","","","","","",""),
                    Leagues(4,"German Bundesliga","4331","Soccer","Bundesliga, Fuzzball-Bundesliga","","","","","","","","","","",""),
                    Leagues(5,"Italian Series A","4332","Soccer","Series A","","","","","","","","","","",""),
                    Leagues(6,"French League 1","4334","Soccer","League 1 Conformal","","","","","","","","","","",""),
                    Leagues(7,"Greek Super league Greece","4336", "Soccer", "","","","","","","","","","","",""),
                    Leagues(8,"Dutch Divisive","4337","Soccer", "Divisive","","","","","","","","","","",""),
                    Leagues(9,"Danish Superlative","4340","Soccer","","","","","","","","","","","",""),
                    Leagues(10,"American Major League Soccer","4346","Soccer","MLS, Major League Soccer","","","","","","","","","","",""),
                    Leagues(11,"Swedish Allusiveness","4347","Soccer","Fotbollsallsvenskan","","","","","","","","","","",""),
                    Leagues(12,"Mexican Primer League","4350","Soccer","Ligand MX","","","","","","","","","","",""),
                    Leagues(13,"Brazilian Series A","4351","Soccer","","","","","","","","","","","",""),
                    Leagues(14,"Ukrainian Premier League","4354","Soccer","","","","","","","","","","","",""),
                    Leagues(15,"Russian Football Premier League","4355","Soccer","Чемпионат России по футболу","","","","","","","","","","",""),
                    Leagues(16,"Australian A-League","4356","Soccer","A-League","","","","","","","","","","",""),
                    Leagues(17,"Norwegian LineSeries","4358","Soccer","LineSeries","","","","","","","","","","",""),
                    Leagues(18,"Chinese Super League","4359","Soccer","","","","","","","","","","","","")

                )

            }

            Column(modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 40.dp),
                verticalArrangement = Arrangement.Bottom){

                Row(modifier = Modifier
                    .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround){

                    Button(onClick = {
                        val intent = Intent(this@MainActivity, AddActivity::class.java)

                        scope.launch {
                            intent.putExtra("Desc",retrieveData(leaguesDao))
                            startActivity(intent)
                            Toast.makeText(context,"Saved Successfully !", Toast.LENGTH_SHORT).show()
                        }


                    }) {
                        Text(text = "Add Leagues to DB")

                    }

                    Button(onClick = {
                        val intent = Intent(this@MainActivity,ClubsByLeague::class.java)
                        startActivity(intent)
                    }) {
                        Text(text = "Search for clubs by League")
                    }
                }

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center){

                    Button(onClick = {
                        val intent = Intent(this@MainActivity,SearchClubs::class.java)
                        startActivity(intent)
                    },modifier = Modifier.width(250.dp)) {
                        Text(text = "Search for Clubs")
                    }
                }
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = leaguesData
            )
        }

        else {

            val scope = rememberCoroutineScope() //remember the scope of the GUI

            val leaguesData by rememberSaveable { mutableStateOf("") } //defining a variable called LeaguesData

            LaunchedEffect(leaguesData) {

                leaguesDao.deleteAll() // delete if there any previously saved data in the database

                //adding all the details of the leagues
                leaguesDao.insertAll(

                    Leagues(1,"English Premier League","4328","Soccer","Premier League, EPL","","","","","","","","","","",""),
                    Leagues(2,"English League Championship","4329","Soccer","Championship","","","","","","","","","","",""),
                    Leagues(3,"Scottish Premier League","4330","Soccer","Scottish Premiership, SPL","","","","","","","","","","",""),
                    Leagues(4,"German Bundesliga","4331","Soccer","Bundesliga, Fuzzball-Bundesliga","","","","","","","","","","",""),
                    Leagues(5,"Italian Series A","4332","Soccer","Series A","","","","","","","","","","",""),
                    Leagues(6,"French League 1","4334","Soccer","League 1 Conformal","","","","","","","","","","",""),
                    Leagues(7,"Greek Super league Greece","4336", "Soccer", "","","","","","","","","","","",""),
                    Leagues(8,"Dutch Divisive","4337","Soccer", "Divisive","","","","","","","","","","",""),
                    Leagues(9,"Danish Superlative","4340","Soccer","","","","","","","","","","","",""),
                    Leagues(10,"American Major League Soccer","4346","Soccer","MLS, Major League Soccer","","","","","","","","","","",""),
                    Leagues(11,"Swedish Allusiveness","4347","Soccer","Fotbollsallsvenskan","","","","","","","","","","",""),
                    Leagues(12,"Mexican Primer League","4350","Soccer","Ligand MX","","","","","","","","","","",""),
                    Leagues(13,"Brazilian Series A","4351","Soccer","","","","","","","","","","","",""),
                    Leagues(14,"Ukrainian Premier League","4354","Soccer","","","","","","","","","","","",""),
                    Leagues(15,"Russian Football Premier League","4355","Soccer","Чемпионат России по футболу","","","","","","","","","","",""),
                    Leagues(16,"Australian A-League","4356","Soccer","A-League","","","","","","","","","","",""),
                    Leagues(17,"Norwegian LineSeries","4358","Soccer","LineSeries","","","","","","","","","","",""),
                    Leagues(18,"Chinese Super League","4359","Soccer","","","","","","","","","","","","")

                )
            }

            Column(modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 40.dp),
                verticalArrangement = Arrangement.Bottom){

                Row(modifier = Modifier
                    .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center){

                    Button(onClick = {
                        val intent = Intent(this@MainActivity, AddActivity::class.java)

                        scope.launch {
                            intent.putExtra("Desc",retrieveData(leaguesDao))
                            startActivity(intent)
                            Toast.makeText(context,"Saved Successfully !", Toast.LENGTH_SHORT).show()
                        }

                    },modifier = Modifier.width(250.dp)) {
                        Text(text = "Add Leagues to DB")
                    }

                    Spacer(modifier = Modifier.size(20.dp))

                    Button(onClick = {
                        val intent = Intent(this@MainActivity,ClubsByLeague::class.java)
                        startActivity(intent)
                    },modifier = Modifier.width(250.dp)) {
                        Text(text = "Search for clubs by League")
                    }

                    Spacer(modifier = Modifier.size(20.dp))

                    Button(onClick = {
                        val intent = Intent(this@MainActivity,SearchClubs::class.java)
                        startActivity(intent)
                    },modifier = Modifier.width(250.dp)) {
                        Text(text = "Search for Clubs")
                    }
                }
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = leaguesData
            )
        }
    }
}

suspend fun retrieveData(leaguesDao: LeaguesDao): String {
    var allLeagues = ""
    // read the data
    val leagues: List<Leagues> = leaguesDao.getAll()

    for (i in leagues)

        allLeagues += "${i.strTeam} \n ${i.strLeague} \n  ${i.strAlternate} \n ${i.strKeywords} \n\n\n"

    return allLeagues
}





