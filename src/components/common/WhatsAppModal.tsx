import React, { useState } from 'react';
import { X, Send, Copy, Check, MessageSquare } from 'lucide-react';

interface WhatsAppModalProps {
  phone: string;
  message: string;
  isOpen: boolean;
  onClose: () => void;
}

export const WhatsAppModal: React.FC<WhatsAppModalProps> = ({
  phone,
  message,
  isOpen,
  onClose,
}) => {
  const [copied, setCopied] = useState(false);

  if (!isOpen) return null;

  const handleCopy = () => {
    navigator.clipboard.writeText(message);
    setCopied(true);
    setTimeout(() => setCopied(false), 2500);
  };

  const cleanPhone = phone.replace(/\D/g, '');
  // Brazilian phone prefix 55 if not present
  const fullPhone = cleanPhone.length <= 11 ? `55${cleanPhone}` : cleanPhone;
  const whatsappUrl = `https://wa.me/${fullPhone}?text=${encodeURIComponent(message)}`;

  const handleSend = () => {
    window.open(whatsappUrl, '_blank', 'noopener,noreferrer');
    onClose();
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/75 backdrop-blur-sm animate-fadeIn">
      <div className="w-full max-w-sm bg-[#22164D] border border-[#7C3AED]/40 rounded-3xl p-5 shadow-2xl">
        <div className="flex items-center justify-between pb-3 border-b border-[#7C3AED]/20">
          <div className="flex items-center gap-2">
            <div className="w-8 h-8 rounded-full bg-[#00FF66]/20 flex items-center justify-center text-[#00FF66]">
              <MessageSquare className="w-4 h-4" />
            </div>
            <div>
              <h3 className="text-base font-bold text-white">Enviar Mensagem</h3>
              <p className="text-xs text-[#00FF66] font-semibold">{phone}</p>
            </div>
          </div>
          <button
            onClick={onClose}
            className="p-1 rounded-full text-[#A2A2BF] hover:text-white hover:bg-white/10 transition"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        <div className="my-4">
          <label className="text-[11px] font-bold text-[#A2A2BF] uppercase tracking-wider block mb-1.5">
            Texto Formatado para o Cliente:
          </label>
          <div className="bg-[#181038] border border-[#7C3AED]/30 rounded-2xl p-3.5 text-xs text-white/90 leading-relaxed font-normal whitespace-pre-wrap">
            {message}
          </div>
        </div>

        <div className="space-y-2">
          <button
            onClick={handleSend}
            className="w-full py-3 bg-[#00FF66] hover:bg-[#00CC52] text-black font-extrabold text-sm rounded-xl transition flex items-center justify-center gap-2 shadow-lg shadow-[#00FF66]/20 active:scale-[0.98]"
          >
            <Send className="w-4 h-4" />
            Abrir no WhatsApp
          </button>

          <button
            onClick={handleCopy}
            className="w-full py-2.5 bg-white/10 hover:bg-white/15 text-white font-medium text-xs rounded-xl transition flex items-center justify-center gap-2 border border-white/10"
          >
            {copied ? (
              <>
                <Check className="w-4 h-4 text-[#00FF66]" />
                <span className="text-[#00FF66] font-bold">Copiado com sucesso!</span>
              </>
            ) : (
              <>
                <Copy className="w-4 h-4 text-[#A2A2BF]" />
                <span>Copiar Mensagem</span>
              </>
            )}
          </button>
        </div>
      </div>
    </div>
  );
};
