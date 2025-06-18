package com.example.ar_kidslab.View.Profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ar_kidslab.R
import com.example.ar_kidslab.ui.theme.ARKidsLabTheme


val DaftarAchivement = listOf(
    Cardindikator("Progress",5,10,"minutes"),
    Cardindikator("Login",5,10,"Days"),
    Cardindikator("Daily",5,10,"Items"),
    Cardindikator("Login",5,10,"Days"),
    Cardindikator("Login",5,10,"Days"),
    )

@Composable
fun ProfileScreen(modifier: Modifier) {
    var currentProgress by remember { mutableStateOf(0.5f) }
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        Image(
            painter = painterResource(id = R.drawable.avatar_girl_svgrepo_com_1), // Ganti dengan resource gambar Anda
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.Gray)
        )

        // Name
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "Andriyanana",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Default
            )
        )

        // Description or Bio
        Text(
            text = "5 Tahun",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Default,
                color = Color.Black
            ),
            modifier = Modifier.padding(top = 8.dp),
            textAlign = TextAlign.Center
        )

        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondary),
            shape = RoundedCornerShape(12.dp),

            ) {

            Column(
                modifier = Modifier
                    .padding(12.dp)
            ) {
                // Title
                Text(
                    text = "Progress",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "12/24",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Progress Bar
                LinearProgressIndicator(
                    color = MaterialTheme.colorScheme.tertiary,
                    trackColor = Color.White,
                    progress = { currentProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CircleShape)
                        .sizeIn(minHeight = 12.dp),
                )
            }
        }
        Text(text = "Achivemnent",
            fontWeight = FontWeight.Bold
        )
        LazyColumn {items(DaftarAchivement){ it ->
            AchimentCard(it)
        }
        }

    }
}

@Composable
fun AchimentCard(cardindikator : Cardindikator){
    var currentProgress by remember { mutableStateOf(cardindikator.inprocess.toFloat()/cardindikator.process.toFloat()) }
    Card(
        modifier = Modifier
            .sizeIn()
            .padding(8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondary),
        shape = RoundedCornerShape(12.dp),

        ) {

        Column(
            modifier = Modifier
                .padding(12.dp)
        ) {
            // Title
            Text(
                text = cardindikator.Judul ,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = cardindikator.inprocess.toString() + "/" + cardindikator.process.toString() + " ${cardindikator.satuan}",
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Progress Bar
            LinearProgressIndicator(
                color = MaterialTheme.colorScheme.tertiary,
                trackColor = Color.White,
                progress = { currentProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape)
                    .sizeIn(minHeight = 12.dp),
            )
        }
    }

}

@Preview
@Composable
fun ProfilePreview() {
    ARKidsLabTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            ProfileScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}