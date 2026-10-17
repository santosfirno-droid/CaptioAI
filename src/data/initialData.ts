import { Appointment, Client, Product, ServiceRequest, Transaction, CaptioNotification } from '../types';

export const INITIAL_CLIENTS: Client[] = [
  { id: 1, name: "Rafael Mendes", status: "Ativo", servicesCount: 3, phone: "(11) 98123-4567", prosthesisType: "Micropele Silk 0.04mm", lastService: "Consulta • Ajuste", notes: "Prefere manutenção a cada 20 dias" },
  { id: 2, name: "Lucas Ferreira", status: "Ativo", servicesCount: 2, phone: "(11) 97234-5678", prosthesisType: "Híbrida Swiss Lace", lastService: "Aplicação de Prótese", notes: "Uso de fita Ultra Hold" },
  { id: 3, name: "Gabriel Santos", status: "Ativo", servicesCount: 1, phone: "(11) 96345-6789", prosthesisType: "Micropele Injetada", lastService: "Manutenção Completa", notes: "Sensibilidade a solvente forte" },
  { id: 4, name: "Matheus Oliveira", status: "Ativo", servicesCount: 4, phone: "(11) 95456-7890", prosthesisType: "Nanopele Ultrathin", lastService: "Avaliação de Densidade", notes: "Cliente VIP" },
  { id: 5, name: "Daniel Costa", status: "Ativo", servicesCount: 2, phone: "(11) 94567-8901", prosthesisType: "Micropele HD", lastService: "Ajuste e Corte", notes: "" },
  { id: 6, name: "André Lima", status: "Ativo", servicesCount: 1, phone: "(11) 93678-9012", prosthesisType: "Híbrida Poli + Lace", lastService: "Primeira Aplicação", notes: "Novo usuário" },
  { id: 7, name: "Bruno Carvalho", status: "Ativo", servicesCount: 5, phone: "(11) 92789-0123", prosthesisType: "Silk Top Injetada", lastService: "Troca de Fita", notes: "" },
  { id: 8, name: "Thiago Ribeiro", status: "Ativo", servicesCount: 2, phone: "(11) 91890-1234", prosthesisType: "Micropele 0.06mm", lastService: "Manutenção Periódica", notes: "" },
  { id: 9, name: "Marcelo Souza", status: "Inativo", servicesCount: 1, phone: "(11) 90901-2345", prosthesisType: "Lace Front", lastService: "Higienização", notes: "Não compareceu na última" },
  { id: 10, name: "Rodrigo Almeida", status: "Inativo", servicesCount: 2, phone: "(11) 98912-3456", prosthesisType: "Micropele Slim", lastService: "Remoção e Limpeza", notes: "Mudou de cidade" }
];

export const INITIAL_APPOINTMENTS: Appointment[] = [
  { id: 1, clientName: "Rafael Mendes", clientPhone: "(11) 98123-4567", service: "Consulta • Ajuste", time: "08:00", durationMinutes: 45, dayOfWeek: "Ter", dayNumber: "29", status: "Confirmado", notes: "Revisão de fixação" },
  { id: 2, clientName: "Lucas Ferreira", clientPhone: "(11) 97234-5678", service: "Instalação de Prótese Capilar", time: "09:00", durationMinutes: 90, dayOfWeek: "Ter", dayNumber: "29", status: "Confirmado", notes: "Micropele nova pronta" },
  { id: 3, clientName: "Gabriel Santos", clientPhone: "(11) 96345-6789", service: "Manutenção de Prótese", time: "11:00", durationMinutes: 60, dayOfWeek: "Ter", dayNumber: "29", status: "Confirmado", notes: "Troca de fita e corte" },
  { id: 4, clientName: "Matheus Oliveira", clientPhone: "(11) 95456-7890", service: "Avaliação e Medição", time: "14:00", durationMinutes: 60, dayOfWeek: "Ter", dayNumber: "29", status: "Confirmado", notes: "Molde capilar" },
  { id: 5, clientName: "Daniel Costa", clientPhone: "(11) 94567-8901", service: "Higienização • Corte", time: "10:00", durationMinutes: 60, dayOfWeek: "Seg", dayNumber: "28", status: "Concluído", notes: "Finalizado com sucesso" },
  { id: 6, clientName: "André Lima", clientPhone: "(11) 93678-9012", service: "Instalação de Prótese Capilar", time: "14:00", durationMinutes: 120, dayOfWeek: "Qua", dayNumber: "30", status: "Confirmado", notes: "Aplicação completa" },
  { id: 7, clientName: "Bruno Carvalho", clientPhone: "(11) 92789-0123", service: "Manutenção de Prótese", time: "09:30", durationMinutes: 60, dayOfWeek: "Qui", dayNumber: "31", status: "Confirmado", notes: "Manutenção rotineira" },
  { id: 8, clientName: "Thiago Ribeiro", clientPhone: "(11) 91890-1234", service: "Consulta e Escolha", time: "11:00", durationMinutes: 60, dayOfWeek: "Sex", dayNumber: "01", status: "Confirmado", notes: "Avaliação de base" }
];

export const INITIAL_REQUESTS: ServiceRequest[] = [
  {
    id: 1,
    clientName: "Lucas Silveira",
    phone: "(11) 98822-1133",
    serviceType: "Instalação de Prótese Capilar",
    preferredDate: "Esta semana",
    preferredPeriod: "Manhã",
    notes: "Tenho calvície grau 5, gostaria de avaliar um modelo micropele natural.",
    status: "Pendente",
    createdAt: "Hoje, às 10:15"
  },
  {
    id: 2,
    clientName: "Carlos Eduardo",
    phone: "(11) 97711-2244",
    serviceType: "Manutenção de Prótese",
    preferredDate: "Próxima terça ou quarta",
    preferredPeriod: "Tarde",
    notes: "Uso micropele silk, preciso de troca de fita e corte de barba.",
    status: "Pendente",
    createdAt: "Hoje, às 11:40"
  },
  {
    id: 3,
    clientName: "Felipe Rocha",
    phone: "(11) 99344-5566",
    serviceType: "Instalação de Prótese Capilar",
    preferredDate: "Sexta-feira",
    preferredPeriod: "Manhã",
    notes: "Primeira prótese, quero tirar dúvidas sobre fixação.",
    status: "Aceito",
    createdAt: "Ontem, às 16:20"
  }
];

export const INITIAL_PRODUCTS: Product[] = [
  { id: 1, name: "Camiseta Feminina Proteção Solar UV", price: 37.90, status: "Ativo", category: "Vestuário", stock: 24 },
  { id: 2, name: "Camiseta Los Angeles Lakers Masculina", price: 69.99, status: "Ativo", category: "Vestuário", stock: 18 },
  { id: 3, name: "Boné CAPTIO", price: 59.90, status: "Ativo", category: "Vestuário", stock: 35 },
  { id: 4, name: "Camiseta Masculina Premium", price: 49.90, status: "Ativo", category: "Vestuário", stock: 40 },
  { id: 5, name: "Prótese Capilar Micropele HD Silk 0.04mm", price: 1850.00, status: "Ativo", category: "Prótese", stock: 8 },
  { id: 6, name: "Fita Adesiva Ultra Hold Walker Tape (3 Jardas)", price: 89.90, status: "Ativo", category: "Insumos", stock: 42 },
  { id: 7, name: "Solvente Removedor C-22 Citrus 118ml", price: 119.00, status: "Ativo", category: "Insumos", stock: 26 },
  { id: 8, name: "Shampoo Neutro Purificante Pós-Prótese", price: 45.00, status: "Ativo", category: "Cosméticos", stock: 30 },
  { id: 9, name: "Cola Líquida Ghost Bond XL 38ml", price: 165.00, status: "Ativo", category: "Insumos", stock: 15 },
  { id: 10, name: "Protetor Térmico Antiumidade CAPTIO Care", price: 58.00, status: "Inativo", category: "Cosméticos", stock: 0 }
];

export const INITIAL_TRANSACTIONS: Transaction[] = [
  { id: 1, title: "Aplicação Prótese Micropele - Rafael M.", amount: 1850.00, paymentMethod: "PIX", isIncome: true, date: "2026-10-29", period: "30 dias" },
  { id: 2, title: "Prótese Híbrida Lace - Lucas F.", amount: 2200.00, paymentMethod: "Cartão", isIncome: true, date: "2026-10-28", period: "30 dias" },
  { id: 3, title: "Manutenção Periódica - Gabriel S.", amount: 280.00, paymentMethod: "PIX", isIncome: true, date: "2026-10-28", period: "30 dias" },
  { id: 4, title: "Combo Insumos + Boné CAPTIO - Matheus O.", amount: 328.80, paymentMethod: "Cartão", isIncome: true, date: "2026-10-27", period: "30 dias" },
  { id: 5, title: "Manutenção e Troca de Fita - Daniel C.", amount: 350.00, paymentMethod: "PIX", isIncome: true, date: "2026-10-26", period: "30 dias" },
  { id: 6, title: "Aplicação Micropele HD - André L.", amount: 1950.00, paymentMethod: "PIX", isIncome: true, date: "2026-10-25", period: "30 dias" },
  { id: 7, title: "Lote de Fitas e Insumos Mensais", amount: 1040.00, paymentMethod: "Outros", isIncome: true, date: "2026-10-24", period: "30 dias" },
  { id: 8, title: "Recebimentos Diversos PIX Semanais", amount: 9809.80, paymentMethod: "PIX", isIncome: true, date: "2026-10-20", period: "30 dias" },
  { id: 9, title: "Vendas Balcão Cartão de Crédito", amount: 4583.60, paymentMethod: "Cartão", isIncome: true, date: "2026-10-18", period: "30 dias" }
];

export const INITIAL_NOTIFICATIONS: CaptioNotification[] = [
  { id: "1", title: "Nova Solicitação", message: "Lucas Silveira pediu Instalação de Prótese (Manhã).", time: "Há 5 min", isRead: false },
  { id: "2", title: "Agendamento Próximo", message: "Rafael Mendes: Consulta • Ajuste às 08:00.", time: "Hoje", isRead: false },
  { id: "3", title: "Recebimento PIX", message: "Recebido R$ 1.850,00 ref. Prótese Micropele Silk.", time: "Há 35 min", isRead: false }
];
