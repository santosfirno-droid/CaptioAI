package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import com.example.data.model.ServiceRequest
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

@Composable
fun AtendimentosScreen(
    viewModel: CaptioViewModel,
    onNavigate: (CaptioScreen) -> Unit,
    onQuickAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val activeTab by viewModel.atendimentosTab.collectAsState()
    val appointments by viewModel.filteredAppointments.collectAsState()
    val selectedDay by viewModel.selectedDay.collectAsState()
    val serviceRequests by viewModel.serviceRequests.collectAsState()
    val pendingCount by viewModel.pendingRequestsCount.collectAsState()

    var requestToConfirm by remember { mutableStateOf<ServiceRequest?>(null) }
    var selectedAppointmentForDetail by remember { mutableStateOf<Appointment?>(null) }
    var appointmentToReschedule by remember { mutableStateOf<Appointment?>(null) }
    var whatsappMessageDialogText by remember { mutableStateOf<Pair<String, String>?>(null) } // Pair(phone, text)

    val daysOfWeek = listOf(
        Pair("Seg", "28"),
        Pair("Ter", "29"),
        Pair("Qua", "30"),
        Pair("Qui", "31"),
        Pair("Sex", "01"),
        Pair("Sáb", "02")
    )

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

            // Top Bar: Back, Title, Public Link shortcut
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onNavigate(CaptioScreen.HOME) },
                    modifier = Modifier.testTag("atendimentos_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Atendimentos",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                IconButton(
                    onClick = { onNavigate(CaptioScreen.PUBLIC_REQUEST) },
                    modifier = Modifier.testTag("open_public_link_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Link,
                        contentDescription = "Página Pública",
                        tint = CaptioNeonGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Public Request Link Banner
            PublicLinkBanner(
                onShare = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "Olá! Para solicitar sua avaliação ou manutenção de prótese capilar comigo, acesse o link: https://captio.app/bruno-protese"
                        )
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Compartilhar Link"))
                },
                onCopy = {
                    clipboardManager.setText(AnnotatedString("https://captio.app/bruno-protese"))
                    Toast.makeText(context, "Link público copiado!", Toast.LENGTH_SHORT).show()
                },
                onOpen = { onNavigate(CaptioScreen.PUBLIC_REQUEST) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Main Two Tabs: "Confirmados" and "Solicitações" (com badge de pendentes)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TabButton(
                    label = "Confirmados",
                    badge = null,
                    isSelected = activeTab == "Confirmados",
                    onClick = { viewModel.setAtendimentosTab("Confirmados") },
                    modifier = Modifier.weight(1f)
                )

                TabButton(
                    label = "Solicitações",
                    badge = if (pendingCount > 0) "$pendingCount" else null,
                    isSelected = activeTab == "Solicitações",
                    onClick = { viewModel.setAtendimentosTab("Solicitações") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (activeTab == "Confirmados") {
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

                Spacer(modifier = Modifier.height(14.dp))

                // Appointments Timeline List
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (appointments.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(CaptioCardBg)
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "Nenhum atendimento confirmado para este dia.",
                                        fontSize = 12.sp,
                                        color = CaptioTextGray
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Toque no botão abaixo para agendar.",
                                        fontSize = 11.sp,
                                        color = CaptioNeonGreen
                                    )
                                }
                            }
                        }
                    } else {
                        items(appointments) { appt ->
                            AppointmentRowItem(
                                appointment = appt,
                                onClick = { selectedAppointmentForDetail = appt }
                            )
                        }
                    }

                    // Add appointment button
                    item {
                        Button(
                            onClick = onQuickAdd,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0x227C3AED)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color(0x447C3AED), RoundedCornerShape(12.dp))
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = CaptioPurpleLight, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Novo Atendimento Manual", color = CaptioPurpleLight, fontSize = 12.sp)
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(95.dp))
                    }
                }
            } else {
                // Tab "Solicitações Recebidas"
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val pendingList = serviceRequests.filter { it.status == "Pendente" }
                    val answeredList = serviceRequests.filter { it.status != "Pendente" }

                    if (pendingList.isEmpty() && answeredList.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 30.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Nenhuma solicitação recebida ainda.\nCompartilhe seu link público com clientes!",
                                    fontSize = 12.sp,
                                    color = CaptioTextGray,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }

                    if (pendingList.isNotEmpty()) {
                        item {
                            Text(
                                text = "Aguardando Confirmação (${pendingList.size})",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = CaptioNeonGreen
                            )
                        }

                        items(pendingList) { request ->
                            ServiceRequestCard(
                                request = request,
                                onConfirm = { requestToConfirm = request },
                                onReject = { viewModel.rejectServiceRequest(request) },
                                onContact = {
                                    openWhatsApp(context, request.phone, "Olá ${request.clientName}! Vi sua solicitação no CAPTIO para ${request.serviceType}. Gostaria de combinar o melhor horário.")
                                }
                            )
                        }
                    }

                    if (answeredList.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Histórico de Solicitações",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = CaptioTextGray
                            )
                        }

                        items(answeredList) { request ->
                            ServiceRequestCard(
                                request = request,
                                onConfirm = { requestToConfirm = request },
                                onReject = { viewModel.rejectServiceRequest(request) },
                                onContact = {
                                    openWhatsApp(context, request.phone, "Olá ${request.clientName}!")
                                }
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(95.dp))
                    }
                }
            }
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = onQuickAdd,
            containerColor = CaptioNeonGreen,
            contentColor = Color.Black,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 85.dp, end = 20.dp)
                .testTag("add_atendimento_fab")
        ) {
            Icon(Icons.Default.Add, contentDescription = "Novo Atendimento")
        }

        // Bottom Navigation Bar
        CaptioBottomBar(
            currentScreen = CaptioScreen.ATENDIMENTOS,
            onNavigate = onNavigate,
            onQuickAdd = onQuickAdd,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        // Modal: Confirmar Solicitação com checagem de conflito
        requestToConfirm?.let { req ->
            ConfirmRequestDialog(
                request = req,
                initialDay = selectedDay,
                onDismiss = { requestToConfirm = null },
                onCheckConflict = { dayNum, time, duration ->
                    viewModel.checkAppointmentConflict(dayNum, time, duration)
                },
                onConfirm = { dayNum, dayOfWeek, time, duration ->
                    val message = viewModel.confirmServiceRequest(req, dayNum, dayOfWeek, time, duration)
                    requestToConfirm = null
                    whatsappMessageDialogText = Pair(req.phone, message)
                }
            )
        }

        // Modal: Detalhes do Atendimento (Concluir, Remarcar, Cancelar)
        selectedAppointmentForDetail?.let { appt ->
            AppointmentActionModal(
                appointment = appt,
                onDismiss = { selectedAppointmentForDetail = null },
                onComplete = {
                    viewModel.completeAppointment(appt)
                    selectedAppointmentForDetail = null
                    Toast.makeText(context, "Atendimento marcado como Concluído!", Toast.LENGTH_SHORT).show()
                },
                onReschedule = {
                    appointmentToReschedule = appt
                    selectedAppointmentForDetail = null
                },
                onCancel = {
                    viewModel.cancelAppointment(appt)
                    selectedAppointmentForDetail = null
                    Toast.makeText(context, "Atendimento Cancelado.", Toast.LENGTH_SHORT).show()
                },
                onContact = {
                    openWhatsApp(context, appt.clientPhone, "Olá ${appt.clientName}! Estou confirmando nosso atendimento de ${appt.service}.")
                }
            )
        }

        // Modal: Remarcar Atendimento com Checagem de Conflito
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
                    whatsappMessageDialogText = Pair(appt.clientPhone, msg)
                }
            )
        }

        // Modal: Preparar e Enviar Mensagem de WhatsApp (Conforme item 4 dos requisitos)
        whatsappMessageDialogText?.let { (phone, msg) ->
            WhatsAppConfirmationDialog(
                phone = phone,
                messageText = msg,
                onDismiss = { whatsappMessageDialogText = null },
                onSendWhatsApp = {
                    openWhatsApp(context, phone, msg)
                    whatsappMessageDialogText = null
                },
                onCopy = {
                    clipboardManager.setText(AnnotatedString(msg))
                    Toast.makeText(context, "Mensagem copiada para a área de transferência!", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

@Composable
private fun PublicLinkBanner(
    onShare: () -> Unit,
    onCopy: () -> Unit,
    onOpen: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(Color(0xFF23144E), Color(0xFF180D38))
                )
            )
            .border(1.dp, Color(0x337C3AED), RoundedCornerShape(16.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Color(0x2200FF66)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Link, contentDescription = null, tint = CaptioNeonGreen, modifier = Modifier.size(20.dp))
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Link Público de Solicitações",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "captio.app/bruno-protese",
                fontSize = 11.sp,
                color = CaptioNeonGreen
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            IconButton(onClick = onCopy, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.ContentCopy, contentDescription = "Copiar", tint = Color.White, modifier = Modifier.size(16.dp))
            }
            IconButton(onClick = onShare, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.Share, contentDescription = "Compartilhar", tint = CaptioNeonGreen, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
private fun TabButton(
    label: String,
    badge: String?,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (isSelected) CaptioPurplePrimary else CaptioCardBg)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else CaptioTextGray
            )
            if (badge != null) {
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(CaptioNeonGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = badge,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
private fun DateSelectorCard(
    dayOfWeek: String,
    dayNumber: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) CaptioPurplePrimary else CaptioCardBg)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = dayOfWeek,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = if (isSelected) Color.White else CaptioTextGray
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = dayNumber,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) CaptioNeonGreen else Color.White
            )
        }
    }
}

@Composable
fun AppointmentRowItem(
    appointment: Appointment,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(52.dp)
        ) {
            Text(
                text = appointment.time,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "${appointment.durationMinutes}m",
                fontSize = 10.sp,
                color = CaptioTextGray
            )
        }

        Row(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(CaptioCardBg)
                .clickable(onClick = onClick)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
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

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = appointment.clientName,
                    fontSize = 13.sp,
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

            val badgeColor = when (appointment.status) {
                "Confirmado" -> CaptioNeonGreen
                "Concluído" -> Color(0xFF38BDF8)
                "Pendente" -> CaptioOrange
                else -> Color(0xFFFF5252)
            }
            val badgeBg = when (appointment.status) {
                "Confirmado" -> Color(0x1F00FF66)
                "Concluído" -> Color(0x2238BDF8)
                "Pendente" -> Color(0x2AF59E0B)
                else -> Color(0x22FF5252)
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(badgeBg)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
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

@Composable
fun ServiceRequestCard(
    request: ServiceRequest,
    onConfirm: () -> Unit,
    onReject: () -> Unit,
    onContact: () -> Unit
) {
    val isPending = request.status == "Pendente"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(CaptioCardBg)
            .border(
                1.dp,
                if (isPending) Color(0x4400FF66) else Color(0x227C3AED),
                RoundedCornerShape(18.dp)
            )
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = request.clientName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "WhatsApp: ${request.phone}",
                    fontSize = 11.sp,
                    color = CaptioNeonGreen
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        when (request.status) {
                            "Pendente" -> Color(0x2AF59E0B)
                            "Aceito" -> Color(0x2200FF66)
                            else -> Color(0x22FF5252)
                        }
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = request.status,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = when (request.status) {
                        "Pendente" -> CaptioOrange
                        "Aceito" -> CaptioNeonGreen
                        else -> Color(0xFFFF5252)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Serviço Desejado: ${request.serviceType}",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
        Text(
            text = "Previsão sugerida: ${request.preferredDate} (${request.preferredPeriod})",
            fontSize = 11.sp,
            color = CaptioTextGray
        )

        if (request.notes.isNotBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Obs: \"${request.notes}\"",
                fontSize = 11.sp,
                color = Color(0xFFD1D1E9)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Action Buttons for Request
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onContact,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = null, tint = CaptioNeonGreen, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("WhatsApp", fontSize = 11.sp)
            }

            if (isPending) {
                Button(
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(containerColor = CaptioNeonGreen),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Aceitar", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }

                IconButton(
                    onClick = onReject,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0x22FF5252))
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Recusar", tint = Color(0xFFFF5252), modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
private fun ConfirmRequestDialog(
    request: ServiceRequest,
    initialDay: String,
    onDismiss: () -> Unit,
    onCheckConflict: (dayNum: String, time: String, duration: Int) -> Appointment?,
    onConfirm: (dayNum: String, dayOfWeek: String, time: String, duration: Int) -> Unit
) {
    var selectedDayNum by remember { mutableStateOf(initialDay) }
    var selectedDayOfWeek by remember { mutableStateOf("Ter") }
    var selectedTime by remember { mutableStateOf(if (request.preferredPeriod == "Tarde") "14:00" else "09:00") }
    var durationMinutes by remember { mutableStateOf(if (request.serviceType.contains("Instalação")) 90 else 60) }

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
                    text = "Confirmar Atendimento",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Cliente: ${request.clientName} (${request.serviceType})",
                    fontSize = 11.sp,
                    color = CaptioNeonGreen
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Select Day
                Text(text = "1. Selecione o Dia:", fontSize = 12.sp, color = CaptioTextGray)
                Spacer(modifier = Modifier.height(6.dp))
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
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(wDay, fontSize = 9.sp, color = Color.White)
                                Text(dNum, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Select Time
                Text(text = "2. Horário de Início:", fontSize = 12.sp, color = CaptioTextGray)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    times.take(5).forEach { tm ->
                        val isSel = selectedTime == tm
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) CaptioNeonGreen else CaptioCardBg)
                                .clickable { selectedTime = tm }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(tm, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.Black else Color.White)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    times.drop(5).forEach { tm ->
                        val isSel = selectedTime == tm
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) CaptioNeonGreen else CaptioCardBg)
                                .clickable { selectedTime = tm }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(tm, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.Black else Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Select Duration
                Text(text = "3. Duração Prevista:", fontSize = 12.sp, color = CaptioTextGray)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    durations.forEach { dur ->
                        val isSel = durationMinutes == dur
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) CaptioPurplePrimary else CaptioCardBg)
                                .clickable { durationMinutes = dur }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("${dur} min", fontSize = 11.sp, color = Color.White)
                        }
                    }
                }

                // Overlap / Conflict Warning Banner (CRITICAL: Item 4)
                if (conflict != null) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x33FF5252))
                            .border(1.dp, Color(0xFFFF5252), RoundedCornerShape(12.dp))
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFFF5252), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Conflito de Horário! Já existe atendimento confirmado para ${conflict.clientName} às ${conflict.time} (${conflict.durationMinutes}m). Escolha outro horário.",
                            fontSize = 11.sp,
                            color = Color(0xFFFF5252),
                            lineHeight = 15.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

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
                            if (conflict == null) {
                                onConfirm(selectedDayNum, selectedDayOfWeek, selectedTime, durationMinutes)
                            }
                        },
                        enabled = conflict == null,
                        colors = ButtonDefaults.buttonColors(containerColor = CaptioNeonGreen)
                    ) {
                        Text("Confirmar Horário", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun AppointmentActionModal(
    appointment: Appointment,
    onDismiss: () -> Unit,
    onComplete: () -> Unit,
    onReschedule: () -> Unit,
    onCancel: () -> Unit,
    onContact: () -> Unit
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
                    Column {
                        Text(
                            text = appointment.clientName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "${appointment.dayOfWeek} ${appointment.dayNumber} às ${appointment.time} (${appointment.durationMinutes} min)",
                            fontSize = 12.sp,
                            color = CaptioNeonGreen
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = null, tint = CaptioTextGray)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Serviço: ${appointment.service}", fontSize = 13.sp, color = Color.White)
                Text(text = "WhatsApp: ${appointment.clientPhone}", fontSize = 12.sp, color = CaptioTextGray)
                Text(text = "Status: ${appointment.status}", fontSize = 12.sp, color = CaptioTextGray)

                Spacer(modifier = Modifier.height(18.dp))

                // Actions: Concluir, Remarcar, Cancelar, WhatsApp
                Button(
                    onClick = onComplete,
                    colors = ButtonDefaults.buttonColors(containerColor = CaptioNeonGreen),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Marcar como Concluído", color = Color.Black, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onReschedule,
                        colors = ButtonDefaults.buttonColors(containerColor = CaptioPurplePrimary),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.EditCalendar, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Remarcar", color = Color.White, fontSize = 12.sp)
                    }

                    Button(
                        onClick = onContact,
                        colors = ButtonDefaults.buttonColors(containerColor = CaptioCardBg),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = null, tint = CaptioNeonGreen, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("WhatsApp", color = Color.White, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onCancel,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF5252)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Cancel, contentDescription = null, tint = Color(0xFFFF5252), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Cancelar Atendimento", color = Color(0xFFFF5252))
                }
            }
        }
    }
}

@Composable
fun RescheduleDialog(
    appointment: Appointment,
    onDismiss: () -> Unit,
    onCheckConflict: (dayNum: String, time: String, duration: Int) -> Appointment?,
    onConfirm: (newDayNum: String, newDayOfWeek: String, newTime: String, newDuration: Int) -> Unit
) {
    var selectedDayNum by remember { mutableStateOf(appointment.dayNumber) }
    var selectedDayOfWeek by remember { mutableStateOf(appointment.dayOfWeek) }
    var selectedTime by remember { mutableStateOf(appointment.time) }
    var durationMinutes by remember { mutableStateOf(appointment.durationMinutes) }

    val days = listOf(
        Pair("Seg", "28"),
        Pair("Ter", "29"),
        Pair("Qua", "30"),
        Pair("Qui", "31"),
        Pair("Sex", "01"),
        Pair("Sáb", "02")
    )
    val times = listOf("08:00", "09:00", "10:00", "11:00", "14:00", "15:00", "16:00", "17:00")
    val conflict = onCheckConflict(selectedDayNum, selectedTime, durationMinutes)

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = CaptioCardBgElevated,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Remarcar Atendimento",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Cliente: ${appointment.clientName}",
                    fontSize = 12.sp,
                    color = CaptioNeonGreen
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(text = "Novo Dia:", fontSize = 12.sp, color = CaptioTextGray)
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
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
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(wDay, fontSize = 9.sp, color = Color.White)
                                Text(dNum, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(text = "Novo Horário:", fontSize = 12.sp, color = CaptioTextGray)
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    times.take(4).forEach { tm ->
                        val isSel = selectedTime == tm
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) CaptioNeonGreen else CaptioCardBg)
                                .clickable { selectedTime = tm }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(tm, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.Black else Color.White)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    times.drop(4).forEach { tm ->
                        val isSel = selectedTime == tm
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) CaptioNeonGreen else CaptioCardBg)
                                .clickable { selectedTime = tm }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(tm, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.Black else Color.White)
                        }
                    }
                }

                if (conflict != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Conflito com atendimento de ${conflict.clientName} às ${conflict.time}!",
                        fontSize = 11.sp,
                        color = Color(0xFFFF5252),
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)) {
                        Text("Cancelar", color = CaptioTextGray)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (conflict == null) {
                                onConfirm(selectedDayNum, selectedDayOfWeek, selectedTime, durationMinutes)
                            }
                        },
                        enabled = conflict == null,
                        colors = ButtonDefaults.buttonColors(containerColor = CaptioNeonGreen)
                    ) {
                        Text("Salvar Remarcação", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun WhatsAppConfirmationDialog(
    phone: String,
    messageText: String,
    onDismiss: () -> Unit,
    onSendWhatsApp: () -> Unit,
    onCopy: () -> Unit
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = CaptioNeonGreen, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Horário Confirmado!",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = null, tint = CaptioTextGray)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Mensagem pronta para enviar ao cliente:",
                    fontSize = 12.sp,
                    color = CaptioTextGray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(CaptioCardBg)
                        .padding(14.dp)
                ) {
                    Text(
                        text = messageText,
                        fontSize = 12.sp,
                        color = Color.White,
                        lineHeight = 17.sp
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = onSendWhatsApp,
                    colors = ButtonDefaults.buttonColors(containerColor = CaptioNeonGreen),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Abrir Conversa no WhatsApp", color = Color.Black, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onCopy,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Copiar", fontSize = 12.sp)
                    }

                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = CaptioCardBg),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Concluir", color = Color.White, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

fun openWhatsApp(context: Context, phone: String, message: String) {
    try {
        val cleanPhone = phone.replace(Regex("[^0-9]"), "")
        val fullPhone = if (cleanPhone.startsWith("55")) cleanPhone else "55$cleanPhone"
        val encodedMsg = Uri.encode(message)
        val uri = Uri.parse("https://api.whatsapp.com/send?phone=$fullPhone&text=$encodedMsg")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Não foi possível abrir o WhatsApp: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
    }
}
