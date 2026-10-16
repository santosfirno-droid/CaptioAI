package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AddAppointmentDialog
import com.example.ui.components.AddClientDialog
import com.example.ui.components.AddProductDialog
import com.example.ui.components.AddTransactionDialog
import com.example.ui.components.QuickActionSelectorDialog
import com.example.ui.screens.AtendimentosScreen
import com.example.ui.screens.ClientsScreen
import com.example.ui.screens.FinanceScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MoreScreen
import com.example.ui.screens.ProductsScreen
import com.example.ui.screens.PublicRequestScreen
import com.example.ui.screens.ReportsScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.CaptioScreen
import com.example.ui.viewmodel.CaptioViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                CaptioMainApp()
            }
        }
    }
}

@Composable
fun CaptioMainApp(
    viewModel: CaptioViewModel = viewModel()
) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val selectedDay by viewModel.selectedDay.collectAsState()

    var showQuickActionSheet by remember { mutableStateOf(false) }
    var showAddClientDialog by remember { mutableStateOf(false) }
    var showAddAppointmentDialog by remember { mutableStateOf(false) }
    var showAddProductDialog by remember { mutableStateOf(false) }
    var showAddTransactionDialog by remember { mutableStateOf(false) }

    // Android Back Button Navigation handling
    BackHandler(enabled = currentScreen != CaptioScreen.HOME) {
        viewModel.navigateTo(CaptioScreen.HOME)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        val modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)

        when (currentScreen) {
            CaptioScreen.HOME -> {
                HomeScreen(
                    viewModel = viewModel,
                    onNavigate = { viewModel.navigateTo(it) },
                    onQuickAdd = { showQuickActionSheet = true },
                    modifier = modifier
                )
            }
            CaptioScreen.CLIENTS -> {
                ClientsScreen(
                    viewModel = viewModel,
                    onNavigate = { viewModel.navigateTo(it) },
                    onQuickAdd = { showQuickActionSheet = true },
                    modifier = modifier
                )
            }
            CaptioScreen.ATENDIMENTOS -> {
                AtendimentosScreen(
                    viewModel = viewModel,
                    onNavigate = { viewModel.navigateTo(it) },
                    onQuickAdd = { showQuickActionSheet = true },
                    modifier = modifier
                )
            }
            CaptioScreen.PUBLIC_REQUEST -> {
                PublicRequestScreen(
                    viewModel = viewModel,
                    onNavigate = { viewModel.navigateTo(it) },
                    modifier = modifier
                )
            }
            CaptioScreen.FINANCE -> {
                FinanceScreen(
                    viewModel = viewModel,
                    onNavigate = { viewModel.navigateTo(it) },
                    onQuickAdd = { showQuickActionSheet = true },
                    modifier = modifier
                )
            }
            CaptioScreen.PRODUCTS -> {
                ProductsScreen(
                    viewModel = viewModel,
                    onNavigate = { viewModel.navigateTo(it) },
                    onQuickAdd = { showQuickActionSheet = true },
                    modifier = modifier
                )
            }
            CaptioScreen.REPORTS -> {
                ReportsScreen(
                    viewModel = viewModel,
                    onNavigate = { viewModel.navigateTo(it) },
                    onQuickAdd = { showQuickActionSheet = true },
                    modifier = modifier
                )
            }
            CaptioScreen.MORE -> {
                MoreScreen(
                    onNavigate = { viewModel.navigateTo(it) },
                    onQuickAdd = { showQuickActionSheet = true },
                    modifier = modifier
                )
            }
        }

        // Quick Action Dialog Sheet
        if (showQuickActionSheet) {
            QuickActionSelectorDialog(
                onDismiss = { showQuickActionSheet = false },
                onSelectClient = { showAddClientDialog = true },
                onSelectAppointment = { showAddAppointmentDialog = true },
                onSelectProduct = { showAddProductDialog = true },
                onSelectTransaction = { showAddTransactionDialog = true }
            )
        }

        // Add Client Dialog
        if (showAddClientDialog) {
            AddClientDialog(
                onDismiss = { showAddClientDialog = false },
                onConfirm = { name, phone, prosthesisType, notes, status ->
                    viewModel.addClient(name, phone, prosthesisType, notes, status)
                    showAddClientDialog = false
                }
            )
        }

        // Add Appointment Dialog
        if (showAddAppointmentDialog) {
            AddAppointmentDialog(
                initialDay = selectedDay,
                onDismiss = { showAddAppointmentDialog = false },
                onConfirm = { clientName, service, time, dayNumber, dayOfWeek, status ->
                    viewModel.addAppointment(
                        clientName = clientName,
                        clientPhone = "(11) 98765-4321",
                        service = service,
                        time = time,
                        durationMinutes = 60,
                        dayNumber = dayNumber,
                        dayOfWeek = dayOfWeek,
                        status = status
                    )
                    showAddAppointmentDialog = false
                }
            )
        }

        // Add Product Dialog
        if (showAddProductDialog) {
            AddProductDialog(
                onDismiss = { showAddProductDialog = false },
                onConfirm = { name, price, category, stock, status ->
                    viewModel.addProduct(name, price, category, stock, status)
                    showAddProductDialog = false
                }
            )
        }

        // Add Transaction Dialog
        if (showAddTransactionDialog) {
            AddTransactionDialog(
                onDismiss = { showAddTransactionDialog = false },
                onConfirm = { title, amount, method ->
                    viewModel.addTransaction(title, amount, method)
                    showAddTransactionDialog = false
                }
            )
        }
    }
}
