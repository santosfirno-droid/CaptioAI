import React, { useState } from 'react';
import { X, DollarSign } from 'lucide-react';
import { useCaptio } from '../../context/CaptioContext';

interface AddTransactionModalProps {
  isOpen: boolean;
  onClose: () => void;
}

export const AddTransactionModal: React.FC<AddTransactionModalProps> = ({ isOpen, onClose }) => {
  const { addTransaction } = useCaptio();

  const [title, setTitle] = useState('');
  const [amount, setAmount] = useState('');
  const [paymentMethod, setPaymentMethod] = useState<'PIX' | 'Cartão' | 'Outros'>('PIX');

  if (!isOpen) return null;

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!title.trim()) return;

    const parsedAmount = parseFloat(amount.replace(',', '.')) || 0;
    addTransaction(title.trim(), parsedAmount, paymentMethod);
    onClose();
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/75 backdrop-blur-sm animate-fadeIn">
      <div className="w-full max-w-md bg-[#22164D] border border-[#7C3AED]/40 rounded-3xl p-5 shadow-2xl">
        <div className="flex items-center justify-between pb-3 border-b border-[#7C3AED]/20">
          <div className="flex items-center gap-2">
            <div className="w-8 h-8 rounded-full bg-[#00E5FF]/20 flex items-center justify-center text-[#00E5FF]">
              <DollarSign className="w-4 h-4" />
            </div>
            <h3 className="text-base font-bold text-white">Nova Venda / Entrada</h3>
          </div>
          <button
            onClick={onClose}
            className="p-1 rounded-full text-[#A2A2BF] hover:text-white hover:bg-white/10 transition"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="mt-4 space-y-3.5">
          <div>
            <label className="text-xs font-semibold text-[#A2A2BF] block mb-1">Descrição / Cliente</label>
            <input
              type="text"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              placeholder="Ex: Manutenção Micropele Silk - Gabriel S."
              required
              className="w-full px-3.5 py-2.5 rounded-xl bg-[#181038] border border-[#7C3AED]/30 text-white placeholder-[#A2A2BF]/50 text-sm focus:outline-none focus:border-[#00FF66]"
            />
          </div>

          <div>
            <label className="text-xs font-semibold text-[#A2A2BF] block mb-1">Valor Total (R$)</label>
            <input
              type="text"
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
              placeholder="Ex: 350,00"
              required
              className="w-full px-3.5 py-2.5 rounded-xl bg-[#181038] border border-[#7C3AED]/30 text-white placeholder-[#A2A2BF]/50 text-sm focus:outline-none focus:border-[#00FF66]"
            />
          </div>

          <div>
            <label className="text-xs font-semibold text-[#A2A2BF] block mb-1.5">Forma de Pagamento</label>
            <div className="grid grid-cols-3 gap-2">
              <button
                type="button"
                onClick={() => setPaymentMethod('PIX')}
                className={`py-2 rounded-xl text-xs font-bold transition-all ${
                  paymentMethod === 'PIX'
                    ? 'bg-[#00E5FF]/20 border border-[#00E5FF] text-[#00E5FF] shadow-sm'
                    : 'bg-[#181038] text-[#A2A2BF] border border-[#7C3AED]/20'
                }`}
              >
                PIX
              </button>
              <button
                type="button"
                onClick={() => setPaymentMethod('Cartão')}
                className={`py-2 rounded-xl text-xs font-bold transition-all ${
                  paymentMethod === 'Cartão'
                    ? 'bg-[#FF9800]/20 border border-[#FF9800] text-[#FF9800] shadow-sm'
                    : 'bg-[#181038] text-[#A2A2BF] border border-[#7C3AED]/20'
                }`}
              >
                Cartão
              </button>
              <button
                type="button"
                onClick={() => setPaymentMethod('Outros')}
                className={`py-2 rounded-xl text-xs font-bold transition-all ${
                  paymentMethod === 'Outros'
                    ? 'bg-[#A855F7]/20 border border-[#A855F7] text-[#A855F7] shadow-sm'
                    : 'bg-[#181038] text-[#A2A2BF] border border-[#7C3AED]/20'
                }`}
              >
                Outros
              </button>
            </div>
          </div>

          <div className="pt-2 flex gap-2">
            <button
              type="button"
              onClick={onClose}
              className="flex-1 py-2.5 rounded-xl bg-white/5 hover:bg-white/10 text-white text-xs font-semibold transition"
            >
              Cancelar
            </button>
            <button
              type="submit"
              disabled={!title.trim() || !amount.trim()}
              className="flex-1 py-2.5 rounded-xl bg-[#00FF66] hover:bg-[#00CC52] disabled:opacity-40 text-black font-extrabold text-xs transition shadow-lg shadow-[#00FF66]/20"
            >
              Registrar Entrada
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};
