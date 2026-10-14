package com.example

import com.example.data.model.Appointment
import com.example.data.model.Client
import com.example.data.model.Product
import com.example.data.model.Transaction
import com.example.ui.viewmodel.FinancialMetrics
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun client_model_creation() {
        val client = Client(
            name = "Rafael Mendes",
            status = "Ativo",
            servicesCount = 3,
            phone = "(11) 98123-4567",
            prosthesisType = "Micropele Silk 0.04mm"
        )
        assertEquals("Rafael Mendes", client.name)
        assertEquals("Ativo", client.status)
        assertEquals(3, client.servicesCount)
    }

    @Test
    fun appointment_status_and_time() {
        val appt = Appointment(
            clientName = "Lucas Ferreira",
            service = "Aplicação de Prótese",
            time = "09:00",
            dayOfWeek = "Ter",
            dayNumber = "29",
            status = "Confirmado"
        )
        assertEquals("Confirmado", appt.status)
        assertEquals("09:00", appt.time)
    }

    @Test
    fun financial_calculations() {
        val metrics = FinancialMetrics(
            totalRevenue = 23392.20,
            netRevenue = 21754.90,
            salesCount = 47,
            pixAmount = 14239.80,
            pixPercent = 61,
            cardAmount = 7112.40,
            cardPercent = 30,
            othersAmount = 1040.00,
            othersPercent = 9
        )
        assertEquals(23392.20, metrics.totalRevenue, 0.01)
        assertEquals(61, metrics.pixPercent)
        assertEquals(30, metrics.cardPercent)
        assertEquals(9, metrics.othersPercent)
        assertTrue(metrics.totalRevenue > metrics.netRevenue)
    }
}
