package com.example.application

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.application.ui.theme.ApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen(){

    val orientation = LocalConfiguration.current.orientation // getting the orientation of the phone

    if (orientation == Configuration.ORIENTATION_PORTRAIT){

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 40.dp),
            verticalArrangement = Arrangement.Bottom){

            Box(modifier = Modifier.fillMaxWidth().offset(y = -199.dp)){
                Image(painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth())
            }

            Row(modifier = Modifier
                .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround){

                Button(onClick = { }) {
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
    }
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ApplicationTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            MainScreen()
        }
    }
}