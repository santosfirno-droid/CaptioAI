package com.example.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CaptioBottomBar
import com.example.ui.theme.CaptioBgDeep
import com.example.ui.theme.CaptioCardBg
import com.example.ui.theme.CaptioNeonGreen
import com.example.ui.theme.CaptioPurpleLight
import com.example.ui.theme.CaptioTextGray
import com.example.ui.theme.CaptioTextWhite
import com.example.ui.viewmodel.CaptioScreen

@Composable
fun MoreScreen(
    onNavigate: (CaptioScreen) -> Unit,
    onQuickAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
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

            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onNavigate(CaptioScreen.HOME) },
                    modifier = Modifier.testTag("more_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Mais Opções",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.size(48.dp)) // balance spacing
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Módulos de Gestão",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = CaptioTextGray
            )

            Spacer(modifier = Modifier.height(10.dp))

            MoreOptionItem(
                title = "Catálogo de Produtos",
                subtitle = "Próteses capilares, fitas, colas e vestuário",
                icon = Icons.Default.Inventory2,
                onClick = { onNavigate(CaptioScreen.PRODUCTS) },
                testTag = "more_opt_products"
            )

            Spacer(modifier = Modifier.height(10.dp))

            MoreOptionItem(
                title = "Controle Financeiro",
                subtitle = "Vendas, receitas, fluxo e formas de pagamento",
                icon = Icons.Default.AttachMoney,
                onClick = { onNavigate(CaptioScreen.FINANCE) },
                testTag = "more_opt_finance"
            )

            Spacer(modifier = Modifier.height(10.dp))

            MoreOptionItem(
                title = "Relatórios & Insights",
                subtitle = "Desempenho, métricas e faturamento mensal",
                icon = Icons.Default.BarChart,
                onClick = { onNavigate(CaptioScreen.REPORTS) },
                testTag = "more_opt_reports"
            )

            Spacer(modifier = Modifier.height(10.dp))

            MoreOptionItem(
                title = "Agendamentos Inteligentes",
                subtitle = "Agenda semanal, manutenções e horários",
                icon = Icons.Default.CalendarMonth,
                onClick = { onNavigate(CaptioScreen.AGENDA) },
                testTag = "more_opt_agenda"
            )

            Spacer(modifier = Modifier.height(10.dp))

            MoreOptionItem(
                title = "Gestão de Clientes",
                subtitle = "Histórico, preferências e cadastro de clientes",
                icon = Icons.Default.People,
                onClick = { onNavigate(CaptioScreen.CLIENTS) },
                testTag = "more_opt_clients"
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Exibição & Vitrine",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = CaptioTextGray
            )

            Spacer(modifier = Modifier.height(10.dp))

            MoreOptionItem(
                title = "Vitrine do Cartaz (Grade 3×2)",
                subtitle = "Visualizar os 6 celulares em grade da apresentação",
                icon = Icons.Default.GridView,
                onClick = { onNavigate(CaptioScreen.SHOWCASE_GRID) },
                testTag = "more_opt_showcase"
            )

            Spacer(modifier = Modifier.height(95.dp))
        }

        CaptioBottomBar(
            currentScreen = CaptioScreen.MORE,
            onNavigate = onNavigate,
            onQuickAdd = onQuickAdd,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun MoreOptionItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit,
    testTag: String = ""
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CaptioCardBg)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 14.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x1F00FF66)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = CaptioNeonGreen,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
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
                color = CaptioTextGray
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Acessar",
            tint = CaptioPurpleLight,
            modifier = Modifier.size(20.dp)
        )
    }
}
