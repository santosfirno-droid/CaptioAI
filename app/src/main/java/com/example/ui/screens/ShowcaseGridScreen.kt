package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CaptioPosterHeader
import com.example.ui.components.PhoneMockupFrame
import com.example.ui.theme.CaptioBgDark
import com.example.ui.theme.CaptioBgDeep
import com.example.ui.theme.CaptioCardBg
import com.example.ui.theme.CaptioNeonGreen
import com.example.ui.theme.CaptioPurpleLight
import com.example.ui.theme.CaptioPurplePrimary
import com.example.ui.theme.CaptioTextGray
import com.example.ui.viewmodel.CaptioScreen
import com.example.ui.viewmodel.CaptioViewModel

@Composable
fun ShowcaseGridScreen(
    viewModel: CaptioViewModel,
    onNavigate: (CaptioScreen) -> Unit,
    onQuickAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF0D0624),
                        Color(0xFF130833),
                        Color(0xFF1B0B47),
                        Color(0xFF10072B),
                        Color(0xFF0B051D)
                    )
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            // Top Header: Poster Title & Tagline
            item {
                CaptioPosterHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 4.dp)
                )
            }

            // Quick interactive hint & Fullscreen Switch Button
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x337C3AED))
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Vitrine 3×2 das Telas",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Toque em qualquer celular para abrir a tela completa",
                            fontSize = 11.sp,
                            color = CaptioTextGray
                        )
                    }

                    Button(
                        onClick = { onNavigate(CaptioScreen.HOME) },
                        colors = ButtonDefaults.buttonColors(containerColor = CaptioNeonGreen),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("open_fullscreen_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fullscreen,
                            contentDescription = "Tela Cheia",
                            tint = Color.Black,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Usar App",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }

            // Phone 1: Painel completo
            item {
                PhoneMockupFrame(
                    title = "Painel completo",
                    subtitle = "Tudo o que você precisa em um só lugar.",
                    featureIcon = Icons.Default.FlashOn,
                    onClick = { onNavigate(CaptioScreen.HOME) },
                    phoneHeight = 490.dp
                ) {
                    HomeScreen(
                        onNavigate = onNavigate,
                        onQuickAdd = onQuickAdd,
                        isEmbeddedInMockup = true
                    )
                }
            }

            // Phone 2: Gestão de clientes
            item {
                PhoneMockupFrame(
                    title = "Gestão de clientes",
                    subtitle = "Organize seus clientes, histórico e preferências.",
                    featureIcon = Icons.Default.People,
                    onClick = { onNavigate(CaptioScreen.CLIENTS) },
                    phoneHeight = 490.dp
                ) {
                    ClientsScreen(
                        viewModel = viewModel,
                        onNavigate = onNavigate,
                        onQuickAdd = onQuickAdd,
                        isEmbeddedInMockup = true
                    )
                }
            }

            // Phone 3: Agendamentos inteligentes
            item {
                PhoneMockupFrame(
                    title = "Agendamentos inteligentes",
                    subtitle = "Organize sua agenda, evite conflitos e reduza faltas.",
                    featureIcon = Icons.Default.CalendarMonth,
                    onClick = { onNavigate(CaptioScreen.AGENDA) },
                    phoneHeight = 490.dp
                ) {
                    AgendaScreen(
                        viewModel = viewModel,
                        onNavigate = onNavigate,
                        onQuickAdd = onQuickAdd,
                        isEmbeddedInMockup = true
                    )
                }
            }

            // Phone 4: Controle financeiro
            item {
                PhoneMockupFrame(
                    title = "Controle financeiro",
                    subtitle = "Acompanhe suas vendas, receitas e formas de pagamento.",
                    featureIcon = Icons.Default.AttachMoney,
                    onClick = { onNavigate(CaptioScreen.FINANCE) },
                    phoneHeight = 490.dp
                ) {
                    FinanceScreen(
                        viewModel = viewModel,
                        onNavigate = onNavigate,
                        onQuickAdd = onQuickAdd,
                        isEmbeddedInMockup = true
                    )
                }
            }

            // Phone 5: Catálogo de produtos
            item {
                PhoneMockupFrame(
                    title = "Catálogo de produtos",
                    subtitle = "Gerencie seus produtos e divulgue com facilidade.",
                    featureIcon = Icons.Default.Inventory2,
                    onClick = { onNavigate(CaptioScreen.PRODUCTS) },
                    phoneHeight = 490.dp
                ) {
                    ProductsScreen(
                        viewModel = viewModel,
                        onNavigate = onNavigate,
                        onQuickAdd = onQuickAdd,
                        isEmbeddedInMockup = true
                    )
                }
            }

            // Phone 6: Relatórios e insights
            item {
                PhoneMockupFrame(
                    title = "Relatórios e insights",
                    subtitle = "Acompanhe o desempenho e tome decisões mais inteligentes.",
                    featureIcon = Icons.Default.TrendingUp,
                    onClick = { onNavigate(CaptioScreen.REPORTS) },
                    phoneHeight = 490.dp
                ) {
                    ReportsScreen(
                        viewModel = viewModel,
                        onNavigate = onNavigate,
                        onQuickAdd = onQuickAdd,
                        isEmbeddedInMockup = true
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}
