package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import model.WeatherResults
import util.ImageDownloader

@Composable
@Preview
fun ContentUI(data: WeatherResults) {
    var imageState by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(data.currentWeather.iconUrl){
        imageState = ImageDownloader().downloadImage(data.currentWeather.iconUrl)
    }

    Text(
        text = "Current Weather",
        modifier = Modifier.padding(16.dp),
        style = MaterialTheme.typography.h6
    )

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 72.dp, vertical = 16.dp),
        backgroundColor = Color.Black
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = data.currentWeather.condition,
                style = MaterialTheme.typography.h6.copy(color = Color.White)
            )
            imageState?.let {bitmap ->
                Image(
                    bitmap = bitmap,
                    contentDescription = null,
                    modifier = Modifier.defaultMinSize(128.dp, 128.dp)
                        .padding(8.dp)
                )
            }
            Text(
                text = "Temperature in °C: ${data.currentWeather.temperature}",
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "Feels Like: ${data.currentWeather.feelsLike} °C",
                style = MaterialTheme.typography.caption
            )
        }
    }

    Divider(
        color = MaterialTheme.colors.primary,
        modifier = Modifier.padding(16.dp)
    )

    Text(
        text = "Forecast",
        modifier = Modifier.padding(16.dp),
        style = MaterialTheme.typography.h6
    )

    LazyRow {
        items(data.forecast) {weatherCard ->
            ForecastUi(weatherCard)
        }
    }


}