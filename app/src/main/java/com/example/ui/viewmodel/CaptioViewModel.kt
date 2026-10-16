package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.CaptioDatabase
import com.example.data.model.Appointment
import com.example.data.model.Client
import com.example.data.model.Product
import com.example.data.model.ServiceRequest
import com.example.data.model.Transaction
import com.example.data.repository.CaptioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class CaptioScreen {
    HOME,
    CLIENTS,
    ATENDIMENTOS,
    FINANCE,
    PRODUCTS,
    REPORTS,
    MORE,
    PUBLIC_REQUEST
}

data class CaptioNotification(
    val id: String,
    val title: String,
    val message: String,
    val time: String,
    val isRead: Boolean = false
)

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

    private val _clientSortOrder = MutableStateFlow("Nome") // "Nome", "Serviços"
    val clientSortOrder: StateFlow<String> = _clientSortOrder.asStateFlow()

    private val _productSearchQuery = MutableStateFlow("")
    val productSearchQuery: StateFlow<String> = _productSearchQuery.asStateFlow()

    private val _productFilterTab = MutableStateFlow("Todos")
    val productFilterTab: StateFlow<String> = _productFilterTab.asStateFlow()

    private val _productCategory = MutableStateFlow("Todas")
    val productCategory: StateFlow<String> = _productCategory.asStateFlow()

    private val _financePeriod = MutableStateFlow("30 dias")
    val financePeriod: StateFlow<String> = _financePeriod.asStateFlow()

    private val _reportsTab = MutableStateFlow("Vendas")
    val reportsTab: StateFlow<String> = _reportsTab.asStateFlow()

    private val _atendimentosTab = MutableStateFlow("Confirmados") // "Confirmados", "Solicitações"
    val atendimentosTab: StateFlow<String> = _atendimentosTab.asStateFlow()

    private val _notifications = MutableStateFlow(
        listOf(
            CaptioNotification("1", "Nova Solicitação", "Lucas Silveira pediu Instalação de Prótese (Manhã).", "Há 5 min"),
            CaptioNotification("2", "Agendamento Próximo", "Rafael Mendes: Consulta • Ajuste às 08:00.", "Hoje"),
            CaptioNotification("3", "Recebimento PIX", "Recebido R$ 1.850,00 ref. Prótese Micropele Silk.", "Há 35 min")
        )
    )
    val notifications: StateFlow<List<CaptioNotification>> = _notifications.asStateFlow()

    init {
        val database = CaptioDatabase.getDatabase(application, viewModelScope)
        repository = CaptioRepository(database.captioDao())
    }

    val clients: StateFlow<List<Client>> = repository.allClients
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val appointments: StateFlow<List<Appointment>> = repository.allAppointments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val serviceRequests: StateFlow<List<ServiceRequest>> = repository.allServiceRequests
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pendingRequestsCount: StateFlow<Int> = serviceRequests.map { list ->
        list.count { it.status == "Pendente" }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 2)

    val products: StateFlow<List<Product>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val transactions: StateFlow<List<Transaction>> = repository.allTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredClients = combine(clients, _clientSearchQuery, _clientFilterTab, _clientSortOrder) { list, query, filter, sort ->
        val filtered = list.filter { client ->
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
        if (sort == "Serviços") {
            filtered.sortedByDescending { it.servicesCount }
        } else {
            filtered.sortedBy { it.name }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredProducts = combine(products, _productSearchQuery, _productFilterTab, _productCategory) { list, query, filter, cat ->
        list.filter { prod ->
            val matchesQuery = query.isBlank() || prod.name.contains(query, ignoreCase = true) ||
                    prod.category.contains(query, ignoreCase = true)
            val matchesFilter = when (filter) {
                "Ativos" -> prod.status == "Ativo"
                "Inativos" -> prod.status == "Inativo"
                else -> true
            }
            val matchesCat = cat == "Todas" || prod.category.equals(cat, ignoreCase = true)
            matchesQuery && matchesFilter && matchesCat
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredAppointments = combine(appointments, _selectedDay) { list, day ->
        list.filter { it.dayNumber == day && it.status != "Cancelado" }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val upcomingAppointments = appointments.map { list ->
        list.filter { it.status == "Confirmado" }.take(3)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val financialMetrics = combine(transactions, _financePeriod) { txList, period ->
        val baselineMultiplier = when (period) {
            "7 dias" -> 0.276
            "90 dias" -> 3.2
            "Personalizado" -> 1.0
            else -> 1.0 // 30 dias
        }
        val salesCountBase = when (period) {
            "7 dias" -> 14
            "90 dias" -> 142
            "Personalizado" -> txList.size.coerceAtLeast(1)
            else -> 47
        }

        val baseSum = if (txList.isEmpty()) 23392.20 else txList.sumOf { it.amount }
        val total = baseSum * baselineMultiplier
        val net = total * 0.93
        val pix = total * 0.61
        val card = total * 0.30
        val others = total * 0.09

        FinancialMetrics(
            totalRevenue = total,
            netRevenue = net,
            salesCount = salesCountBase + (if (period == "30 dias") (txList.size - 9).coerceAtLeast(0) else 0),
            ticketMedio = if (salesCountBase > 0) total / salesCountBase else 497.70,
            growthPercent = 18,
            salesGrowthPercent = 21,
            netGrowthPercent = 20,
            pixAmount = pix,
            pixPercent = 61,
            cardAmount = card,
            cardPercent = 30,
            othersAmount = others,
            othersPercent = 9
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FinancialMetrics())

    fun navigateTo(screen: CaptioScreen) {
        _currentScreen.value = screen
    }

    fun setSelectedDay(day: String) {
        _selectedDay.value = day
    }

    fun setAtendimentosTab(tab: String) {
        _atendimentosTab.value = tab
    }

    fun setClientSearchQuery(q: String) {
        _clientSearchQuery.value = q
    }

    fun setClientFilterTab(tab: String) {
        _clientFilterTab.value = tab
    }

    fun setClientSortOrder(order: String) {
        _clientSortOrder.value = order
    }

    fun setProductSearchQuery(q: String) {
        _productSearchQuery.value = q
    }

    fun setProductFilterTab(tab: String) {
        _productFilterTab.value = tab
    }

    fun setProductCategory(cat: String) {
        _productCategory.value = cat
    }

    fun setFinancePeriod(period: String) {
        _financePeriod.value = period
    }

    fun setReportsTab(tab: String) {
        _reportsTab.value = tab
    }

    fun dismissNotification(id: String) {
        _notifications.value = _notifications.value.filter { it.id != id }
    }

    // CONFLICT CHECK LOGIC (Validação de Sobreposição de Horários)
    fun checkAppointmentConflict(
        dayNumber: String,
        time: String,
        durationMinutes: Int,
        excludeAppointmentId: Long? = null
    ): Appointment? {
        val targetStart = parseTimeToMinutes(time)
        val targetEnd = targetStart + durationMinutes

        return appointments.value.firstOrNull { appt ->
            if (appt.id == excludeAppointmentId) return@firstOrNull false
            if (appt.dayNumber != dayNumber) return@firstOrNull false
            if (appt.status != "Confirmado") return@firstOrNull false

            val apptStart = parseTimeToMinutes(appt.time)
            val apptEnd = apptStart + appt.durationMinutes
            // Overlap condition: targetStart < apptEnd && apptStart < targetEnd
            targetStart < apptEnd && apptStart < targetEnd
        }
    }

    private fun parseTimeToMinutes(timeStr: String): Int {
        val clean = timeStr.trim()
        val parts = clean.split(":")
        if (parts.size >= 2) {
            val h = parts[0].toIntOrNull() ?: 0
            val m = parts[1].toIntOrNull() ?: 0
            return h * 60 + m
        }
        return 8 * 60
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

    fun updateClient(client: Client) {
        viewModelScope.launch {
            repository.updateClient(client)
        }
    }

    fun deleteClient(client: Client) {
        viewModelScope.launch {
            repository.deleteClient(client)
        }
    }

    // CRUD Appointment / Atendimentos
    fun addAppointment(
        clientName: String,
        clientPhone: String,
        service: String,
        time: String,
        durationMinutes: Int,
        dayNumber: String,
        dayOfWeek: String,
        status: String = "Confirmado",
        notes: String = ""
    ) {
        viewModelScope.launch {
            repository.insertAppointment(
                Appointment(
                    clientName = clientName,
                    clientPhone = clientPhone,
                    service = service,
                    time = time,
                    durationMinutes = durationMinutes,
                    dayNumber = dayNumber,
                    dayOfWeek = dayOfWeek,
                    status = status,
                    notes = notes
                )
            )
        }
    }

    fun rescheduleAppointment(
        appointment: Appointment,
        newDayNumber: String,
        newDayOfWeek: String,
        newTime: String,
        newDuration: Int
    ) {
        viewModelScope.launch {
            repository.updateAppointment(
                appointment.copy(
                    dayNumber = newDayNumber,
                    dayOfWeek = newDayOfWeek,
                    time = newTime,
                    durationMinutes = newDuration,
                    status = "Confirmado"
                )
            )
        }
    }

    fun completeAppointment(appointment: Appointment) {
        viewModelScope.launch {
            repository.updateAppointment(appointment.copy(status = "Concluído"))
            // Update client history if client exists
            val existingClient = clients.value.firstOrNull { it.name.equals(appointment.clientName, ignoreCase = true) }
            if (existingClient != null) {
                repository.updateClient(
                    existingClient.copy(
                        servicesCount = existingClient.servicesCount + 1,
                        lastService = appointment.service
                    )
                )
            }
        }
    }

    fun cancelAppointment(appointment: Appointment) {
        viewModelScope.launch {
            repository.updateAppointment(appointment.copy(status = "Cancelado"))
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

    // SERVICE REQUESTS (Solicitações do Link Público)
    fun addPublicServiceRequest(
        clientName: String,
        phone: String,
        serviceType: String,
        preferredDate: String,
        preferredPeriod: String,
        notes: String
    ) {
        viewModelScope.launch {
            repository.insertServiceRequest(
                ServiceRequest(
                    clientName = clientName,
                    phone = phone,
                    serviceType = serviceType,
                    preferredDate = preferredDate,
                    preferredPeriod = preferredPeriod,
                    notes = notes,
                    status = "Pendente",
                    createdAt = "Agora"
                )
            )
            // Add notification to clinic
            _notifications.value = listOf(
                CaptioNotification(
                    id = System.currentTimeMillis().toString(),
                    title = "Nova Solicitação Recebida",
                    message = "$clientName solicitou $serviceType ($preferredPeriod).",
                    time = "Agora"
                )
            ) + _notifications.value
        }
    }

    fun confirmServiceRequest(
        request: ServiceRequest,
        dayNumber: String,
        dayOfWeek: String,
        time: String,
        durationMinutes: Int
    ): String {
        viewModelScope.launch {
            repository.updateServiceRequest(request.copy(status = "Aceito"))
            // Insert confirmed appointment
            repository.insertAppointment(
                Appointment(
                    clientName = request.clientName,
                    clientPhone = request.phone,
                    service = request.serviceType,
                    time = time,
                    durationMinutes = durationMinutes,
                    dayNumber = dayNumber,
                    dayOfWeek = dayOfWeek,
                    status = "Confirmado",
                    notes = request.notes
                )
            )
            // Auto add or update client if not already registered
            val existing = clients.value.firstOrNull { it.phone == request.phone || it.name.equals(request.clientName, ignoreCase = true) }
            if (existing == null) {
                repository.insertClient(
                    Client(
                        name = request.clientName,
                        phone = request.phone,
                        prosthesisType = if (request.serviceType.contains("Instalação", ignoreCase = true)) "Avaliação / Novo Modelo" else "Micropele HD",
                        lastService = request.serviceType,
                        servicesCount = 1,
                        notes = request.notes
                    )
                )
            }
        }

        // Return the exact suggested WhatsApp message
        return "Olá, ${request.clientName}! Seu atendimento de ${request.serviceType} está confirmado para ${dayOfWeek} (dia ${dayNumber}), às ${time}. Qualquer dúvida ou necessidade de alteração, entre em contato conosco."
    }

    fun rejectServiceRequest(request: ServiceRequest) {
        viewModelScope.launch {
            repository.updateServiceRequest(request.copy(status = "Recusado"))
        }
    }

    fun deleteServiceRequest(request: ServiceRequest) {
        viewModelScope.launch {
            repository.deleteServiceRequest(request)
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

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            repository.updateProduct(product)
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
