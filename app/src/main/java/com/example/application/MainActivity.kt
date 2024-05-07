package com.example.application

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.application.ui.theme.ApplicationTheme
import kotlinx.coroutines.launch

lateinit var database :FootBallDataBase
lateinit var clubsDao :ClubsDao

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        database = Room.databaseBuilder(
            this,FootBallDataBase::class.java,
            "FootBallDataBase"
        ).build()

        clubsDao = database.clubsDao()

        setContent {
            ApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainScreen() // calling the composable function
                }
            }
        }
    }
}

@Composable
fun MainScreen(){

    val orientation = LocalConfiguration.current.orientation // getting the orientation of the phone

    if (orientation == Configuration.ORIENTATION_PORTRAIT){

        var clubsData by remember { mutableStateOf("") }
        LaunchedEffect(clubsData) {
            clubsDao.insertAll(
                Clubs(1,"Manchester","0001"),
                Clubs(2,"Liverpool","0002"),
                Clubs(3,"kaichen","0003"),
                Clubs(4,"Crossby","0004")
            )

        }

        val scope = rememberCoroutineScope()

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 40.dp),
            verticalArrangement = Arrangement.Bottom){

//                Box(modifier = Modifier.fillMaxWidth().offset(y = (-199).dp)){
//                    Image(painter = painterResource(id = R.drawable.logo),
//                        contentDescription = null,
//                        modifier = Modifier.fillMaxWidth())
//                }

            Row(modifier = Modifier
                .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround){

                Button(onClick = {
                    scope.launch {
                        clubsData = retrieveData(clubsDao)
                    }

                }) {
                    Text(text = "Add Leagues to DB")

                }

                Button(onClick = { }) {
                    Text(text = "Search for clubs by League")

                }


            }

            Row(modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center){

                Button(onClick = { }) {
                    Text(text = "Search for Clubs")

                }
            }

        }

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = clubsData
        )
    }

    else {
        Row(modifier = Modifier
            .fillMaxSize()
            .padding(end = 20.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically){

//                Box(modifier = Modifier.offset(x = (-280).dp)){
//                    Image(painter = painterResource(id = R.drawable.logo),
//                        contentDescription = null,
//                        modifier = Modifier.fillMaxWidth())
//                }

            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround) {
                Row(horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically){

                    Button(onClick = {}) {
                        Text(text = "Add Leagues to DB")

                    }

                    Button(onClick = { }) {
                        Text(text = "Search for clubs by League")

                    }

                }

                Column(horizontalAlignment = Alignment.Start,
                    modifier = Modifier.padding(20.dp)) {
                    Button(onClick = { }) {
                        Text(text = "Search for Clubs")

                    }
                }
            }
        }


    }
}
suspend fun retrieveData(clubsDao: ClubsDao): String {
    var allClubs = ""
    // read the data
    val clubs: List<Clubs> = clubsDao.getAll()

    for (i in clubs)
        allClubs += "${i.clubName} ${i.clubID} \n"

    return allClubs
}





