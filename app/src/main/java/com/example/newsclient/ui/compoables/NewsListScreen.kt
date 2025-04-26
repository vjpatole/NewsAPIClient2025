package com.example.newsclient.ui.compoables

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.newsclient.ui.theme.NewsClientTheme

@Composable
fun NewsListScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val newsList = listOf(
        NewsItem("Title 1", "Description 1", "2023-10-01", "https://google.com", "https://picsum.photos/536/354"),
        NewsItem("Title 2", "Description 2", "2023-10-02", "https://gmail.com", "https://picsum.photos/536/354")
    )

    Column {
        // Search Bar
        BasicTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth(),
            decorationBox = { innerTextField ->
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray)
                        .padding(10.dp)
                ) {
                    if (searchQuery.text.isEmpty()) {
                        Text("Search news...", color = Color.Gray)
                    }
                    innerTextField()
                }
            }
        )

        // News List
        Column {
            newsList.filter {
                it.title.contains(searchQuery.text, ignoreCase = true)
            }.forEach { newsItem ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { navController.navigate("newsDetail/${newsItem.url}") }
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(newsItem.imageUrl),
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(newsItem.title, style = MaterialTheme.typography.bodyLarge)
                        Text(newsItem.description, style = MaterialTheme.typography.bodySmall)
                        Text(newsItem.date, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
fun NewsDetailScreen(url: String) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Save news to Room database */ }) {
                Icon(Icons.Default.Add, contentDescription = "Save")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            AndroidView(factory = { context ->
                WebView(context).apply {
                    webViewClient = WebViewClient()
                    loadUrl(url)
                }
            })
        }
    }
}

data class NewsItem(
    val title: String,
    val description: String,
    val date: String,
    val url: String,
    val imageUrl: String
)

@Preview(showBackground = true)
@Composable
fun NewsListPreview() {
    NewsClientTheme {
        NewsListScreen(rememberNavController())
    }
}