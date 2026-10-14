package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.CaptioNeonGreen
import com.example.ui.theme.CaptioTextGray

@Composable
fun CaptioPosterHeader(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_captio_logo),
                contentDescription = "CAPTIO Logo",
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "CAPTIO",
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.SansSerif,
                letterSpacing = 1.sp,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "GESTÃO INTELIGENTE PARA PROFISSIONAIS\nDE PRÓTESE CAPILAR",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFD1D1E9),
            letterSpacing = 0.8.sp,
            textAlign = TextAlign.Center,
            lineHeight = 15.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Sua clínica mais organizada, seu negócio mais lucrativo.",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = CaptioNeonGreen,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CaptioTopBarLogo(
    iconSize: Dp = 26.dp,
    fontSize: Int = 18
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_captio_logo),
            contentDescription = "CAPTIO Logo",
            modifier = Modifier
                .size(iconSize)
                .clip(RoundedCornerShape(6.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "CAPTIO",
            fontSize = fontSize.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp,
            color = Color.White
        )
    }
}
