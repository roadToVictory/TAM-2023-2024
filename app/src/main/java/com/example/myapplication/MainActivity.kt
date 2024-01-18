package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myapplication.repository.model.RickMorty
import com.example.myapplication.state.UiState
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getData()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ShowCase(
                        viewModel = viewModel,
                        onClick = { id -> navigateToDetailsActivity(id) }
                    )
                }
            }
        }
    }

    private fun navigateToDetailsActivity(rickMorty: RickMorty) {
        val detailsIntent = Intent(this, DetailsActivity::class.java)
        detailsIntent.putExtra("CUSTOM_ID", rickMorty.id)
        startActivity(detailsIntent)
    }
}


@Composable
fun ShowCase(viewModel: MainViewModel, onClick: (RickMorty) -> Unit) {
    val uiState by viewModel.immutableRickMortyData.observeAsState(UiState())

    when {
        uiState.isLoading -> {
            LoadingView()
        }

        uiState.error != null -> {
            ErrorView()
        }

        uiState.data != null -> {
            uiState.data?.let {
                RickMortyView(
                    rickMorty = it,
                    onClick = { id -> onClick.invoke(id) }
                )
            }
        }
    }
}

@Composable
fun RickMortyView(rickMorty: List<RickMorty>, onClick: (RickMorty) -> Unit) {
    LazyColumn {
        items(rickMorty) { item ->
            Log.d("Main", "${item.name}, ${item.image}")
            MainView(
                rickMorty = item,
                onClick = { id -> onClick.invoke(id) }
            )
        }
    }
}

@Composable
fun LoadingView() {
    CircularProgressIndicator(
        modifier = Modifier.width(50.dp),
        color = MaterialTheme.colorScheme.primary,
    )
}

@Composable
fun ErrorView() {
    Toast.makeText(
        LocalContext.current,
        "Wystąpił błąd podczas ładowania danych",
        Toast.LENGTH_LONG
    ).show()
}

@Composable
fun MainView(rickMorty: RickMorty, onClick: (RickMorty) -> Unit) {
    Column(
        modifier = Modifier
            .clickable { onClick.invoke(rickMorty) }
            .padding(all = 2.dp)
            .shadow(1.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = rickMorty.name,
            color = Color.DarkGray,
            fontSize = 28.sp,
            modifier = Modifier.align(CenterHorizontally),
        )

        Row {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(start = 120.dp, top = 1.dp),
            ) {
                Text(
                    text = "species",
                    fontSize = 10.sp,
                )
                Text(
                    text = rickMorty.species,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(bottom = 3.dp),
                )

                Text(
                    text = "gender",
                    fontSize = 10.sp,
                )
                Text(
                    text = rickMorty.gender,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(bottom = 3.dp),
                )

                Text(
                    text = "status",
                    fontSize = 10.sp,
                )
                Text(
                    text = rickMorty.status,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(bottom = 3.dp),
                )

            }
            AsyncImage(
                model = rickMorty.image,
                contentDescription = "Obrazek",
                placeholder = painterResource(R.drawable.image),
                modifier = Modifier
                    .padding(start = 10.dp)
                    .scale(0.85f)
                    .border(border = BorderStroke(1.dp, Color.Gray))
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
    }
}

