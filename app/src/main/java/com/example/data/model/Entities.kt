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
    val service: String, // e.g. "Consulta • Ajuste", "Aplicação de Prótese", "Manutenção", "Avaliação"
    val time: String, // e.g. "08:00", "09:00", "10:00", "12:00"
    val dayOfWeek: String = "Ter", // Seg, Ter, Qua, Qui, Sex
    val dayNumber: String = "29", // 28, 29, 30, 31, 01
    val status: String = "Confirmado", // "Confirmado", "Pendente", "Concluído"
    val notes: String = ""
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
