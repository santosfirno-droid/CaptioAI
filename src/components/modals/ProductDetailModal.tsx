import React, { useState } from 'react';
import { X, Package, Trash2, Edit2, Plus, Minus } from 'lucide-react';
import { Product } from '../../types';
import { useCaptio } from '../../context/CaptioContext';

interface ProductDetailModalProps {
  product: Product | null;
  isOpen: boolean;
  onClose: () => void;
}

export const ProductDetailModal: React.FC<ProductDetailModalProps> = ({
  product,
  isOpen,
  onClose,
}) => {
  const { updateProduct, deleteProduct } = useCaptio();
  const [stock, setStock] = useState(product?.stock || 0);

  React.useEffect(() => {
    if (product) setStock(product.stock);
  }, [product]);

  if (!isOpen || !product) return null;

  const handleUpdateStock = (newStock: number) => {
    const val = Math.max(0, newStock);
    setStock(val);
    updateProduct({
      ...product,
      stock: val,
      status: val === 0 ? 'Inativo' : product.status,
    });
  };

  const handleToggleStatus = () => {
    const newStatus = product.status === 'Ativo' ? 'Inativo' : 'Ativo';
    updateProduct({ ...product, status: newStatus });
  };

  const handleDelete = () => {
    if (confirm(`Deseja excluir o produto "${product.name}"?`)) {
      deleteProduct(product.id);
      onClose();
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/75 backdrop-blur-sm animate-fadeIn">
      <div className="w-full max-w-sm bg-[#22164D] border border-[#7C3AED]/40 rounded-3xl p-5 shadow-2xl">
        <div className="flex items-center justify-between pb-3 border-b border-[#7C3AED]/20">
          <div className="flex items-center gap-2">
            <div className="w-8 h-8 rounded-full bg-[#F59E0B]/20 flex items-center justify-center text-[#F59E0B]">
              <Package className="w-4 h-4" />
            </div>
            <div>
              <h3 className="text-sm font-bold text-white line-clamp-1">{product.name}</h3>
              <span className="text-[10px] text-[#A2A2BF]">{product.category}</span>
            </div>
          </div>
          <button
            onClick={onClose}
            className="p-1 rounded-full text-[#A2A2BF] hover:text-white hover:bg-white/10 transition"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        <div className="my-4 space-y-3 bg-[#181038] border border-[#7C3AED]/20 rounded-2xl p-4 text-xs">
          <div className="flex items-center justify-between">
            <span className="text-[#A2A2BF]">Preço Unitário:</span>
            <span className="text-base font-extrabold text-[#00FF66]">
              R$ {product.price.toFixed(2).replace('.', ',')}
            </span>
          </div>

          <div className="flex items-center justify-between">
            <span className="text-[#A2A2BF]">Status de Venda:</span>
            <button
              onClick={handleToggleStatus}
              className={`px-2.5 py-0.5 rounded-full text-[10px] font-bold border transition ${
                product.status === 'Ativo'
                  ? 'bg-[#00FF66]/20 text-[#00FF66] border-[#00FF66]/40'
                  : 'bg-red-500/20 text-red-400 border-red-500/40'
              }`}
            >
              {product.status} (Clique p/ alternar)
            </button>
          </div>

          {/* Quick Stock adjust */}
          <div className="pt-2 border-t border-[#7C3AED]/20">
            <label className="text-[#A2A2BF] block mb-2 font-medium">Controle de Estoque:</label>
            <div className="flex items-center justify-between bg-[#22164D] rounded-xl p-2 border border-[#7C3AED]/30">
              <button
                onClick={() => handleUpdateStock(stock - 1)}
                className="w-8 h-8 rounded-lg bg-white/10 hover:bg-white/20 text-white flex items-center justify-center font-bold"
              >
                <Minus className="w-4 h-4" />
              </button>
              <div className="text-center">
                <span className="text-lg font-black text-white">{stock}</span>
                <span className="text-[10px] text-[#A2A2BF] block">unidades</span>
              </div>
              <button
                onClick={() => handleUpdateStock(stock + 1)}
                className="w-8 h-8 rounded-lg bg-[#00FF66] hover:bg-[#00CC52] text-black flex items-center justify-center font-bold"
              >
                <Plus className="w-4 h-4" />
              </button>
            </div>
          </div>
        </div>

        <div className="flex gap-2">
          <button
            onClick={onClose}
            className="flex-1 py-2.5 rounded-xl bg-white/10 hover:bg-white/15 text-white font-semibold text-xs transition"
          >
            Concluído
          </button>
          <button
            onClick={handleDelete}
            className="py-2.5 px-3 rounded-xl bg-red-500/15 hover:bg-red-500/25 text-red-400 font-semibold text-xs transition flex items-center justify-center gap-1"
          >
            <Trash2 className="w-3.5 h-3.5" />
            Excluir
          </button>
        </div>
      </div>
    </div>
  );
};
