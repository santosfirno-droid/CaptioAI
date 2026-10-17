import React, { useState } from 'react';
import { CaptioProvider, useCaptio } from './context/CaptioContext';
import { CaptioBottomBar } from './components/common/CaptioBottomBar';
import { NotificationModal } from './components/common/NotificationModal';
import { QuickActionModal } from './components/common/QuickActionModal';
import { WhatsAppModal } from './components/common/WhatsAppModal';

import { AddAppointmentModal } from './components/modals/AddAppointmentModal';
import { AddClientModal } from './components/modals/AddClientModal';
import { AddProductModal } from './components/modals/AddProductModal';
import { AddTransactionModal } from './components/modals/AddTransactionModal';
import { AppointmentDetailModal } from './components/modals/AppointmentDetailModal';
import { RescheduleModal } from './components/modals/RescheduleModal';
import { ConfirmRequestModal } from './components/modals/ConfirmRequestModal';
import { ClientDetailModal } from './components/modals/ClientDetailModal';
import { ProductDetailModal } from './components/modals/ProductDetailModal';

import { HomeScreen } from './screens/HomeScreen';
import { AtendimentosScreen } from './screens/AtendimentosScreen';
import { ClientsScreen } from './screens/ClientsScreen';
import { FinanceScreen } from './screens/FinanceScreen';
import { ProductsScreen } from './screens/ProductsScreen';
import { ReportsScreen } from './screens/ReportsScreen';
import { MoreScreen } from './screens/MoreScreen';
import { PublicRequestScreen } from './screens/PublicRequestScreen';

import { Appointment, Client, Product, ServiceRequest } from './types';

const MainApp: React.FC = () => {
  const { currentScreen, setCurrentScreen } = useCaptio();

  // Modals state
  const [showNotifications, setShowNotifications] = useState(false);
  const [showQuickAction, setShowQuickAction] = useState(false);

  // Add modals
  const [showAddAppointment, setShowAddAppointment] = useState(false);
  const [showAddClient, setShowAddClient] = useState(false);
  const [showAddProduct, setShowAddProduct] = useState(false);
  const [showAddTransaction, setShowAddTransaction] = useState(false);

  // Detail & interactive modals
  const [selectedAppointment, setSelectedAppointment] = useState<Appointment | null>(null);
  const [appointmentToReschedule, setAppointmentToReschedule] = useState<Appointment | null>(null);
  const [requestToConfirm, setRequestToConfirm] = useState<ServiceRequest | null>(null);
  const [selectedClient, setSelectedClient] = useState<Client | null>(null);
  const [selectedProduct, setSelectedProduct] = useState<Product | null>(null);
  const [preselectedClientForAppt, setPreselectedClientForAppt] = useState<Client | undefined>(undefined);

  // WhatsApp dialog state
  const [whatsappData, setWhatsappData] = useState<{ phone: string; message: string } | null>(null);

  const handleSelectQuickAction = (action: 'appointment' | 'client' | 'transaction' | 'product') => {
    switch (action) {
      case 'appointment':
        setPreselectedClientForAppt(undefined);
        setShowAddAppointment(true);
        break;
      case 'client':
        setShowAddClient(true);
        break;
      case 'transaction':
        setShowAddTransaction(true);
        break;
      case 'product':
        setShowAddProduct(true);
        break;
    }
  };

  const handleScheduleForClient = (client: Client) => {
    setPreselectedClientForAppt(client);
    setShowAddAppointment(true);
  };

  const handleOpenWhatsApp = (phone: string, message: string) => {
    setWhatsappData({ phone, message });
  };

  return (
    <div className="min-h-screen bg-[#070414] text-white flex justify-center">
      {/* Mobile container simulation that scales naturally */}
      <div className="w-full max-w-md bg-[#0B071E] min-h-screen relative shadow-2xl flex flex-col border-x border-[#7C3AED]/20">
        <div className="flex-1">
          {currentScreen === 'HOME' && (
            <HomeScreen
              onOpenNotifications={() => setShowNotifications(true)}
              onSelectAppointment={(appt) => setSelectedAppointment(appt)}
            />
          )}

          {currentScreen === 'ATENDIMENTOS' && (
            <AtendimentosScreen
              onQuickAdd={() => {
                setPreselectedClientForAppt(undefined);
                setShowAddAppointment(true);
              }}
              onSelectAppointment={(appt) => setSelectedAppointment(appt)}
              onConfirmRequest={(req) => setRequestToConfirm(req)}
              onOpenWhatsApp={handleOpenWhatsApp}
            />
          )}

          {currentScreen === 'CLIENTS' && (
            <ClientsScreen
              onAddClient={() => setShowAddClient(true)}
              onSelectClient={(c) => setSelectedClient(c)}
              onOpenWhatsApp={handleOpenWhatsApp}
            />
          )}

          {currentScreen === 'FINANCE' && (
            <FinanceScreen onAddTransaction={() => setShowAddTransaction(true)} />
          )}

          {currentScreen === 'PRODUCTS' && (
            <ProductsScreen
              onAddProduct={() => setShowAddProduct(true)}
              onSelectProduct={(p) => setSelectedProduct(p)}
            />
          )}

          {currentScreen === 'REPORTS' && <ReportsScreen />}

          {currentScreen === 'MORE' && <MoreScreen />}

          {currentScreen === 'PUBLIC_REQUEST' && <PublicRequestScreen />}
        </div>

        {/* Bottom Nav Bar (hidden in public request page) */}
        {currentScreen !== 'PUBLIC_REQUEST' && (
          <CaptioBottomBar onQuickAdd={() => setShowQuickAction(true)} />
        )}

        {/* Global Modals */}
        <NotificationModal
          isOpen={showNotifications}
          onClose={() => setShowNotifications(false)}
        />

        <QuickActionModal
          isOpen={showQuickAction}
          onClose={() => setShowQuickAction(false)}
          onSelectAction={handleSelectQuickAction}
        />

        <AddAppointmentModal
          isOpen={showAddAppointment}
          onClose={() => setShowAddAppointment(false)}
          preselectedClient={preselectedClientForAppt}
          onSuccessWithMessage={handleOpenWhatsApp}
        />

        <AddClientModal
          isOpen={showAddClient}
          onClose={() => setShowAddClient(false)}
        />

        <AddProductModal
          isOpen={showAddProduct}
          onClose={() => setShowAddProduct(false)}
        />

        <AddTransactionModal
          isOpen={showAddTransaction}
          onClose={() => setShowAddTransaction(false)}
        />

        <AppointmentDetailModal
          appointment={selectedAppointment}
          isOpen={!!selectedAppointment}
          onClose={() => setSelectedAppointment(null)}
          onReschedule={(appt) => setAppointmentToReschedule(appt)}
          onOpenWhatsApp={handleOpenWhatsApp}
        />

        <RescheduleModal
          appointment={appointmentToReschedule}
          isOpen={!!appointmentToReschedule}
          onClose={() => setAppointmentToReschedule(null)}
          onSuccessWithMessage={handleOpenWhatsApp}
        />

        <ConfirmRequestModal
          request={requestToConfirm}
          isOpen={!!requestToConfirm}
          onClose={() => setRequestToConfirm(null)}
          onSuccessWithMessage={handleOpenWhatsApp}
        />

        <ClientDetailModal
          client={selectedClient}
          isOpen={!!selectedClient}
          onClose={() => setSelectedClient(null)}
          onScheduleForClient={handleScheduleForClient}
          onOpenWhatsApp={handleOpenWhatsApp}
        />

        <ProductDetailModal
          product={selectedProduct}
          isOpen={!!selectedProduct}
          onClose={() => setSelectedProduct(null)}
        />

        {whatsappData && (
          <WhatsAppModal
            phone={whatsappData.phone}
            message={whatsappData.message}
            isOpen={true}
            onClose={() => setWhatsappData(null)}
          />
        )}
      </div>
    </div>
  );
};

export default function App() {
  return (
    <CaptioProvider>
      <MainApp />
    </CaptioProvider>
  );
}
