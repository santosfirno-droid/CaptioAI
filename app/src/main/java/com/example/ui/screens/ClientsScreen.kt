package com.example.ui.screens

import android.content.Context
import android.widget.Toast
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Appointment
import com.example.data.model.Client
import com.example.ui.components.CaptioBottomBar
import com.example.ui.components.CaptioInputField
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

@Composable
fun ClientsScreen(
    viewModel: CaptioViewModel,
    onNavigate: (CaptioScreen) -> Unit,
    onQuickAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val clients by viewModel.filteredClients.collectAsState()
    val allClients by viewModel.clients.collectAsState()
    val allAppointments by viewModel.appointments.collectAsState()
    val searchQuery by viewModel.clientSearchQuery.collectAsState()
    val activeFilterTab by viewModel.clientFilterTab.collectAsState()
    val currentSortOrder by viewModel.clientSortOrder.collectAsState()

    var selectedClientForDetail by remember { mutableStateOf<Client?>(null) }
    var clientToEdit by remember { mutableStateOf<Client?>(null) }
    var clientForNewAppointment by remember { mutableStateOf<Client?>(null) }
    var appointmentToReschedule by remember { mutableStateOf<Appointment?>(null) }
    var whatsappConfirmationPair by remember { mutableStateOf<Pair<String, String>?>(null) }
    var showSortMenu by remember { mutableStateOf(false) }

    val totalCount = allClients.size
    val activeCount = allClients.count { it.status == "Ativo" }
    val inactiveCount = allClients.count { it.status == "Inativo" }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CaptioBgDeep)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp)
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

                Box {
                    IconButton(
                        onClick = { showSortMenu = true },
                        modifier = Modifier.testTag("clients_filter_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filtrar / Ordenar",
                            tint = Color.White
                        )
                    }

                    DropdownMenu(
                        expanded = showSortMenu,
                        onDismissRequest = { showSortMenu = false },
                        modifier = Modifier.background(CaptioCardBgElevated)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Ordenar por Nome (A-Z)", color = Color.White) },
                            onClick = {
                                viewModel.setClientSortOrder("Nome")
                                showSortMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Ordenar por Mais Serviços", color = Color.White) },
                            onClick = {
                                viewModel.setClientSortOrder("Serviços")
                                showSortMenu = false
                            }
                        )
                    }
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

            // Filter Tabs: "Todos", "Ativos", "Inativos"
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
                    Spacer(modifier = Modifier.height(95.dp))
                }
            }
        }

        // Floating Action Button to Add Client directly from this screen
        FloatingActionButton(
            onClick = onQuickAdd,
            containerColor = CaptioNeonGreen,
            contentColor = Color.Black,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 85.dp, end = 20.dp)
                .testTag("add_client_fab")
        ) {
            Icon(Icons.Default.Add, contentDescription = "Adicionar Cliente")
        }

        // Bottom Navigation Bar
        CaptioBottomBar(
            currentScreen = CaptioScreen.CLIENTS,
            onNavigate = onNavigate,
            onQuickAdd = onQuickAdd,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        // Client Details Dialog
        selectedClientForDetail?.let { client ->
            ClientDetailModal(
                client = client,
                appointments = allAppointments.filter { 
                    it.clientName.equals(client.name, ignoreCase = true) || it.clientPhone == client.phone 
                },
                onDismiss = { selectedClientForDetail = null },
                onEdit = {
                    clientToEdit = client
                    selectedClientForDetail = null
                },
                onDelete = {
                    viewModel.deleteClient(client)
                    selectedClientForDetail = null
                },
                onNewAppointment = {
                    clientForNewAppointment = client
                    selectedClientForDetail = null
                },
                onCompleteAppointment = { appt ->
                    viewModel.completeAppointment(appt)
                    Toast.makeText(context, "Atendimento marcado como Concluído!", Toast.LENGTH_SHORT).show()
                },
                onRescheduleAppointment = { appt ->
                    appointmentToReschedule = appt
                    selectedClientForDetail = null
                },
                onCancelAppointment = { appt ->
                    viewModel.cancelAppointment(appt)
                    Toast.makeText(context, "Atendimento Cancelado.", Toast.LENGTH_SHORT).show()
                },
                onContactClient = {
                    openWhatsApp(context, client.phone, "Olá ${client.name}! Como estão os cuidados com sua prótese capilar?")
                }
            )
        }

        // New Appointment for Client Dialog (Requirement 5)
        clientForNewAppointment?.let { client ->
            CreateAppointmentForClientDialog(
                client = client,
                onDismiss = { clientForNewAppointment = null },
                onCheckConflict = { dayNum, time, duration ->
                    viewModel.checkAppointmentConflict(dayNum, time, duration)
                },
                onConfirm = { service, dayNum, dayOfWeek, time, duration ->
                    viewModel.addAppointment(
                        clientName = client.name,
                        clientPhone = client.phone,
                        service = service,
                        time = time,
                        durationMinutes = duration,
                        dayNumber = dayNum,
                        dayOfWeek = dayOfWeek,
                        status = "Confirmado"
                    )
                    clientForNewAppointment = null
                    val msg = "Olá, ${client.name}! Seu atendimento de $service está confirmado para $dayOfWeek (dia $dayNum), às $time. Qualquer dúvida ou necessidade de alteração, entre em contato conosco."
                    whatsappConfirmationPair = Pair(client.phone, msg)
                }
            )
        }

        // Reschedule Dialog
        appointmentToReschedule?.let { appt ->
            RescheduleDialog(
                appointment = appt,
                onDismiss = { appointmentToReschedule = null },
                onCheckConflict = { dayNum, time, duration ->
                    viewModel.checkAppointmentConflict(dayNum, time, duration, appt.id)
                },
                onConfirm = { newDayNum, newDayOfWeek, newTime, newDuration ->
                    viewModel.rescheduleAppointment(appt, newDayNum, newDayOfWeek, newTime, newDuration)
                    val msg = "Olá, ${appt.clientName}! Seu atendimento de ${appt.service} foi reagendado para $newDayOfWeek (dia $newDayNum), às $newTime. Qualquer dúvida, entre em contato."
                    appointmentToReschedule = null
                    whatsappConfirmationPair = Pair(appt.clientPhone, msg)
                }
            )
        }

        // WhatsApp Confirmation Message Dialog (Requirement 4)
        whatsappConfirmationPair?.let { (phone, msg) ->
            WhatsAppConfirmationDialog(
                phone = phone,
                messageText = msg,
                onDismiss = { whatsappConfirmationPair = null },
                onSendWhatsApp = {
                    openWhatsApp(context, phone, msg)
                    whatsappConfirmationPair = null
                },
                onCopy = {
                    clipboardManager.setText(AnnotatedString(msg))
                    Toast.makeText(context, "Mensagem copiada para a área de transferência!", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // Client Edit Dialog
        clientToEdit?.let { client ->
            EditClientModal(
                client = client,
                onDismiss = { clientToEdit = null },
                onSave = { updated ->
                    viewModel.updateClient(updated)
                    clientToEdit = null
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
                text = "Cliente ${client.status.lowercase()} • ${client.servicesCount} serviços",
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
    appointments: List<Appointment>,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onNewAppointment: () -> Unit,
    onCompleteAppointment: (Appointment) -> Unit,
    onRescheduleAppointment: (Appointment) -> Unit,
    onCancelAppointment: (Appointment) -> Unit,
    onContactClient: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = CaptioCardBgElevated,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
            ) {
                // Header: Name, Status, Edit, Delete
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = client.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (client.status == "Ativo") Color(0x2200FF66) else Color(0x229E9E9E))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = client.status,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (client.status == "Ativo") CaptioNeonGreen else CaptioTextGray
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${client.servicesCount} serviços realizados",
                                fontSize = 11.sp,
                                color = CaptioTextGray
                            )
                        }
                    }

                    Row {
                        IconButton(onClick = onEdit) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar", tint = CaptioPurpleLight)
                        }
                        IconButton(onClick = onDelete) {
                            Icon(Icons.Default.Delete, contentDescription = "Excluir", tint = Color(0xFFFF5252))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // WhatsApp Contact Card
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(CaptioCardBg)
                        .clickable(onClick = onContactClient)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = null, tint = CaptioNeonGreen, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = client.phone, fontSize = 12.sp, color = CaptioNeonGreen, fontWeight = FontWeight.Bold)
                    }
                    Text(text = "Abrir conversa", fontSize = 10.sp, color = CaptioPurpleLight)
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Prótese: ${client.prosthesisType}", fontSize = 12.sp, color = Color.White)
                Text(text = "Último serviço: ${client.lastService}", fontSize = 11.sp, color = CaptioTextGray)
                if (client.notes.isNotBlank()) {
                    Text(text = "Obs: ${client.notes}", fontSize = 11.sp, color = CaptioTextGray)
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Section: Atendimentos e Histórico (Requisito 5)
                Text(
                    text = "Histórico de Atendimentos",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(6.dp))

                if (appointments.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(CaptioCardBg)
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Nenhum atendimento registrado para este cliente.",
                            fontSize = 11.sp,
                            color = CaptioTextGray
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(appointments) { appt ->
                            val statusColor = when (appt.status) {
                                "Confirmado" -> CaptioNeonGreen
                                "Concluído" -> Color(0xFF64B5F6)
                                else -> Color(0xFFFF5252)
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(CaptioCardBg)
                                    .padding(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = appt.service,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = appt.status,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = statusColor
                                    )
                                }

                                Text(
                                    text = "${appt.dayOfWeek} dia ${appt.dayNumber} às ${appt.time} (${appt.durationMinutes} min)",
                                    fontSize = 11.sp,
                                    color = CaptioTextGray
                                )

                                if (appt.status == "Confirmado") {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Button(
                                            onClick = { onCompleteAppointment(appt) },
                                            colors = ButtonDefaults.buttonColors(containerColor = CaptioNeonGreen),
                                            shape = RoundedCornerShape(6.dp),
                                            modifier = Modifier.height(26.dp)
                                        ) {
                                            Text("Concluir", color = Color.Black, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        }

                                        Button(
                                            onClick = { onRescheduleAppointment(appt) },
                                            colors = ButtonDefaults.buttonColors(containerColor = CaptioPurplePrimary),
                                            shape = RoundedCornerShape(6.dp),
                                            modifier = Modifier.height(26.dp)
                                        ) {
                                            Text("Remarcar", color = Color.White, fontSize = 9.sp)
                                        }

                                        Button(
                                            onClick = { onCancelAppointment(appt) },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0x33FF5252)),
                                            shape = RoundedCornerShape(6.dp),
                                            modifier = Modifier.height(26.dp)
                                        ) {
                                            Text("Cancelar", color = Color(0xFFFF5252), fontSize = 9.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Actions: Novo Atendimento and Fechar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onNewAppointment,
                        colors = ButtonDefaults.buttonColors(containerColor = CaptioNeonGreen),
                        modifier = Modifier.weight(1.3f)
                    ) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Novo Atendimento", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }

                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = CaptioPurplePrimary),
                        modifier = Modifier.weight(0.7f)
                    ) {
                        Text("Fechar", color = Color.White, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun CreateAppointmentForClientDialog(
    client: Client,
    onDismiss: () -> Unit,
    onCheckConflict: (dayNum: String, time: String, duration: Int) -> Appointment?,
    onConfirm: (service: String, dayNum: String, dayOfWeek: String, time: String, duration: Int) -> Unit
) {
    var selectedService by remember { mutableStateOf("Manutenção de Prótese") }
    var selectedDayNum by remember { mutableStateOf("29") }
    var selectedDayOfWeek by remember { mutableStateOf("Ter") }
    var selectedTime by remember { mutableStateOf("14:00") }
    var durationMinutes by remember { mutableStateOf(60) }

    val services = listOf(
        "Instalação de Prótese Capilar",
        "Manutenção de Prótese",
        "Consulta • Ajuste",
        "Avaliação / Molde"
    )

    val days = listOf(
        Pair("Seg", "28"),
        Pair("Ter", "29"),
        Pair("Qua", "30"),
        Pair("Qui", "31"),
        Pair("Sex", "01"),
        Pair("Sáb", "02")
    )

    val times = listOf("08:00", "09:00", "10:00", "11:00", "13:30", "14:00", "15:00", "16:30", "17:00")
    val durations = listOf(45, 60, 90, 120)

    val conflict = onCheckConflict(selectedDayNum, selectedTime, durationMinutes)

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = CaptioCardBgElevated,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Novo Atendimento",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Cliente: ${client.name}",
                    fontSize = 12.sp,
                    color = CaptioNeonGreen
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 1. Serviço
                Text(text = "1. Serviço:", fontSize = 11.sp, color = CaptioTextGray)
                Spacer(modifier = Modifier.height(4.dp))
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    services.forEach { srv ->
                        val isSel = selectedService == srv
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) CaptioPurplePrimary else CaptioCardBg)
                                .clickable {
                                    selectedService = srv
                                    if (srv.contains("Instalação")) durationMinutes = 90
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = srv,
                                fontSize = 11.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 2. Dia
                Text(text = "2. Dia:", fontSize = 11.sp, color = CaptioTextGray)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    days.forEach { (wDay, dNum) ->
                        val isSel = selectedDayNum == dNum
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) CaptioPurplePrimary else CaptioCardBg)
                                .clickable {
                                    selectedDayNum = dNum
                                    selectedDayOfWeek = wDay
                                }
                                .padding(vertical = 5.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(wDay, fontSize = 9.sp, color = Color.White)
                                Text(dNum, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 3. Horário
                Text(text = "3. Horário:", fontSize = 11.sp, color = CaptioTextGray)
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    times.take(5).forEach { tm ->
                        val isSel = selectedTime == tm
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) CaptioNeonGreen else CaptioCardBg)
                                .clickable { selectedTime = tm }
                                .padding(vertical = 5.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(tm, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.Black else Color.White)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    times.drop(5).forEach { tm ->
                        val isSel = selectedTime == tm
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) CaptioNeonGreen else CaptioCardBg)
                                .clickable { selectedTime = tm }
                                .padding(vertical = 5.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(tm, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.Black else Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 4. Duração
                Text(text = "4. Duração:", fontSize = 11.sp, color = CaptioTextGray)
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    durations.forEach { dur ->
                        val isSel = durationMinutes == dur
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) CaptioPurplePrimary else CaptioCardBg)
                                .clickable { durationMinutes = dur }
                                .padding(vertical = 5.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("${dur}m", fontSize = 11.sp, color = Color.White)
                        }
                    }
                }

                // Conflict warning banner
                if (conflict != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0x33FF5252))
                            .border(1.dp, Color(0xFFFF5252), RoundedCornerShape(10.dp))
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFFF5252), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Conflito! Ocupado por ${conflict.clientName} às ${conflict.time} (${conflict.durationMinutes}m).",
                            fontSize = 10.sp,
                            color = Color(0xFFFF5252)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)) {
                        Text("Cancelar", color = CaptioTextGray)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (conflict == null) {
                                onConfirm(selectedService, selectedDayNum, selectedDayOfWeek, selectedTime, durationMinutes)
                            }
                        },
                        enabled = conflict == null,
                        colors = ButtonDefaults.buttonColors(containerColor = CaptioNeonGreen)
                    ) {
                        Text("Confirmar", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun EditClientModal(
    client: Client,
    onDismiss: () -> Unit,
    onSave: (Client) -> Unit
) {
    var name by remember { mutableStateOf(client.name) }
    var phone by remember { mutableStateOf(client.phone) }
    var prosthesisType by remember { mutableStateOf(client.prosthesisType) }
    var notes by remember { mutableStateOf(client.notes) }
    var status by remember { mutableStateOf(client.status) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = CaptioCardBgElevated,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Editar Cliente",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(14.dp))

                CaptioInputField(value = name, onValueChange = { name = it }, label = "Nome Completo")
                Spacer(modifier = Modifier.height(8.dp))
                CaptioInputField(value = phone, onValueChange = { phone = it }, label = "Telefone")
                Spacer(modifier = Modifier.height(8.dp))
                CaptioInputField(value = prosthesisType, onValueChange = { prosthesisType = it }, label = "Tipo de Prótese")
                Spacer(modifier = Modifier.height(8.dp))
                CaptioInputField(value = notes, onValueChange = { notes = it }, label = "Observações")

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)) {
                        Text("Cancelar", color = CaptioTextGray)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onSave(client.copy(name = name, phone = phone, prosthesisType = prosthesisType, notes = notes, status = status))
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CaptioNeonGreen)
                    ) {
                        Text("Salvar Alterações", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
