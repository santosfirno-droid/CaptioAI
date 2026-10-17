import React, { createContext, useContext, useState, useEffect, useMemo } from 'react';
import {
  Screen,
  Client,
  Appointment,
  ServiceRequest,
  Product,
  Transaction,
  CaptioNotification,
  FinancialMetrics,
} from '../types';
import {
  INITIAL_CLIENTS,
  INITIAL_APPOINTMENTS,
  INITIAL_REQUESTS,
  INITIAL_PRODUCTS,
  INITIAL_TRANSACTIONS,
  INITIAL_NOTIFICATIONS,
} from '../data/initialData';

interface CaptioContextType {
  currentScreen: Screen;
  setCurrentScreen: (screen: Screen) => void;
  selectedDay: string;
  setSelectedDay: (day: string) => void;
  clientSearchQuery: string;
  setClientSearchQuery: (query: string) => void;
  clientFilterTab: string;
  setClientFilterTab: (tab: string) => void;
  clientSortOrder: string;
  setClientSortOrder: (order: string) => void;
  productSearchQuery: string;
  setProductSearchQuery: (query: string) => void;
  productFilterTab: string;
  setProductFilterTab: (tab: string) => void;
  productCategory: string;
  setProductCategory: (category: string) => void;
  financePeriod: string;
  setFinancePeriod: (period: string) => void;
  reportsTab: string;
  setReportsTab: (tab: string) => void;
  atendimentosTab: string;
  setAtendimentosTab: (tab: string) => void;

  notifications: CaptioNotification[];
  dismissNotification: (id: string) => void;
  addNotification: (title: string, message: string) => void;

  clients: Client[];
  filteredClients: Client[];
  addClient: (name: string, phone: string, prosthesisType: string, notes: string, status?: 'Ativo' | 'Inativo') => void;
  updateClient: (client: Client) => void;
  deleteClient: (id: number) => void;

  appointments: Appointment[];
  filteredAppointments: Appointment[];
  upcomingAppointments: Appointment[];
  addAppointment: (
    clientName: string,
    clientPhone: string,
    service: string,
    time: string,
    durationMinutes: number,
    dayNumber: string,
    dayOfWeek: string,
    status?: 'Confirmado' | 'Pendente' | 'Concluído' | 'Cancelado',
    notes?: string
  ) => void;
  rescheduleAppointment: (
    appointmentId: number,
    newDayNumber: string,
    newDayOfWeek: string,
    newTime: string,
    newDuration: number
  ) => void;
  completeAppointment: (appointment: Appointment) => void;
  cancelAppointment: (appointmentId: number) => void;
  deleteAppointment: (appointmentId: number) => void;
  checkAppointmentConflict: (
    dayNumber: string,
    time: string,
    durationMinutes: number,
    excludeAppointmentId?: number
  ) => Appointment | null;

  serviceRequests: ServiceRequest[];
  pendingRequestsCount: number;
  addPublicServiceRequest: (
    clientName: string,
    phone: string,
    serviceType: string,
    preferredDate: string,
    preferredPeriod: 'Manhã' | 'Tarde',
    notes: string
  ) => void;
  confirmServiceRequest: (
    requestId: number,
    dayNumber: string,
    dayOfWeek: string,
    time: string,
    durationMinutes: number
  ) => string;
  rejectServiceRequest: (requestId: number) => void;
  deleteServiceRequest: (requestId: number) => void;

  products: Product[];
  filteredProducts: Product[];
  addProduct: (name: string, price: number, category: 'Vestuário' | 'Prótese' | 'Insumos' | 'Cosméticos', stock: number, status?: 'Ativo' | 'Inativo') => void;
  updateProduct: (product: Product) => void;
  deleteProduct: (id: number) => void;

  transactions: Transaction[];
  financialMetrics: FinancialMetrics;
  addTransaction: (title: string, amount: number, paymentMethod: 'PIX' | 'Cartão' | 'Outros') => void;

  resetDemoData: () => void;
}

const CaptioContext = createContext<CaptioContextType | undefined>(undefined);

function parseTimeToMinutes(timeStr: string): number {
  const parts = timeStr.trim().split(':');
  if (parts.length >= 2) {
    const h = parseInt(parts[0], 10) || 0;
    const m = parseInt(parts[1], 10) || 0;
    return h * 60 + m;
  }
  return 8 * 60;
}

export const CaptioProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [currentScreen, setCurrentScreen] = useState<Screen>('HOME');
  const [selectedDay, setSelectedDay] = useState<string>('29');
  const [clientSearchQuery, setClientSearchQuery] = useState('');
  const [clientFilterTab, setClientFilterTab] = useState('Todos');
  const [clientSortOrder, setClientSortOrder] = useState('Nome');
  const [productSearchQuery, setProductSearchQuery] = useState('');
  const [productFilterTab, setProductFilterTab] = useState('Todos');
  const [productCategory, setProductCategory] = useState('Todas');
  const [financePeriod, setFinancePeriod] = useState('30 dias');
  const [reportsTab, setReportsTab] = useState('Vendas');
  const [atendimentosTab, setAtendimentosTab] = useState('Confirmados');

  const [notifications, setNotifications] = useState<CaptioNotification[]>(() => {
    const saved = localStorage.getItem('captio_notifications');
    return saved ? JSON.parse(saved) : INITIAL_NOTIFICATIONS;
  });

  const [clients, setClients] = useState<Client[]>(() => {
    const saved = localStorage.getItem('captio_clients');
    return saved ? JSON.parse(saved) : INITIAL_CLIENTS;
  });

  const [appointments, setAppointments] = useState<Appointment[]>(() => {
    const saved = localStorage.getItem('captio_appointments');
    return saved ? JSON.parse(saved) : INITIAL_APPOINTMENTS;
  });

  const [serviceRequests, setServiceRequests] = useState<ServiceRequest[]>(() => {
    const saved = localStorage.getItem('captio_requests');
    return saved ? JSON.parse(saved) : INITIAL_REQUESTS;
  });

  const [products, setProducts] = useState<Product[]>(() => {
    const saved = localStorage.getItem('captio_products');
    return saved ? JSON.parse(saved) : INITIAL_PRODUCTS;
  });

  const [transactions, setTransactions] = useState<Transaction[]>(() => {
    const saved = localStorage.getItem('captio_transactions');
    return saved ? JSON.parse(saved) : INITIAL_TRANSACTIONS;
  });

  // Local storage syncing
  useEffect(() => {
    localStorage.setItem('captio_notifications', JSON.stringify(notifications));
  }, [notifications]);
  useEffect(() => {
    localStorage.setItem('captio_clients', JSON.stringify(clients));
  }, [clients]);
  useEffect(() => {
    localStorage.setItem('captio_appointments', JSON.stringify(appointments));
  }, [appointments]);
  useEffect(() => {
    localStorage.setItem('captio_requests', JSON.stringify(serviceRequests));
  }, [serviceRequests]);
  useEffect(() => {
    localStorage.setItem('captio_products', JSON.stringify(products));
  }, [products]);
  useEffect(() => {
    localStorage.setItem('captio_transactions', JSON.stringify(transactions));
  }, [transactions]);

  const dismissNotification = (id: string) => {
    setNotifications((prev) => prev.filter((n) => n.id !== id));
  };

  const addNotification = (title: string, message: string) => {
    const newNotif: CaptioNotification = {
      id: Date.now().toString(),
      title,
      message,
      time: 'Agora',
      isRead: false,
    };
    setNotifications((prev) => [newNotif, ...prev]);
  };

  const pendingRequestsCount = useMemo(() => {
    return serviceRequests.filter((r) => r.status === 'Pendente').length;
  }, [serviceRequests]);

  const filteredClients = useMemo(() => {
    const q = clientSearchQuery.trim().toLowerCase();
    const filtered = clients.filter((c) => {
      const matchQ =
        !q ||
        c.name.toLowerCase().includes(q) ||
        c.prosthesisType.toLowerCase().includes(q) ||
        c.phone.includes(q);
      const matchFilter =
        clientFilterTab === 'Ativos'
          ? c.status === 'Ativo'
          : clientFilterTab === 'Inativos'
          ? c.status === 'Inativo'
          : true;
      return matchQ && matchFilter;
    });

    if (clientSortOrder === 'Serviços') {
      return [...filtered].sort((a, b) => b.servicesCount - a.servicesCount);
    }
    return [...filtered].sort((a, b) => a.name.localeCompare(b.name));
  }, [clients, clientSearchQuery, clientFilterTab, clientSortOrder]);

  const filteredProducts = useMemo(() => {
    const q = productSearchQuery.trim().toLowerCase();
    return products.filter((p) => {
      const matchQ =
        !q || p.name.toLowerCase().includes(q) || p.category.toLowerCase().includes(q);
      const matchFilter =
        productFilterTab === 'Ativos'
          ? p.status === 'Ativo'
          : productFilterTab === 'Inativos'
          ? p.status === 'Inativo'
          : true;
      const matchCat = productCategory === 'Todas' || p.category.toLowerCase() === productCategory.toLowerCase();
      return matchQ && matchFilter && matchCat;
    });
  }, [products, productSearchQuery, productFilterTab, productCategory]);

  const filteredAppointments = useMemo(() => {
    return appointments.filter(
      (a) => a.dayNumber === selectedDay && a.status !== 'Cancelado'
    );
  }, [appointments, selectedDay]);

  const upcomingAppointments = useMemo(() => {
    return appointments.filter((a) => a.status === 'Confirmado').slice(0, 3);
  }, [appointments]);

  const financialMetrics = useMemo<FinancialMetrics>(() => {
    const baselineMultiplier =
      financePeriod === '7 dias'
        ? 0.276
        : financePeriod === '90 dias'
        ? 3.2
        : 1.0;

    const salesCountBase =
      financePeriod === '7 dias'
        ? 14
        : financePeriod === '90 dias'
        ? 142
        : financePeriod === 'Personalizado'
        ? Math.max(transactions.length, 1)
        : 47;

    const baseSum = transactions.length === 0 ? 23392.2 : transactions.reduce((acc, t) => acc + t.amount, 0);
    const total = baseSum * baselineMultiplier;
    const net = total * 0.93;
    const pix = total * 0.61;
    const card = total * 0.3;
    const others = total * 0.09;

    return {
      totalRevenue: total,
      netRevenue: net,
      salesCount: salesCountBase + (financePeriod === '30 dias' ? Math.max(0, transactions.length - 9) : 0),
      ticketMedio: salesCountBase > 0 ? total / salesCountBase : 497.7,
      growthPercent: 18,
      salesGrowthPercent: 21,
      netGrowthPercent: 20,
      pixAmount: pix,
      pixPercent: 61,
      cardAmount: card,
      cardPercent: 30,
      othersAmount: others,
      othersPercent: 9,
    };
  }, [transactions, financePeriod]);

  // Conflict checking
  const checkAppointmentConflict = (
    dayNumber: string,
    time: string,
    durationMinutes: number,
    excludeAppointmentId?: number
  ): Appointment | null => {
    const targetStart = parseTimeToMinutes(time);
    const targetEnd = targetStart + durationMinutes;

    for (const appt of appointments) {
      if (excludeAppointmentId && appt.id === excludeAppointmentId) continue;
      if (appt.dayNumber !== dayNumber) continue;
      if (appt.status !== 'Confirmado') continue;

      const apptStart = parseTimeToMinutes(appt.time);
      const apptEnd = apptStart + appt.durationMinutes;

      // Overlap condition
      if (targetStart < apptEnd && apptStart < targetEnd) {
        return appt;
      }
    }
    return null;
  };

  // CRUD Client
  const addClient = (
    name: string,
    phone: string,
    prosthesisType: string,
    notes: string,
    status: 'Ativo' | 'Inativo' = 'Ativo'
  ) => {
    const newClient: Client = {
      id: Date.now(),
      name,
      phone,
      prosthesisType,
      notes,
      status,
      servicesCount: 1,
      lastService: 'Novo Cadastro',
    };
    setClients((prev) => [newClient, ...prev]);
  };

  const updateClient = (updated: Client) => {
    setClients((prev) => prev.map((c) => (c.id === updated.id ? updated : c)));
  };

  const deleteClient = (id: number) => {
    setClients((prev) => prev.filter((c) => c.id !== id));
  };

  // CRUD Appointment
  const addAppointment = (
    clientName: string,
    clientPhone: string,
    service: string,
    time: string,
    durationMinutes: number,
    dayNumber: string,
    dayOfWeek: string,
    status: 'Confirmado' | 'Pendente' | 'Concluído' | 'Cancelado' = 'Confirmado',
    notes: string = ''
  ) => {
    const newAppt: Appointment = {
      id: Date.now(),
      clientName,
      clientPhone,
      service,
      time,
      durationMinutes,
      dayNumber,
      dayOfWeek,
      status,
      notes,
    };
    setAppointments((prev) => [...prev, newAppt]);
  };

  const rescheduleAppointment = (
    appointmentId: number,
    newDayNumber: string,
    newDayOfWeek: string,
    newTime: string,
    newDuration: number
  ) => {
    setAppointments((prev) =>
      prev.map((a) =>
        a.id === appointmentId
          ? {
              ...a,
              dayNumber: newDayNumber,
              dayOfWeek: newDayOfWeek,
              time: newTime,
              durationMinutes: newDuration,
              status: 'Confirmado',
            }
          : a
      )
    );
  };

  const completeAppointment = (appointment: Appointment) => {
    setAppointments((prev) =>
      prev.map((a) => (a.id === appointment.id ? { ...a, status: 'Concluído' } : a))
    );
    // Increment client history if matches
    setClients((prev) =>
      prev.map((c) =>
        c.name.toLowerCase() === appointment.clientName.toLowerCase()
          ? {
              ...c,
              servicesCount: c.servicesCount + 1,
              lastService: appointment.service,
            }
          : c
      )
    );
  };

  const cancelAppointment = (appointmentId: number) => {
    setAppointments((prev) =>
      prev.map((a) => (a.id === appointmentId ? { ...a, status: 'Cancelado' } : a))
    );
  };

  const deleteAppointment = (appointmentId: number) => {
    setAppointments((prev) => prev.filter((a) => a.id !== appointmentId));
  };

  // Service Requests
  const addPublicServiceRequest = (
    clientName: string,
    phone: string,
    serviceType: string,
    preferredDate: string,
    preferredPeriod: 'Manhã' | 'Tarde',
    notes: string
  ) => {
    const newReq: ServiceRequest = {
      id: Date.now(),
      clientName,
      phone,
      serviceType,
      preferredDate,
      preferredPeriod,
      notes,
      status: 'Pendente',
      createdAt: 'Agora',
    };
    setServiceRequests((prev) => [newReq, ...prev]);
    addNotification('Nova Solicitação Recebida', `${clientName} solicitou ${serviceType} (${preferredPeriod}).`);
  };

  const confirmServiceRequest = (
    requestId: number,
    dayNumber: string,
    dayOfWeek: string,
    time: string,
    durationMinutes: number
  ): string => {
    const req = serviceRequests.find((r) => r.id === requestId);
    if (!req) return '';

    setServiceRequests((prev) =>
      prev.map((r) => (r.id === requestId ? { ...r, status: 'Aceito' } : r))
    );

    // Create confirmed appointment
    addAppointment(
      req.clientName,
      req.phone,
      req.serviceType,
      time,
      durationMinutes,
      dayNumber,
      dayOfWeek,
      'Confirmado',
      req.notes
    );

    // Check if client exists
    const clientExists = clients.some(
      (c) => c.phone === req.phone || c.name.toLowerCase() === req.clientName.toLowerCase()
    );
    if (!clientExists) {
      addClient(
        req.clientName,
        req.phone,
        req.serviceType.includes('Instalação') ? 'Avaliação / Novo Modelo' : 'Micropele HD',
        req.notes
      );
    }

    return `Olá, ${req.clientName}! Seu atendimento de ${req.serviceType} está confirmado para ${dayOfWeek} (dia ${dayNumber}), às ${time}. Qualquer dúvida ou necessidade de alteração, entre em contato conosco.`;
  };

  const rejectServiceRequest = (requestId: number) => {
    setServiceRequests((prev) =>
      prev.map((r) => (r.id === requestId ? { ...r, status: 'Recusado' } : r))
    );
  };

  const deleteServiceRequest = (requestId: number) => {
    setServiceRequests((prev) => prev.filter((r) => r.id !== requestId));
  };

  // CRUD Product
  const addProduct = (
    name: string,
    price: number,
    category: 'Vestuário' | 'Prótese' | 'Insumos' | 'Cosméticos',
    stock: number,
    status: 'Ativo' | 'Inativo' = 'Ativo'
  ) => {
    const newProd: Product = {
      id: Date.now(),
      name,
      price,
      category,
      stock,
      status,
    };
    setProducts((prev) => [newProd, ...prev]);
  };

  const updateProduct = (updated: Product) => {
    setProducts((prev) => prev.map((p) => (p.id === updated.id ? updated : p)));
  };

  const deleteProduct = (id: number) => {
    setProducts((prev) => prev.filter((p) => p.id !== id));
  };

  // CRUD Transaction
  const addTransaction = (title: string, amount: number, paymentMethod: 'PIX' | 'Cartão' | 'Outros') => {
    const newTx: Transaction = {
      id: Date.now(),
      title,
      amount,
      paymentMethod,
      isIncome: true,
      date: new Date().toISOString().split('T')[0],
      period: financePeriod,
    };
    setTransactions((prev) => [newTx, ...prev]);
  };

  const resetDemoData = () => {
    localStorage.removeItem('captio_notifications');
    localStorage.removeItem('captio_clients');
    localStorage.removeItem('captio_appointments');
    localStorage.removeItem('captio_requests');
    localStorage.removeItem('captio_products');
    localStorage.removeItem('captio_transactions');
    setNotifications(INITIAL_NOTIFICATIONS);
    setClients(INITIAL_CLIENTS);
    setAppointments(INITIAL_APPOINTMENTS);
    setServiceRequests(INITIAL_REQUESTS);
    setProducts(INITIAL_PRODUCTS);
    setTransactions(INITIAL_TRANSACTIONS);
  };

  return (
    <CaptioContext.Provider
      value={{
        currentScreen,
        setCurrentScreen,
        selectedDay,
        setSelectedDay,
        clientSearchQuery,
        setClientSearchQuery,
        clientFilterTab,
        setClientFilterTab,
        clientSortOrder,
        setClientSortOrder,
        productSearchQuery,
        setProductSearchQuery,
        productFilterTab,
        setProductFilterTab,
        productCategory,
        setProductCategory,
        financePeriod,
        setFinancePeriod,
        reportsTab,
        setReportsTab,
        atendimentosTab,
        setAtendimentosTab,

        notifications,
        dismissNotification,
        addNotification,

        clients,
        filteredClients,
        addClient,
        updateClient,
        deleteClient,

        appointments,
        filteredAppointments,
        upcomingAppointments,
        addAppointment,
        rescheduleAppointment,
        completeAppointment,
        cancelAppointment,
        deleteAppointment,
        checkAppointmentConflict,

        serviceRequests,
        pendingRequestsCount,
        addPublicServiceRequest,
        confirmServiceRequest,
        rejectServiceRequest,
        deleteServiceRequest,

        products,
        filteredProducts,
        addProduct,
        updateProduct,
        deleteProduct,

        transactions,
        financialMetrics,
        addTransaction,

        resetDemoData,
      }}
    >
      {children}
    </CaptioContext.Provider>
  );
};

export const useCaptio = () => {
  const context = useContext(CaptioContext);
  if (!context) {
    throw new Error('useCaptio must be used within a CaptioProvider');
  }
  return context;
};
