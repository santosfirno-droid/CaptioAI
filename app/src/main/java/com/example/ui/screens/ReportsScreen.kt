package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CaptioBottomBar
import com.example.ui.theme.CaptioBgDeep
import com.example.ui.theme.CaptioCardBg
import com.example.ui.theme.CaptioCardBgElevated
import com.example.ui.theme.CaptioNeonGreen
import com.example.ui.theme.CaptioOrange
import com.example.ui.theme.CaptioPurpleLight
import com.example.ui.theme.CaptioPurplePrimary
import com.example.ui.theme.CaptioTextGray
import com.example.ui.theme.CaptioTextWhite
import com.example.ui.viewmodel.CaptioScreen
import com.example.ui.viewmodel.CaptioViewModel
import java.util.Locale

@Composable
fun ReportsScreen(
    viewModel: CaptioViewModel,
    onNavigate: (CaptioScreen) -> Unit,
    onQuickAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    val metrics by viewModel.financialMetrics.collectAsState()
    val activeTab by viewModel.reportsTab.collectAsState()
    val allClients by viewModel.clients.collectAsState()

    var showPeriodDropdown by remember { mutableStateOf(false) }
    var selectedPeriodLabel by remember { mutableStateOf("Últimos 30 dias") }

    val tabs = listOf("Vendas", "Clientes", "Financeiro")

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

            // Top Bar: Back, "Relatórios", Calendar icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onNavigate(CaptioScreen.HOME) },
                    modifier = Modifier.testTag("reports_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Relatórios",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                IconButton(
                    onClick = { showPeriodDropdown = true },
                    modifier = Modifier.testTag("reports_calendar_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Calendário",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Category Toggle Pills: "Vendas", "Clientes", "Financeiro"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tabs.forEach { tabName ->
                    val isSelected = activeTab == tabName
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) CaptioPurplePrimary else CaptioCardBg)
                            .clickable { viewModel.setReportsTab(tabName) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tabName,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else CaptioTextGray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Dropdown Period Selector
            Box {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(CaptioCardBg)
                        .clickable { showPeriodDropdown = true }
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedPeriodLabel,
                        fontSize = 12.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Selecionar Período",
                        tint = CaptioTextGray,
                        modifier = Modifier.size(16.dp)
                    )
                }

                DropdownMenu(
                    expanded = showPeriodDropdown,
                    onDismissRequest = { showPeriodDropdown = false },
                    modifier = Modifier.background(CaptioCardBgElevated)
                ) {
                    listOf("Últimos 7 dias", "Últimos 30 dias", "Últimos 90 dias", "Ano Vigente (2026)").forEach { p ->
                        DropdownMenuItem(
                            text = { Text(p, color = Color.White) },
                            onClick = {
                                selectedPeriodLabel = p
                                val mapToPeriod = when (p) {
                                    "Últimos 7 dias" -> "7 dias"
                                    "Últimos 90 dias" -> "90 dias"
                                    else -> "30 dias"
                                }
                                viewModel.setFinancePeriod(mapToPeriod)
                                showPeriodDropdown = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Main Dynamic Chart Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF22144B), Color(0xFF160B32))
                        )
                    )
                    .padding(18.dp)
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x228B5CF6)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (activeTab) {
                                    "Clientes" -> Icons.Default.People
                                    "Financeiro" -> Icons.Default.AccountBalance
                                    else -> Icons.Default.TrendingUp
                                },
                                contentDescription = "Trend",
                                tint = CaptioPurpleLight,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            val mainVal = when (activeTab) {
                                "Clientes" -> "${allClients.size} Clientes Registrados"
                                "Financeiro" -> "Lucro Líq. R$ ${String.format(Locale.GERMANY, "%,.2f", metrics.netRevenue)}"
                                else -> "R$ ${String.format(Locale.GERMANY, "%,.2f", metrics.totalRevenue)}"
                            }
                            Text(
                                text = mainVal,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "+${metrics.growthPercent}% em relação ao período anterior",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = CaptioNeonGreen
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    NeonLineChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3 Stat Cards Row based on active tab
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                when (activeTab) {
                    "Clientes" -> {
                        StatCard(
                            icon = Icons.Default.People,
                            iconTint = CaptioPurpleLight,
                            value = "${allClients.count { it.status == "Ativo" }}",
                            label = "Ativos",
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            icon = Icons.Default.CalendarMonth,
                            iconTint = CaptioPurpleLight,
                            value = "12",
                            label = "Novas Próteses",
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            icon = Icons.Default.TrendingUp,
                            iconTint = CaptioPurpleLight,
                            value = "94%",
                            label = "Retenção",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    "Financeiro" -> {
                        StatCard(
                            icon = Icons.Default.TrendingUp,
                            iconTint = CaptioPurpleLight,
                            value = "82%",
                            label = "Margem",
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            icon = Icons.Default.AccountBalance,
                            iconTint = CaptioPurpleLight,
                            value = "R$ 4.2k",
                            label = "Insumos",
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            icon = Icons.Default.BarChart,
                            iconTint = CaptioPurpleLight,
                            value = "R$ 21.7k",
                            label = "Líquido",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    else -> { // Vendas
                        StatCard(
                            icon = Icons.Default.ShoppingCart,
                            iconTint = CaptioPurpleLight,
                            value = "${metrics.salesCount}",
                            label = "Vendas",
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            icon = Icons.Default.People,
                            iconTint = CaptioPurpleLight,
                            value = "38",
                            label = "Clientes",
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            icon = Icons.Default.BarChart,
                            iconTint = CaptioPurpleLight,
                            value = "3.9x",
                            label = "Ticket médio",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Motivational Banner Card: Trophy
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF3B1578), Color(0xFF260D52), Color(0xFF1B083A))
                        )
                    )
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0x33F59E0B)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "Troféu",
                            tint = CaptioOrange,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "Seu negócio crescendo!",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "Continue focado, você está no caminho certo.",
                            fontSize = 11.sp,
                            color = CaptioTextGray,
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(95.dp))
        }

        // Bottom Navigation Bar
        CaptioBottomBar(
            currentScreen = CaptioScreen.REPORTS,
            onNavigate = onNavigate,
            onQuickAdd = onQuickAdd,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun StatCard(
    icon: ImageVector,
    iconTint: Color,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(CaptioCardBg)
            .padding(vertical = 12.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = iconTint,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = value,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            color = CaptioTextGray
        )
    }
}

@Composable
fun NeonLineChart(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val points = listOf(
            Offset(0f, size.height * 0.75f),
            Offset(size.width * 0.20f, size.height * 0.70f),
            Offset(size.width * 0.40f, size.height * 0.45f),
            Offset(size.width * 0.60f, size.height * 0.50f),
            Offset(size.width * 0.80f, size.height * 0.20f),
            Offset(size.width, size.height * 0.10f)
        )

        val path = Path().apply {
            moveTo(points.first().x, points.first().y)
            for (i in 1 until points.size) {
                val prev = points[i - 1]
                val curr = points[i]
                val cX = (prev.x + curr.x) / 2
                cubicTo(cX, prev.y, cX, curr.y, curr.x, curr.y)
            }
        }

        // Draw Line Stroke
        drawPath(
            path = path,
            color = Color(0xFF00FF66),
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )

        // Draw glowing circular points on line
        points.forEach { pt ->
            drawCircle(
                color = Color(0xFF00FF66),
                radius = 4.dp.toPx(),
                center = pt
            )
            drawCircle(
                color = Color.White,
                radius = 2.dp.toPx(),
                center = pt
            )
        }
    }
}
