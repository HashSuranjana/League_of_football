package com.example.application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.application.ui.theme.ApplicationTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedInputStream
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
                    GUI()

                }
            }
        }
    }
    @Composable

    fun GUI() {
        var bookInfoDisplay by remember { mutableStateOf(" ") }
// the book title keyword to search for
        var keyword by remember { mutableStateOf("") }
// Creates a CoroutineScope bound to the GUI composable lifecycle
        val scope = rememberCoroutineScope()
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                Button(onClick = {
                    scope.launch {
                        bookInfoDisplay = fetchBooks(keyword)
                    }
                }) {
                    Text("Fetch Books")
                }
                TextField(value = keyword, onValueChange = { keyword = it })
            }
            Text(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                text = bookInfoDisplay
            )
        }
    }

    suspend fun fetchBooks(keyword: String): String {
//val url_string = "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=25"
        val url_string = "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=$keyword"
        val url = URL(url_string)
        val con: HttpURLConnection = url.openConnection() as HttpURLConnection
// collecting all the JSON string
        var stb = StringBuilder()
// run the code of the launched coroutine in a new thread
        withContext(Dispatchers.IO) {
            var bf = BufferedReader(InputStreamReader(con.inputStream))
            var line: String? = bf.readLine()
            while (line != null) { // keep reading until no more lines of text
                stb.append(line + "\n")
                line = bf.readLine()
            }
        }
        val allBooks = parseJSON(stb)
        return allBooks
    }


    fun parseJSON(stb: StringBuilder): String {
// this contains the full JSON returned by the Web Service
        val json = JSONObject(stb.toString())
// Information about all the books extracted by this function
        var allBooks = StringBuilder()
        var jsonArray: JSONArray = json.getJSONArray("teams")
// extract all the books from the JSON array
        for (i in 0..jsonArray.length() - 1) {
            val book: JSONObject = jsonArray[i] as JSONObject // this is a json object
// extract the title

            allBooks.append(book.getString("strTeam"))
        }
        return allBooks.toString()
    }
}




