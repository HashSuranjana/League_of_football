package com.example.application

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.application.ui.theme.ApplicationTheme

class AddActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val info = intent.getStringExtra("Desc")

                    val orientation = LocalConfiguration.current.orientation // getting the orientation of the phone

                    if(orientation == Configuration.ORIENTATION_PORTRAIT){

                        LazyColumn {

                            items(1) { index ->

                                if (info != null) {
                                    Text(
                                        modifier = Modifier.fillMaxWidth().padding(start=20.dp, top = 30.dp),
                                        style = TextStyle(textAlign = TextAlign.Start, fontSize = 20.sp, fontWeight = FontWeight.Bold),
                                        text = info
                                    )
                                }
                            }
                        }

                    }else {

                        LazyColumn {

                            items(1) { index ->

                                if (info != null) {
                                    Text(
                                        modifier = Modifier.fillMaxWidth().padding(start=150.dp, top = 30.dp),
                                        style = TextStyle(textAlign = TextAlign.Start, fontSize = 30.sp, fontWeight = FontWeight.Bold),
                                        text = info
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
