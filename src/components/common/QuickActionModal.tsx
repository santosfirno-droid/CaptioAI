import React from 'react';
import { X, Calendar, UserPlus, DollarSign, Package } from 'lucide-react';

interface QuickActionModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSelectAction: (action: 'appointment' | 'client' | 'transaction' | 'product') => void;
}

export const QuickActionModal: React.FC<QuickActionModalProps> = ({
  isOpen,
  onClose,
  onSelectAction,
}) => {
  if (!isOpen) return null;

  const actions = [
    {
      id: 'appointment' as const,
      title: 'Novo Atendimento / Agendamento',
      subtitle: 'Consulta, manutenção ou instalação',
      icon: <Calendar className="w-6 h-6 text-[#00FF66]" />,
      bg: 'bg-[#00FF66]/10 border-[#00FF66]/30',
    },
    {
      id: 'client' as const,
      title: 'Novo Cliente',
      subtitle: 'Cadastrar cliente de prótese capilar',
      icon: <UserPlus className="w-6 h-6 text-[#8B5CF6]" />,
      bg: 'bg-[#8B5CF6]/10 border-[#8B5CF6]/30',
    },
    {
      id: 'transaction' as const,
      title: 'Nova Venda / Pagamento',
      subtitle: 'Registrar entrada via PIX ou Cartão',
      icon: <DollarSign className="w-6 h-6 text-[#00E5FF]" />,
      bg: 'bg-[#00E5FF]/10 border-[#00E5FF]/30',
    },
    {
      id: 'product' as const,
      title: 'Novo Produto',
      subtitle: 'Prótese, insumo, fita ou vestuário',
      icon: <Package className="w-6 h-6 text-[#F59E0B]" />,
      bg: 'bg-[#F59E0B]/10 border-[#F59E0B]/30',
    },
  ];

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/75 backdrop-blur-sm animate-fadeIn">
      <div className="w-full max-w-sm bg-[#22164D] border border-[#7C3AED]/40 rounded-3xl p-5 shadow-2xl">
        <div className="flex items-center justify-between pb-3 border-b border-[#7C3AED]/20">
          <div>
            <h3 className="text-base font-bold text-white">Ação Rápida</h3>
            <p className="text-xs text-[#A2A2BF]">O que você deseja registrar agora?</p>
          </div>
          <button
            onClick={onClose}
            className="p-1 rounded-full text-[#A2A2BF] hover:text-white hover:bg-white/10 transition"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        <div className="my-4 space-y-2.5">
          {actions.map((act) => (
            <button
              key={act.id}
              onClick={() => {
                onClose();
                onSelectAction(act.id);
              }}
              className="w-full text-left p-3.5 rounded-2xl bg-[#181038] hover:bg-[#281A5A] border border-[#7C3AED]/20 hover:border-[#00FF66]/50 transition-all flex items-center gap-3.5 group active:scale-[0.98]"
            >
              <div
                className={`w-12 h-12 rounded-xl flex items-center justify-center border ${act.bg} shrink-0 group-hover:scale-105 transition-transform`}
              >
                {act.icon}
              </div>
              <div className="flex-1 min-w-0">
                <h4 className="text-sm font-bold text-white group-hover:text-[#00FF66] transition-colors">
                  {act.title}
                </h4>
                <p className="text-xs text-[#A2A2BF] truncate">{act.subtitle}</p>
              </div>
            </button>
          ))}
        </div>
      </div>
    </div>
  );
};
