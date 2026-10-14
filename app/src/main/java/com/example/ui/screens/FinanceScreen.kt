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
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CaptioBottomBar
import com.example.ui.theme.CaptioBgDark
import com.example.ui.theme.CaptioBgDeep
import com.example.ui.theme.CaptioBluePix
import com.example.ui.theme.CaptioCardBg
import com.example.ui.theme.CaptioCardOrange
import com.example.ui.theme.CaptioNeonGreen
import com.example.ui.theme.CaptioOtherPurple
import com.example.ui.theme.CaptioPurpleLight
import com.example.ui.theme.CaptioPurplePrimary
import com.example.ui.theme.CaptioTextGray
import com.example.ui.theme.CaptioTextWhite
import com.example.ui.viewmodel.CaptioScreen
import com.example.ui.viewmodel.CaptioViewModel
import java.util.Locale

@Composable
fun FinanceScreen(
    viewModel: CaptioViewModel,
    onNavigate: (CaptioScreen) -> Unit,
    onQuickAdd: () -> Unit,
    modifier: Modifier = Modifier,
    isEmbeddedInMockup: Boolean = false
) {
    val metrics by viewModel.financialMetrics.collectAsState()
    val period by viewModel.financePeriod.collectAsState()

    val periods = listOf("7 dias", "30 dias", "90 dias", "Personalizado")

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CaptioBgDeep)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = if (isEmbeddedInMockup) 10.dp else 18.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Top Bar: Menu, Trending icon + "Financeiro", Filter
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onNavigate(CaptioScreen.HOME) },
                    modifier = Modifier.testTag("finance_menu_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = "Financeiro",
                        tint = CaptioNeonGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Financeiro",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                IconButton(
                    onClick = { /* Filter */ },
                    modifier = Modifier.testTag("finance_filter_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filtrar",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Period Selector Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                periods.forEach { p ->
                    val isSelected = period == p
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isSelected) CaptioPurplePrimary else CaptioCardBg)
                            .clickable { viewModel.setFinancePeriod(p) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = p,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else CaptioTextGray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Total Vendido Card with mini bar chart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF24154E), Color(0xFF190E38))
                        )
                    )
                    .padding(18.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total Vendido",
                            fontSize = 12.sp,
                            color = CaptioTextGray
                        )

                        Text(
                            text = "↑ ${metrics.growthPercent}%",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = CaptioNeonGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "R$ ${String.format(Locale.GERMANY, "%,.2f", metrics.totalRevenue)}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Stylized Neon Green ascending bars visualizer (matching poster!)
                    NeonBarChart(modifier = Modifier.fillMaxWidth().height(48.dp))
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Two Metric Cards (Vendas / Receita Líquida)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Vendas
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(18.dp))
                        .background(CaptioCardBg)
                        .padding(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x228B5CF6)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = "Vendas",
                            tint = CaptioPurpleLight,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Vendas", fontSize = 11.sp, color = CaptioTextGray)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${metrics.salesCount}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "↑ ${metrics.salesGrowthPercent}%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CaptioNeonGreen
                    )
                }

                // Receita Líquida
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(18.dp))
                        .background(CaptioCardBg)
                        .padding(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x2200FF66)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Paid,
                            contentDescription = "Receita Líquida",
                            tint = CaptioNeonGreen,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Receita Líquida", fontSize = 11.sp, color = CaptioTextGray)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "R$ ${String.format(Locale.GERMANY, "%,.2f", metrics.netRevenue)}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "↑ ${metrics.netGrowthPercent}%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CaptioNeonGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Section "Por método de pagamento"
            Text(
                text = "Por método de pagamento",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(10.dp))

            // PIX Row
            PaymentMethodRow(
                icon = Icons.Default.Diamond,
                iconColor = CaptioBluePix,
                iconBg = Color(0x2200E5FF),
                name = "PIX",
                amountFormatted = "R$ ${String.format(Locale.GERMANY, "%,.2f", metrics.pixAmount)}",
                percentage = "${metrics.pixPercent}%"
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Cartão Row
            PaymentMethodRow(
                icon = Icons.Default.CreditCard,
                iconColor = CaptioCardOrange,
                iconBg = Color(0x22FF9800),
                name = "Cartão",
                amountFormatted = "R$ ${String.format(Locale.GERMANY, "%,.2f", metrics.cardAmount)}",
                percentage = "${metrics.cardPercent}%"
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Outros Row
            PaymentMethodRow(
                icon = Icons.Default.MoreHoriz,
                iconColor = CaptioOtherPurple,
                iconBg = Color(0x22A855F7),
                name = "Outros",
                amountFormatted = "R$ ${String.format(Locale.GERMANY, "%,.2f", metrics.othersAmount)}",
                percentage = "${metrics.othersPercent}%"
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Register Transaction Button
            Button(
                onClick = onQuickAdd,
                colors = ButtonDefaults.buttonColors(containerColor = CaptioPurplePrimary),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("finance_add_btn")
            ) {
                Text("+ Registrar Venda / Recebimento", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(if (isEmbeddedInMockup) 60.dp else 90.dp))
        }

        // Bottom Navigation Bar
        if (!isEmbeddedInMockup) {
            CaptioBottomBar(
                currentScreen = CaptioScreen.FINANCE,
                onNavigate = onNavigate,
                onQuickAdd = onQuickAdd,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun PaymentMethodRow(
    icon: ImageVector,
    iconColor: Color,
    iconBg: Color,
    name: String,
    amountFormatted: String,
    percentage: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CaptioCardBg)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = name,
                tint = iconColor,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = name,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = amountFormatted,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = percentage,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = CaptioTextGray
        )
    }
}

@Composable
fun NeonBarChart(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val barCount = 18
        val spacing = 4.dp.toPx()
        val totalSpacing = spacing * (barCount - 1)
        val barWidth = (size.width - totalSpacing) / barCount

        val normalizedHeights = floatArrayOf(
            0.15f, 0.20f, 0.25f, 0.22f, 0.30f, 0.35f, 0.40f, 0.38f,
            0.45f, 0.50f, 0.55f, 0.52f, 0.65f, 0.70f, 0.80f, 0.85f, 0.92f, 1.0f
        )

        for (i in 0 until barCount) {
            val left = i * (barWidth + spacing)
            val barH = size.height * normalizedHeights[i]
            val top = size.height - barH

            drawRoundRect(
                color = Color(0xFF00FF66),
                topLeft = Offset(left, top),
                size = Size(barWidth, barH),
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
            )
        }
    }
}
