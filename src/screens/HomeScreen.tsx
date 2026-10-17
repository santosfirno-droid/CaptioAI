import React, { useState } from 'react';
import {
  Users,
  Calendar,
  TrendingUp,
  Package,
  DollarSign,
  BarChart3,
  Search,
  ArrowRight,
  Clock,
  ChevronRight,
  Sparkles,
} from 'lucide-react';
import { useCaptio } from '../context/CaptioContext';
import { Header } from '../components/common/Header';

interface HomeScreenProps {
  onOpenNotifications: () => void;
  onSelectAppointment: (appointment: any) => void;
}

export const HomeScreen: React.FC<HomeScreenProps> = ({
  onOpenNotifications,
  onSelectAppointment,
}) => {
  const {
    setCurrentScreen,
    clientSearchQuery,
    setClientSearchQuery,
    pendingRequestsCount,
    upcomingAppointments,
  } = useCaptio();

  const [localSearch, setLocalSearch] = useState(clientSearchQuery);

  const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setLocalSearch(e.target.value);
    setClientSearchQuery(e.target.value);
  };

  const handleSearchSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (localSearch.trim()) {
      setCurrentScreen('CLIENTS');
    }
  };

  const quickActions = [
    {
      title: 'Clientes',
      icon: <Users className="w-6 h-6 text-[#00FF66]" />,
      screen: 'CLIENTS' as const,
      testTag: 'quick_clients',
    },
    {
      title: 'Atendimentos',
      icon: <Calendar className="w-6 h-6 text-[#00FF66]" />,
      screen: 'ATENDIMENTOS' as const,
      testTag: 'quick_atendimentos',
    },
    {
      title: 'Vendas',
      icon: <TrendingUp className="w-6 h-6 text-[#00FF66]" />,
      screen: 'FINANCE' as const,
      testTag: 'quick_vendas',
    },
    {
      title: 'Produtos',
      icon: <Package className="w-6 h-6 text-[#00FF66]" />,
      screen: 'PRODUCTS' as const,
      testTag: 'quick_produtos',
    },
    {
      title: 'Financeiro',
      icon: <DollarSign className="w-6 h-6 text-[#00FF66]" />,
      screen: 'FINANCE' as const,
      testTag: 'quick_financeiro',
    },
    {
      title: 'Relatórios',
      icon: <BarChart3 className="w-6 h-6 text-[#00FF66]" />,
      screen: 'REPORTS' as const,
      testTag: 'quick_relatorios',
    },
  ];

  return (
    <div className="min-h-full pb-24">
      <Header onOpenNotifications={onOpenNotifications} />

      <main className="px-4 space-y-4 mt-1">
        {/* Search Bar */}
        <form onSubmit={handleSearchSubmit} className="relative">
          <div className="flex items-center w-full h-11 px-4 rounded-full bg-[#181038] border border-[#7C3AED]/20 focus-within:border-[#00FF66]/60 transition-all">
            <Search className="w-4 h-4 text-[#A2A2BF] mr-2.5 shrink-0" />
            <input
              type="text"
              value={localSearch}
              onChange={handleSearchChange}
              placeholder="Buscar por cliente, serviço..."
              data-testid="home_search_input"
              className="w-full bg-transparent text-white text-xs placeholder-[#A2A2BF] focus:outline-none"
            />
            {localSearch && (
              <button
                type="button"
                onClick={() => {
                  setLocalSearch('');
                  setClientSearchQuery('');
                }}
                className="text-[#A2A2BF] text-xs hover:text-white"
              >
                Limpar
              </button>
            )}
          </div>
        </form>

        {/* Highlight: Pending Service Requests */}
        {pendingRequestsCount > 0 && (
          <div
            onClick={() => setCurrentScreen('ATENDIMENTOS')}
            className="w-full rounded-2xl p-3.5 bg-gradient-to-r from-[#2E195E] to-[#1E1044] border border-[#00FF66]/50 shadow-lg shadow-[#00FF66]/10 cursor-pointer hover:border-[#00FF66] transition flex items-center justify-between gap-3 active:scale-[0.99]"
          >
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 rounded-full bg-[#00FF66]/15 flex items-center justify-center font-black text-lg text-[#00FF66] shrink-0">
                {pendingRequestsCount}
              </div>
              <div>
                <h4 className="text-xs font-bold text-white flex items-center gap-1.5">
                  Novas Solicitações de Clientes
                  <span className="w-2 h-2 rounded-full bg-[#00FF66] animate-ping" />
                </h4>
                <p className="text-[11px] text-[#A2A2BF]">
                  {pendingRequestsCount} {pendingRequestsCount === 1 ? 'pessoa aguardando' : 'pessoas aguardando'} confirmação
                </p>
              </div>
            </div>

            <button
              onClick={(e) => {
                e.stopPropagation();
                setCurrentScreen('ATENDIMENTOS');
              }}
              className="py-1.5 px-3 bg-[#00FF66] hover:bg-[#00CC52] text-black font-extrabold text-[11px] rounded-lg transition shrink-0"
            >
              Ver
            </button>
          </div>
        )}

        {/* 6 Quick Action Grid */}
        <div className="grid grid-cols-3 gap-2.5">
          {quickActions.map((act) => (
            <button
              key={act.title}
              onClick={() => setCurrentScreen(act.screen)}
              data-testid={act.testTag}
              className="flex flex-col items-center justify-center p-3.5 rounded-2xl bg-[#181038] hover:bg-[#22164D] border border-[#7C3AED]/20 hover:border-[#00FF66]/40 transition group active:scale-95"
            >
              <div className="w-11 h-11 rounded-xl bg-[#00FF66]/10 flex items-center justify-center group-hover:scale-105 transition-transform">
                {act.icon}
              </div>
              <span className="text-xs font-semibold text-white mt-2 group-hover:text-[#00FF66] transition-colors">
                {act.title}
              </span>
            </button>
          ))}
        </div>

        {/* Section: Próximos Atendimentos */}
        <div className="pt-2">
          <div className="flex items-center justify-between mb-2.5">
            <h3 className="text-sm font-bold text-white flex items-center gap-1.5">
              <Clock className="w-4 h-4 text-[#00FF66]" />
              Próximos Atendimentos
            </h3>
            <button
              onClick={() => setCurrentScreen('ATENDIMENTOS')}
              className="text-[11px] font-semibold text-[#00FF66] hover:underline flex items-center gap-0.5"
            >
              Ver todos
              <ChevronRight className="w-3.5 h-3.5" />
            </button>
          </div>

          <div className="space-y-2">
            {upcomingAppointments.length === 0 ? (
              <div className="p-4 rounded-xl bg-[#181038] text-center text-xs text-[#A2A2BF]">
                Nenhum atendimento confirmado próximo.
              </div>
            ) : (
              upcomingAppointments.map((appt) => (
                <div
                  key={appt.id}
                  onClick={() => onSelectAppointment(appt)}
                  className="w-full p-3 rounded-2xl bg-[#181038] hover:bg-[#201548] border border-[#7C3AED]/15 hover:border-[#7C3AED]/40 transition flex items-center gap-3 cursor-pointer"
                >
                  <div className="text-center w-12 shrink-0 border-r border-[#7C3AED]/20 pr-2">
                    <div className="text-xs font-black text-[#00FF66]">{appt.time}</div>
                    <div className="text-[10px] text-[#A2A2BF]">
                      {appt.dayOfWeek} {appt.dayNumber}
                    </div>
                  </div>

                  <div className="flex-1 min-w-0">
                    <h4 className="text-xs font-bold text-white truncate">{appt.clientName}</h4>
                    <p className="text-[11px] text-[#A2A2BF] truncate">
                      {appt.service} • {appt.durationMinutes} min
                    </p>
                  </div>

                  <span className="px-2 py-0.5 rounded-lg text-[10px] font-semibold bg-[#00FF66]/15 text-[#00FF66] border border-[#00FF66]/30 shrink-0">
                    {appt.status}
                  </span>
                </div>
              ))
            )}
          </div>
        </div>

        {/* Highlight Card: Resumo da Semana with Mascot */}
        <div
          onClick={() => setCurrentScreen('REPORTS')}
          className="rounded-3xl p-4 bg-gradient-to-r from-[#22134E] via-[#1B0E3E] to-[#12082C] border border-[#7C3AED]/30 hover:border-[#7C3AED]/60 transition cursor-pointer flex items-center gap-3.5 shadow-xl group active:scale-[0.99]"
        >
          <img
            src="/robot.jpg"
            alt="Mascote CAPTIO"
            className="w-16 h-16 rounded-2xl object-cover shrink-0 border border-[#7C3AED]/40 shadow-md group-hover:scale-105 transition-transform"
            onError={(e) => {
              // fallback
              (e.currentTarget as HTMLImageElement).src = '/logo.jpg';
            }}
          />

          <div className="flex-1 min-w-0">
            <h4 className="text-sm font-bold text-white flex items-center gap-1.5">
              Resumo da semana
              <Sparkles className="w-3.5 h-3.5 text-[#00FF66]" />
            </h4>
            <p className="text-[11px] text-[#A2A2BF] leading-relaxed mt-0.5">
              Veja seu desempenho, novas vendas e atendimentos da clínica.
            </p>
          </div>

          <div className="w-9 h-9 rounded-full bg-[#7C3AED] flex items-center justify-center text-white shrink-0 group-hover:translate-x-1 transition-transform">
            <ArrowRight className="w-4 h-4" />
          </div>
        </div>
      </main>
    </div>
  );
};
