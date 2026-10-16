package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clients")
data class Client(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val status: String = "Ativo", // "Ativo" or "Inativo"
    val servicesCount: Int = 1,
    val phone: String = "(11) 98765-4321",
    val prosthesisType: String = "Micropele HD",
    val lastService: String = "Manutenção e Corte",
    val notes: String = ""
)

@Entity(tableName = "appointments")
data class Appointment(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val clientName: String,
    val clientPhone: String = "(11) 98765-4321",
    val service: String, // e.g. "Instalação de Prótese Capilar", "Manutenção de Prótese", "Consulta • Ajuste", "Avaliação"
    val time: String, // e.g. "08:00", "09:00", "10:00", "12:00", "14:00"
    val durationMinutes: Int = 60, // 45, 60, 90, 120 min
    val dayOfWeek: String = "Ter", // Seg, Ter, Qua, Qui, Sex, Sáb
    val dayNumber: String = "29", // 28, 29, 30, 31, 01
    val status: String = "Confirmado", // "Confirmado", "Pendente", "Concluído", "Cancelado"
    val notes: String = ""
)

@Entity(tableName = "service_requests")
data class ServiceRequest(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val clientName: String,
    val phone: String,
    val serviceType: String, // "Instalação de Prótese Capilar" or "Manutenção de Prótese"
    val preferredDate: String, // e.g. "Esta semana", "Próxima terça", "29/10"
    val preferredPeriod: String, // "Manhã" or "Tarde"
    val notes: String = "",
    val status: String = "Pendente", // "Pendente", "Aceito", "Recusado"
    val createdAt: String = "Hoje, às 10:15"
)

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val price: Double,
    val status: String = "Ativo", // "Ativo" or "Inativo"
    val category: String = "Vestuário", // "Vestuário", "Prótese", "Insumos", "Cosméticos"
    val stock: Int = 15
)

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val amount: Double,
    val paymentMethod: String, // "PIX", "Cartão", "Outros"
    val isIncome: Boolean = true,
    val date: String = "2026-10-29",
    val period: String = "30 dias"
)
