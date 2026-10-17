import React, { useState } from 'react';
import { X, UserPlus } from 'lucide-react';
import { useCaptio } from '../../context/CaptioContext';

interface AddClientModalProps {
  isOpen: boolean;
  onClose: () => void;
}

export const AddClientModal: React.FC<AddClientModalProps> = ({ isOpen, onClose }) => {
  const { addClient } = useCaptio();

  const [name, setName] = useState('');
  const [phone, setPhone] = useState('');
  const [prosthesisType, setProsthesisType] = useState('Micropele Silk 0.04mm');
  const [status, setStatus] = useState<'Ativo' | 'Inativo'>('Ativo');
  const [notes, setNotes] = useState('');

  if (!isOpen) return null;

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!name.trim()) return;

    addClient(name.trim(), phone.trim() || '(11) 98765-4321', prosthesisType, notes.trim(), status);
    onClose();
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/75 backdrop-blur-sm animate-fadeIn">
      <div className="w-full max-w-md bg-[#22164D] border border-[#7C3AED]/40 rounded-3xl p-5 shadow-2xl">
        <div className="flex items-center justify-between pb-3 border-b border-[#7C3AED]/20">
          <div className="flex items-center gap-2">
            <div className="w-8 h-8 rounded-full bg-[#8B5CF6]/20 flex items-center justify-center text-[#8B5CF6]">
              <UserPlus className="w-4 h-4" />
            </div>
            <h3 className="text-base font-bold text-white">Novo Cliente</h3>
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
            <label className="text-xs font-semibold text-[#A2A2BF] block mb-1">Nome Completo</label>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="Ex: Matheus Oliveira"
              required
              className="w-full px-3.5 py-2.5 rounded-xl bg-[#181038] border border-[#7C3AED]/30 text-white placeholder-[#A2A2BF]/50 text-sm focus:outline-none focus:border-[#00FF66]"
            />
          </div>

          <div>
            <label className="text-xs font-semibold text-[#A2A2BF] block mb-1">WhatsApp / Telefone</label>
            <input
              type="text"
              value={phone}
              onChange={(e) => setPhone(e.target.value)}
              placeholder="(11) 98765-4321"
              className="w-full px-3.5 py-2.5 rounded-xl bg-[#181038] border border-[#7C3AED]/30 text-white placeholder-[#A2A2BF]/50 text-sm focus:outline-none focus:border-[#00FF66]"
            />
          </div>

          <div>
            <label className="text-xs font-semibold text-[#A2A2BF] block mb-1">Tipo de Prótese / Modelo</label>
            <select
              value={prosthesisType}
              onChange={(e) => setProsthesisType(e.target.value)}
              className="w-full px-3.5 py-2.5 rounded-xl bg-[#181038] border border-[#7C3AED]/30 text-white text-sm focus:outline-none focus:border-[#00FF66]"
            >
              <option value="Micropele Silk 0.04mm">Micropele Silk 0.04mm</option>
              <option value="Micropele HD 0.06mm">Micropele HD 0.06mm</option>
              <option value="Híbrida Swiss Lace">Híbrida Swiss Lace</option>
              <option value="Nanopele Ultrathin">Nanopele Ultrathin</option>
              <option value="Micropele Injetada">Micropele Injetada</option>
              <option value="Lace Front Natural">Lace Front Natural</option>
            </select>
          </div>

          <div>
            <label className="text-xs font-semibold text-[#A2A2BF] block mb-1">Status</label>
            <div className="grid grid-cols-2 gap-2">
              <button
                type="button"
                onClick={() => setStatus('Ativo')}
                className={`py-2 rounded-xl text-xs font-bold transition-all ${
                  status === 'Ativo'
                    ? 'bg-[#00FF66]/20 border border-[#00FF66] text-[#00FF66]'
                    : 'bg-[#181038] text-[#A2A2BF] border border-[#7C3AED]/20'
                }`}
              >
                Ativo
              </button>
              <button
                type="button"
                onClick={() => setStatus('Inativo')}
                className={`py-2 rounded-xl text-xs font-bold transition-all ${
                  status === 'Inativo'
                    ? 'bg-red-500/20 border border-red-500 text-red-400'
                    : 'bg-[#181038] text-[#A2A2BF] border border-[#7C3AED]/20'
                }`}
              >
                Inativo
              </button>
            </div>
          </div>

          <div>
            <label className="text-xs font-semibold text-[#A2A2BF] block mb-1">Observações Clínicas</label>
            <textarea
              rows={2}
              value={notes}
              onChange={(e) => setNotes(e.target.value)}
              placeholder="Preferências de fita, corte, cuidados de couro cabeludo..."
              className="w-full px-3.5 py-2.5 rounded-xl bg-[#181038] border border-[#7C3AED]/30 text-white placeholder-[#A2A2BF]/50 text-sm focus:outline-none focus:border-[#00FF66] resize-none"
            />
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
              disabled={!name.trim()}
              className="flex-1 py-2.5 rounded-xl bg-[#7C3AED] hover:bg-[#6D28D9] disabled:opacity-40 text-white font-extrabold text-xs transition shadow-lg shadow-[#7C3AED]/30"
            >
              Salvar Cliente
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};
