
package com.example.application

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
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
                    ClubsSearch()  //calling the composable function
                }
            }
        }
    }



    @SuppressLint("MutableCollectionMutableState")
    @Composable
    fun ClubsSearch(){

        val context = LocalContext.current

        var searchTerm by rememberSaveable { mutableStateOf("") } //initializing the searchTerm

        var buttonClicked by rememberSaveable { mutableStateOf(false) }  //initializing a button click checking variable

        val scope = rememberCoroutineScope() // Creates a CoroutineScope bound to the GUI composable lifecycle

        var clubsFound by rememberSaveable { mutableStateOf<List<Clubs>>(emptyList()) }

        val orientation = LocalConfiguration.current.orientation // getting the orientation of the phone

        //check the phone's orientation
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

                    TextField(value = searchTerm, onValueChange = { searchTerm = it },
                        modifier = Modifier.width(400.dp))
                }

                Spacer(modifier = Modifier.size(20.dp))


                Row {

                    Button(onClick = {
                        //check whether user enter something or not
                        if(searchTerm != "") {

                            scope.launch {

                                buttonClicked = true
                                clubsFound = clubsFinding(searchTerm, clubsDao)

                            }
                        }else {
                            Toast.makeText(context,"Please Enter Something!", Toast.LENGTH_SHORT).show()
                        }
                    }, modifier = Modifier
                        .padding(top = 10.dp)
                        .width(150.dp)) {
                        Text("Search")
                    }

                }

                Spacer(modifier = Modifier.size(20.dp))

                //display only the when the button is clicked
                if(buttonClicked){

                    Column(
                        modifier =Modifier.verticalScroll(rememberScrollState())
                    ) {

                        //runs through the List
                        for (i in clubsFound) {

                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start) {

                                //displaying the team logo
                                Image(bitmap = i.strTeamLogo?.let { loadImageFromUrl(it)!!.asImageBitmap() }!!, contentDescription =null,modifier = Modifier.size(100.dp))

                                Spacer(modifier = Modifier.size(100.dp))

                                //displaying the team name
                                i.strTeam?.let { Text(text = it, style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold)) }

                            }
                        }
                    }
                }
            }
        }else { //if phone's in Landscape mode

            Row(modifier = Modifier.fillMaxSize()) {

                Column(
                    modifier = Modifier
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.Start
                ) {

                    Text(
                        text = "Find Your Club .......",
                        style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.ExtraBold),
                        modifier = Modifier.padding(top = 30.dp, start = 20.dp)
                    )

                    Spacer(modifier = Modifier.size(60.dp))

                    Column(modifier = Modifier.padding(start = 20.dp)) {

                        TextField(value = searchTerm, onValueChange = { searchTerm = it })
                    }

                    Spacer(modifier = Modifier.size(30.dp))

                    Row(
                        modifier = Modifier
                            .padding(start = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Button(
                            onClick = {
                                scope.launch {
                                    if (searchTerm != "") {
                                        clubsFound = clubsFinding(searchTerm, clubsDao)
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Please Enter Something!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                }
                            }, modifier = Modifier
                                .padding(top = 10.dp)
                                .width(150.dp)
                        ) {
                            Text("Search")
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(545.dp)
                        .padding(start = 20.dp)
                        .verticalScroll(rememberScrollState())
                ) {

                    for (i in clubsFound) {

                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start) {

                            Image(bitmap = i.strTeamLogo?.let { loadImageFromUrl(it)!!.asImageBitmap() }!!, contentDescription =null,modifier = Modifier.size(100.dp))

                            Spacer(modifier = Modifier.size(50.dp))

                            i.strTeam?.let { Text(text = it, style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold)) }

                        }
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
    private suspend fun clubsFinding(searchTerm:String, clubsDao: ClubsDao): List<Clubs> {

        val clubsFound = mutableListOf<Clubs>() //initializing a List of clubs

        withContext(Dispatchers.IO) {

            val leagues: List<Clubs> = clubsDao.filterClubs(searchTerm)  //get the team objects that contains the searchTerm

            //run through the league items
            for (i in leagues) {
                //prevent adding duplicates
                if(!clubsFound.contains(i)){

                    clubsFound.addAll(leagues) //adding club details
                }
            }
        }


        return clubsFound  //returning the list
    }

}
