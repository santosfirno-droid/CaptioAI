import React, { useState } from 'react';
import {
  ArrowLeft,
  Link as LinkIcon,
  Copy,
  Share2,
  Calendar,
  Clock,
  Check,
  CheckCircle,
  Plus,
  MessageSquare,
  X,
  AlertCircle,
} from 'lucide-react';
import { useCaptio } from '../context/CaptioContext';
import { Appointment, ServiceRequest } from '../types';

interface AtendimentosScreenProps {
  onQuickAdd: () => void;
  onSelectAppointment: (appt: Appointment) => void;
  onConfirmRequest: (req: ServiceRequest) => void;
  onOpenWhatsApp: (phone: string, msg: string) => void;
}

export const AtendimentosScreen: React.FC<AtendimentosScreenProps> = ({
  onQuickAdd,
  onSelectAppointment,
  onConfirmRequest,
  onOpenWhatsApp,
}) => {
  const {
    setCurrentScreen,
    atendimentosTab,
    setAtendimentosTab,
    selectedDay,
    setSelectedDay,
    filteredAppointments,
    serviceRequests,
    pendingRequestsCount,
    rejectServiceRequest,
  } = useCaptio();

  const [copiedLink, setCopiedLink] = useState(false);

  const daysOfWeek = [
    { label: 'Seg', num: '28' },
    { label: 'Ter', num: '29' },
    { label: 'Qua', num: '30' },
    { label: 'Qui', num: '31' },
    { label: 'Sex', num: '01' },
    { label: 'Sáb', num: '02' },
  ];

  const handleCopyLink = () => {
    navigator.clipboard.writeText('https://captio.app/bruno-protese');
    setCopiedLink(true);
    setTimeout(() => setCopiedLink(false), 2500);
  };

  const handleShareLink = () => {
    if (navigator.share) {
      navigator.share({
        title: 'CAPTIO - Agendamento de Prótese Capilar',
        text: 'Olá! Para solicitar sua avaliação ou manutenção de prótese capilar comigo, acesse:',
        url: 'https://captio.app/bruno-protese',
      }).catch(() => {});
    } else {
      handleCopyLink();
    }
  };

  const pendingList = serviceRequests.filter((r) => r.status === 'Pendente');
  const answeredList = serviceRequests.filter((r) => r.status !== 'Pendente');

  return (
    <div className="min-h-full pb-24 px-4 pt-3 space-y-3.5">
      {/* Top Bar */}
      <div className="flex items-center justify-between pb-1">
        <button
          onClick={() => setCurrentScreen('HOME')}
          data-testid="atendimentos_back_btn"
          className="w-10 h-10 rounded-full flex items-center justify-center text-white hover:bg-white/5 active:scale-95 transition"
        >
          <ArrowLeft className="w-5 h-5" />
        </button>

        <h2 className="text-base font-bold text-white">Atendimentos</h2>

        <button
          onClick={() => setCurrentScreen('PUBLIC_REQUEST')}
          data-testid="open_public_link_btn"
          className="w-10 h-10 rounded-full bg-[#181038] border border-[#7C3AED]/20 flex items-center justify-center text-[#00FF66] hover:border-[#00FF66]/50 active:scale-95 transition"
          title="Ver Página Pública do Cliente"
        >
          <LinkIcon className="w-4 h-4" />
        </button>
      </div>

      {/* Public Link Banner */}
      <div className="p-3.5 rounded-2xl bg-gradient-to-r from-[#23144E] to-[#180D38] border border-[#7C3AED]/30 flex items-center justify-between gap-3 shadow-md">
        <div className="flex items-center gap-3 min-w-0">
          <div className="w-9 h-9 rounded-full bg-[#00FF66]/15 flex items-center justify-center text-[#00FF66] shrink-0">
            <LinkIcon className="w-4 h-4" />
          </div>
          <div className="min-w-0">
            <div className="text-[11px] font-bold text-white">Link Público de Solicitações</div>
            <div
              onClick={() => setCurrentScreen('PUBLIC_REQUEST')}
              className="text-[11px] font-mono text-[#00FF66] truncate cursor-pointer hover:underline"
            >
              captio.app/bruno-protese
            </div>
          </div>
        </div>

        <div className="flex items-center gap-1.5 shrink-0">
          <button
            onClick={handleCopyLink}
            className="p-2 rounded-xl bg-white/5 hover:bg-white/10 text-white transition text-xs flex items-center gap-1"
            title="Copiar Link"
          >
            {copiedLink ? <Check className="w-3.5 h-3.5 text-[#00FF66]" /> : <Copy className="w-3.5 h-3.5" />}
          </button>
          <button
            onClick={handleShareLink}
            className="p-2 rounded-xl bg-[#00FF66]/20 hover:bg-[#00FF66]/30 text-[#00FF66] transition text-xs"
            title="Compartilhar"
          >
            <Share2 className="w-3.5 h-3.5" />
          </button>
        </div>
      </div>

      {/* Main Tabs: Confirmados / Solicitações */}
      <div className="grid grid-cols-2 gap-2 bg-[#181038] p-1 rounded-2xl border border-[#7C3AED]/20">
        <button
          onClick={() => setAtendimentosTab('Confirmados')}
          className={`py-2 px-3 rounded-xl text-xs font-bold transition flex items-center justify-center gap-1.5 ${
            atendimentosTab === 'Confirmados'
              ? 'bg-[#7C3AED] text-white shadow-md'
              : 'text-[#A2A2BF] hover:text-white'
          }`}
        >
          <Calendar className="w-3.5 h-3.5" />
          Confirmados
        </button>

        <button
          onClick={() => setAtendimentosTab('Solicitações')}
          className={`py-2 px-3 rounded-xl text-xs font-bold transition flex items-center justify-center gap-1.5 ${
            atendimentosTab === 'Solicitações'
              ? 'bg-[#7C3AED] text-white shadow-md'
              : 'text-[#A2A2BF] hover:text-white'
          }`}
        >
          <MessageSquare className="w-3.5 h-3.5" />
          Solicitações
          {pendingRequestsCount > 0 && (
            <span className="w-5 h-5 rounded-full bg-[#00FF66] text-black font-extrabold text-[10px] flex items-center justify-center ml-0.5">
              {pendingRequestsCount}
            </span>
          )}
        </button>
      </div>

      {atendimentosTab === 'Confirmados' ? (
        <div className="space-y-3.5">
          {/* Horizontal Day Selector Strip */}
          <div className="grid grid-cols-6 gap-1.5">
            {daysOfWeek.map((d) => {
              const isSelected = selectedDay === d.num;
              return (
                <button
                  key={d.num}
                  onClick={() => setSelectedDay(d.num)}
                  className={`py-2 px-1 rounded-xl text-center transition ${
                    isSelected
                      ? 'bg-[#7C3AED] text-white font-bold shadow-md shadow-[#7C3AED]/40 border border-[#8B5CF6]'
                      : 'bg-[#181038] text-[#A2A2BF] hover:text-white border border-[#7C3AED]/20'
                  }`}
                >
                  <div className="text-[10px] font-medium">{d.label}</div>
                  <div
                    className={`text-xs font-extrabold mt-0.5 ${
                      isSelected ? 'text-[#00FF66]' : 'text-white'
                    }`}
                  >
                    {d.num}
                  </div>
                </button>
              );
            })}
          </div>

          {/* Appointments Timeline */}
          <div className="space-y-2">
            {filteredAppointments.length === 0 ? (
              <div className="p-6 rounded-2xl bg-[#181038] border border-[#7C3AED]/20 text-center space-y-2">
                <p className="text-xs text-[#A2A2BF]">Nenhum atendimento confirmado para este dia.</p>
                <button
                  onClick={onQuickAdd}
                  className="text-xs font-bold text-[#00FF66] hover:underline"
                >
                  Toque aqui para agendar um horário
                </button>
              </div>
            ) : (
              filteredAppointments.map((appt) => {
                const badgeColor =
                  appt.status === 'Confirmado'
                    ? 'text-[#00FF66] bg-[#00FF66]/15'
                    : appt.status === 'Concluído'
                    ? 'text-sky-400 bg-sky-400/15'
                    : 'text-[#F59E0B] bg-[#F59E0B]/15';

                return (
                  <div
                    key={appt.id}
                    onClick={() => onSelectAppointment(appt)}
                    className="p-3 rounded-2xl bg-[#181038] hover:bg-[#201548] border border-[#7C3AED]/20 hover:border-[#7C3AED]/50 transition flex items-center gap-3 cursor-pointer"
                  >
                    <div className="text-center w-12 shrink-0 border-r border-[#7C3AED]/20 pr-2">
                      <div className="text-xs font-black text-white">{appt.time}</div>
                      <div className="text-[10px] text-[#A2A2BF]">{appt.durationMinutes}m</div>
                    </div>

                    <div className="w-8 h-8 rounded-full bg-gradient-to-tr from-[#6366F1] to-[#4F46E5] flex items-center justify-center text-white font-bold text-xs shrink-0">
                      {appt.clientName
                        .split(' ')
                        .map((n) => n[0])
                        .slice(0, 2)
                        .join('')}
                    </div>

                    <div className="flex-1 min-w-0">
                      <h4 className="text-xs font-bold text-white truncate">{appt.clientName}</h4>
                      <p className="text-[11px] text-[#A2A2BF] truncate">{appt.service}</p>
                    </div>

                    <span className={`px-2 py-0.5 rounded-lg text-[10px] font-semibold shrink-0 ${badgeColor}`}>
                      {appt.status}
                    </span>
                  </div>
                );
              })
            )}

            <button
              onClick={onQuickAdd}
              className="w-full py-2.5 rounded-xl bg-[#7C3AED]/20 hover:bg-[#7C3AED]/30 border border-[#7C3AED]/40 text-[#8B5CF6] text-xs font-bold transition flex items-center justify-center gap-2 mt-2"
            >
              <Plus className="w-4 h-4" />
              Novo Atendimento Manual
            </button>
          </div>
        </div>
      ) : (
        /* Solicitações Tab */
        <div className="space-y-4">
          {pendingList.length === 0 && answeredList.length === 0 ? (
            <div className="p-8 rounded-2xl bg-[#181038] text-center text-xs text-[#A2A2BF] leading-relaxed">
              Nenhuma solicitação recebida ainda.
              <br />
              Compartilhe seu link público com clientes!
            </div>
          ) : null}

          {pendingList.length > 0 && (
            <div className="space-y-2.5">
              <h3 className="text-xs font-bold text-[#00FF66] uppercase tracking-wider flex items-center gap-1.5">
                <AlertCircle className="w-4 h-4" />
                Aguardando Confirmação ({pendingList.length})
              </h3>

              {pendingList.map((req) => (
                <div
                  key={req.id}
                  className="p-3.5 rounded-2xl bg-[#181038] border border-[#00FF66]/40 shadow-lg shadow-[#00FF66]/5 space-y-2.5"
                >
                  <div className="flex items-start justify-between gap-2">
                    <div>
                      <h4 className="text-sm font-bold text-white">{req.clientName}</h4>
                      <p className="text-xs text-[#00FF66] font-mono">{req.phone}</p>
                    </div>
                    <span className="px-2 py-0.5 rounded-full text-[10px] font-bold bg-[#F59E0B]/20 text-[#F59E0B] border border-[#F59E0B]/40">
                      Pendente
                    </span>
                  </div>

                  <div className="text-xs space-y-0.5">
                    <p className="font-semibold text-white">Serviço: {req.serviceType}</p>
                    <p className="text-[#A2A2BF]">
                      Preferência: {req.preferredDate} ({req.preferredPeriod})
                    </p>
                    {req.notes && (
                      <p className="text-white/80 italic text-[11px] pt-1">"{req.notes}"</p>
                    )}
                  </div>

                  <div className="flex items-center gap-2 pt-1 border-t border-[#7C3AED]/20">
                    <button
                      onClick={() =>
                        onOpenWhatsApp(
                          req.phone,
                          `Olá ${req.clientName}! Vi sua solicitação no CAPTIO para ${req.serviceType}. Gostaria de combinar o melhor horário.`
                        )
                      }
                      className="py-1.5 px-3 rounded-xl bg-white/10 hover:bg-white/15 text-white text-xs font-medium transition flex items-center gap-1.5 flex-1 justify-center"
                    >
                      <MessageSquare className="w-3.5 h-3.5 text-[#00FF66]" />
                      WhatsApp
                    </button>

                    <button
                      onClick={() => onConfirmRequest(req)}
                      className="py-1.5 px-3 rounded-xl bg-[#00FF66] hover:bg-[#00CC52] text-black font-extrabold text-xs transition flex-1 text-center"
                    >
                      Aceitar
                    </button>

                    <button
                      onClick={() => rejectServiceRequest(req.id)}
                      className="p-1.5 rounded-xl bg-red-500/15 hover:bg-red-500/25 text-red-400 transition"
                      title="Recusar"
                    >
                      <X className="w-4 h-4" />
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}

          {answeredList.length > 0 && (
            <div className="space-y-2.5 pt-2">
              <h3 className="text-xs font-bold text-[#A2A2BF] uppercase tracking-wider">
                Histórico de Solicitações
              </h3>

              {answeredList.map((req) => (
                <div
                  key={req.id}
                  className="p-3 rounded-2xl bg-[#181038] border border-[#7C3AED]/15 flex items-center justify-between text-xs"
                >
                  <div>
                    <h4 className="font-bold text-white">{req.clientName}</h4>
                    <p className="text-[11px] text-[#A2A2BF]">{req.serviceType}</p>
                  </div>

                  <span
                    className={`px-2 py-0.5 rounded text-[10px] font-bold ${
                      req.status === 'Aceito'
                        ? 'bg-[#00FF66]/20 text-[#00FF66]'
                        : 'bg-red-500/20 text-red-400'
                    }`}
                  >
                    {req.status}
                  </span>
                </div>
              ))}
            </div>
          )}
        </div>
      )}
    </div>
  );
};
