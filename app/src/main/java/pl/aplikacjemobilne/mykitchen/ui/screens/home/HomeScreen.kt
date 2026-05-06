package pl.aplikacjemobilne.mykitchen.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import pl.aplikacjemobilne.mykitchen.R
import pl.aplikacjemobilne.mykitchen.ui.theme.BackgroundLight
import pl.aplikacjemobilne.mykitchen.ui.theme.Cream
import pl.aplikacjemobilne.mykitchen.ui.theme.OrangeLight
import pl.aplikacjemobilne.mykitchen.ui.theme.Primary

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(brush = Brush.verticalGradient(colors = listOf(Primary, OrangeLight)))
                .windowInsetsPadding(WindowInsets.statusBars)
                .height(220.dp)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text("Dzień dobry, Tadeusz!", color = Cream, fontSize = 6.em)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.category_desery),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .height(32.dp)
                .width(48.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Dom 🐫🐢")
    }
}
