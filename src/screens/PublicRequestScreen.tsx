import React, { useState } from 'react';
import {
  ArrowLeft,
  CheckCircle,
  Scissors,
  Calendar,
  Clock,
  Send,
  Sparkles,
  Phone,
  User,
  MessageSquare,
  ShieldCheck,
} from 'lucide-react';
import { useCaptio } from '../context/CaptioContext';

export const PublicRequestScreen: React.FC = () => {
  const { setCurrentScreen, addPublicServiceRequest } = useCaptio();

  const [name, setName] = useState('');
  const [phone, setPhone] = useState('');
  const [serviceType, setServiceType] = useState('Instalação de Prótese Capilar');
  const [preferredDate, setPreferredDate] = useState('Esta semana');
  const [customDate, setCustomDate] = useState('');
  const [preferredPeriod, setPreferredPeriod] = useState<'Manhã' | 'Tarde'>('Manhã');
  const [notes, setNotes] = useState('');
  const [submitted, setSubmitted] = useState(false);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!name.trim() || !phone.trim()) return;

    const finalDate = preferredDate === 'Outro dia' ? customDate || 'A combinar' : preferredDate;

    addPublicServiceRequest(
      name.trim(),
      phone.trim(),
      serviceType,
      finalDate,
      preferredPeriod,
      notes.trim()
    );

    setSubmitted(true);
  };

  return (
    <div className="min-h-full pb-16 px-4 pt-3 space-y-4">
      {/* Top bar with button to exit back to admin app */}
      <div className="flex items-center justify-between pb-1">
        <button
          onClick={() => setCurrentScreen('HOME')}
          data-testid="public_exit_btn"
          className="py-1.5 px-3 rounded-full bg-white/10 hover:bg-white/15 text-white text-xs font-semibold flex items-center gap-1.5 transition active:scale-95"
        >
          <ArrowLeft className="w-4 h-4" />
          Voltar ao App
        </button>

        <span className="px-2.5 py-1 rounded-full bg-[#00FF66]/20 border border-[#00FF66]/40 text-[#00FF66] font-extrabold text-[10px] tracking-wider uppercase">
          Link Público • Cliente
        </span>
      </div>

      {/* Studio Header Card */}
      <div className="p-5 rounded-3xl bg-gradient-to-b from-[#2E195E] via-[#201044] to-[#140A2C] border border-[#7C3AED]/40 shadow-xl text-center space-y-2">
        <div className="w-16 h-16 rounded-2xl bg-gradient-to-tr from-[#7C3AED] to-[#00FF66] p-0.5 mx-auto shadow-lg shadow-[#00FF66]/20">
          <img
            src="/logo.jpg"
            alt="Studio Logo"
            className="w-full h-full rounded-2xl object-cover"
          />
        </div>

        <div>
          <h2 className="text-lg font-black text-white tracking-wide">
            BRUNO PRÓTESE CAPILAR
          </h2>
          <p className="text-xs text-[#00FF66] font-semibold mt-0.5">
            Especialista em Micropele HD & Nanopele Natural
          </p>
        </div>

        <p className="text-xs text-[#A2A2BF] max-w-xs mx-auto leading-relaxed pt-1">
          Solicite seu atendimento ou avaliação de forma rápida. Entraremos em contato via WhatsApp para confirmar o melhor horário.
        </p>
      </div>

      {submitted ? (
        /* Success confirmation */
        <div className="p-6 rounded-3xl bg-[#181038] border border-[#00FF66]/50 shadow-2xl text-center space-y-4 animate-fadeIn">
          <div className="w-14 h-14 rounded-full bg-[#00FF66]/20 border border-[#00FF66] flex items-center justify-center text-[#00FF66] mx-auto shadow-lg shadow-[#00FF66]/30">
            <CheckCircle className="w-8 h-8" />
          </div>

          <div className="space-y-1">
            <h3 className="text-lg font-black text-white">Solicitação Recebida!</h3>
            <p className="text-xs text-[#00FF66] font-semibold">
              Obrigado, {name}!
            </p>
            <p className="text-xs text-[#A2A2BF] leading-relaxed pt-1">
              Recebemos seu pedido para <strong>{serviceType}</strong> no período da{' '}
              <strong>{preferredPeriod}</strong>. Entraremos em contato no WhatsApp{' '}
              <strong className="text-white">{phone}</strong> em poucos minutos para confirmar o agendamento!
            </p>
          </div>

          <div className="pt-2 flex flex-col gap-2">
            <button
              onClick={() => {
                setSubmitted(false);
                setName('');
                setPhone('');
                setNotes('');
              }}
              className="w-full py-2.5 rounded-xl bg-white/10 hover:bg-white/15 text-white text-xs font-semibold transition"
            >
              Fazer Outra Solicitação
            </button>
            <button
              onClick={() => setCurrentScreen('ATENDIMENTOS')}
              className="w-full py-2.5 rounded-xl bg-[#00FF66] hover:bg-[#00CC52] text-black text-xs font-extrabold transition shadow-md shadow-[#00FF66]/20"
            >
              Ver Solicitação no Painel do Barbeiro
            </button>
          </div>
        </div>
      ) : (
        /* Booking Request Form */
        <form
          onSubmit={handleSubmit}
          className="p-5 rounded-3xl bg-[#181038] border border-[#7C3AED]/30 shadow-xl space-y-4"
        >
          <div className="flex items-center gap-2 pb-2 border-b border-[#7C3AED]/20">
            <Calendar className="w-4 h-4 text-[#00FF66]" />
            <h3 className="text-sm font-bold text-white">Preencha seus dados</h3>
          </div>

          <div>
            <label className="text-xs font-semibold text-[#A2A2BF] block mb-1">
              Seu Nome Completo *
            </label>
            <div className="relative">
              <User className="w-4 h-4 text-[#A2A2BF] absolute left-3.5 top-3" />
              <input
                type="text"
                value={name}
                onChange={(e) => setName(e.target.value)}
                placeholder="Ex: Carlos Eduardo Silveira"
                required
                className="w-full pl-10 pr-4 py-2.5 rounded-xl bg-[#22164D] border border-[#7C3AED]/30 text-white placeholder-[#A2A2BF]/50 text-xs focus:outline-none focus:border-[#00FF66]"
              />
            </div>
          </div>

          <div>
            <label className="text-xs font-semibold text-[#A2A2BF] block mb-1">
              WhatsApp com DDD *
            </label>
            <div className="relative">
              <Phone className="w-4 h-4 text-[#00FF66] absolute left-3.5 top-3" />
              <input
                type="text"
                value={phone}
                onChange={(e) => setPhone(e.target.value)}
                placeholder="(11) 98765-4321"
                required
                className="w-full pl-10 pr-4 py-2.5 rounded-xl bg-[#22164D] border border-[#7C3AED]/30 text-white placeholder-[#A2A2BF]/50 text-xs focus:outline-none focus:border-[#00FF66]"
              />
            </div>
          </div>

          <div>
            <label className="text-xs font-semibold text-[#A2A2BF] block mb-1">
              Procedimento Desejado
            </label>
            <div className="space-y-2">
              {[
                { title: 'Instalação de Prótese Capilar', desc: 'Avaliação, molde, corte e aplicação' },
                { title: 'Manutenção de Prótese', desc: 'Higienização profunda, troca de fita e corte' },
              ].map((serv) => (
                <div
                  key={serv.title}
                  onClick={() => setServiceType(serv.title)}
                  className={`p-3 rounded-2xl border cursor-pointer transition flex items-start gap-3 ${
                    serviceType === serv.title
                      ? 'bg-[#7C3AED]/30 border-[#00FF66] shadow-md shadow-[#00FF66]/10'
                      : 'bg-[#22164D] border-[#7C3AED]/20 hover:border-[#7C3AED]/40'
                  }`}
                >
                  <div
                    className={`w-4 h-4 rounded-full mt-0.5 border flex items-center justify-center shrink-0 ${
                      serviceType === serv.title
                        ? 'border-[#00FF66] bg-[#00FF66]'
                        : 'border-[#A2A2BF]'
                    }`}
                  >
                    {serviceType === serv.title && <div className="w-1.5 h-1.5 rounded-full bg-black" />}
                  </div>
                  <div>
                    <h4 className="text-xs font-bold text-white">{serv.title}</h4>
                    <p className="text-[11px] text-[#A2A2BF]">{serv.desc}</p>
                  </div>
                </div>
              ))}
            </div>
          </div>

          {/* Preferred date */}
          <div>
            <label className="text-xs font-semibold text-[#A2A2BF] block mb-1.5">
              Melhor Período da Semana
            </label>
            <div className="grid grid-cols-3 gap-2">
              {['Esta semana', 'Próxima terça', 'Outro dia'].map((d) => (
                <button
                  type="button"
                  key={d}
                  onClick={() => setPreferredDate(d)}
                  className={`py-2 rounded-xl text-xs font-medium transition ${
                    preferredDate === d
                      ? 'bg-[#7C3AED] text-white font-bold border border-[#8B5CF6]'
                      : 'bg-[#22164D] text-[#A2A2BF] border border-[#7C3AED]/20'
                  }`}
                >
                  {d}
                </button>
              ))}
            </div>
            {preferredDate === 'Outro dia' && (
              <input
                type="text"
                value={customDate}
                onChange={(e) => setCustomDate(e.target.value)}
                placeholder="Ex: Sexta-feira dia 01"
                className="w-full mt-2 px-3 py-2 rounded-xl bg-[#22164D] border border-[#7C3AED]/30 text-white text-xs focus:outline-none focus:border-[#00FF66]"
              />
            )}
          </div>

          {/* Period preference */}
          <div>
            <label className="text-xs font-semibold text-[#A2A2BF] block mb-1.5">
              Turno de Preferência
            </label>
            <div className="grid grid-cols-2 gap-2">
              <button
                type="button"
                onClick={() => setPreferredPeriod('Manhã')}
                className={`py-2.5 rounded-xl text-xs font-bold transition flex items-center justify-center gap-1.5 ${
                  preferredPeriod === 'Manhã'
                    ? 'bg-[#00FF66] text-black shadow-md shadow-[#00FF66]/20'
                    : 'bg-[#22164D] text-[#A2A2BF] border border-[#7C3AED]/20'
                }`}
              >
                <Clock className="w-3.5 h-3.5" />
                Manhã (08h às 12h)
              </button>

              <button
                type="button"
                onClick={() => setPreferredPeriod('Tarde')}
                className={`py-2.5 rounded-xl text-xs font-bold transition flex items-center justify-center gap-1.5 ${
                  preferredPeriod === 'Tarde'
                    ? 'bg-[#00FF66] text-black shadow-md shadow-[#00FF66]/20'
                    : 'bg-[#22164D] text-[#A2A2BF] border border-[#7C3AED]/20'
                }`}
              >
                <Clock className="w-3.5 h-3.5" />
                Tarde (13h às 18h)
              </button>
            </div>
          </div>

          <div>
            <label className="text-xs font-semibold text-[#A2A2BF] block mb-1">
              Dúvidas ou Observações (opcional)
            </label>
            <textarea
              rows={2}
              value={notes}
              onChange={(e) => setNotes(e.target.value)}
              placeholder="Ex: Gostaria de tirar dúvidas sobre a fixação e durabilidade..."
              className="w-full px-3.5 py-2.5 rounded-xl bg-[#22164D] border border-[#7C3AED]/30 text-white placeholder-[#A2A2BF]/50 text-xs focus:outline-none focus:border-[#00FF66] resize-none"
            />
          </div>

          <button
            type="submit"
            disabled={!name.trim() || !phone.trim()}
            className="w-full py-3.5 rounded-2xl bg-[#00FF66] hover:bg-[#00CC52] disabled:opacity-40 text-black font-extrabold text-sm transition flex items-center justify-center gap-2 shadow-xl shadow-[#00FF66]/25 active:scale-[0.98]"
          >
            <Send className="w-4 h-4" />
            Enviar Solicitação de Horário
          </button>
        </form>
      )}
    </div>
  );
};
