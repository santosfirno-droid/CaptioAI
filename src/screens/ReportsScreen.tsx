import React, { useState } from 'react';
import {
  ArrowLeft,
  Calendar,
  BarChart3,
  TrendingUp,
  Users,
  Award,
  Sparkles,
  ShoppingBag,
  DollarSign,
} from 'lucide-react';
import { useCaptio } from '../context/CaptioContext';

export const ReportsScreen: React.FC = () => {
  const {
    setCurrentScreen,
    reportsTab,
    setReportsTab,
    financialMetrics,
    clients,
    products,
  } = useCaptio();

  const [periodLabel, setPeriodLabel] = useState('Últimos 30 dias');
  const tabs = ['Vendas', 'Clientes', 'Financeiro'];

  const formatCurrency = (val: number) => {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL',
    }).format(val);
  };

  const topClients = [...clients]
    .sort((a, b) => b.servicesCount - a.servicesCount)
    .slice(0, 4);

  return (
    <div className="min-h-full pb-24 px-4 pt-3 space-y-4">
      {/* Top Bar */}
      <div className="flex items-center justify-between pb-1">
        <button
          onClick={() => setCurrentScreen('HOME')}
          data-testid="reports_back_btn"
          className="w-10 h-10 rounded-full flex items-center justify-center text-white hover:bg-white/5 active:scale-95 transition"
        >
          <ArrowLeft className="w-5 h-5" />
        </button>

        <h2 className="text-base font-bold text-white">Relatórios & Métricas</h2>

        <div className="w-10 h-10 rounded-full bg-[#181038] border border-[#7C3AED]/20 flex items-center justify-center text-white">
          <Calendar className="w-4 h-4" />
        </div>
      </div>

      {/* Category Toggle Pills */}
      <div className="grid grid-cols-3 gap-2 bg-[#181038] p-1 rounded-2xl border border-[#7C3AED]/20">
        {tabs.map((tab) => {
          const isSelected = reportsTab === tab;
          return (
            <button
              key={tab}
              onClick={() => setReportsTab(tab)}
              className={`py-2 rounded-xl text-xs font-semibold transition ${
                isSelected
                  ? 'bg-[#7C3AED] text-white font-bold shadow-md shadow-[#7C3AED]/30'
                  : 'text-[#A2A2BF] hover:text-white'
              }`}
            >
              {tab}
            </button>
          );
        })}
      </div>

      {reportsTab === 'Vendas' && (
        <div className="space-y-4 animate-fadeIn">
          {/* Main Chart Card */}
          <div className="p-5 rounded-3xl bg-gradient-to-b from-[#24154E] to-[#180E38] border border-[#7C3AED]/30 shadow-xl space-y-3">
            <div className="flex items-center justify-between text-xs">
              <span className="text-[#A2A2BF]">Curva de Faturamento Semanal</span>
              <span className="text-[#00FF66] font-bold">+18% este mês</span>
            </div>

            <div className="text-2xl font-black text-white">
              {formatCurrency(financialMetrics.totalRevenue)}
            </div>

            {/* SVG Area Sparkline Chart */}
            <div className="pt-2">
              <svg className="w-full h-24 overflow-visible" viewBox="0 0 300 80">
                <defs>
                  <linearGradient id="neonGradient" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="0%" stopColor="#00FF66" stopOpacity="0.4" />
                    <stop offset="100%" stopColor="#7C3AED" stopOpacity="0.0" />
                  </linearGradient>
                </defs>
                {/* Area fill */}
                <path
                  d="M 0,65 Q 40,40 75,55 T 150,30 T 225,45 T 300,10 L 300,80 L 0,80 Z"
                  fill="url(#neonGradient)"
                />
                {/* Stroke line */}
                <path
                  d="M 0,65 Q 40,40 75,55 T 150,30 T 225,45 T 300,10"
                  fill="none"
                  stroke="#00FF66"
                  strokeWidth="3"
                  strokeLinecap="round"
                />
                {/* Highlight points */}
                <circle cx="150" cy="30" r="4" fill="#00FF66" />
                <circle cx="300" cy="10" r="5" fill="#FFFFFF" stroke="#00FF66" strokeWidth="2" />
              </svg>
              <div className="flex justify-between text-[10px] text-[#A2A2BF] pt-1">
                <span>Semana 1</span>
                <span>Semana 2</span>
                <span>Semana 3</span>
                <span>Semana 4</span>
              </div>
            </div>
          </div>

          {/* Ticket Médio & Volume */}
          <div className="grid grid-cols-2 gap-3">
            <div className="p-4 rounded-2xl bg-[#181038] border border-[#7C3AED]/20">
              <span className="text-[11px] text-[#A2A2BF] block">Ticket Médio</span>
              <div className="text-lg font-bold text-white mt-1">
                {formatCurrency(financialMetrics.ticketMedio)}
              </div>
              <span className="text-[10px] text-[#00FF66] font-semibold mt-0.5 block">
                Por atendimento / venda
              </span>
            </div>

            <div className="p-4 rounded-2xl bg-[#181038] border border-[#7C3AED]/20">
              <span className="text-[11px] text-[#A2A2BF] block">Total de Vendas</span>
              <div className="text-lg font-bold text-white mt-1">
                {financialMetrics.salesCount} pedidos
              </div>
              <span className="text-[10px] text-[#8B5CF6] font-semibold mt-0.5 block">
                30 dias corridos
              </span>
            </div>
          </div>

          {/* Top Selling Products / Services */}
          <div className="p-4 rounded-2xl bg-[#181038] border border-[#7C3AED]/20 space-y-3">
            <h4 className="text-xs font-bold text-white uppercase tracking-wider flex items-center gap-1.5">
              <Award className="w-4 h-4 text-[#F59E0B]" />
              Produtos & Serviços Mais Vendidos
            </h4>

            <div className="space-y-2 text-xs">
              <div className="flex items-center justify-between p-2 rounded-xl bg-[#22164D]/50 border border-[#7C3AED]/15">
                <span className="font-semibold text-white">Prótese Micropele Silk HD</span>
                <span className="font-mono text-[#00FF66] font-bold">12 un.</span>
              </div>
              <div className="flex items-center justify-between p-2 rounded-xl bg-[#22164D]/50 border border-[#7C3AED]/15">
                <span className="font-semibold text-white">Manutenção Completa com Fita</span>
                <span className="font-mono text-[#00FF66] font-bold">28 ated.</span>
              </div>
              <div className="flex items-center justify-between p-2 rounded-xl bg-[#22164D]/50 border border-[#7C3AED]/15">
                <span className="font-semibold text-white">Fita Adesiva Ultra Hold Walker</span>
                <span className="font-mono text-[#00FF66] font-bold">19 un.</span>
              </div>
              <div className="flex items-center justify-between p-2 rounded-xl bg-[#22164D]/50 border border-[#7C3AED]/15">
                <span className="font-semibold text-white">Solvente C-22 Citrus 118ml</span>
                <span className="font-mono text-[#00FF66] font-bold">14 un.</span>
              </div>
            </div>
          </div>
        </div>
      )}

      {reportsTab === 'Clientes' && (
        <div className="space-y-4 animate-fadeIn">
          {/* Client Retention Stats */}
          <div className="p-5 rounded-3xl bg-gradient-to-b from-[#24154E] to-[#180E38] border border-[#7C3AED]/30 shadow-xl space-y-2">
            <span className="text-xs text-[#A2A2BF]">Taxa de Retenção de Clientes</span>
            <div className="text-3xl font-black text-[#00FF66]">89%</div>
            <p className="text-xs text-[#A2A2BF] leading-relaxed">
              89% dos clientes de prótese retornam a cada 20-30 dias para manutenção periódica e higienização.
            </p>
          </div>

          {/* Top Frequent Clients */}
          <div className="p-4 rounded-2xl bg-[#181038] border border-[#7C3AED]/20 space-y-3">
            <h4 className="text-xs font-bold text-white uppercase tracking-wider flex items-center gap-1.5">
              <Users className="w-4 h-4 text-[#8B5CF6]" />
              Clientes Mais Assíduos
            </h4>

            <div className="space-y-2">
              {topClients.map((c, i) => (
                <div
                  key={c.id}
                  className="p-3 rounded-xl bg-[#22164D]/50 border border-[#7C3AED]/15 flex items-center justify-between text-xs"
                >
                  <div className="flex items-center gap-2.5">
                    <div className="w-6 h-6 rounded-full bg-[#7C3AED]/30 text-[#8B5CF6] font-bold flex items-center justify-center text-[10px]">
                      #{i + 1}
                    </div>
                    <div>
                      <h5 className="font-bold text-white">{c.name}</h5>
                      <span className="text-[10px] text-[#00FF66]">{c.prosthesisType}</span>
                    </div>
                  </div>

                  <span className="font-bold text-white bg-[#7C3AED]/20 px-2 py-0.5 rounded-lg">
                    {c.servicesCount} atendimentos
                  </span>
                </div>
              ))}
            </div>
          </div>
        </div>
      )}

      {reportsTab === 'Financeiro' && (
        <div className="space-y-4 animate-fadeIn">
          <div className="p-5 rounded-3xl bg-gradient-to-b from-[#24154E] to-[#180E38] border border-[#7C3AED]/30 shadow-xl space-y-3">
            <span className="text-xs text-[#A2A2BF]">Lucratividade Estimada</span>
            <div className="text-3xl font-black text-white">
              {formatCurrency(financialMetrics.netRevenue)}
            </div>
            <p className="text-xs text-[#00FF66] font-semibold">
              Margem operacional de 93% após taxas de transação
            </p>
          </div>

          <div className="p-4 rounded-2xl bg-[#181038] border border-[#7C3AED]/20 space-y-2.5 text-xs">
            <div className="flex justify-between py-1 border-b border-[#7C3AED]/15">
              <span className="text-[#A2A2BF]">Faturamento Bruto</span>
              <span className="font-bold text-white">{formatCurrency(financialMetrics.totalRevenue)}</span>
            </div>
            <div className="flex justify-between py-1 border-b border-[#7C3AED]/15">
              <span className="text-[#A2A2BF]">Recebimentos via PIX</span>
              <span className="font-bold text-[#00E5FF]">{formatCurrency(financialMetrics.pixAmount)}</span>
            </div>
            <div className="flex justify-between py-1 border-b border-[#7C3AED]/15">
              <span className="text-[#A2A2BF]">Recebimentos Cartão</span>
              <span className="font-bold text-[#FF9800]">{formatCurrency(financialMetrics.cardAmount)}</span>
            </div>
            <div className="flex justify-between py-1">
              <span className="text-[#A2A2BF]">Outras Formas</span>
              <span className="font-bold text-[#A855F7]">{formatCurrency(financialMetrics.othersAmount)}</span>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
