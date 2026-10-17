export type Screen =
  | 'HOME'
  | 'CLIENTS'
  | 'ATENDIMENTOS'
  | 'FINANCE'
  | 'PRODUCTS'
  | 'REPORTS'
  | 'MORE'
  | 'PUBLIC_REQUEST';

export interface Client {
  id: number;
  name: string;
  status: 'Ativo' | 'Inativo';
  servicesCount: number;
  phone: string;
  prosthesisType: string;
  lastService: string;
  notes: string;
}

export interface Appointment {
  id: number;
  clientName: string;
  clientPhone: string;
  service: string;
  time: string; // e.g. "08:00", "09:00", "11:00", "14:00"
  durationMinutes: number; // 45, 60, 90, 120
  dayOfWeek: string; // Seg, Ter, Qua, Qui, Sex, Sáb
  dayNumber: string; // 28, 29, 30, 31, 01, 02
  status: 'Confirmado' | 'Pendente' | 'Concluído' | 'Cancelado';
  notes: string;
}

export interface ServiceRequest {
  id: number;
  clientName: string;
  phone: string;
  serviceType: string; // "Instalação de Prótese Capilar" | "Manutenção de Prótese"
  preferredDate: string; // "Esta semana", "Próxima terça", "29/10"
  preferredPeriod: 'Manhã' | 'Tarde';
  notes: string;
  status: 'Pendente' | 'Aceito' | 'Recusado';
  createdAt: string;
}

export interface Product {
  id: number;
  name: string;
  price: number;
  status: 'Ativo' | 'Inativo';
  category: 'Vestuário' | 'Prótese' | 'Insumos' | 'Cosméticos';
  stock: number;
}

export interface Transaction {
  id: number;
  title: string;
  amount: number;
  paymentMethod: 'PIX' | 'Cartão' | 'Outros';
  isIncome: boolean;
  date: string;
  period: string; // "7 dias" | "30 dias" | "90 dias" | "Personalizado"
}

export interface CaptioNotification {
  id: string;
  title: string;
  message: string;
  time: string;
  isRead?: boolean;
}

export interface FinancialMetrics {
  totalRevenue: number;
  netRevenue: number;
  salesCount: number;
  ticketMedio: number;
  growthPercent: number;
  salesGrowthPercent: number;
  netGrowthPercent: number;
  pixAmount: number;
  pixPercent: number;
  cardAmount: number;
  cardPercent: number;
  othersAmount: number;
  othersPercent: number;
}
