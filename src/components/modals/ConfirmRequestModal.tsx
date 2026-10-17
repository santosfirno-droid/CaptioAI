import React, { useState, useMemo } from 'react';
import { X, Calendar, AlertTriangle, Clock } from 'lucide-react';
import { ServiceRequest } from '../../types';
import { useCaptio } from '../../context/CaptioContext';

interface ConfirmRequestModalProps {
  request: ServiceRequest | null;
  isOpen: boolean;
  onClose: () => void;
  onSuccessWithMessage: (phone: string, message: string) => void;
}

export const ConfirmRequestModal: React.FC<ConfirmRequestModalProps> = ({
  request,
  isOpen,
  onClose,
  onSuccessWithMessage,
}) => {
  const { confirmServiceRequest, checkAppointmentConflict } = useCaptio();

  const [dayNumber, setDayNumber] = useState('29');
  const [dayOfWeek, setDayOfWeek] = useState('Ter');
  const [time, setTime] = useState(request?.preferredPeriod === 'Tarde' ? '14:00' : '09:00');
  const [durationMinutes, setDurationMinutes] = useState(
    request?.serviceType.includes('Instalação') ? 90 : 60
  );

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

  if (!isOpen || !request) return null;

  const handleConfirm = () => {
    if (conflict) return;

    const message = confirmServiceRequest(
      request.id,
      dayNumber,
      dayOfWeek,
      time,
      durationMinutes
    );

    onClose();
    onSuccessWithMessage(request.phone, message);
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/75 backdrop-blur-sm animate-fadeIn">
      <div className="w-full max-w-md bg-[#22164D] border border-[#7C3AED]/40 rounded-3xl p-5 shadow-2xl max-h-[90vh] overflow-y-auto">
        <div className="flex items-center justify-between pb-3 border-b border-[#7C3AED]/20">
          <div>
            <h3 className="text-base font-bold text-white">Aceitar Solicitação</h3>
            <p className="text-xs text-[#00FF66] font-semibold">
              {request.clientName} • {request.serviceType}
            </p>
          </div>
          <button
            onClick={onClose}
            className="p-1 rounded-full text-[#A2A2BF] hover:text-white hover:bg-white/10 transition"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        <div className="mt-3 p-3 rounded-2xl bg-[#181038] border border-[#7C3AED]/20 text-xs text-[#A2A2BF] space-y-1">
          <div>
            <span className="text-white font-semibold">Previsão sugerida pelo cliente:</span>{' '}
            {request.preferredDate} ({request.preferredPeriod})
          </div>
          {request.notes && (
            <div>
              <span className="text-white font-semibold">Mensagem do cliente:</span> "{request.notes}"
            </div>
          )}
        </div>

        <div className="mt-4 space-y-4">
          {/* Day */}
          <div>
            <label className="text-xs font-semibold text-[#A2A2BF] block mb-1.5">1. Selecionar Dia</label>
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

          {/* Time */}
          <div>
            <label className="text-xs font-semibold text-[#A2A2BF] block mb-1.5">2. Horário do Agendamento</label>
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
            <label className="text-xs font-semibold text-[#A2A2BF] block mb-1.5">3. Duração</label>
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

          {/* Conflict */}
          {conflict && (
            <div className="p-3 rounded-xl bg-red-500/15 border border-red-500/60 flex items-start gap-2.5 animate-pulse">
              <AlertTriangle className="w-5 h-5 text-red-400 shrink-0 mt-0.5" />
              <div className="text-xs text-red-300 leading-snug">
                <span className="font-bold text-red-200">Conflito de Horário!</span> Já existe
                atendimento confirmado para <span className="font-semibold text-white">{conflict.clientName}</span> às{' '}
                <span className="font-bold text-[#00FF66]">{conflict.time}</span>. Escolha outro horário.
              </div>
            </div>
          )}

          <div className="pt-2 flex gap-2">
            <button
              type="button"
              onClick={onClose}
              className="flex-1 py-2.5 rounded-xl bg-white/5 hover:bg-white/10 text-white text-xs font-semibold transition"
            >
              Cancelar
            </button>
            <button
              type="button"
              disabled={!!conflict}
              onClick={handleConfirm}
              className="flex-1 py-2.5 rounded-xl bg-[#00FF66] hover:bg-[#00CC52] disabled:opacity-40 disabled:cursor-not-allowed text-black font-extrabold text-xs transition shadow-lg shadow-[#00FF66]/20"
            >
              Confirmar & Avisar Cliente
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};
