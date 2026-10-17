import React, { useState } from 'react';
import {
  ArrowLeft,
  Search,
  Filter,
  UserPlus,
  Phone,
  MessageSquare,
  Scissors,
  ChevronRight,
  Plus,
} from 'lucide-react';
import { useCaptio } from '../context/CaptioContext';
import { Client } from '../types';

interface ClientsScreenProps {
  onAddClient: () => void;
  onSelectClient: (client: Client) => void;
  onOpenWhatsApp: (phone: string, msg: string) => void;
}

export const ClientsScreen: React.FC<ClientsScreenProps> = ({
  onAddClient,
  onSelectClient,
  onOpenWhatsApp,
}) => {
  const {
    setCurrentScreen,
    clients,
    filteredClients,
    clientSearchQuery,
    setClientSearchQuery,
    clientFilterTab,
    setClientFilterTab,
    clientSortOrder,
    setClientSortOrder,
  } = useCaptio();

  const [showSortMenu, setShowSortMenu] = useState(false);

  const totalCount = clients.length;
  const activeCount = clients.filter((c) => c.status === 'Ativo').length;
  const inactiveCount = clients.filter((c) => c.status === 'Inativo').length;

  return (
    <div className="min-h-full pb-24 px-4 pt-3 space-y-3.5">
      {/* Top Bar */}
      <div className="flex items-center justify-between pb-1">
        <button
          onClick={() => setCurrentScreen('HOME')}
          data-testid="clients_back_btn"
          className="w-10 h-10 rounded-full flex items-center justify-center text-white hover:bg-white/5 active:scale-95 transition"
        >
          <ArrowLeft className="w-5 h-5" />
        </button>

        <h2 className="text-base font-bold text-white">Clientes</h2>

        <div className="relative">
          <button
            onClick={() => setShowSortMenu(!showSortMenu)}
            data-testid="clients_filter_btn"
            className="w-10 h-10 rounded-full bg-[#181038] border border-[#7C3AED]/20 flex items-center justify-center text-white hover:border-[#7C3AED]/60 active:scale-95 transition"
            title="Ordenar"
          >
            <Filter className="w-4 h-4" />
          </button>

          {showSortMenu && (
            <div className="absolute right-0 top-12 w-48 bg-[#22164D] border border-[#7C3AED]/40 rounded-2xl p-2 shadow-2xl z-30 animate-fadeIn">
              <button
                onClick={() => {
                  setClientSortOrder('Nome');
                  setShowSortMenu(false);
                }}
                className={`w-full text-left px-3 py-2 text-xs rounded-xl font-medium transition ${
                  clientSortOrder === 'Nome'
                    ? 'bg-[#7C3AED] text-white font-bold'
                    : 'text-[#A2A2BF] hover:text-white'
                }`}
              >
                Ordenar por Nome (A-Z)
              </button>
              <button
                onClick={() => {
                  setClientSortOrder('Serviços');
                  setShowSortMenu(false);
                }}
                className={`w-full text-left px-3 py-2 text-xs rounded-xl font-medium transition mt-1 ${
                  clientSortOrder === 'Serviços'
                    ? 'bg-[#7C3AED] text-white font-bold'
                    : 'text-[#A2A2BF] hover:text-white'
                }`}
              >
                Mais Atendimentos
              </button>
            </div>
          )}
        </div>
      </div>

      {/* Search Bar */}
      <div className="flex items-center w-full h-11 px-4 rounded-full bg-[#181038] border border-[#7C3AED]/20 focus-within:border-[#00FF66]/60 transition-all">
        <Search className="w-4 h-4 text-[#A2A2BF] mr-2.5 shrink-0" />
        <input
          type="text"
          value={clientSearchQuery}
          onChange={(e) => setClientSearchQuery(e.target.value)}
          placeholder="Buscar por nome, prótese ou telefone..."
          className="w-full bg-transparent text-white text-xs placeholder-[#A2A2BF] focus:outline-none"
        />
        {clientSearchQuery && (
          <button
            onClick={() => setClientSearchQuery('')}
            className="text-[#A2A2BF] text-xs hover:text-white"
          >
            Limpar
          </button>
        )}
      </div>

      {/* Filter Tabs: Todos, Ativos, Inativos */}
      <div className="grid grid-cols-3 gap-2 bg-[#181038] p-1 rounded-2xl border border-[#7C3AED]/20">
        <button
          onClick={() => setClientFilterTab('Todos')}
          className={`py-2 rounded-xl text-xs font-semibold transition ${
            clientFilterTab === 'Todos'
              ? 'bg-[#7C3AED] text-white font-bold shadow-md'
              : 'text-[#A2A2BF] hover:text-white'
          }`}
        >
          Todos ({totalCount})
        </button>

        <button
          onClick={() => setClientFilterTab('Ativos')}
          className={`py-2 rounded-xl text-xs font-semibold transition ${
            clientFilterTab === 'Ativos'
              ? 'bg-[#7C3AED] text-white font-bold shadow-md'
              : 'text-[#A2A2BF] hover:text-white'
          }`}
        >
          Ativos ({activeCount})
        </button>

        <button
          onClick={() => setClientFilterTab('Inativos')}
          className={`py-2 rounded-xl text-xs font-semibold transition ${
            clientFilterTab === 'Inativos'
              ? 'bg-[#7C3AED] text-white font-bold shadow-md'
              : 'text-[#A2A2BF] hover:text-white'
          }`}
        >
          Inativos ({inactiveCount})
        </button>
      </div>

      {/* Clients List */}
      <div className="space-y-2.5">
        {filteredClients.length === 0 ? (
          <div className="p-8 rounded-2xl bg-[#181038] border border-[#7C3AED]/20 text-center space-y-2">
            <p className="text-xs text-[#A2A2BF]">Nenhum cliente encontrado com estes filtros.</p>
            <button
              onClick={onAddClient}
              className="text-xs font-bold text-[#00FF66] hover:underline"
            >
              Cadastrar novo cliente
            </button>
          </div>
        ) : (
          filteredClients.map((client) => (
            <div
              key={client.id}
              onClick={() => onSelectClient(client)}
              className="p-3.5 rounded-2xl bg-[#181038] hover:bg-[#201548] border border-[#7C3AED]/20 hover:border-[#7C3AED]/50 transition flex items-center gap-3 cursor-pointer group"
            >
              <div className="w-10 h-10 rounded-full bg-gradient-to-tr from-[#6366F1] to-[#4F46E5] flex items-center justify-center text-white font-bold text-xs shrink-0 group-hover:scale-105 transition-transform">
                {client.name
                  .split(' ')
                  .map((n) => n[0])
                  .slice(0, 2)
                  .join('')}
              </div>

              <div className="flex-1 min-w-0">
                <div className="flex items-center gap-2">
                  <h4 className="text-xs font-bold text-white truncate">{client.name}</h4>
                  <span
                    className={`text-[9px] font-bold px-1.5 py-0.2 rounded-full ${
                      client.status === 'Ativo'
                        ? 'bg-[#00FF66]/20 text-[#00FF66]'
                        : 'bg-red-500/20 text-red-400'
                    }`}
                  >
                    {client.status}
                  </span>
                </div>

                <p className="text-[11px] text-[#00FF66] font-medium truncate flex items-center gap-1 mt-0.5">
                  <Scissors className="w-3 h-3 shrink-0" />
                  {client.prosthesisType}
                </p>

                <p className="text-[10px] text-[#A2A2BF] truncate mt-0.5">
                  {client.servicesCount} {client.servicesCount === 1 ? 'serviço' : 'serviços'} • {client.lastService}
                </p>
              </div>

              <div className="flex items-center gap-1.5 shrink-0">
                <button
                  onClick={(e) => {
                    e.stopPropagation();
                    onOpenWhatsApp(
                      client.phone,
                      `Olá ${client.name}! Como estão os cuidados com sua prótese capilar? Estamos à disposição.`
                    );
                  }}
                  className="p-2 rounded-xl bg-white/5 hover:bg-[#00FF66]/20 text-[#A2A2BF] hover:text-[#00FF66] transition"
                  title="Conversar no WhatsApp"
                >
                  <MessageSquare className="w-4 h-4" />
                </button>
                <ChevronRight className="w-4 h-4 text-[#A2A2BF] group-hover:text-white group-hover:translate-x-0.5 transition" />
              </div>
            </div>
          ))
        )}
      </div>

      {/* FAB */}
      <button
        onClick={onAddClient}
        className="fixed bottom-20 right-4 sm:right-auto sm:left-[calc(50%+150px)] w-12 h-12 rounded-full bg-[#00FF66] text-black flex items-center justify-center shadow-xl shadow-[#00FF66]/30 hover:scale-105 active:scale-95 transition z-30"
        title="Novo Cliente"
      >
        <Plus className="w-6 h-6 stroke-[3]" />
      </button>
    </div>
  );
};
