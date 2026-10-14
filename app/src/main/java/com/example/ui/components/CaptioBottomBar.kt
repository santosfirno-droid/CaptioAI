package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.People
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CaptioBgDark
import com.example.ui.theme.CaptioCardBg
import com.example.ui.theme.CaptioNeonGreen
import com.example.ui.theme.CaptioPurpleLight
import com.example.ui.theme.CaptioPurplePrimary
import com.example.ui.theme.CaptioTextGray
import com.example.ui.theme.CaptioTextWhite
import com.example.ui.viewmodel.CaptioScreen

@Composable
fun CaptioBottomBar(
    currentScreen: CaptioScreen,
    onNavigate: (CaptioScreen) -> Unit,
    onQuickAdd: () -> Unit,
    modifier: Modifier = Modifier,
    barHeight: Dp = 68.dp,
    iconSize: Dp = 22.dp,
    fontSize: Int = 11
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(barHeight),
        color = CaptioCardBg,
        tonalElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Início
            BottomNavItem(
                icon = Icons.Filled.Home,
                label = "Início",
                isSelected = currentScreen == CaptioScreen.HOME,
                onClick = { onNavigate(CaptioScreen.HOME) },
                iconSize = iconSize,
                fontSize = fontSize,
                testTag = "nav_home"
            )

            // Clientes
            BottomNavItem(
                icon = Icons.Filled.People,
                label = "Clientes",
                isSelected = currentScreen == CaptioScreen.CLIENTS,
                onClick = { onNavigate(CaptioScreen.CLIENTS) },
                iconSize = iconSize,
                fontSize = fontSize,
                testTag = "nav_clients"
            )

            // Center + Floating Green Button
            Box(
                modifier = Modifier
                    .offset(y = (-8).dp)
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(Color(0xFF33FF85), CaptioNeonGreen, Color(0xFF00CC52))
                        )
                    )
                    .shadow(elevation = 12.dp, shape = CircleShape)
                    .clickable(onClick = onQuickAdd)
                    .testTag("nav_add_fab"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adicionar Novo",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            // Agenda
            BottomNavItem(
                icon = Icons.Filled.CalendarMonth,
                label = "Agenda",
                isSelected = currentScreen == CaptioScreen.AGENDA,
                onClick = { onNavigate(CaptioScreen.AGENDA) },
                iconSize = iconSize,
                fontSize = fontSize,
                testTag = "nav_agenda"
            )

            // Mais
            BottomNavItem(
                icon = Icons.Filled.Menu,
                label = "Mais",
                isSelected = currentScreen == CaptioScreen.MORE,
                onClick = { onNavigate(CaptioScreen.MORE) },
                iconSize = iconSize,
                fontSize = fontSize,
                testTag = "nav_more"
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    iconSize: Dp,
    fontSize: Int,
    testTag: String
) {
    val interactionSource = remember { MutableInteractionSource() }
    val color = if (isSelected) CaptioPurpleLight else CaptioTextGray

    Column(
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 6.dp, vertical = 4.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(iconSize)
        )
        Text(
            text = label,
            fontSize = fontSize.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = color
        )
    }
}
