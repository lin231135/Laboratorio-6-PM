package com.uvg.laboratorio6pm.network

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(pokemonName: String, navController: NavController) {
    val pokemonDetail = remember { mutableStateOf<PokemonDetail?>(null) }
    val speciesDetail = remember { mutableStateOf<PokemonSpecies?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pokemonName) {
        coroutineScope.launch {
            val detail = RetrofitClient.apiService.getPokemonDetail(pokemonName)
            pokemonDetail.value = detail

            val species = RetrofitClient.apiService.getPokemonSpecies(pokemonName)
            speciesDetail.value = species
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pokémon Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            pokemonDetail.value?.let { detail ->
                Text(
                    text = "Name: ${pokemonName.capitalize()}",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Mostrar las imágenes en 2 columnas y 2 filas
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Front", textAlign = TextAlign.Center)
                            Image(
                                painter = rememberImagePainter(detail.sprites.front_default),
                                contentDescription = "Front",
                                modifier = Modifier.size(100.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Back", textAlign = TextAlign.Center)
                            Image(
                                painter = rememberImagePainter(detail.sprites.back_default),
                                contentDescription = "Back",
                                modifier = Modifier.size(100.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Shiny Front", textAlign = TextAlign.Center)
                            Image(
                                painter = rememberImagePainter(detail.sprites.front_shiny),
                                contentDescription = "Shiny Front",
                                modifier = Modifier.size(100.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Shiny Back", textAlign = TextAlign.Center)
                            Image(
                                painter = rememberImagePainter(detail.sprites.back_shiny),
                                contentDescription = "Shiny Back",
                                modifier = Modifier.size(100.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Mostrar tipos de Pokémon con estilo
                detail.types?.forEach { type ->
                    val color = when (type.type.name) {
                        "grass" -> Color(0xFF00B050) // Verde
                        "fire" -> Color(0xFFE97132) // Naranja
                        "water" -> Color(0xFF0070C0) // Azul
                        "electric" -> Color(0xFFFFC000) // Amarillo
                        "poison" -> Color(0xFF92D050) // Verde claro
                        else -> Color.Gray
                    }
                    Box(
                        modifier = Modifier
                            .background(color, shape = RoundedCornerShape(8.dp))
                            .padding(8.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Type: ${type.type.name}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Mostrar barras de estadísticas
                detail.stats?.forEach { stat ->
                    Text(text = stat.stat.name)
                    LinearProgressIndicator(
                        progress = stat.base_stat / 100f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar debilidades
            speciesDetail.value?.types?.forEach { type ->
                Text(text = "Weakness: ${type.type.name}", style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}