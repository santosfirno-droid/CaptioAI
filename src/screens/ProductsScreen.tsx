import React, { useState } from 'react';
import {
  ArrowLeft,
  Search,
  Filter,
  Package,
  Plus,
  Scissors,
  Sparkles,
  Shirt,
  Layers,
  ChevronRight,
} from 'lucide-react';
import { useCaptio } from '../context/CaptioContext';
import { Product } from '../types';

interface ProductsScreenProps {
  onAddProduct: () => void;
  onSelectProduct: (product: Product) => void;
}

export const ProductsScreen: React.FC<ProductsScreenProps> = ({
  onAddProduct,
  onSelectProduct,
}) => {
  const {
    setCurrentScreen,
    products,
    filteredProducts,
    productSearchQuery,
    setProductSearchQuery,
    productFilterTab,
    setProductFilterTab,
    productCategory,
    setProductCategory,
  } = useCaptio();

  const [showCategoryMenu, setShowCategoryMenu] = useState(false);
  const categories = ['Todas', 'Prótese', 'Insumos', 'Cosméticos', 'Vestuário'];

  const totalCount = products.length;
  const activeCount = products.filter((p) => p.status === 'Ativo').length;
  const inactiveCount = products.filter((p) => p.status === 'Inativo').length;

  const getCategoryIcon = (cat: string) => {
    switch (cat) {
      case 'Prótese':
        return <Scissors className="w-4 h-4 text-[#00FF66]" />;
      case 'Insumos':
        return <Layers className="w-4 h-4 text-[#8B5CF6]" />;
      case 'Cosméticos':
        return <Sparkles className="w-4 h-4 text-[#00E5FF]" />;
      case 'Vestuário':
      default:
        return <Shirt className="w-4 h-4 text-[#F59E0B]" />;
    }
  };

  return (
    <div className="min-h-full pb-24 px-4 pt-3 space-y-3.5">
      {/* Top Bar */}
      <div className="flex items-center justify-between pb-1">
        <button
          onClick={() => setCurrentScreen('HOME')}
          data-testid="products_back_btn"
          className="w-10 h-10 rounded-full flex items-center justify-center text-white hover:bg-white/5 active:scale-95 transition"
        >
          <ArrowLeft className="w-5 h-5" />
        </button>

        <h2 className="text-base font-bold text-white">
          {productCategory === 'Todas' ? 'Produtos' : `Produtos: ${productCategory}`}
        </h2>

        <div className="relative">
          <button
            onClick={() => setShowCategoryMenu(!showCategoryMenu)}
            data-testid="products_filter_btn"
            className="w-10 h-10 rounded-full bg-[#181038] border border-[#7C3AED]/20 flex items-center justify-center text-white hover:border-[#7C3AED]/60 active:scale-95 transition"
            title="Filtrar Categoria"
          >
            <Filter className="w-4 h-4" />
          </button>

          {showCategoryMenu && (
            <div className="absolute right-0 top-12 w-44 bg-[#22164D] border border-[#7C3AED]/40 rounded-2xl p-2 shadow-2xl z-30 animate-fadeIn">
              {categories.map((c) => (
                <button
                  key={c}
                  onClick={() => {
                    setProductCategory(c);
                    setShowCategoryMenu(false);
                  }}
                  className={`w-full text-left px-3 py-2 text-xs rounded-xl font-medium transition ${
                    productCategory === c
                      ? 'bg-[#7C3AED] text-white font-bold'
                      : 'text-[#A2A2BF] hover:text-white'
                  }`}
                >
                  {c}
                </button>
              ))}
            </div>
          )}
        </div>
      </div>

      {/* Category Pills Strip */}
      <div className="flex gap-1.5 overflow-x-auto pb-1 no-scrollbar">
        {categories.map((cat) => {
          const isSelected = productCategory === cat;
          return (
            <button
              key={cat}
              onClick={() => setProductCategory(cat)}
              className={`py-1.5 px-3 rounded-full text-xs font-semibold whitespace-nowrap transition ${
                isSelected
                  ? 'bg-[#00FF66] text-black font-extrabold shadow-sm shadow-[#00FF66]/30'
                  : 'bg-[#181038] text-[#A2A2BF] hover:text-white border border-[#7C3AED]/20'
              }`}
            >
              {cat}
            </button>
          );
        })}
      </div>

      {/* Search Bar */}
      <div className="flex items-center w-full h-11 px-4 rounded-full bg-[#181038] border border-[#7C3AED]/20 focus-within:border-[#00FF66]/60 transition-all">
        <Search className="w-4 h-4 text-[#A2A2BF] mr-2.5 shrink-0" />
        <input
          type="text"
          value={productSearchQuery}
          onChange={(e) => setProductSearchQuery(e.target.value)}
          placeholder="Buscar produto, marca ou tipo..."
          className="w-full bg-transparent text-white text-xs placeholder-[#A2A2BF] focus:outline-none"
        />
        {productSearchQuery && (
          <button
            onClick={() => setProductSearchQuery('')}
            className="text-[#A2A2BF] text-xs hover:text-white"
          >
            Limpar
          </button>
        )}
      </div>

      {/* Filter Tabs: Todos, Ativos, Inativos */}
      <div className="grid grid-cols-3 gap-2 bg-[#181038] p-1 rounded-2xl border border-[#7C3AED]/20">
        <button
          onClick={() => setProductFilterTab('Todos')}
          className={`py-2 rounded-xl text-xs font-semibold transition ${
            productFilterTab === 'Todos'
              ? 'bg-[#7C3AED] text-white font-bold shadow-md'
              : 'text-[#A2A2BF] hover:text-white'
          }`}
        >
          Todos ({totalCount})
        </button>

        <button
          onClick={() => setProductFilterTab('Ativos')}
          className={`py-2 rounded-xl text-xs font-semibold transition ${
            productFilterTab === 'Ativos'
              ? 'bg-[#7C3AED] text-white font-bold shadow-md'
              : 'text-[#A2A2BF] hover:text-white'
          }`}
        >
          Ativos ({activeCount})
        </button>

        <button
          onClick={() => setProductFilterTab('Inativos')}
          className={`py-2 rounded-xl text-xs font-semibold transition ${
            productFilterTab === 'Inativos'
              ? 'bg-[#7C3AED] text-white font-bold shadow-md'
              : 'text-[#A2A2BF] hover:text-white'
          }`}
        >
          Inativos ({inactiveCount})
        </button>
      </div>

      {/* Products List */}
      <div className="space-y-2.5">
        {filteredProducts.length === 0 ? (
          <div className="p-8 rounded-2xl bg-[#181038] border border-[#7C3AED]/20 text-center space-y-2">
            <p className="text-xs text-[#A2A2BF]">Nenhum produto encontrado com estes filtros.</p>
            <button
              onClick={onAddProduct}
              className="text-xs font-bold text-[#00FF66] hover:underline"
            >
              Cadastrar novo produto
            </button>
          </div>
        ) : (
          filteredProducts.map((prod) => (
            <div
              key={prod.id}
              onClick={() => onSelectProduct(prod)}
              className="p-3.5 rounded-2xl bg-[#181038] hover:bg-[#201548] border border-[#7C3AED]/20 hover:border-[#7C3AED]/50 transition flex items-center gap-3 cursor-pointer group"
            >
              <div className="w-10 h-10 rounded-xl bg-[#22164D] border border-[#7C3AED]/30 flex items-center justify-center shrink-0 group-hover:scale-105 transition-transform">
                {getCategoryIcon(prod.category)}
              </div>

              <div className="flex-1 min-w-0">
                <div className="flex items-center gap-2">
                  <span className="text-[10px] font-bold uppercase text-[#A2A2BF] tracking-wider">
                    {prod.category}
                  </span>
                  <span
                    className={`text-[9px] font-bold px-1.5 rounded ${
                      prod.status === 'Ativo'
                        ? 'bg-[#00FF66]/15 text-[#00FF66]'
                        : 'bg-red-500/15 text-red-400'
                    }`}
                  >
                    {prod.status}
                  </span>
                </div>

                <h4 className="text-xs font-bold text-white truncate mt-0.5">{prod.name}</h4>

                <div className="flex items-center gap-3 mt-1 text-xs">
                  <span className="font-extrabold text-[#00FF66]">
                    R$ {prod.price.toFixed(2).replace('.', ',')}
                  </span>
                  <span className="text-[11px] text-[#A2A2BF]">
                    Estoque: <strong className="text-white font-bold">{prod.stock} un.</strong>
                  </span>
                </div>
              </div>

              <ChevronRight className="w-4 h-4 text-[#A2A2BF] group-hover:text-white group-hover:translate-x-0.5 transition shrink-0" />
            </div>
          ))
        )}
      </div>

      {/* FAB */}
      <button
        onClick={onAddProduct}
        className="fixed bottom-20 right-4 sm:right-auto sm:left-[calc(50%+150px)] w-12 h-12 rounded-full bg-[#00FF66] text-black flex items-center justify-center shadow-xl shadow-[#00FF66]/30 hover:scale-105 active:scale-95 transition z-30"
        title="Novo Produto"
      >
        <Plus className="w-6 h-6 stroke-[3]" />
      </button>
    </div>
  );
};
