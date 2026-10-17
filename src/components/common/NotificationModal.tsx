import React from 'react';
import { X, Check } from 'lucide-react';
import { useCaptio } from '../../context/CaptioContext';

interface NotificationModalProps {
  isOpen: boolean;
  onClose: () => void;
}

export const NotificationModal: React.FC<NotificationModalProps> = ({ isOpen, onClose }) => {
  const { notifications, dismissNotification } = useCaptio();

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/70 backdrop-blur-sm animate-fadeIn">
      <div className="w-full max-w-sm bg-[#22164D] border border-[#7C3AED]/40 rounded-2xl p-5 shadow-2xl">
        <div className="flex items-center justify-between pb-3 border-b border-[#7C3AED]/20">
          <h3 className="text-base font-bold text-white flex items-center gap-2">
            Notificações da Clínica
          </h3>
          <button
            onClick={onClose}
            className="p-1 rounded-full text-[#A2A2BF] hover:text-white hover:bg-white/10 transition"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        <div className="my-4 max-h-72 overflow-y-auto space-y-2.5">
          {notifications.length === 0 ? (
            <div className="py-8 text-center text-sm text-[#A2A2BF]">
              Nenhuma nova notificação no momento.
            </div>
          ) : (
            notifications.map((notif) => (
              <div
                key={notif.id}
                className="bg-[#181038] border border-[#7C3AED]/20 rounded-xl p-3 flex items-start justify-between gap-2 transition hover:border-[#00FF66]/40"
              >
                <div className="flex-1">
                  <div className="flex items-center justify-between">
                    <span className="text-xs font-bold text-[#00FF66]">{notif.title}</span>
                    <span className="text-[10px] text-[#A2A2BF]">{notif.time}</span>
                  </div>
                  <p className="text-xs text-white/90 mt-1 leading-relaxed">{notif.message}</p>
                </div>
                <button
                  onClick={() => dismissNotification(notif.id)}
                  className="p-1 text-[#A2A2BF] hover:text-[#FF334B] transition"
                  title="Marcar como lida"
                >
                  <X className="w-4 h-4" />
                </button>
              </div>
            ))
          )}
        </div>

        <button
          onClick={onClose}
          className="w-full py-2.5 bg-[#7C3AED] hover:bg-[#6D28D9] text-white font-semibold text-sm rounded-xl transition flex items-center justify-center gap-1.5 shadow-lg shadow-[#7C3AED]/30"
        >
          <Check className="w-4 h-4" />
          OK, Entendi
        </button>
      </div>
    </div>
  );
};
