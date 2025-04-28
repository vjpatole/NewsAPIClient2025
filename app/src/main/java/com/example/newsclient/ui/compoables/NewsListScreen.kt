package com.example.newsclient.ui.compoables

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.newsclient.data.model.Article
import com.example.newsclient.data.util.Resource
import com.example.newsclient.ui.theme.NewsClientTheme
import com.example.newsclient.ui.theme.Purple80
import com.example.newsclient.ui.viewmodel.NewsViewModel

@Composable
fun NewsListScreen(navController: NavController, newsViewModel: NewsViewModel = viewModel()) {

    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    val newsHeadLinesList by newsViewModel.newsHeadlines.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        newsViewModel.getNewsHeadlines("us", 1)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Purple80)
            .padding(16.dp)
    ) {
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

        Spacer(modifier = Modifier.height(16.dp))

        // News List
        Column {

            if (newsHeadLinesList is Resource.Success){
                val articles = (newsHeadLinesList as Resource.Success).data?.articles ?: emptyList()

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(
                        vertical = 8.dp
                    )
                ) {
                    items(articles.size) { index ->
                        val newsItem = articles[index]
                        NewsItemCard(newsItem, navController)
                        if (index < articles.size - 1) {
                            Divider(color = Color.Gray, thickness = 1.dp)
                        }
                    }
                }
            } else if (newsHeadLinesList is Resource.Error) {
                Text("Error loading news", color = Color.Red)
            } else if (newsHeadLinesList is Resource.Loading) {
                Text("Loading...", color = Color.Gray)
            }

        }
    }
}

@Composable
fun NewsItemCard(newsItem: Article, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { navController.navigate("newsDetail/${newsItem.url}") },
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(newsItem.urlToImage),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(newsItem.title, style = MaterialTheme.typography.bodyLarge)
                Text(newsItem.description, style = MaterialTheme.typography.bodySmall)
                Text(newsItem.author, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
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

@Preview(showBackground = true)
@Composable
fun NewsListPreview() {
    NewsClientTheme {
        NewsListScreen(rememberNavController())
    }
}