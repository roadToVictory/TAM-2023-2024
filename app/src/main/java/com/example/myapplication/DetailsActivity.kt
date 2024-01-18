package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myapplication.repository.model.RickMortyDetails
import com.example.myapplication.state.UiState
import com.example.myapplication.ui.theme.MyApplicationTheme

class DetailsActivity : ComponentActivity() {

    private val detailsViewModel: DetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id = intent.getStringExtra("CUSTOM_ID")

        if (id != null) {
            detailsViewModel.getData(id)
        }

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ShowCaseDetails(
                        viewModel = detailsViewModel,
                        onClick = { navigateToMainActivity() }
                    )
                }
            }
        }
    }

    private fun navigateToMainActivity() {
        val mainViewIntent = Intent(this, MainActivity::class.java)
        startActivity(mainViewIntent)
    }
}

@Composable
fun ShowCaseDetails(viewModel: DetailsViewModel, onClick: () -> Unit) {
    val uiState by viewModel.immutableRickMortyDetailsData.observeAsState(UiState())

    when {
        uiState.isLoading -> {
            LoadingView()
        }

        uiState.error != null -> {
            ErrorView()
        }

        uiState.data != null -> {
            uiState.data?.let {
                RickMortyDetailsView(
                    rickMortyDetails = it,
                    onClick = { onClick.invoke() }
                )
            }
        }
    }
}

@Composable
fun RickMortyDetailsView(rickMortyDetails: RickMortyDetails, onClick: () -> Unit) {
    DetailsView(
        rickMortyDetails = rickMortyDetails,
        onClick = { onClick.invoke() }
    )
}

@Composable
fun DetailsView(rickMortyDetails: RickMortyDetails, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .background(color = Color.DarkGray)
            .shadow(1.dp)
            .fillMaxWidth(),
        horizontalAlignment = CenterHorizontally
    ) {
        IconButton(
            modifier = Modifier
                .align(Start),
            onClick = { onClick.invoke() }
        ) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier
                    .size(48.dp)
                    .padding(5.dp)
            )
        }
        Row(
            verticalAlignment = CenterVertically
        ) {
            ExampleBox(
                shape = RoundedCornerShape(100.dp),
                status = rickMortyDetails.status
            )
            Text(
                text = rickMortyDetails.name,
                color = Color.White,
                fontSize = 30.sp,
                modifier = Modifier
                    .padding(start = 3.dp, bottom = 0.dp),
            )
        }

        Text(
            text = rickMortyDetails.species,
            fontSize = 19.sp,
            color = Color.White,
            modifier = Modifier
                .padding(bottom = 10.dp),
        )

        Row {
            AsyncImage(
                model = rickMortyDetails.image,
                contentDescription = "Obrazek",
                contentScale = ContentScale.FillBounds,
                alignment = Center,
                placeholder = painterResource(R.drawable.image),
                modifier = Modifier
                    .size((LocalConfiguration.current.screenWidthDp / 1.6).dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(border = BorderStroke(1.dp, Color.Gray))
            )
        }
        Row {
            Column(
                modifier = Modifier
                    .padding(start = 10.dp, top = 1.dp)
            ) {
                Text(
                    text = "gender - ${rickMortyDetails.gender}",
                    fontSize = 20.sp,
                    color = Color.White,
                    modifier = Modifier
                        .padding(top = 5.dp, bottom = 15.dp)
                        .align(CenterHorizontally),
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 50.dp)
                ) {
                    Text(
                        text = "Last known location:",
                        fontSize = 18.sp,
                        color = Color.LightGray,
                    )
                    Text(
                        text = rickMortyDetails.location.name,
                        fontSize = 22.sp,
                        color = Color.White,
                        modifier = Modifier
                            .padding(top = 2.dp, bottom = 14.dp)
                    )

                    Text(
                        text = "Origin:",
                        fontSize = 18.sp,
                        color = Color.LightGray,
                    )
                    Text(
                        text = rickMortyDetails.origin.name,
                        fontSize = 22.sp,
                        color = Color.White,
                        modifier = Modifier
                            .padding(top = 2.dp, bottom = 14.dp),
                    )

                    Text(
                        text = "First time seen in:",
                        fontSize = 18.sp,
                        color = Color.LightGray,
                    )
                    Text(
                        text = "${rickMortyDetails.first.name} (${rickMortyDetails.first.episode})",
                        fontSize = 22.sp,
                        color = Color.White,
                        modifier = Modifier
                            .padding(top = 2.dp),
                    )
                    Text(
                        text = rickMortyDetails.first.air_date,
                        fontSize = 17.sp,
                        color = Color.White,
                        modifier = Modifier
                            .padding(top = 2.dp, bottom = 5.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun ExampleBox(shape: Shape, status: String) {
    Box(
        modifier = Modifier
            .size(18.dp)
            .clip(shape)
            .fillMaxHeight()
            .background(
                when (status) {
                    "Alive" -> Color.Green
                    "Dead" -> Color.Red
                    else -> Color.Gray
                }
            )
    )
}
