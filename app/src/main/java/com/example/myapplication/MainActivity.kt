package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
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
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
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
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ShowCase(viewModel = viewModel)
//                    MainView("Teacher Rick", "Human", "Male", "Alive",)
                }
            }
        }
    }
}


@Composable
fun ShowCase(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val uiState by viewModel.immutableRickMortyData.observeAsState(UiState())

    when {
        uiState.isLoading -> {
            LoadingView()
        }
        uiState.error != null -> {
            ErrorView()
        }
        uiState.data != null -> {
            uiState.data?.let { RickMortyView(rickMorty = it) }
        }
    }
}

@Composable
fun RickMortyView(rickMorty: List<RickMorty>) {
    LazyColumn {
        items(rickMorty) {item ->
            Log.d("Main", "${item.name}, ${item.image}")
            MainView(
                name = item.name,
                species = item.species,
                gender = item.gender,
                status = item.status,
                image = item.image
            )
        }
    }
}

@Composable
fun LoadingView() {
    CircularProgressIndicator(
        modifier = Modifier.width(70.dp),
        color = MaterialTheme.colorScheme.primary,
    )
}

@Composable
fun ErrorView() {
    Text(text = "Wystapil blad")
}


@Composable
fun MainView(name: String, species: String, gender: String, status: String, image: String) {
    Column(
        modifier = Modifier
            .padding(all = 2.dp)
            .shadow(1.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = name,
            color = Color.DarkGray,
            fontSize = 28.sp,
            modifier = Modifier.align(CenterHorizontally),
        )

        Row {
            Column (
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(start = 120.dp, top = 1.dp),
            ) {
                Text(
                    text = "species",
                    fontSize = 10.sp,
                )
                Text(
                    text = species,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(bottom = 3.dp),
                )

                Text(
                    text = "gender",
                    fontSize = 10.sp,
                )
                Text(
                    text = gender,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(bottom = 3.dp),
                )

                Text(
                    text = "status",
                    fontSize = 10.sp,
                )
                Text(
                    text = status,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(bottom = 3.dp),
                )

            }
            AsyncImage(
                model = image,
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
//        ShowCase(MainViewModel(), Modifier)
//        MainView("Teacher Rick", "Human", "Male", "Alive")
    }
}

