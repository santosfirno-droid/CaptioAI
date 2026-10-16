package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.ui.components.CaptioBottomBar
import com.example.ui.components.CaptioTopBarLogo
import com.example.ui.theme.CaptioBgDeep
import com.example.ui.theme.CaptioCardBg
import com.example.ui.theme.CaptioCardBgElevated
import com.example.ui.theme.CaptioNeonGreen
import com.example.ui.theme.CaptioOrange
import com.example.ui.theme.CaptioPurpleLight
import com.example.ui.theme.CaptioPurplePrimary
import com.example.ui.theme.CaptioTextGray
import com.example.ui.viewmodel.CaptioNotification
import com.example.ui.viewmodel.CaptioScreen
import com.example.ui.viewmodel.CaptioViewModel

@Composable
fun HomeScreen(
    viewModel: CaptioViewModel,
    onNavigate: (CaptioScreen) -> Unit,
    onQuickAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var showNotificationDialog by remember { mutableStateOf(false) }

    val notifications by viewModel.notifications.collectAsState()
    val pendingRequestsCount by viewModel.pendingRequestsCount.collectAsState()
    val upcomingAppointments by viewModel.upcomingAppointments.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CaptioBgDeep)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Top Header: Hamburger Menu, CAPTIO Logo, Notifications Bell
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onNavigate(CaptioScreen.MORE) },
                    modifier = Modifier.testTag("home_menu_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                }

                CaptioTopBarLogo(iconSize = 26.dp, fontSize = 20)

                // Notification Bell with dynamic badge count
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(CaptioCardBg)
                        .clickable { showNotificationDialog = true }
                        .testTag("home_notif_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notificações",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    if (notifications.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 4.dp, end = 4.dp)
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFF334B)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${notifications.size}",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Search Bar (Interactive)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .clip(RoundedCornerShape(23.dp))
                    .background(CaptioCardBg)
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = CaptioTextGray,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        viewModel.setClientSearchQuery(it)
                    },
                    placeholder = {
                        Text(
                            text = "Buscar por cliente, serviço...",
                            fontSize = 12.sp,
                            color = CaptioTextGray
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("home_search_input"),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // DESTAQUE: Novas Solicitações Recebidas (Requisito 1)
            if (pendingRequestsCount > 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF2E195E), Color(0xFF1E1044))
                            )
                        )
                        .border(1.dp, CaptioNeonGreen.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                        .clickable { onNavigate(CaptioScreen.ATENDIMENTOS) }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color(0x2200FF66)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$pendingRequestsCount",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = CaptioNeonGreen
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Novas Solicitações de Clientes",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "$pendingRequestsCount pessoas aguardando confirmação",
                            fontSize = 11.sp,
                            color = CaptioTextGray
                        )
                    }

                    Button(
                        onClick = { onNavigate(CaptioScreen.ATENDIMENTOS) },
                        colors = ButtonDefaults.buttonColors(containerColor = CaptioNeonGreen),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text("Ver", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // 6 Quick Actions Grid (Com "Atendimentos" substituindo "Agendamentos" genérico)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionItem(
                    title = "Clientes",
                    icon = Icons.Default.Person,
                    onClick = { onNavigate(CaptioScreen.CLIENTS) },
                    modifier = Modifier.weight(1f),
                    testTag = "quick_clients"
                )
                QuickActionItem(
                    title = "Atendimentos",
                    icon = Icons.Default.EventNote,
                    onClick = { onNavigate(CaptioScreen.ATENDIMENTOS) },
                    modifier = Modifier.weight(1f),
                    testTag = "quick_atendimentos"
                )
                QuickActionItem(
                    title = "Vendas",
                    icon = Icons.Default.TrendingUp,
                    onClick = { onNavigate(CaptioScreen.FINANCE) },
                    modifier = Modifier.weight(1f),
                    testTag = "quick_vendas"
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionItem(
                    title = "Produtos",
                    icon = Icons.Default.Inventory2,
                    onClick = { onNavigate(CaptioScreen.PRODUCTS) },
                    modifier = Modifier.weight(1f),
                    testTag = "quick_produtos"
                )
                QuickActionItem(
                    title = "Financeiro",
                    icon = Icons.Default.AttachMoney,
                    onClick = { onNavigate(CaptioScreen.FINANCE) },
                    modifier = Modifier.weight(1f),
                    testTag = "quick_financeiro"
                )
                QuickActionItem(
                    title = "Relatórios",
                    icon = Icons.Default.BarChart,
                    onClick = { onNavigate(CaptioScreen.REPORTS) },
                    modifier = Modifier.weight(1f),
                    testTag = "quick_relatorios"
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // DESTAQUE: Próximos Atendimentos (Requisito 1)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Próximos Atendimentos",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "Ver todos",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CaptioNeonGreen,
                    modifier = Modifier
                        .clickable { onNavigate(CaptioScreen.ATENDIMENTOS) }
                        .padding(4.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (upcomingAppointments.isEmpty()) {
                Text(
                    text = "Nenhum atendimento confirmado próximo.",
                    fontSize = 12.sp,
                    color = CaptioTextGray
                )
            } else {
                upcomingAppointments.forEach { appt ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(CaptioCardBg)
                            .clickable { onNavigate(CaptioScreen.ATENDIMENTOS) }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(46.dp)
                        ) {
                            Text(
                                text = appt.time,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = CaptioNeonGreen
                            )
                            Text(
                                text = "${appt.dayOfWeek} ${appt.dayNumber}",
                                fontSize = 9.sp,
                                color = CaptioTextGray
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = appt.clientName,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "${appt.service} • ${appt.durationMinutes} min",
                                fontSize = 11.sp,
                                color = CaptioTextGray
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x2200FF66))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Confirmado",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = CaptioNeonGreen
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // "Resumo da semana" Card with 3D Robot mascot
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF22134E), Color(0xFF1B0E3E), Color(0xFF12082C))
                        )
                    )
                    .clickable { onNavigate(CaptioScreen.REPORTS) }
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.robot_mascot),
                        contentDescription = "Mascote CAPTIO",
                        modifier = Modifier
                            .size(76.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Resumo da semana",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Veja seu desempenho, novas vendas e atendimentos.",
                            fontSize = 11.sp,
                            color = CaptioTextGray,
                            lineHeight = 15.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(CaptioPurplePrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Acessar Relatórios",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(95.dp))
        }

        // Bottom Navigation Bar (Início, Clientes, (+), Relatórios, Mais)
        CaptioBottomBar(
            currentScreen = CaptioScreen.HOME,
            onNavigate = onNavigate,
            onQuickAdd = onQuickAdd,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        // Notification Modal Dialog
        if (showNotificationDialog) {
            NotificationDialog(
                notifications = notifications,
                onDismiss = { showNotificationDialog = false },
                onDismissItem = { viewModel.dismissNotification(it) }
            )
        }
    }
}

@Composable
private fun QuickActionItem(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = ""
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(CaptioCardBg)
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp, horizontal = 4.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0x1A00FF66)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = CaptioNeonGreen,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}

@Composable
private fun NotificationDialog(
    notifications: List<CaptioNotification>,
    onDismiss: () -> Unit,
    onDismissItem: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = CaptioCardBgElevated,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Notificações da Clínica",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Fechar", tint = CaptioTextGray)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (notifications.isEmpty()) {
                    Text(
                        text = "Nenhuma nova notificação no momento.",
                        fontSize = 12.sp,
                        color = CaptioTextGray,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                } else {
                    notifications.forEach { notif ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(CaptioCardBg)
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = notif.title,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CaptioNeonGreen
                                    )
                                    Text(
                                        text = notif.time,
                                        fontSize = 10.sp,
                                        color = CaptioTextGray
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = notif.message,
                                    fontSize = 11.sp,
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(
                                onClick = { onDismissItem(notif.id) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Limpar",
                                    tint = CaptioTextGray,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = CaptioPurplePrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("OK", color = Color.White)
                }
            }
        }
    }
}
