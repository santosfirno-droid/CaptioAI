package com.example.data.repository

import com.example.data.dao.CaptioDao
import com.example.data.model.Appointment
import com.example.data.model.Client
import com.example.data.model.Product
import com.example.data.model.ServiceRequest
import com.example.data.model.Transaction
import kotlinx.coroutines.flow.Flow

class CaptioRepository(private val dao: CaptioDao) {
    val allClients: Flow<List<Client>> = dao.getAllClients()
    val allAppointments: Flow<List<Appointment>> = dao.getAllAppointments()
    val allServiceRequests: Flow<List<ServiceRequest>> = dao.getAllServiceRequests()
    val allProducts: Flow<List<Product>> = dao.getAllProducts()
    val allTransactions: Flow<List<Transaction>> = dao.getAllTransactions()

    fun getAppointmentsByDay(dayNumber: String): Flow<List<Appointment>> =
        dao.getAppointmentsByDay(dayNumber)

    suspend fun insertClient(client: Client) = dao.insertClient(client)
    suspend fun updateClient(client: Client) = dao.updateClient(client)
    suspend fun deleteClient(client: Client) = dao.deleteClient(client)

    suspend fun insertAppointment(appointment: Appointment) = dao.insertAppointment(appointment)
    suspend fun updateAppointment(appointment: Appointment) = dao.updateAppointment(appointment)
    suspend fun deleteAppointment(appointment: Appointment) = dao.deleteAppointment(appointment)

    suspend fun insertServiceRequest(request: ServiceRequest) = dao.insertServiceRequest(request)
    suspend fun updateServiceRequest(request: ServiceRequest) = dao.updateServiceRequest(request)
    suspend fun deleteServiceRequest(request: ServiceRequest) = dao.deleteServiceRequest(request)

    suspend fun insertProduct(product: Product) = dao.insertProduct(product)
    suspend fun updateProduct(product: Product) = dao.updateProduct(product)
    suspend fun deleteProduct(product: Product) = dao.deleteProduct(product)

    suspend fun insertTransaction(transaction: Transaction) = dao.insertTransaction(transaction)
    suspend fun deleteTransaction(transaction: Transaction) = dao.deleteTransaction(transaction)
}
