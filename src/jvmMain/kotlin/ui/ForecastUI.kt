package ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import model.WeatherCard
import util.ImageDownloader

@Composable
fun ForecastUi(weatherCard: WeatherCard) {
    var imageState by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(weatherCard.iconUrl){
        imageState = ImageDownloader().downloadImage(weatherCard.iconUrl)
    }

    Card(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = weatherCard.condition,
                style = MaterialTheme.typography.h6
            )
            imageState?.let {bitmap ->
                Image(
                    bitmap = bitmap,
                    contentDescription = null,
                    modifier = Modifier.defaultMinSize(128.dp, 128.dp)
                        .padding(8.dp)
                )
            }

            val chanceOfRain = String.format(
                "Chance of rain: %.2f%%", weatherCard.chanceOfRain
            )

            Text(
                text = chanceOfRain,
                style = MaterialTheme.typography.caption
            )
        }
    }
}