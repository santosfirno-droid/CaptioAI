import React, { useState } from 'react';
import { X, Package } from 'lucide-react';
import { useCaptio } from '../../context/CaptioContext';

interface AddProductModalProps {
  isOpen: boolean;
  onClose: () => void;
}

export const AddProductModal: React.FC<AddProductModalProps> = ({ isOpen, onClose }) => {
  const { addProduct } = useCaptio();

  const [name, setName] = useState('');
  const [price, setPrice] = useState('');
  const [category, setCategory] = useState<'Vestuário' | 'Prótese' | 'Insumos' | 'Cosméticos'>('Insumos');
  const [stock, setStock] = useState('10');
  const [status, setStatus] = useState<'Ativo' | 'Inativo'>('Ativo');

  if (!isOpen) return null;

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!name.trim()) return;

    const parsedPrice = parseFloat(price.replace(',', '.')) || 0;
    const parsedStock = parseInt(stock, 10) || 0;

    addProduct(name.trim(), parsedPrice, category, parsedStock, status);
    onClose();
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/75 backdrop-blur-sm animate-fadeIn">
      <div className="w-full max-w-md bg-[#22164D] border border-[#7C3AED]/40 rounded-3xl p-5 shadow-2xl">
        <div className="flex items-center justify-between pb-3 border-b border-[#7C3AED]/20">
          <div className="flex items-center gap-2">
            <div className="w-8 h-8 rounded-full bg-[#F59E0B]/20 flex items-center justify-center text-[#F59E0B]">
              <Package className="w-4 h-4" />
            </div>
            <h3 className="text-base font-bold text-white">Novo Produto</h3>
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
            <label className="text-xs font-semibold text-[#A2A2BF] block mb-1">Nome do Item</label>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="Ex: Fita Ultra Hold 3 Jardas"
              required
              className="w-full px-3.5 py-2.5 rounded-xl bg-[#181038] border border-[#7C3AED]/30 text-white placeholder-[#A2A2BF]/50 text-sm focus:outline-none focus:border-[#00FF66]"
            />
          </div>

          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="text-xs font-semibold text-[#A2A2BF] block mb-1">Preço (R$)</label>
              <input
                type="text"
                value={price}
                onChange={(e) => setPrice(e.target.value)}
                placeholder="89,90"
                required
                className="w-full px-3.5 py-2.5 rounded-xl bg-[#181038] border border-[#7C3AED]/30 text-white placeholder-[#A2A2BF]/50 text-sm focus:outline-none focus:border-[#00FF66]"
              />
            </div>
            <div>
              <label className="text-xs font-semibold text-[#A2A2BF] block mb-1">Estoque Inicial</label>
              <input
                type="number"
                value={stock}
                onChange={(e) => setStock(e.target.value)}
                min="0"
                className="w-full px-3.5 py-2.5 rounded-xl bg-[#181038] border border-[#7C3AED]/30 text-white text-sm focus:outline-none focus:border-[#00FF66]"
              />
            </div>
          </div>

          <div>
            <label className="text-xs font-semibold text-[#A2A2BF] block mb-1">Categoria</label>
            <select
              value={category}
              onChange={(e) => setCategory(e.target.value as any)}
              className="w-full px-3.5 py-2.5 rounded-xl bg-[#181038] border border-[#7C3AED]/30 text-white text-sm focus:outline-none focus:border-[#00FF66]"
            >
              <option value="Insumos">Insumos (Fitas, Colas, Solventes)</option>
              <option value="Prótese">Prótese Capilar</option>
              <option value="Cosméticos">Cosméticos (Shampoos, Protetores)</option>
              <option value="Vestuário">Vestuário (Camisetas, Bonés)</option>
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
              className="flex-1 py-2.5 rounded-xl bg-[#F59E0B] hover:bg-[#D97706] disabled:opacity-40 text-black font-extrabold text-xs transition shadow-lg shadow-[#F59E0B]/30"
            >
              Salvar Produto
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};
