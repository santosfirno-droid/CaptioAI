import React, { useState, useMemo } from 'react';
import { X, Calendar, AlertTriangle, Clock } from 'lucide-react';
import { useCaptio } from '../../context/CaptioContext';
import { Client } from '../../types';

interface AddAppointmentModalProps {
  isOpen: boolean;
  onClose: () => void;
  preselectedClient?: Client;
  onSuccessWithMessage?: (phone: string, message: string) => void;
}

export const AddAppointmentModal: React.FC<AddAppointmentModalProps> = ({
  isOpen,
  onClose,
  preselectedClient,
  onSuccessWithMessage,
}) => {
  const { clients, addAppointment, checkAppointmentConflict } = useCaptio();

  const [clientName, setClientName] = useState(preselectedClient?.name || '');
  const [clientPhone, setClientPhone] = useState(preselectedClient?.phone || '(11) 98765-4321');
  const [service, setService] = useState('Instalação de Prótese Capilar');
  const [dayNumber, setDayNumber] = useState('29');
  const [dayOfWeek, setDayOfWeek] = useState('Ter');
  const [time, setTime] = useState('09:00');
  const [durationMinutes, setDurationMinutes] = useState(60);
  const [notes, setNotes] = useState('');

  const days = [
    { label: 'Seg', num: '28' },
    { label: 'Ter', num: '29' },
    { label: 'Qua', num: '30' },
    { label: 'Qui', num: '31' },
    { label: 'Sex', num: '01' },
    { label: 'Sáb', num: '02' },
  ];

  const times = ['08:00', '09:00', '10:00', '11:00', '12:00', '13:30', '14:00', '15:00', '16:30', '17:00'];
  const durations = [45, 60, 90, 120];

  const conflict = useMemo(() => {
    return checkAppointmentConflict(dayNumber, time, durationMinutes);
  }, [dayNumber, time, durationMinutes, checkAppointmentConflict]);

  if (!isOpen) return null;

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!clientName.trim() || conflict) return;

    addAppointment(
      clientName.trim(),
      clientPhone.trim(),
      service,
      time,
      durationMinutes,
      dayNumber,
      dayOfWeek,
      'Confirmado',
      notes.trim()
    );

    const confirmationMsg = `Olá, ${clientName}! Seu agendamento de ${service} está confirmado para ${dayOfWeek} (dia ${dayNumber}), às ${time}. Duração estimada: ${durationMinutes} min. Nos vemos em breve!`;

    onClose();
    if (onSuccessWithMessage) {
      onSuccessWithMessage(clientPhone, confirmationMsg);
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/75 backdrop-blur-sm animate-fadeIn">
      <div className="w-full max-w-md bg-[#22164D] border border-[#7C3AED]/40 rounded-3xl p-5 shadow-2xl max-h-[90vh] overflow-y-auto">
        <div className="flex items-center justify-between pb-3 border-b border-[#7C3AED]/20">
          <div className="flex items-center gap-2">
            <div className="w-8 h-8 rounded-full bg-[#00FF66]/20 flex items-center justify-center text-[#00FF66]">
              <Calendar className="w-4 h-4" />
            </div>
            <h3 className="text-base font-bold text-white">Novo Atendimento</h3>
          </div>
          <button
            onClick={onClose}
            className="p-1 rounded-full text-[#A2A2BF] hover:text-white hover:bg-white/10 transition"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="mt-4 space-y-4">
          {/* Client picker / input */}
          <div>
            <label className="text-xs font-semibold text-[#A2A2BF] block mb-1">Cliente</label>
            <input
              type="text"
              list="client-suggestions"
              value={clientName}
              onChange={(e) => {
                const val = e.target.value;
                setClientName(val);
                const found = clients.find((c) => c.name.toLowerCase() === val.toLowerCase());
                if (found) setClientPhone(found.phone);
              }}
              placeholder="Nome do cliente"
              required
              className="w-full px-3.5 py-2.5 rounded-xl bg-[#181038] border border-[#7C3AED]/30 text-white placeholder-[#A2A2BF]/50 text-sm focus:outline-none focus:border-[#00FF66]"
            />
            <datalist id="client-suggestions">
              {clients.map((c) => (
                <option key={c.id} value={c.name} />
              ))}
            </datalist>
          </div>

          <div>
            <label className="text-xs font-semibold text-[#A2A2BF] block mb-1">WhatsApp / Telefone</label>
            <input
              type="text"
              value={clientPhone}
              onChange={(e) => setClientPhone(e.target.value)}
              placeholder="(11) 98765-4321"
              required
              className="w-full px-3.5 py-2.5 rounded-xl bg-[#181038] border border-[#7C3AED]/30 text-white placeholder-[#A2A2BF]/50 text-sm focus:outline-none focus:border-[#00FF66]"
            />
          </div>

          {/* Service */}
          <div>
            <label className="text-xs font-semibold text-[#A2A2BF] block mb-1">Procedimento</label>
            <select
              value={service}
              onChange={(e) => setService(e.target.value)}
              className="w-full px-3.5 py-2.5 rounded-xl bg-[#181038] border border-[#7C3AED]/30 text-white text-sm focus:outline-none focus:border-[#00FF66]"
            >
              <option value="Instalação de Prótese Capilar">Instalação de Prótese Capilar</option>
              <option value="Manutenção de Prótese">Manutenção de Prótese</option>
              <option value="Consulta • Ajuste">Consulta • Ajuste</option>
              <option value="Higienização • Corte">Higienização • Corte</option>
              <option value="Avaliação e Medição">Avaliação e Medição</option>
            </select>
          </div>

          {/* Day Selector */}
          <div>
            <label className="text-xs font-semibold text-[#A2A2BF] block mb-1.5">Dia do Atendimento</label>
            <div className="grid grid-cols-6 gap-1.5">
              {days.map((d) => {
                const isSelected = dayNumber === d.num;
                return (
                  <button
                    type="button"
                    key={d.num}
                    onClick={() => {
                      setDayNumber(d.num);
                      setDayOfWeek(d.label);
                    }}
                    className={`py-2 px-1 rounded-xl text-center transition-all ${
                      isSelected
                        ? 'bg-[#7C3AED] text-white font-bold shadow-md shadow-[#7C3AED]/40 border border-[#8B5CF6]'
                        : 'bg-[#181038] text-[#A2A2BF] hover:text-white border border-[#7C3AED]/20'
                    }`}
                  >
                    <div className="text-[10px] uppercase font-medium">{d.label}</div>
                    <div className="text-xs font-extrabold mt-0.5">{d.num}</div>
                  </button>
                );
              })}
            </div>
          </div>

          {/* Time Selector */}
          <div>
            <label className="text-xs font-semibold text-[#A2A2BF] block mb-1.5">Horário de Início</label>
            <div className="grid grid-cols-5 gap-1.5">
              {times.map((t) => {
                const isSelected = time === t;
                return (
                  <button
                    type="button"
                    key={t}
                    onClick={() => setTime(t)}
                    className={`py-1.5 rounded-lg text-xs font-bold transition-all ${
                      isSelected
                        ? 'bg-[#00FF66] text-black shadow-md shadow-[#00FF66]/30'
                        : 'bg-[#181038] text-white hover:bg-[#281A5A] border border-[#7C3AED]/20'
                    }`}
                  >
                    {t}
                  </button>
                );
              })}
            </div>
          </div>

          {/* Duration */}
          <div>
            <label className="text-xs font-semibold text-[#A2A2BF] block mb-1.5">Duração Prevista</label>
            <div className="grid grid-cols-4 gap-2">
              {durations.map((dur) => {
                const isSelected = durationMinutes === dur;
                return (
                  <button
                    type="button"
                    key={dur}
                    onClick={() => setDurationMinutes(dur)}
                    className={`py-2 rounded-xl text-xs font-bold transition-all flex items-center justify-center gap-1 ${
                      isSelected
                        ? 'bg-[#8B5CF6] text-white shadow-md'
                        : 'bg-[#181038] text-[#A2A2BF] hover:text-white border border-[#7C3AED]/20'
                    }`}
                  >
                    <Clock className="w-3.5 h-3.5" />
                    {dur}m
                  </button>
                );
              })}
            </div>
          </div>

          {/* CONFLICT WARNING */}
          {conflict && (
            <div className="p-3 rounded-xl bg-red-500/15 border border-red-500/60 flex items-start gap-2.5 animate-pulse">
              <AlertTriangle className="w-5 h-5 text-red-400 shrink-0 mt-0.5" />
              <div className="text-xs text-red-300 leading-snug">
                <span className="font-bold text-red-200">Conflito de Horário!</span> Já existe
                atendimento confirmado para <span className="font-semibold text-white">{conflict.clientName}</span> às{' '}
                <span className="font-bold text-[#00FF66]">{conflict.time}</span> ({conflict.durationMinutes} min).
                Por favor, altere o horário ou dia.
              </div>
            </div>
          )}

          {/* Notes */}
          <div>
            <label className="text-xs font-semibold text-[#A2A2BF] block mb-1">Observações (opcional)</label>
            <input
              type="text"
              value={notes}
              onChange={(e) => setNotes(e.target.value)}
              placeholder="Ex: Primeira colocação, base micropele..."
              className="w-full px-3.5 py-2.5 rounded-xl bg-[#181038] border border-[#7C3AED]/30 text-white placeholder-[#A2A2BF]/50 text-sm focus:outline-none focus:border-[#00FF66]"
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
              disabled={!clientName.trim() || !!conflict}
              className="flex-1 py-2.5 rounded-xl bg-[#00FF66] hover:bg-[#00CC52] disabled:opacity-40 disabled:cursor-not-allowed text-black font-extrabold text-xs transition shadow-lg shadow-[#00FF66]/20"
            >
              Confirmar Agendamento
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};
