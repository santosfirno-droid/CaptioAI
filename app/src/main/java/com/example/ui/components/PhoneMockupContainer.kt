package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CaptioBgDark
import com.example.ui.theme.CaptioBgDeep
import com.example.ui.theme.CaptioCardBg
import com.example.ui.theme.CaptioNeonGreen
import com.example.ui.theme.CaptioPurpleLight
import com.example.ui.theme.CaptioTextGray
import com.example.ui.theme.CaptioTextWhite

@Composable
fun PhoneMockupFrame(
    title: String,
    subtitle: String,
    featureIcon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    phoneHeight: Dp = 500.dp,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Phone Device Outer Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(phoneHeight)
                .shadow(elevation = 20.dp, shape = RoundedCornerShape(32.dp), spotColor = CaptioPurpleLight)
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF281C4F), Color(0xFF130A2A), Color(0xFF0D0620))
                    ),
                    shape = RoundedCornerShape(32.dp)
                )
                .border(
                    width = 2.dp,
                    brush = Brush.verticalGradient(
                        listOf(Color(0xFF6366F1), Color(0xFF4338CA), Color(0xFF1E1B4B))
                    ),
                    shape = RoundedCornerShape(32.dp)
                )
                .padding(4.dp)
        ) {
            // Phone Screen inner area
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(28.dp))
                    .background(CaptioBgDeep)
            ) {
                // Actual Screen Content
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .padding(top = 28.dp, bottom = 12.dp)
                ) {
                    content()
                }

                // Phone Status Bar Overlay
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "9:41",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    // Dynamic Island Pill
                    Box(
                        modifier = Modifier
                            .width(68.dp)
                            .height(14.dp)
                            .clip(CircleShape)
                            .background(Color.Black)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SignalCellularAlt,
                            contentDescription = "Signal",
                            tint = Color.White,
                            modifier = Modifier.size(11.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.Wifi,
                            contentDescription = "Wifi",
                            tint = Color.White,
                            modifier = Modifier.size(11.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.BatteryFull,
                            contentDescription = "Battery",
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }

                // Bottom Home Bar Indicator
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 4.dp)
                        .width(72.dp)
                        .height(3.dp)
                        .clip(CircleShape)
                        .background(Color(0x88FFFFFF))
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Feature Description below the phone (matching poster)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0x1F00FF66)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = featureIcon,
                    contentDescription = title,
                    tint = CaptioNeonGreen,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = CaptioTextWhite
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = CaptioTextGray,
                    lineHeight = 15.sp
                )
            }
        }
    }
}
