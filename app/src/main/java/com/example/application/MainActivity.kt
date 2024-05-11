package com.example.application

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.application.ui.theme.ApplicationTheme
import kotlinx.coroutines.launch

lateinit var database :FootBallDataBase
lateinit var clubsDao: ClubsDao
lateinit var leaguesDao: LeaguesDao

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        database = Room.databaseBuilder(
            this, FootBallDataBase::class.java,
            "FootBallDataBase"
        ).fallbackToDestructiveMigration()
            .build()

        clubsDao = database.clubsDao()
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

                    Leagues(1,"4329","English League Championship","Soccer","Championship, English League Championship"),
                    Leagues(2,"4330","Scottish Premier League","Soccer","Scottish Premiership, SPL, Scottish Premier League"),
                    Leagues(3,"4331","German Bundesliga","Soccer","Bundesliga, Fuzzball-Bundesliga, German Bundesliga"),
                    Leagues(4,"4332","Italian Series A","Soccer","Series A, Italian Series A"),
                    Leagues(5,"4334","French League 1","Soccer","League 1 Conformal, French League 1"),
                    Leagues(6,"4335","Spanish La Ligand","Soccer","LaLigand Santander, La Ligand, Spanish La Ligand"),
                    Leagues(7,"4336","Greek Super league Greece","Soccer","Greek Super league Greece"),
                    Leagues(8,"4337","Dutch Eredivisie","Soccer","Divisive, Dutch Eredivisie"),
                    Leagues(9,"4338","Belgian First Division A","Soccer","Jupiter Pro League, Belgian First Division A"),
                    Leagues(10,"4339","Turkish Super Lig","Soccer","Super Lig, Turkish Super Lig"),
                    Leagues(11,"4340","Danish Superlative","Soccer","Danish Superlative"),
                    Leagues(12,"4344","Portuguese Prairie Ligand","Soccer","Ligand NOS, Portuguese Prairie Ligand"),
                    Leagues(13,"4346","American Major League Soccer","Soccer","MLS, Major League Soccer, American Major League Soccer"),
                    Leagues(14,"4347","Swedish Allusiveness","Soccer","Fotbollsallsvenskan, Swedish Allusiveness"),
                    Leagues(15,"4350","Mexican Primer League","Soccer","Ligand MX, Mexican Primer League"),
                    Leagues(16,"4351","Brazilian Series A","Soccer","Brazilian Series A"),
                    Leagues(17,"4354","Ukrainian Premier League","Soccer","Ukrainian Premier League"),
                    Leagues(18,"4355","Russian Football Premier League","Soccer","Чемпионат России по футболу, Russian Football Premier League"),
                    Leagues(19,"4356","Australian A-League","Soccer","A-League, Australian A-League"),
                    Leagues(20,"4358","Norwegian LineSeries","Soccer","LineSeries, Norwegian LineSeries"),
                    Leagues(21,"4359","Chinese Super League","Soccer","Chinese Super League")

                )

            }

            Column(modifier = Modifier
                .fillMaxSize().background(Color(193, 221, 131)),
                verticalArrangement = Arrangement.Top){

                Box(modifier = Modifier.fillMaxWidth()){
                    Image(painter = painterResource(id = R.drawable.logo), contentDescription = null)
                }

                Spacer(modifier = Modifier.size(30.dp))

                Row(modifier = Modifier
                    .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround){

                    //Button to divert into Adding leagues to the Database
                    Button(onClick = {
                        val intent = Intent(this@MainActivity, AddActivity::class.java)

                        scope.launch {
                            intent.putExtra("Desc",retrieveData(clubsDao))
                            startActivity(intent)
                            Toast.makeText(context,"Saved Successfully !", Toast.LENGTH_SHORT).show()
                        }


                    }) {
                        Text(text = "Add Leagues to DB")

                    }

                    //Button to divert into Search for clubs by League
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

                    //Button to divert into Search for Clubs
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

                    Leagues(1,"4329","English League Championship","Soccer","Championship, English League Championship"),
                    Leagues(2,"4330","Scottish Premier League","Soccer","Scottish Premiership, SPL, Scottish Premier League"),
                    Leagues(3,"4331","German Bundesliga","Soccer","Bundesliga, Fuzzball-Bundesliga, German Bundesliga"),
                    Leagues(4,"4332","Italian Series A","Soccer","Series A, Italian Series A"),
                    Leagues(5,"4334","French League 1","Soccer","League 1 Conformal, French League 1"),
                    Leagues(6,"4335","Spanish La Ligand","Soccer","LaLigand Santander, La Ligand, Spanish La Ligand"),
                    Leagues(7,"4336","Greek Super league Greece","Soccer","Greek Super league Greece"),
                    Leagues(8,"4337","Dutch Eredivisie","Soccer","Divisive, Dutch Eredivisie"),
                    Leagues(9,"4338","Belgian First Division A","Soccer","Jupiter Pro League, Belgian First Division A"),
                    Leagues(10,"4339","Turkish Super Lig","Soccer","Super Lig, Turkish Super Lig"),
                    Leagues(11,"4340","Danish Superlative","Soccer","Danish Superlative"),
                    Leagues(12,"4344","Portuguese Prairie Ligand","Soccer","Ligand NOS, Portuguese Prairie Ligand"),
                    Leagues(13,"4346","American Major League Soccer","Soccer","MLS, Major League Soccer, American Major League Soccer"),
                    Leagues(14,"4347","Swedish Allusiveness","Soccer","Fotbollsallsvenskan, Swedish Allusiveness"),
                    Leagues(15,"4350","Mexican Primer League","Soccer","Ligand MX, Mexican Primer League"),
                    Leagues(16,"4351","Brazilian Series A","Soccer","Brazilian Series A"),
                    Leagues(17,"4354","Ukrainian Premier League","Soccer","Ukrainian Premier League"),
                    Leagues(18,"4355","Russian Football Premier League","Soccer","Чемпионат России по футболу, Russian Football Premier League"),
                    Leagues(19,"4356","Australian A-League","Soccer","A-League, Australian A-League"),
                    Leagues(20,"4358","Norwegian LineSeries","Soccer","LineSeries, Norwegian LineSeries"),
                    Leagues(21,"4359","Chinese Super League","Soccer","Chinese Super League")
                )
            }

            Column(modifier = Modifier
                .fillMaxSize().background(Color(193, 221, 131))
                .padding(bottom = 10.dp),
                verticalArrangement = Arrangement.Bottom){

                Box(modifier = Modifier.fillMaxWidth()){
                    Image(painter = painterResource(id = R.drawable.logo2), contentDescription = null)
                }

                Spacer(modifier = Modifier.size(20.dp))

                Row(modifier = Modifier
                    .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center){

                    Button(onClick = {
                        val intent = Intent(this@MainActivity, AddActivity::class.java)

                        scope.launch {
                            intent.putExtra("Desc",retrieveData(clubsDao))
                            startActivity(intent)
                            Toast.makeText(context,"Load Successfully !", Toast.LENGTH_SHORT).show()
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

//function to show all the details that has saved into the data base
suspend fun retrieveData(clubsDao: ClubsDao): String {
    var allLeagues = ""
    // read the data
    val leagues: List<Clubs> = clubsDao.getAll()

    for (i in leagues)

        allLeagues += " Name : ${i.strTeam} \n"+
                " Team Id : ${i.idTeam} \n" +
                " League Name : ${i.strLeague} \n" +
                " League ID : ${i.idLeague} \n" +
                " Year : ${i.intFormedYear} \n" +
                " Short Name : ${i.strTeamShort} \n" +
                " Alternate : ${i.strAlternate} \n" +
                " Keywords : ${i.strKeywords} \n" +
                " Stadium : ${i.strStadium} \n" +
                " Stadium Logo : ${i.strStadiumThumb} \n" +
                " Stadium Loc : ${i.strStadiumLocation} \n" +
                " Stadium Capacity : ${i.strStadiumLocation} \n" +
                " Web : ${i.strWebsite} \n" +
                " Jersey : ${i.strTeamJersey} \n" +
                " Team Logo : ${i.strTeamLogo} \n" +
                "\n\n\n"


    return allLeagues // returns a String
}





