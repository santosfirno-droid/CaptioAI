package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.CaptioDao
import com.example.data.model.Appointment
import com.example.data.model.Client
import com.example.data.model.Product
import com.example.data.model.ServiceRequest
import com.example.data.model.Transaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Client::class, Appointment::class, Product::class, Transaction::class, ServiceRequest::class],
    version = 2,
    exportSchema = false
)
abstract class CaptioDatabase : RoomDatabase() {
    abstract fun captioDao(): CaptioDao

    companion object {
        @Volatile
        private var INSTANCE: CaptioDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): CaptioDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CaptioDatabase::class.java,
                    "captio_database"
                )
                .addCallback(CaptioDatabaseCallback(scope))
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class CaptioDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.captioDao())
                }
            }
        }

        suspend fun populateDatabase(dao: CaptioDao) {
            // Seed clients matching the original poster UI
            val initialClients = listOf(
                Client(name = "Rafael Mendes", status = "Ativo", servicesCount = 3, phone = "(11) 98123-4567", prosthesisType = "Micropele Silk 0.04mm", lastService = "Consulta • Ajuste"),
                Client(name = "Lucas Ferreira", status = "Ativo", servicesCount = 2, phone = "(11) 97234-5678", prosthesisType = "Híbrida Swiss Lace", lastService = "Aplicação de Prótese"),
                Client(name = "Gabriel Santos", status = "Ativo", servicesCount = 1, phone = "(11) 96345-6789", prosthesisType = "Micropele Injetada", lastService = "Manutenção Completa"),
                Client(name = "Matheus Oliveira", status = "Ativo", servicesCount = 4, phone = "(11) 95456-7890", prosthesisType = "Nanopele Ultrathin", lastService = "Avaliação de Densidade"),
                Client(name = "Daniel Costa", status = "Ativo", servicesCount = 2, phone = "(11) 94567-8901", prosthesisType = "Micropele HD", lastService = "Ajuste e Corte"),
                Client(name = "André Lima", status = "Ativo", servicesCount = 1, phone = "(11) 93678-9012", prosthesisType = "Híbrida Poli + Lace", lastService = "Primeira Aplicação"),
                Client(name = "Bruno Carvalho", status = "Ativo", servicesCount = 5, phone = "(11) 92789-0123", prosthesisType = "Silk Top Injetada", lastService = "Troca de Fita"),
                Client(name = "Thiago Ribeiro", status = "Ativo", servicesCount = 2, phone = "(11) 91890-1234", prosthesisType = "Micropele 0.06mm", lastService = "Manutenção Periódica"),
                Client(name = "Marcelo Souza", status = "Inativo", servicesCount = 1, phone = "(11) 90901-2345", prosthesisType = "Lace Front", lastService = "Higienização"),
                Client(name = "Rodrigo Almeida", status = "Inativo", servicesCount = 2, phone = "(11) 98912-3456", prosthesisType = "Micropele Slim", lastService = "Remoção e Limpeza")
            )
            dao.insertClients(initialClients)

            // Seed appointments with duration and contact
            val initialAppointments = listOf(
                Appointment(clientName = "Rafael Mendes", clientPhone = "(11) 98123-4567", service = "Consulta • Ajuste", time = "08:00", durationMinutes = 45, dayOfWeek = "Ter", dayNumber = "29", status = "Confirmado"),
                Appointment(clientName = "Lucas Ferreira", clientPhone = "(11) 97234-5678", service = "Instalação de Prótese Capilar", time = "09:00", durationMinutes = 90, dayOfWeek = "Ter", dayNumber = "29", status = "Confirmado"),
                Appointment(clientName = "Gabriel Santos", clientPhone = "(11) 96345-6789", service = "Manutenção de Prótese", time = "11:00", durationMinutes = 60, dayOfWeek = "Ter", dayNumber = "29", status = "Confirmado"),
                Appointment(clientName = "Matheus Oliveira", clientPhone = "(11) 95456-7890", service = "Avaliação e Medição", time = "14:00", durationMinutes = 60, dayOfWeek = "Ter", dayNumber = "29", status = "Confirmado"),
                Appointment(clientName = "Daniel Costa", clientPhone = "(11) 94567-8901", service = "Higienização • Corte", time = "10:00", durationMinutes = 60, dayOfWeek = "Seg", dayNumber = "28", status = "Concluído"),
                Appointment(clientName = "André Lima", clientPhone = "(11) 93678-9012", service = "Instalação de Prótese Capilar", time = "14:00", durationMinutes = 120, dayOfWeek = "Qua", dayNumber = "30", status = "Confirmado"),
                Appointment(clientName = "Bruno Carvalho", clientPhone = "(11) 92789-0123", service = "Manutenção de Prótese", time = "09:30", durationMinutes = 60, dayOfWeek = "Qui", dayNumber = "31", status = "Confirmado"),
                Appointment(clientName = "Thiago Ribeiro", clientPhone = "(11) 91890-1234", service = "Consulta e Escolha", time = "11:00", durationMinutes = 60, dayOfWeek = "Sex", dayNumber = "01", status = "Confirmado")
            )
            dao.insertAppointments(initialAppointments)

            // Seed received client requests from public link
            val initialRequests = listOf(
                ServiceRequest(
                    clientName = "Lucas Silveira",
                    phone = "(11) 98822-1133",
                    serviceType = "Instalação de Prótese Capilar",
                    preferredDate = "Esta semana",
                    preferredPeriod = "Manhã",
                    notes = "Tenho calvície grau 5, gostaria de avaliar um modelo micropele natural.",
                    status = "Pendente",
                    createdAt = "Hoje, às 10:15"
                ),
                ServiceRequest(
                    clientName = "Carlos Eduardo",
                    phone = "(11) 97711-2244",
                    serviceType = "Manutenção de Prótese",
                    preferredDate = "Próxima terça ou quarta",
                    preferredPeriod = "Tarde",
                    notes = "Uso micropele silk, preciso de troca de fita e corte de barba.",
                    status = "Pendente",
                    createdAt = "Hoje, às 11:40"
                ),
                ServiceRequest(
                    clientName = "Felipe Rocha",
                    phone = "(11) 99344-5566",
                    serviceType = "Instalação de Prótese Capilar",
                    preferredDate = "Sexta-feira",
                    preferredPeriod = "Manhã",
                    notes = "Primeira prótese, quero tirar dúvidas sobre fixação.",
                    status = "Aceito",
                    createdAt = "Ontem, às 16:20"
                )
            )
            dao.insertServiceRequests(initialRequests)

            // Seed products matching the poster exactly
            val initialProducts = listOf(
                Product(name = "Camiseta Feminina Proteção Solar UV", price = 37.90, status = "Ativo", category = "Vestuário", stock = 24),
                Product(name = "Camiseta Los Angeles Lakers Masculina", price = 69.99, status = "Ativo", category = "Vestuário", stock = 18),
                Product(name = "Boné CAPTIO", price = 59.90, status = "Ativo", category = "Vestuário", stock = 35),
                Product(name = "Camiseta Masculina Premium", price = 49.90, status = "Ativo", category = "Vestuário", stock = 40),
                Product(name = "Prótese Capilar Micropele HD Silk 0.04mm", price = 1850.00, status = "Ativo", category = "Prótese", stock = 8),
                Product(name = "Fita Adesiva Ultra Hold Walker Tape (3 Jardas)", price = 89.90, status = "Ativo", category = "Insumos", stock = 42),
                Product(name = "Solvente Removedor C-22 Citrus 118ml", price = 119.00, status = "Ativo", category = "Insumos", stock = 26),
                Product(name = "Shampoo Neutro Purificante Pós-Prótese", price = 45.00, status = "Ativo", category = "Cosméticos", stock = 30),
                Product(name = "Cola Líquida Ghost Bond XL 38ml", price = 165.00, status = "Ativo", category = "Insumos", stock = 15),
                Product(name = "Protetor Térmico Antiumidade CAPTIO Care", price = 58.00, status = "Inativo", category = "Cosméticos", stock = 0)
            )
            dao.insertProducts(initialProducts)

            // Seed financial transactions matching poster statistics (Total: R$ 23.392,20)
            val initialTransactions = listOf(
                Transaction(title = "Aplicação Prótese Micropele - Rafael M.", amount = 1850.00, paymentMethod = "PIX", isIncome = true, period = "30 dias"),
                Transaction(title = "Prótese Híbrida Lace - Lucas F.", amount = 2200.00, paymentMethod = "Cartão", isIncome = true, period = "30 dias"),
                Transaction(title = "Manutenção Periódica - Gabriel S.", amount = 280.00, paymentMethod = "PIX", isIncome = true, period = "30 dias"),
                Transaction(title = "Combo Insumos + Boné CAPTIO - Matheus O.", amount = 328.80, paymentMethod = "Cartão", isIncome = true, period = "30 dias"),
                Transaction(title = "Manutenção e Troca de Fita - Daniel C.", amount = 350.00, paymentMethod = "PIX", isIncome = true, period = "30 dias"),
                Transaction(title = "Aplicação Micropele HD - André L.", amount = 1950.00, paymentMethod = "PIX", isIncome = true, period = "30 dias"),
                Transaction(title = "Lote de Fitas e Insumos Mensais", amount = 1040.00, paymentMethod = "Outros", isIncome = true, period = "30 dias"),
                Transaction(title = "Recebimentos Diversos PIX Semanais", amount = 9809.80, paymentMethod = "PIX", isIncome = true, period = "30 dias"),
                Transaction(title = "Vendas Balcão Cartão de Crédito", amount = 4583.60, paymentMethod = "Cartão", isIncome = true, period = "30 dias")
            )
            dao.insertTransactions(initialTransactions)
        }
    }
}
