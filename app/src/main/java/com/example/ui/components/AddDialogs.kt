package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.PersonAdd
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.CaptioCardBg
import com.example.ui.theme.CaptioCardBgElevated
import com.example.ui.theme.CaptioNeonGreen
import com.example.ui.theme.CaptioPurpleLight
import com.example.ui.theme.CaptioPurplePrimary
import com.example.ui.theme.CaptioTextGray
import com.example.ui.theme.CaptioTextWhite

@Composable
fun QuickActionSelectorDialog(
    onDismiss: () -> Unit,
    onSelectClient: () -> Unit,
    onSelectAppointment: () -> Unit,
    onSelectProduct: () -> Unit,
    onSelectTransaction: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = CaptioCardBgElevated,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ação Rápida",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Fechar", tint = CaptioTextGray)
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))

                QuickActionButton(
                    icon = Icons.Default.CalendarMonth,
                    title = "Novo Agendamento",
                    subtitle = "Consulta, manutenção ou prótese",
                    onClick = { onDismiss(); onSelectAppointment() }
                )
                Spacer(modifier = Modifier.height(8.dp))

                QuickActionButton(
                    icon = Icons.Default.PersonAdd,
                    title = "Novo Cliente",
                    subtitle = "Cadastrar cliente de prótese capilar",
                    onClick = { onDismiss(); onSelectClient() }
                )
                Spacer(modifier = Modifier.height(8.dp))

                QuickActionButton(
                    icon = Icons.Default.AttachMoney,
                    title = "Nova Venda / Pagamento",
                    subtitle = "Registrar entrada via PIX ou Cartão",
                    onClick = { onDismiss(); onSelectTransaction() }
                )
                Spacer(modifier = Modifier.height(8.dp))

                QuickActionButton(
                    icon = Icons.Default.Inventory2,
                    title = "Novo Produto",
                    subtitle = "Prótese, insumo ou vestuário",
                    onClick = { onDismiss(); onSelectProduct() }
                )
            }
        }
    }
}

@Composable
private fun QuickActionButton(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(CaptioCardBg)
            .clickable(onClick = onClick)
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
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = CaptioNeonGreen,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = CaptioTextWhite
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = CaptioTextGray
            )
        }
    }
}

@Composable
fun AddClientDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, phone: String, prosthesisType: String, notes: String, status: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var prosthesisType by remember { mutableStateOf("Micropele Silk 0.04mm") }
    var notes by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Ativo") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = CaptioCardBgElevated,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Novo Cliente",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(14.dp))

                CaptioInputField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Nome completo",
                    testTag = "client_name_input"
                )
                Spacer(modifier = Modifier.height(8.dp))

                CaptioInputField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = "WhatsApp / Telefone",
                    keyboardType = KeyboardType.Phone,
                    testTag = "client_phone_input"
                )
                Spacer(modifier = Modifier.height(8.dp))

                CaptioInputField(
                    value = prosthesisType,
                    onValueChange = { prosthesisType = it },
                    label = "Tipo de Prótese Capilar",
                    testTag = "client_prosthesis_input"
                )
                Spacer(modifier = Modifier.height(8.dp))

                CaptioInputField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = "Observações / Manutenções",
                    testTag = "client_notes_input"
                )
                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text("Cancelar", color = CaptioTextGray)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (name.isNotBlank()) {
                                onConfirm(name, phone.ifBlank { "(11) 98765-4321" }, prosthesisType, notes, status)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CaptioNeonGreen),
                        modifier = Modifier.testTag("save_client_button")
                    ) {
                        Text("Salvar Cliente", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun AddAppointmentDialog(
    initialDay: String,
    onDismiss: () -> Unit,
    onConfirm: (clientName: String, service: String, time: String, dayNumber: String, dayOfWeek: String, status: String) -> Unit
) {
    var clientName by remember { mutableStateOf("") }
    var service by remember { mutableStateOf("Manutenção") }
    var time by remember { mutableStateOf("14:00") }
    var status by remember { mutableStateOf("Confirmado") }

    val services = listOf("Consulta • Ajuste", "Aplicação de Prótese", "Manutenção", "Avaliação", "Higienização e Corte")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = CaptioCardBgElevated,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Novo Agendamento",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(14.dp))

                CaptioInputField(
                    value = clientName,
                    onValueChange = { clientName = it },
                    label = "Nome do Cliente",
                    testTag = "appointment_client_input"
                )
                Spacer(modifier = Modifier.height(8.dp))

                CaptioInputField(
                    value = service,
                    onValueChange = { service = it },
                    label = "Serviço de Prótese",
                    testTag = "appointment_service_input"
                )
                Spacer(modifier = Modifier.height(8.dp))

                CaptioInputField(
                    value = time,
                    onValueChange = { time = it },
                    label = "Horário (ex: 10:30)",
                    testTag = "appointment_time_input"
                )
                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text("Cancelar", color = CaptioTextGray)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (clientName.isNotBlank()) {
                                onConfirm(clientName, service, time, initialDay, "Ter", status)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CaptioNeonGreen),
                        modifier = Modifier.testTag("save_appointment_button")
                    ) {
                        Text("Agendar", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun AddProductDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, price: Double, category: String, stock: Int, status: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var priceText by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Prótese") }
    var stockText by remember { mutableStateOf("10") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = CaptioCardBgElevated,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Novo Produto",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(14.dp))

                CaptioInputField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Nome do Produto",
                    testTag = "product_name_input"
                )
                Spacer(modifier = Modifier.height(8.dp))

                CaptioInputField(
                    value = priceText,
                    onValueChange = { priceText = it },
                    label = "Preço (ex: 89.90)",
                    keyboardType = KeyboardType.Decimal,
                    testTag = "product_price_input"
                )
                Spacer(modifier = Modifier.height(8.dp))

                CaptioInputField(
                    value = category,
                    onValueChange = { category = it },
                    label = "Categoria (Prótese, Insumos, Vestuário)",
                    testTag = "product_category_input"
                )
                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text("Cancelar", color = CaptioTextGray)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val price = priceText.toDoubleOrNull() ?: 50.0
                            val stock = stockText.toIntOrNull() ?: 10
                            if (name.isNotBlank()) {
                                onConfirm(name, price, category, stock, "Ativo")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CaptioNeonGreen),
                        modifier = Modifier.testTag("save_product_button")
                    ) {
                        Text("Cadastrar", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun AddTransactionDialog(
    onDismiss: () -> Unit,
    onConfirm: (title: String, amount: Double, method: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var method by remember { mutableStateOf("PIX") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = CaptioCardBgElevated,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Registrar Venda / Recebimento",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(14.dp))

                CaptioInputField(
                    value = title,
                    onValueChange = { title = it },
                    label = "Descrição / Cliente",
                    testTag = "trans_title_input"
                )
                Spacer(modifier = Modifier.height(8.dp))

                CaptioInputField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = "Valor R$ (ex: 280.00)",
                    keyboardType = KeyboardType.Decimal,
                    testTag = "trans_amount_input"
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("PIX", "Cartão", "Outros").forEach { m ->
                        val selected = method == m
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (selected) CaptioPurplePrimary else CaptioCardBg)
                                .clickable { method = m }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = m,
                                fontSize = 12.sp,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                color = Color.White
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text("Cancelar", color = CaptioTextGray)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val amount = amountText.toDoubleOrNull() ?: 100.0
                            if (title.isNotBlank()) {
                                onConfirm(title, amount, method)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CaptioNeonGreen),
                        modifier = Modifier.testTag("save_trans_button")
                    ) {
                        Text("Registrar", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun CaptioInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    testTag: String = ""
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = CaptioTextGray, fontSize = 12.sp) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = CaptioNeonGreen,
            unfocusedBorderColor = Color(0x447C3AED),
            focusedContainerColor = CaptioCardBg,
            unfocusedContainerColor = CaptioCardBg
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag),
        singleLine = true
    )
}
