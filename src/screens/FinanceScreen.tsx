import React, { useState } from 'react';
import {
  ArrowLeft,
  TrendingUp,
  Filter,
  DollarSign,
  CreditCard,
  Zap,
  Layers,
  Plus,
  ArrowUpRight,
} from 'lucide-react';
import { useCaptio } from '../context/CaptioContext';

interface FinanceScreenProps {
  onAddTransaction: () => void;
}

export const FinanceScreen: React.FC<FinanceScreenProps> = ({ onAddTransaction }) => {
  const {
    setCurrentScreen,
    financialMetrics,
    financePeriod,
    setFinancePeriod,
    transactions,
  } = useCaptio();

  const [showFilterMenu, setShowFilterMenu] = useState(false);
  const periods = ['7 dias', '30 dias', '90 dias', 'Personalizado'];

  const formatCurrency = (val: number) => {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL',
    }).format(val);
  };

  return (
    <div className="min-h-full pb-24 px-4 pt-3 space-y-4">
      {/* Top Bar */}
      <div className="flex items-center justify-between pb-1">
        <button
          onClick={() => setCurrentScreen('HOME')}
          data-testid="finance_back_btn"
          className="w-10 h-10 rounded-full flex items-center justify-center text-white hover:bg-white/5 active:scale-95 transition"
        >
          <ArrowLeft className="w-5 h-5" />
        </button>

        <div className="flex items-center gap-2">
          <TrendingUp className="w-5 h-5 text-[#00FF66]" />
          <h2 className="text-base font-bold text-white">Financeiro</h2>
        </div>

        <div className="relative">
          <button
            onClick={() => setShowFilterMenu(!showFilterMenu)}
            data-testid="finance_filter_btn"
            className="w-10 h-10 rounded-full bg-[#181038] border border-[#7C3AED]/20 flex items-center justify-center text-white hover:border-[#7C3AED]/60 active:scale-95 transition"
            title="Filtrar Período"
          >
            <Filter className="w-4 h-4" />
          </button>

          {showFilterMenu && (
            <div className="absolute right-0 top-12 w-44 bg-[#22164D] border border-[#7C3AED]/40 rounded-2xl p-2 shadow-2xl z-30 animate-fadeIn">
              {periods.map((p) => (
                <button
                  key={p}
                  onClick={() => {
                    setFinancePeriod(p);
                    setShowFilterMenu(false);
                  }}
                  className={`w-full text-left px-3 py-2 text-xs rounded-xl font-medium transition ${
                    financePeriod === p
                      ? 'bg-[#7C3AED] text-white font-bold'
                      : 'text-[#A2A2BF] hover:text-white'
                  }`}
                >
                  {p}
                </button>
              ))}
            </div>
          )}
        </div>
      </div>

      {/* Period Chips */}
      <div className="grid grid-cols-4 gap-1.5">
        {periods.map((p) => {
          const isSelected = financePeriod === p;
          return (
            <button
              key={p}
              onClick={() => setFinancePeriod(p)}
              className={`py-2 rounded-xl text-[11px] font-semibold transition ${
                isSelected
                  ? 'bg-[#7C3AED] text-white font-bold shadow-md shadow-[#7C3AED]/30'
                  : 'bg-[#181038] text-[#A2A2BF] hover:text-white border border-[#7C3AED]/20'
              }`}
            >
              {p}
            </button>
          );
        })}
      </div>

      {/* Total Vendido Main Card */}
      <div className="p-5 rounded-3xl bg-gradient-to-b from-[#24154E] to-[#190E38] border border-[#7C3AED]/30 shadow-xl space-y-3">
        <div className="flex items-center justify-between text-xs">
          <span className="text-[#A2A2BF]">Total Vendido ({financePeriod})</span>
          <span className="px-2 py-0.5 rounded-full bg-[#00FF66]/20 text-[#00FF66] font-extrabold text-[11px] border border-[#00FF66]/30 flex items-center gap-0.5">
            <ArrowUpRight className="w-3.5 h-3.5" />
            {financialMetrics.growthPercent}%
          </span>
        </div>

        <div className="text-3xl font-black text-white tracking-tight">
          {formatCurrency(financialMetrics.totalRevenue)}
        </div>

        {/* Mini Neon Bar Chart */}
        <div className="pt-2">
          <div className="flex items-end justify-between h-14 gap-1.5 px-1">
            {[40, 65, 52, 78, 60, 92, 85, 100, 75, 95].map((height, i) => (
              <div key={i} className="flex-1 flex flex-col items-center gap-1 group">
                <div
                  style={{ height: `${height}%` }}
                  className="w-full rounded-md bg-gradient-to-t from-[#7C3AED] to-[#00FF66] group-hover:brightness-125 transition-all shadow-[0_0_8px_rgba(0,255,102,0.3)]"
                />
              </div>
            ))}
          </div>
          <div className="flex justify-between text-[10px] text-[#A2A2BF] pt-1.5 px-1">
            <span>Início</span>
            <span>Semana passada</span>
            <span>Hoje</span>
          </div>
        </div>
      </div>

      {/* Two Metric Cards */}
      <div className="grid grid-cols-2 gap-3">
        {/* Vendas */}
        <div className="p-4 rounded-2xl bg-[#181038] border border-[#7C3AED]/20 space-y-1.5">
          <div className="w-8 h-8 rounded-lg bg-[#8B5CF6]/20 flex items-center justify-center text-[#8B5CF6]">
            <TrendingUp className="w-4 h-4" />
          </div>
          <span className="text-[11px] text-[#A2A2BF] block">Vendas Realizadas</span>
          <div className="text-xl font-bold text-white">{financialMetrics.salesCount}</div>
          <span className="text-[11px] text-[#00FF66] font-semibold flex items-center gap-0.5">
            ↑ {financialMetrics.salesGrowthPercent}%
          </span>
        </div>

        {/* Receita Líquida */}
        <div className="p-4 rounded-2xl bg-[#181038] border border-[#7C3AED]/20 space-y-1.5">
          <div className="w-8 h-8 rounded-lg bg-[#00FF66]/20 flex items-center justify-center text-[#00FF66]">
            <DollarSign className="w-4 h-4" />
          </div>
          <span className="text-[11px] text-[#A2A2BF] block">Receita Líquida</span>
          <div className="text-xl font-bold text-white truncate">
            {formatCurrency(financialMetrics.netRevenue)}
          </div>
          <span className="text-[11px] text-[#00FF66] font-semibold flex items-center gap-0.5">
            ↑ {financialMetrics.netGrowthPercent}%
          </span>
        </div>
      </div>

      {/* Payment Methods Breakdown */}
      <div className="p-4 rounded-2xl bg-[#181038] border border-[#7C3AED]/20 space-y-3">
        <h4 className="text-xs font-bold text-white uppercase tracking-wider">
          Formas de Pagamento
        </h4>

        {/* Progress bar */}
        <div className="w-full h-3 rounded-full bg-[#0B071E] flex overflow-hidden p-0.5 gap-0.5">
          <div
            style={{ width: `${financialMetrics.pixPercent}%` }}
            className="bg-[#00E5FF] rounded-l-full"
            title="PIX"
          />
          <div
            style={{ width: `${financialMetrics.cardPercent}%` }}
            className="bg-[#FF9800]"
            title="Cartão"
          />
          <div
            style={{ width: `${financialMetrics.othersPercent}%` }}
            className="bg-[#A855F7] rounded-r-full"
            title="Outros"
          />
        </div>

        <div className="grid grid-cols-3 gap-2 pt-1 text-xs">
          <div className="p-2 rounded-xl bg-[#22164D]/60 border border-[#00E5FF]/30">
            <div className="flex items-center gap-1 text-[#00E5FF] font-bold">
              <Zap className="w-3.5 h-3.5" />
              PIX ({financialMetrics.pixPercent}%)
            </div>
            <div className="text-[11px] text-white font-bold mt-1">
              {formatCurrency(financialMetrics.pixAmount)}
            </div>
          </div>

          <div className="p-2 rounded-xl bg-[#22164D]/60 border border-[#FF9800]/30">
            <div className="flex items-center gap-1 text-[#FF9800] font-bold">
              <CreditCard className="w-3.5 h-3.5" />
              Cartão ({financialMetrics.cardPercent}%)
            </div>
            <div className="text-[11px] text-white font-bold mt-1">
              {formatCurrency(financialMetrics.cardAmount)}
            </div>
          </div>

          <div className="p-2 rounded-xl bg-[#22164D]/60 border border-[#A855F7]/30">
            <div className="flex items-center gap-1 text-[#A855F7] font-bold">
              <Layers className="w-3.5 h-3.5" />
              Outros ({financialMetrics.othersPercent}%)
            </div>
            <div className="text-[11px] text-white font-bold mt-1">
              {formatCurrency(financialMetrics.othersAmount)}
            </div>
          </div>
        </div>
      </div>

      {/* Recent Transactions List */}
      <div className="space-y-2.5">
        <div className="flex items-center justify-between">
          <h4 className="text-xs font-bold text-white uppercase tracking-wider">
            Últimas Transações
          </h4>
          <button
            onClick={onAddTransaction}
            className="text-xs font-bold text-[#00FF66] hover:underline flex items-center gap-1"
          >
            <Plus className="w-3.5 h-3.5" />
            Nova Entrada
          </button>
        </div>

        <div className="space-y-2 max-h-64 overflow-y-auto">
          {transactions.map((tx) => (
            <div
              key={tx.id}
              className="p-3 rounded-2xl bg-[#181038] border border-[#7C3AED]/20 flex items-center justify-between text-xs"
            >
              <div className="flex items-center gap-2.5 min-w-0">
                <div
                  className={`w-7 h-7 rounded-lg flex items-center justify-center font-bold text-[10px] shrink-0 ${
                    tx.paymentMethod === 'PIX'
                      ? 'bg-[#00E5FF]/20 text-[#00E5FF]'
                      : tx.paymentMethod === 'Cartão'
                      ? 'bg-[#FF9800]/20 text-[#FF9800]'
                      : 'bg-[#A855F7]/20 text-[#A855F7]'
                  }`}
                >
                  {tx.paymentMethod}
                </div>
                <div className="min-w-0">
                  <h5 className="font-bold text-white truncate">{tx.title}</h5>
                  <p className="text-[10px] text-[#A2A2BF]">{tx.date}</p>
                </div>
              </div>

              <span className="font-extrabold text-[#00FF66] shrink-0 ml-2">
                +{formatCurrency(tx.amount)}
              </span>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};
