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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
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
fun PublicRequestScreen(
    viewModel: CaptioViewModel,
    onNavigate: (CaptioScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var selectedService by remember { mutableStateOf("Instalação de Prótese Capilar") }
    var preferredDate by remember { mutableStateOf("Esta semana") }
    var customDate by remember { mutableStateOf("") }
    var preferredPeriod by remember { mutableStateOf("Manhã") }
    var notes by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF0F0826),
                        Color(0xFF150B35),
                        Color(0xFF0B061A)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Top bar with exit button back to admin app
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onNavigate(CaptioScreen.HOME) },
                    modifier = Modifier.testTag("public_exit_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar ao App",
                        tint = Color.White
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x2200FF66))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "LINK PÚBLICO • CLIENTE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = CaptioNeonGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (!isSubmitted) {
                // Public Clinic Header
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_captio_logo),
                        contentDescription = "CAPTIO",
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(14.dp))
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Bruno Carvalho",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Especialista em Prótese Capilar Masculina",
                        fontSize = 12.sp,
                        color = CaptioNeonGreen,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Solicite seu agendamento de forma rápida e sem cadastro",
                        fontSize = 11.sp,
                        color = CaptioTextGray,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Informational Notice Card
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0x267C3AED))
                        .border(1.dp, Color(0x4D7C3AED), RoundedCornerShape(14.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = CaptioPurpleLight,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "A data e período informados são preferências. Seu horário exato será confirmado pelo Bruno no WhatsApp.",
                        fontSize = 11.sp,
                        color = Color.White,
                        lineHeight = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Field 1: Nome Completo
                Text(
                    text = "1. Seu Nome Completo *",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it; errorMessage = "" },
                    placeholder = { Text("Ex: Rafael Mendes", color = CaptioTextGray, fontSize = 12.sp) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = CaptioNeonGreen,
                        unfocusedBorderColor = Color(0x337C3AED),
                        focusedContainerColor = CaptioCardBg,
                        unfocusedContainerColor = CaptioCardBg
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("public_name_input")
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Field 2: WhatsApp
                Text(
                    text = "2. Seu WhatsApp com DDD *",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it; errorMessage = "" },
                    placeholder = { Text("(11) 98765-4321", color = CaptioTextGray, fontSize = 12.sp) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = CaptioNeonGreen,
                        unfocusedBorderColor = Color(0x337C3AED),
                        focusedContainerColor = CaptioCardBg,
                        unfocusedContainerColor = CaptioCardBg
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("public_phone_input")
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Field 3: Serviço Desejado
                Text(
                    text = "3. Qual serviço você precisa? *",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))

                ServiceSelectCard(
                    title = "Instalação de Prótese Capilar",
                    description = "Primeira aplicação, molde, corte, ajuste e fixação de nova prótese.",
                    icon = Icons.Default.ContentCut,
                    isSelected = selectedService == "Instalação de Prótese Capilar",
                    onClick = { selectedService = "Instalação de Prótese Capilar" }
                )

                Spacer(modifier = Modifier.height(8.dp))

                ServiceSelectCard(
                    title = "Manutenção de Prótese",
                    description = "Higienização da base, troca de fitas/colas, limpeza do couro cabeludo e corte.",
                    icon = Icons.Default.Spa,
                    isSelected = selectedService == "Manutenção de Prótese",
                    onClick = { selectedService = "Manutenção de Prótese" }
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Field 4: Quando pretende realizar
                Text(
                    text = "4. Quando pretende realizar o serviço? *",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Esta semana", "Próxima semana", "Outra data").forEach { opt ->
                        val isSelected = preferredDate == opt
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) CaptioPurplePrimary else CaptioCardBg)
                                .clickable { preferredDate = opt }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = opt,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = Color.White
                            )
                        }
                    }
                }

                if (preferredDate == "Outra data") {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = customDate,
                        onValueChange = { customDate = it },
                        placeholder = { Text("Ex: A partir do dia 25/11", color = CaptioTextGray, fontSize = 12.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = CaptioNeonGreen,
                            unfocusedBorderColor = Color(0x337C3AED),
                            focusedContainerColor = CaptioCardBg,
                            unfocusedContainerColor = CaptioCardBg
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Field 5: Período preferido (Manhã ou Tarde)
                Text(
                    text = "5. Período de sua preferência *",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PeriodSelectCard(
                        title = "Manhã",
                        timeRange = "08:00 às 12:00",
                        icon = Icons.Default.WbSunny,
                        isSelected = preferredPeriod == "Manhã",
                        onClick = { preferredPeriod = "Manhã" },
                        modifier = Modifier.weight(1f)
                    )

                    PeriodSelectCard(
                        title = "Tarde",
                        timeRange = "13:00 às 18:00",
                        icon = Icons.Default.WbTwilight,
                        isSelected = preferredPeriod == "Tarde",
                        onClick = { preferredPeriod = "Tarde" },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Field 6: Observações opcionais
                Text(
                    text = "6. Observações ou dúvidas (opcional)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    placeholder = { Text("Ex: Gostaria de saber sobre densidade, tenho pele sensível...", color = CaptioTextGray, fontSize = 12.sp) },
                    maxLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = CaptioNeonGreen,
                        unfocusedBorderColor = Color(0x337C3AED),
                        focusedContainerColor = CaptioCardBg,
                        unfocusedContainerColor = CaptioCardBg
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                if (errorMessage.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMessage,
                        fontSize = 12.sp,
                        color = Color(0xFFFF5252),
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Submit Button
                Button(
                    onClick = {
                        if (name.isBlank()) {
                            errorMessage = "Por favor, digite seu nome completo."
                            return@Button
                        }
                        if (phone.isBlank()) {
                            errorMessage = "Por favor, informe seu número de WhatsApp."
                            return@Button
                        }
                        val finalDate = if (preferredDate == "Outra data" && customDate.isNotBlank()) customDate else preferredDate
                        viewModel.addPublicServiceRequest(
                            clientName = name.trim(),
                            phone = phone.trim(),
                            serviceType = selectedService,
                            preferredDate = finalDate,
                            preferredPeriod = preferredPeriod,
                            notes = notes.trim()
                        )
                        isSubmitted = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CaptioNeonGreen),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("submit_public_request_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Enviar Solicitação de Atendimento",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))
            } else {
                // Success Confirmation State (Conforme item 2 dos requisitos)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color(0x2200FF66)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Sucesso",
                            tint = CaptioNeonGreen,
                            modifier = Modifier.size(54.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Solicitação Recebida!",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Obrigado, $name! Recebemos seu pedido para $selectedService no período da $preferredPeriod.",
                        fontSize = 14.sp,
                        color = CaptioNeonGreen,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(CaptioCardBg)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Importante: O seu horário ainda depende da confirmação do Bruno Carvalho. Ele entrará em contato diretamente pelo WhatsApp $phone para combinar a data e hora exatas.",
                            fontSize = 12.sp,
                            color = CaptioTextGray,
                            textAlign = TextAlign.Center,
                            lineHeight = 17.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Button(
                        onClick = {
                            isSubmitted = false
                            name = ""
                            phone = ""
                            notes = ""
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CaptioCardBgElevated),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Enviar outra solicitação", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = { onNavigate(CaptioScreen.HOME) },
                        colors = ButtonDefaults.buttonColors(containerColor = CaptioPurplePrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Voltar ao Painel Principal", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun ServiceSelectCard(
    title: String,
    description: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(if (isSelected) Color(0x337C3AED) else CaptioCardBg)
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) CaptioNeonGreen else Color(0x227C3AED),
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) CaptioNeonGreen else Color(0x2200FF66)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) Color.Black else CaptioNeonGreen,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                fontSize = 10.sp,
                color = CaptioTextGray,
                lineHeight = 14.sp
            )
        }
    }
}

@Composable
private fun PeriodSelectCard(
    title: String,
    timeRange: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (isSelected) Color(0x337C3AED) else CaptioCardBg)
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) CaptioNeonGreen else Color(0x227C3AED),
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) CaptioNeonGreen else CaptioTextGray,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = timeRange,
            fontSize = 10.sp,
            color = CaptioTextGray
        )
    }
}
