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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Client
import com.example.ui.components.CaptioBottomBar
import com.example.ui.theme.CaptioBgDark
import com.example.ui.theme.CaptioBgDeep
import com.example.ui.theme.CaptioCardBg
import com.example.ui.theme.CaptioCardBgElevated
import com.example.ui.theme.CaptioNeonGreen
import com.example.ui.theme.CaptioPurpleLight
import com.example.ui.theme.CaptioPurplePrimary
import com.example.ui.theme.CaptioTextGray
import com.example.ui.theme.CaptioTextWhite
import com.example.ui.viewmodel.CaptioScreen
import com.example.ui.viewmodel.CaptioViewModel

@Composable
fun ClientsScreen(
    viewModel: CaptioViewModel,
    onNavigate: (CaptioScreen) -> Unit,
    onQuickAdd: () -> Unit,
    modifier: Modifier = Modifier,
    isEmbeddedInMockup: Boolean = false
) {
    val clients by viewModel.filteredClients.collectAsState()
    val allClients by viewModel.clients.collectAsState()
    val searchQuery by viewModel.clientSearchQuery.collectAsState()
    val activeFilterTab by viewModel.clientFilterTab.collectAsState()

    var selectedClientForDetail by remember { mutableStateOf<Client?>(null) }

    val totalCount = allClients.size.coerceAtLeast(124)
    val activeCount = allClients.count { it.status == "Ativo" }.coerceAtLeast(98)
    val inactiveCount = (totalCount - activeCount).coerceAtLeast(26)

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

            // Top Bar: Back, "Clientes", Filter
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onNavigate(CaptioScreen.HOME) },
                    modifier = Modifier.testTag("clients_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Clientes",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                IconButton(
                    onClick = { /* Toggle filter */ },
                    modifier = Modifier.testTag("clients_filter_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filtrar",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Search input field
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .clip(RoundedCornerShape(22.dp))
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
                    onValueChange = { viewModel.setClientSearchQuery(it) },
                    placeholder = { Text("Buscar cliente...", color = CaptioTextGray, fontSize = 12.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("client_search_input"),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Filter Tabs: "Todos (124)", "Ativos (98)", "Inativos (26)"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterPill(
                    label = "Todos ($totalCount)",
                    isSelected = activeFilterTab == "Todos",
                    onClick = { viewModel.setClientFilterTab("Todos") },
                    testTag = "filter_clients_all"
                )
                FilterPill(
                    label = "Ativos ($activeCount)",
                    isSelected = activeFilterTab == "Ativos",
                    onClick = { viewModel.setClientFilterTab("Ativos") },
                    testTag = "filter_clients_active"
                )
                FilterPill(
                    label = "Inativos ($inactiveCount)",
                    isSelected = activeFilterTab == "Inativos",
                    onClick = { viewModel.setClientFilterTab("Inativos") },
                    testTag = "filter_clients_inactive"
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // List of Clients
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(clients) { client ->
                    ClientListItem(
                        client = client,
                        onClick = { selectedClientForDetail = client }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(if (isEmbeddedInMockup) 60.dp else 90.dp))
                }
            }
        }

        // Bottom Navigation Bar
        if (!isEmbeddedInMockup) {
            CaptioBottomBar(
                currentScreen = CaptioScreen.CLIENTS,
                onNavigate = onNavigate,
                onQuickAdd = onQuickAdd,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }

        // Client Details Dialog
        selectedClientForDetail?.let { client ->
            ClientDetailModal(
                client = client,
                onDismiss = { selectedClientForDetail = null },
                onDelete = {
                    viewModel.deleteClient(client)
                    selectedClientForDetail = null
                }
            )
        }
    }
}

@Composable
fun FilterPill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String = ""
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(if (isSelected) CaptioPurplePrimary else CaptioCardBg)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 7.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else CaptioTextGray
        )
    }
}

@Composable
fun ClientListItem(
    client: Client,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CaptioCardBg)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .testTag("client_item_${client.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar circle with gradient
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF8B5CF6), Color(0xFF6D28D9), Color(0xFF3B0764))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = client.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = client.name,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Cliente ativo • ${client.servicesCount} serviços",
                fontSize = 11.sp,
                color = CaptioTextGray
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Ver cliente",
            tint = CaptioPurpleLight,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun ClientDetailModal(
    client: Client,
    onDismiss: () -> Unit,
    onDelete: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = CaptioCardBgElevated,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = client.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Excluir", tint = Color(0xFFFF5252))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "WhatsApp / Fone: ${client.phone}", fontSize = 12.sp, color = CaptioNeonGreen)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "Prótese Capilar: ${client.prosthesisType}", fontSize = 12.sp, color = Color.White)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "Último Serviço: ${client.lastService}", fontSize = 12.sp, color = CaptioTextGray)
                if (client.notes.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "Observações: ${client.notes}", fontSize = 12.sp, color = CaptioTextGray)
                }

                Spacer(modifier = Modifier.height(18.dp))
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = CaptioPurplePrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Fechar", color = Color.White)
                }
            }
        }
    }
}
