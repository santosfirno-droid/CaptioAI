import React from 'react';
import { X, CheckCircle, Calendar, XCircle, Phone, Trash2, Clock, User } from 'lucide-react';
import { Appointment } from '../../types';
import { useCaptio } from '../../context/CaptioContext';

interface AppointmentDetailModalProps {
  appointment: Appointment | null;
  isOpen: boolean;
  onClose: () => void;
  onReschedule: (appt: Appointment) => void;
  onOpenWhatsApp: (phone: string, msg: string) => void;
}

export const AppointmentDetailModal: React.FC<AppointmentDetailModalProps> = ({
  appointment,
  isOpen,
  onClose,
  onReschedule,
  onOpenWhatsApp,
}) => {
  const { completeAppointment, cancelAppointment, deleteAppointment } = useCaptio();

  if (!isOpen || !appointment) return null;

  const handleComplete = () => {
    completeAppointment(appointment);
    onClose();
  };

  const handleCancel = () => {
    cancelAppointment(appointment.id);
    onClose();
  };

  const handleDelete = () => {
    if (confirm('Deseja realmente excluir este agendamento?')) {
      deleteAppointment(appointment.id);
      onClose();
    }
  };

  const handleWhatsApp = () => {
    const msg = `Olá, ${appointment.clientName}! Estou entrando em contato para confirmar nosso atendimento de ${appointment.service} marcado para ${appointment.dayOfWeek} (dia ${appointment.dayNumber}), às ${appointment.time}. Qualquer dúvida, fico à disposição.`;
    onOpenWhatsApp(appointment.clientPhone, msg);
  };

  const badgeColor =
    appointment.status === 'Confirmado'
      ? 'text-[#00FF66] bg-[#00FF66]/15 border-[#00FF66]/30'
      : appointment.status === 'Concluído'
      ? 'text-[#38BDF8] bg-[#38BDF8]/15 border-[#38BDF8]/30'
      : appointment.status === 'Pendente'
      ? 'text-[#F59E0B] bg-[#F59E0B]/15 border-[#F59E0B]/30'
      : 'text-red-400 bg-red-500/15 border-red-500/30';

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/75 backdrop-blur-sm animate-fadeIn">
      <div className="w-full max-w-sm bg-[#22164D] border border-[#7C3AED]/40 rounded-3xl p-5 shadow-2xl">
        <div className="flex items-center justify-between pb-3 border-b border-[#7C3AED]/20">
          <div>
            <span className={`inline-block px-2.5 py-0.5 rounded-full text-[10px] font-bold border ${badgeColor}`}>
              {appointment.status}
            </span>
            <h3 className="text-base font-bold text-white mt-1">{appointment.clientName}</h3>
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
            <span className="text-[#A2A2BF] flex items-center gap-1.5">
              <Calendar className="w-3.5 h-3.5 text-[#00FF66]" />
              Data & Horário:
            </span>
            <span className="font-bold text-white">
              {appointment.dayOfWeek}, dia {appointment.dayNumber} às {appointment.time}
            </span>
          </div>

          <div className="flex items-center justify-between">
            <span className="text-[#A2A2BF] flex items-center gap-1.5">
              <Clock className="w-3.5 h-3.5 text-[#8B5CF6]" />
              Duração Estimada:
            </span>
            <span className="font-bold text-white">{appointment.durationMinutes} minutos</span>
          </div>

          <div className="flex items-center justify-between">
            <span className="text-[#A2A2BF] flex items-center gap-1.5">
              <User className="w-3.5 h-3.5 text-[#00E5FF]" />
              Procedimento:
            </span>
            <span className="font-bold text-[#00FF66] truncate max-w-[160px] text-right">
              {appointment.service}
            </span>
          </div>

          <div className="flex items-center justify-between">
            <span className="text-[#A2A2BF] flex items-center gap-1.5">
              <Phone className="w-3.5 h-3.5 text-[#00FF66]" />
              Contato:
            </span>
            <span className="font-mono text-white">{appointment.clientPhone}</span>
          </div>

          {appointment.notes && (
            <div className="pt-2 border-t border-[#7C3AED]/20 text-[11px] text-[#A2A2BF]">
              <span className="font-semibold text-white">Obs:</span> {appointment.notes}
            </div>
          )}
        </div>

        {/* Actions */}
        <div className="space-y-2">
          {appointment.status !== 'Concluído' && (
            <button
              onClick={handleComplete}
              className="w-full py-2.5 rounded-xl bg-[#00FF66] hover:bg-[#00CC52] text-black font-extrabold text-xs transition flex items-center justify-center gap-2 shadow-lg shadow-[#00FF66]/20"
            >
              <CheckCircle className="w-4 h-4" />
              Marcar como Concluído
            </button>
          )}

          <div className="grid grid-cols-2 gap-2">
            <button
              onClick={() => {
                onClose();
                onReschedule(appointment);
              }}
              className="py-2.5 rounded-xl bg-[#7C3AED]/20 hover:bg-[#7C3AED]/30 text-[#8B5CF6] border border-[#7C3AED]/40 font-bold text-xs transition flex items-center justify-center gap-1.5"
            >
              <Calendar className="w-3.5 h-3.5" />
              Remarcar
            </button>

            <button
              onClick={handleWhatsApp}
              className="py-2.5 rounded-xl bg-white/5 hover:bg-white/10 text-[#00FF66] border border-[#00FF66]/30 font-bold text-xs transition flex items-center justify-center gap-1.5"
            >
              <Phone className="w-3.5 h-3.5" />
              WhatsApp
            </button>
          </div>

          <div className="grid grid-cols-2 gap-2 pt-1">
            {appointment.status !== 'Cancelado' && (
              <button
                onClick={handleCancel}
                className="py-2 rounded-xl bg-red-500/10 hover:bg-red-500/20 text-red-400 font-medium text-xs transition flex items-center justify-center gap-1.5"
              >
                <XCircle className="w-3.5 h-3.5" />
                Cancelar
              </button>
            )}

            <button
              onClick={handleDelete}
              className="py-2 rounded-xl bg-white/5 hover:bg-white/10 text-[#A2A2BF] hover:text-white font-medium text-xs transition flex items-center justify-center gap-1.5 col-span-1"
            >
              <Trash2 className="w-3.5 h-3.5 text-red-400" />
              Excluir
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};
