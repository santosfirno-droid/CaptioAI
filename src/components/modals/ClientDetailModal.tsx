import React, { useState } from 'react';
import { X, Phone, Calendar, Scissors, Edit2, Trash2, Check, MessageSquare } from 'lucide-react';
import { Client } from '../../types';
import { useCaptio } from '../../context/CaptioContext';

interface ClientDetailModalProps {
  client: Client | null;
  isOpen: boolean;
  onClose: () => void;
  onScheduleForClient: (client: Client) => void;
  onOpenWhatsApp: (phone: string, msg: string) => void;
}

export const ClientDetailModal: React.FC<ClientDetailModalProps> = ({
  client,
  isOpen,
  onClose,
  onScheduleForClient,
  onOpenWhatsApp,
}) => {
  const { updateClient, deleteClient, appointments } = useCaptio();
  const [isEditing, setIsEditing] = useState(false);
  const [name, setName] = useState('');
  const [phone, setPhone] = useState('');
  const [prosthesisType, setProsthesisType] = useState('');
  const [status, setStatus] = useState<'Ativo' | 'Inativo'>('Ativo');
  const [notes, setNotes] = useState('');

  React.useEffect(() => {
    if (client) {
      setName(client.name);
      setPhone(client.phone);
      setProsthesisType(client.prosthesisType);
      setStatus(client.status);
      setNotes(client.notes);
      setIsEditing(false);
    }
  }, [client]);

  if (!isOpen || !client) return null;

  const clientAppointments = appointments.filter(
    (a) => a.clientName.toLowerCase() === client.name.toLowerCase()
  );

  const handleSave = (e: React.FormEvent) => {
    e.preventDefault();
    updateClient({
      ...client,
      name,
      phone,
      prosthesisType,
      status,
      notes,
    });
    setIsEditing(false);
  };

  const handleDelete = () => {
    if (confirm(`Deseja excluir o cliente ${client.name}?`)) {
      deleteClient(client.id);
      onClose();
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/75 backdrop-blur-sm animate-fadeIn">
      <div className="w-full max-w-md bg-[#22164D] border border-[#7C3AED]/40 rounded-3xl p-5 shadow-2xl max-h-[90vh] overflow-y-auto">
        <div className="flex items-center justify-between pb-3 border-b border-[#7C3AED]/20">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-full bg-gradient-to-tr from-[#6366F1] to-[#4F46E5] flex items-center justify-center text-white font-bold text-sm shadow-md">
              {client.name
                .split(' ')
                .map((n) => n[0])
                .slice(0, 2)
                .join('')}
            </div>
            <div>
              <h3 className="text-base font-bold text-white">{client.name}</h3>
              <span
                className={`text-[10px] font-bold px-2 py-0.5 rounded-full inline-block mt-0.5 ${
                  client.status === 'Ativo'
                    ? 'bg-[#00FF66]/20 text-[#00FF66] border border-[#00FF66]/30'
                    : 'bg-red-500/20 text-red-400 border border-red-500/30'
                }`}
              >
                {client.status}
              </span>
            </div>
          </div>
          <button
            onClick={onClose}
            className="p-1 rounded-full text-[#A2A2BF] hover:text-white hover:bg-white/10 transition"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        {isEditing ? (
          <form onSubmit={handleSave} className="mt-4 space-y-3">
            <div>
              <label className="text-xs font-semibold text-[#A2A2BF] block mb-1">Nome</label>
              <input
                type="text"
                value={name}
                onChange={(e) => setName(e.target.value)}
                required
                className="w-full px-3 py-2 rounded-xl bg-[#181038] border border-[#7C3AED]/30 text-white text-xs focus:outline-none focus:border-[#00FF66]"
              />
            </div>
            <div>
              <label className="text-xs font-semibold text-[#A2A2BF] block mb-1">Telefone</label>
              <input
                type="text"
                value={phone}
                onChange={(e) => setPhone(e.target.value)}
                className="w-full px-3 py-2 rounded-xl bg-[#181038] border border-[#7C3AED]/30 text-white text-xs focus:outline-none focus:border-[#00FF66]"
              />
            </div>
            <div>
              <label className="text-xs font-semibold text-[#A2A2BF] block mb-1">Tipo de Prótese</label>
              <input
                type="text"
                value={prosthesisType}
                onChange={(e) => setProsthesisType(e.target.value)}
                className="w-full px-3 py-2 rounded-xl bg-[#181038] border border-[#7C3AED]/30 text-white text-xs focus:outline-none focus:border-[#00FF66]"
              />
            </div>
            <div>
              <label className="text-xs font-semibold text-[#A2A2BF] block mb-1">Status</label>
              <select
                value={status}
                onChange={(e) => setStatus(e.target.value as any)}
                className="w-full px-3 py-2 rounded-xl bg-[#181038] border border-[#7C3AED]/30 text-white text-xs focus:outline-none focus:border-[#00FF66]"
              >
                <option value="Ativo">Ativo</option>
                <option value="Inativo">Inativo</option>
              </select>
            </div>
            <div>
              <label className="text-xs font-semibold text-[#A2A2BF] block mb-1">Observações</label>
              <textarea
                rows={2}
                value={notes}
                onChange={(e) => setNotes(e.target.value)}
                className="w-full px-3 py-2 rounded-xl bg-[#181038] border border-[#7C3AED]/30 text-white text-xs focus:outline-none focus:border-[#00FF66] resize-none"
              />
            </div>
            <div className="flex gap-2 pt-1">
              <button
                type="button"
                onClick={() => setIsEditing(false)}
                className="flex-1 py-2 rounded-xl bg-white/5 text-white text-xs"
              >
                Cancelar
              </button>
              <button
                type="submit"
                className="flex-1 py-2 rounded-xl bg-[#7C3AED] text-white text-xs font-bold"
              >
                Salvar Alterações
              </button>
            </div>
          </form>
        ) : (
          <div className="mt-4 space-y-3">
            <div className="bg-[#181038] border border-[#7C3AED]/20 rounded-2xl p-4 text-xs space-y-2.5">
              <div className="flex items-center justify-between">
                <span className="text-[#A2A2BF] flex items-center gap-1.5">
                  <Scissors className="w-3.5 h-3.5 text-[#00FF66]" />
                  Prótese / Modelo:
                </span>
                <span className="font-bold text-white text-right">{client.prosthesisType}</span>
              </div>

              <div className="flex items-center justify-between">
                <span className="text-[#A2A2BF] flex items-center gap-1.5">
                  <Phone className="w-3.5 h-3.5 text-[#8B5CF6]" />
                  WhatsApp:
                </span>
                <span className="font-mono text-[#00FF66]">{client.phone}</span>
              </div>

              <div className="flex items-center justify-between">
                <span className="text-[#A2A2BF]">Atendimentos Realizados:</span>
                <span className="font-bold text-white bg-[#7C3AED]/30 px-2 py-0.5 rounded-md">
                  {client.servicesCount} atendimentos
                </span>
              </div>

              <div className="flex items-center justify-between">
                <span className="text-[#A2A2BF]">Último Procedimento:</span>
                <span className="font-semibold text-white/90">{client.lastService}</span>
              </div>

              {client.notes && (
                <div className="pt-2 border-t border-[#7C3AED]/20 text-[11px] text-[#A2A2BF]">
                  <span className="font-semibold text-white">Notas:</span> {client.notes}
                </div>
              )}
            </div>

            {/* Quick Actions for this client */}
            <div className="grid grid-cols-2 gap-2">
              <button
                onClick={() => {
                  onClose();
                  onScheduleForClient(client);
                }}
                className="py-2.5 bg-[#00FF66] hover:bg-[#00CC52] text-black font-extrabold text-xs rounded-xl transition flex items-center justify-center gap-1.5 shadow-md shadow-[#00FF66]/20"
              >
                <Calendar className="w-4 h-4" />
                Agendar Horário
              </button>

              <button
                onClick={() => {
                  onOpenWhatsApp(
                    client.phone,
                    `Olá ${client.name}! Aqui é da equipe CAPTIO de prótese capilar. Como podemos te ajudar hoje?`
                  );
                }}
                className="py-2.5 bg-white/10 hover:bg-white/15 text-white font-semibold text-xs rounded-xl transition flex items-center justify-center gap-1.5 border border-white/10"
              >
                <MessageSquare className="w-4 h-4 text-[#00FF66]" />
                WhatsApp
              </button>
            </div>

            {/* Historic appointments of client */}
            {clientAppointments.length > 0 && (
              <div className="mt-2">
                <h4 className="text-xs font-bold text-[#A2A2BF] mb-2 uppercase tracking-wider">
                  Histórico no Sistema
                </h4>
                <div className="space-y-1.5 max-h-36 overflow-y-auto">
                  {clientAppointments.map((appt) => (
                    <div
                      key={appt.id}
                      className="p-2.5 rounded-xl bg-[#181038] border border-[#7C3AED]/15 flex items-center justify-between text-xs"
                    >
                      <div>
                        <div className="font-semibold text-white">{appt.service}</div>
                        <div className="text-[10px] text-[#A2A2BF]">
                          {appt.dayOfWeek} {appt.dayNumber} às {appt.time}
                        </div>
                      </div>
                      <span
                        className={`text-[9px] font-bold px-1.5 py-0.5 rounded ${
                          appt.status === 'Confirmado'
                            ? 'bg-[#00FF66]/20 text-[#00FF66]'
                            : appt.status === 'Concluído'
                            ? 'bg-sky-500/20 text-sky-400'
                            : 'bg-white/10 text-[#A2A2BF]'
                        }`}
                      >
                        {appt.status}
                      </span>
                    </div>
                  ))}
                </div>
              </div>
            )}

            <div className="flex gap-2 pt-2 border-t border-[#7C3AED]/20">
              <button
                onClick={() => setIsEditing(true)}
                className="flex-1 py-2 rounded-xl bg-white/5 hover:bg-white/10 text-white text-xs font-medium transition flex items-center justify-center gap-1.5"
              >
                <Edit2 className="w-3.5 h-3.5 text-[#8B5CF6]" />
                Editar Dados
              </button>
              <button
                onClick={handleDelete}
                className="py-2 px-3 rounded-xl bg-red-500/10 hover:bg-red-500/20 text-red-400 text-xs font-medium transition flex items-center justify-center gap-1.5"
              >
                <Trash2 className="w-3.5 h-3.5" />
                Excluir
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};
