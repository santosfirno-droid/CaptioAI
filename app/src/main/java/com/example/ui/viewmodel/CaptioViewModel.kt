package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.CaptioDatabase
import com.example.data.model.Appointment
import com.example.data.model.Client
import com.example.data.model.Product
import com.example.data.model.Transaction
import com.example.data.repository.CaptioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class CaptioScreen {
    HOME,
    CLIENTS,
    AGENDA,
    FINANCE,
    PRODUCTS,
    REPORTS,
    MORE,
    SHOWCASE_GRID
}

data class FinancialMetrics(
    val totalRevenue: Double = 23392.20,
    val netRevenue: Double = 21754.90,
    val salesCount: Int = 47,
    val ticketMedio: Double = 497.70,
    val growthPercent: Int = 18,
    val salesGrowthPercent: Int = 21,
    val netGrowthPercent: Int = 20,
    val pixAmount: Double = 14239.80,
    val pixPercent: Int = 61,
    val cardAmount: Double = 7112.40,
    val cardPercent: Int = 30,
    val othersAmount: Double = 1040.00,
    val othersPercent: Int = 9
)

class CaptioViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: CaptioRepository

    private val _currentScreen = MutableStateFlow(CaptioScreen.HOME)
    val currentScreen: StateFlow<CaptioScreen> = _currentScreen.asStateFlow()

    private val _selectedDay = MutableStateFlow("29")
    val selectedDay: StateFlow<String> = _selectedDay.asStateFlow()

    private val _clientSearchQuery = MutableStateFlow("")
    val clientSearchQuery: StateFlow<String> = _clientSearchQuery.asStateFlow()

    private val _clientFilterTab = MutableStateFlow("Todos") // "Todos", "Ativos", "Inativos"
    val clientFilterTab: StateFlow<String> = _clientFilterTab.asStateFlow()

    private val _productSearchQuery = MutableStateFlow("")
    val productSearchQuery: StateFlow<String> = _productSearchQuery.asStateFlow()

    private val _productFilterTab = MutableStateFlow("Todos")
    val productFilterTab: StateFlow<String> = _productFilterTab.asStateFlow()

    private val _financePeriod = MutableStateFlow("30 dias")
    val financePeriod: StateFlow<String> = _financePeriod.asStateFlow()

    private val _reportsTab = MutableStateFlow("Vendas")
    val reportsTab: StateFlow<String> = _reportsTab.asStateFlow()

    init {
        val database = CaptioDatabase.getDatabase(application, viewModelScope)
        repository = CaptioRepository(database.captioDao())
    }

    val clients: StateFlow<List<Client>> = repository.allClients
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val appointments: StateFlow<List<Appointment>> = repository.allAppointments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val products: StateFlow<List<Product>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val transactions: StateFlow<List<Transaction>> = repository.allTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredClients = combine(clients, _clientSearchQuery, _clientFilterTab) { list, query, filter ->
        list.filter { client ->
            val matchesQuery = query.isBlank() || client.name.contains(query, ignoreCase = true) ||
                    client.prosthesisType.contains(query, ignoreCase = true) ||
                    client.phone.contains(query)
            val matchesFilter = when (filter) {
                "Ativos" -> client.status == "Ativo"
                "Inativos" -> client.status == "Inativo"
                else -> true
            }
            matchesQuery && matchesFilter
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredProducts = combine(products, _productSearchQuery, _productFilterTab) { list, query, filter ->
        list.filter { prod ->
            val matchesQuery = query.isBlank() || prod.name.contains(query, ignoreCase = true) ||
                    prod.category.contains(query, ignoreCase = true)
            val matchesFilter = when (filter) {
                "Ativos" -> prod.status == "Ativo"
                "Inativos" -> prod.status == "Inativo"
                else -> true
            }
            matchesQuery && matchesFilter
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredAppointments = combine(appointments, _selectedDay) { list, day ->
        list.filter { it.dayNumber == day }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val financialMetrics = combine(transactions, _financePeriod) { txList, period ->
        if (txList.isEmpty()) {
            FinancialMetrics()
        } else {
            val total = txList.sumOf { it.amount }
            val net = total * 0.93 // Deducting typical material/payment processing expenses
            val pix = txList.filter { it.paymentMethod.equals("PIX", true) }.sumOf { it.amount }
            val card = txList.filter { it.paymentMethod.equals("Cartão", true) }.sumOf { it.amount }
            val others = txList.filter { !it.paymentMethod.equals("PIX", true) && !it.paymentMethod.equals("Cartão", true) }.sumOf { it.amount }
            val pixPct = if (total > 0) ((pix / total) * 100).toInt() else 61
            val cardPct = if (total > 0) ((card / total) * 100).toInt() else 30
            val othersPct = if (total > 0) (100 - pixPct - cardPct).coerceAtLeast(0) else 9

            FinancialMetrics(
                totalRevenue = total,
                netRevenue = net,
                salesCount = txList.size.coerceAtLeast(47),
                ticketMedio = if (txList.isNotEmpty()) total / txList.size else 497.70,
                growthPercent = 18,
                salesGrowthPercent = 21,
                netGrowthPercent = 20,
                pixAmount = pix,
                pixPercent = pixPct,
                cardAmount = card,
                cardPercent = cardPct,
                othersAmount = others,
                othersPercent = othersPct
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FinancialMetrics())

    fun navigateTo(screen: CaptioScreen) {
        _currentScreen.value = screen
    }

    fun setSelectedDay(day: String) {
        _selectedDay.value = day
    }

    fun setClientSearchQuery(q: String) {
        _clientSearchQuery.value = q
    }

    fun setClientFilterTab(tab: String) {
        _clientFilterTab.value = tab
    }

    fun setProductSearchQuery(q: String) {
        _productSearchQuery.value = q
    }

    fun setProductFilterTab(tab: String) {
        _productFilterTab.value = tab
    }

    fun setFinancePeriod(period: String) {
        _financePeriod.value = period
    }

    fun setReportsTab(tab: String) {
        _reportsTab.value = tab
    }

    // CRUD Client
    fun addClient(name: String, phone: String, prosthesisType: String, notes: String, status: String) {
        viewModelScope.launch {
            repository.insertClient(
                Client(
                    name = name,
                    phone = phone,
                    prosthesisType = prosthesisType,
                    notes = notes,
                    status = status,
                    servicesCount = 1,
                    lastService = "Novo Cadastro"
                )
            )
        }
    }

    fun deleteClient(client: Client) {
        viewModelScope.launch {
            repository.deleteClient(client)
        }
    }

    // CRUD Appointment
    fun addAppointment(clientName: String, service: String, time: String, dayNumber: String, dayOfWeek: String, status: String) {
        viewModelScope.launch {
            repository.insertAppointment(
                Appointment(
                    clientName = clientName,
                    service = service,
                    time = time,
                    dayNumber = dayNumber,
                    dayOfWeek = dayOfWeek,
                    status = status
                )
            )
        }
    }

    fun updateAppointmentStatus(appointment: Appointment, newStatus: String) {
        viewModelScope.launch {
            repository.updateAppointment(appointment.copy(status = newStatus))
        }
    }

    fun deleteAppointment(appointment: Appointment) {
        viewModelScope.launch {
            repository.deleteAppointment(appointment)
        }
    }

    // CRUD Product
    fun addProduct(name: String, price: Double, category: String, stock: Int, status: String) {
        viewModelScope.launch {
            repository.insertProduct(
                Product(
                    name = name,
                    price = price,
                    category = category,
                    stock = stock,
                    status = status
                )
            )
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.deleteProduct(product)
        }
    }

    // CRUD Sale / Transaction
    fun addTransaction(title: String, amount: Double, paymentMethod: String) {
        viewModelScope.launch {
            repository.insertTransaction(
                Transaction(
                    title = title,
                    amount = amount,
                    paymentMethod = paymentMethod,
                    isIncome = true,
                    period = _financePeriod.value
                )
            )
        }
    }
}
