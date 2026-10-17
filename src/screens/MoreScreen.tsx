import React from 'react';
import {
  ArrowLeft,
  Package,
  DollarSign,
  BarChart3,
  Calendar,
  Link as LinkIcon,
  RotateCcw,
  Sparkles,
  ChevronRight,
  ShieldCheck,
  Building,
} from 'lucide-react';
import { useCaptio } from '../context/CaptioContext';
import { CaptioLogo } from '../components/common/CaptioLogo';

export const MoreScreen: React.FC = () => {
  const { setCurrentScreen, resetDemoData } = useCaptio();

  const handleReset = () => {
    if (confirm('Deseja restaurar todos os dados para o padrão inicial de demonstração?')) {
      resetDemoData();
      alert('Dados restaurados com sucesso!');
      setCurrentScreen('HOME');
    }
  };

  const navOptions = [
    {
      title: 'Atendimentos & Solicitações',
      subtitle: 'Controle de horários, confirmações e WhatsApp',
      icon: <Calendar className="w-5 h-5 text-[#00FF66]" />,
      action: () => setCurrentScreen('ATENDIMENTOS'),
      testTag: 'more_opt_atendimentos',
    },
    {
      title: 'Catálogo de Produtos',
      subtitle: 'Próteses capilares, fitas, colas e vestuário',
      icon: <Package className="w-5 h-5 text-[#F59E0B]" />,
      action: () => setCurrentScreen('PRODUCTS'),
      testTag: 'more_opt_products',
    },
    {
      title: 'Controle Financeiro',
      subtitle: 'Vendas, receitas, fluxo e formas de pagamento',
      icon: <DollarSign className="w-5 h-5 text-[#00E5FF]" />,
      action: () => setCurrentScreen('FINANCE'),
      testTag: 'more_opt_finance',
    },
    {
      title: 'Relatórios & Insights',
      subtitle: 'Desempenho, métricas e faturamento mensal',
      icon: <BarChart3 className="w-5 h-5 text-[#8B5CF6]" />,
      action: () => setCurrentScreen('REPORTS'),
      testTag: 'more_opt_reports',
    },
    {
      title: 'Link Público de Agendamento',
      subtitle: 'Página exclusiva para seus clientes solicitarem horário',
      icon: <LinkIcon className="w-5 h-5 text-[#00FF66]" />,
      action: () => setCurrentScreen('PUBLIC_REQUEST'),
      testTag: 'more_opt_public',
    },
  ];

  return (
    <div className="min-h-full pb-24 px-4 pt-3 space-y-4">
      {/* Top Bar */}
      <div className="flex items-center justify-between pb-1">
        <button
          onClick={() => setCurrentScreen('HOME')}
          data-testid="more_back_btn"
          className="w-10 h-10 rounded-full flex items-center justify-center text-white hover:bg-white/5 active:scale-95 transition"
        >
          <ArrowLeft className="w-5 h-5" />
        </button>

        <h2 className="text-base font-bold text-white">Mais Opções</h2>

        <div className="w-10" />
      </div>

      {/* Hero Poster Header */}
      <div className="p-5 rounded-3xl bg-gradient-to-b from-[#24154E] via-[#1A0E3C] to-[#12082C] border border-[#7C3AED]/30 shadow-xl text-center space-y-2">
        <CaptioLogo size="lg" showSubtitle={true} />
      </div>

      {/* Modules List */}
      <div className="space-y-2.5">
        <h3 className="text-xs font-bold text-[#A2A2BF] uppercase tracking-wider px-1">
          Módulos de Gestão
        </h3>

        {navOptions.map((opt) => (
          <button
            key={opt.title}
            onClick={opt.action}
            data-testid={opt.testTag}
            className="w-full text-left p-3.5 rounded-2xl bg-[#181038] hover:bg-[#201548] border border-[#7C3AED]/20 hover:border-[#00FF66]/40 transition flex items-center gap-3.5 group active:scale-[0.99]"
          >
            <div className="w-10 h-10 rounded-xl bg-[#22164D] border border-[#7C3AED]/30 flex items-center justify-center shrink-0 group-hover:scale-105 transition-transform">
              {opt.icon}
            </div>

            <div className="flex-1 min-w-0">
              <h4 className="text-xs font-bold text-white group-hover:text-[#00FF66] transition-colors">
                {opt.title}
              </h4>
              <p className="text-[11px] text-[#A2A2BF] truncate">{opt.subtitle}</p>
            </div>

            <ChevronRight className="w-4 h-4 text-[#A2A2BF] group-hover:text-white group-hover:translate-x-0.5 transition shrink-0" />
          </button>
        ))}
      </div>

      {/* System Actions */}
      <div className="pt-2 space-y-2">
        <h3 className="text-xs font-bold text-[#A2A2BF] uppercase tracking-wider px-1">
          Configurações & Dados
        </h3>

        <button
          onClick={handleReset}
          className="w-full p-3.5 rounded-2xl bg-[#181038] hover:bg-red-500/10 border border-[#7C3AED]/20 hover:border-red-500/40 transition flex items-center gap-3.5 text-left text-xs"
        >
          <div className="w-10 h-10 rounded-xl bg-red-500/15 flex items-center justify-center text-red-400 shrink-0">
            <RotateCcw className="w-5 h-5" />
          </div>
          <div className="flex-1 min-w-0">
            <h4 className="font-bold text-white">Restaurar Dados Demo</h4>
            <p className="text-[11px] text-[#A2A2BF]">Reinicia clientes, produtos e faturamento</p>
          </div>
        </button>
      </div>

      <div className="pt-4 text-center text-[11px] text-[#A2A2BF]/60 space-y-1">
        <p className="font-semibold text-white/50">CAPTIO Sistema Profissional de Prótese Capilar</p>
        <p>Versão 1.0.0 Web • Inteligente & Seguro</p>
      </div>
    </div>
  );
};
