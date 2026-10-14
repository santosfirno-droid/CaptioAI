package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Appointment
import com.example.ui.components.CaptioBottomBar
import com.example.ui.theme.CaptioBgDark
import com.example.ui.theme.CaptioBgDeep
import com.example.ui.theme.CaptioCardBg
import com.example.ui.theme.CaptioNeonGreen
import com.example.ui.theme.CaptioOrange
import com.example.ui.theme.CaptioPurpleLight
import com.example.ui.theme.CaptioPurplePrimary
import com.example.ui.theme.CaptioTextGray
import com.example.ui.theme.CaptioTextWhite
import com.example.ui.viewmodel.CaptioScreen
import com.example.ui.viewmodel.CaptioViewModel

@Composable
fun AgendaScreen(
    viewModel: CaptioViewModel,
    onNavigate: (CaptioScreen) -> Unit,
    onQuickAdd: () -> Unit,
    modifier: Modifier = Modifier,
    isEmbeddedInMockup: Boolean = false
) {
    val appointments by viewModel.filteredAppointments.collectAsState()
    val selectedDay by viewModel.selectedDay.collectAsState()

    val daysOfWeek = listOf(
        Pair("Seg", "28"),
        Pair("Ter", "29"),
        Pair("Qua", "30"),
        Pair("Qui", "31"),
        Pair("Sex", "01")
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CaptioBgDeep)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = if (isEmbeddedInMockup) 10.dp else 18.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Top bar: Menu, "Agenda", Filter
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onNavigate(CaptioScreen.HOME) },
                    modifier = Modifier.testTag("agenda_menu_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Agenda",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                IconButton(
                    onClick = { /* Filter */ },
                    modifier = Modifier.testTag("agenda_filter_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filtrar",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Horizontal date selector strip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                daysOfWeek.forEach { (weekDay, dayNum) ->
                    val isSelected = selectedDay == dayNum
                    DateSelectorCard(
                        dayOfWeek = weekDay,
                        dayNumber = dayNum,
                        isSelected = isSelected,
                        onClick = { viewModel.setSelectedDay(dayNum) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Appointments Timeline List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(appointments) { appt ->
                    AppointmentTimeRow(
                        appointment = appt,
                        onToggleStatus = {
                            val newStatus = if (appt.status == "Confirmado") "Pendente" else "Confirmado"
                            viewModel.updateAppointmentStatus(appt, newStatus)
                        }
                    )
                }

                // Add Appointment dashed slot button (matching poster "+ Adicionar Agendamento")
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "12:00",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = CaptioTextGray,
                            modifier = Modifier.width(46.dp)
                        )

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .border(
                                    width = 1.dp,
                                    color = CaptioPurpleLight.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .background(Color(0x157C3AED))
                                .clickable(onClick = onQuickAdd)
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Adicionar",
                                    tint = CaptioPurpleLight,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Adicionar Agendamento",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = CaptioPurpleLight
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(if (isEmbeddedInMockup) 60.dp else 90.dp))
                }
            }
        }

        // Bottom Navigation Bar
        if (!isEmbeddedInMockup) {
            CaptioBottomBar(
                currentScreen = CaptioScreen.AGENDA,
                onNavigate = onNavigate,
                onQuickAdd = onQuickAdd,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun DateSelectorCard(
    dayOfWeek: String,
    dayNumber: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(52.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) CaptioPurplePrimary else CaptioCardBg)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = dayOfWeek,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = if (isSelected) Color.White else CaptioTextGray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = dayNumber,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun AppointmentTimeRow(
    appointment: Appointment,
    onToggleStatus: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Time label on the left (e.g. "08:00", "09:00", "10:00")
        Text(
            text = appointment.time,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = CaptioTextGray,
            modifier = Modifier.width(46.dp)
        )

        // Appointment Card
        Row(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(CaptioCardBg)
                .clickable(onClick = onToggleStatus)
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Client avatar thumbnail
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF6366F1), Color(0xFF4F46E5))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = appointment.clientName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = appointment.clientName,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = appointment.service,
                    fontSize = 11.sp,
                    color = CaptioTextGray
                )
            }

            // Status Badge (Confirmado -> Green, Pendente -> Orange)
            val isConfirmed = appointment.status == "Confirmado"
            val badgeBg = if (isConfirmed) Color(0x1F00FF66) else Color(0x2AF59E0B)
            val badgeColor = if (isConfirmed) CaptioNeonGreen else CaptioOrange

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(badgeBg)
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = appointment.status,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = badgeColor
                )
            }
        }
    }
}
