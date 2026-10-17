import React from 'react';
import { Menu, Bell } from 'lucide-react';
import { CaptioLogo } from './CaptioLogo';
import { useCaptio } from '../../context/CaptioContext';

interface HeaderProps {
  onOpenNotifications: () => void;
}

export const Header: React.FC<HeaderProps> = ({ onOpenNotifications }) => {
  const { setCurrentScreen, notifications } = useCaptio();

  return (
    <header className="flex items-center justify-between py-3 px-4">
      <button
        onClick={() => setCurrentScreen('MORE')}
        data-testid="home_menu_btn"
        className="w-10 h-10 rounded-full flex items-center justify-center text-white hover:bg-white/5 active:scale-95 transition"
        aria-label="Abrir menu"
      >
        <Menu className="w-6 h-6" />
      </button>

      <CaptioLogo size="sm" />

      <button
        onClick={onOpenNotifications}
        data-testid="home_notif_btn"
        className="relative w-10 h-10 rounded-full bg-[#181038] border border-[#7C3AED]/20 flex items-center justify-center text-white hover:border-[#7C3AED]/60 active:scale-95 transition"
        aria-label="Notificações"
      >
        <Bell className="w-5 h-5" />
        {notifications.length > 0 && (
          <span className="absolute -top-1 -right-1 w-5 h-5 bg-[#FF334B] text-white text-[10px] font-bold rounded-full flex items-center justify-center shadow-md animate-pulse">
            {notifications.length}
          </span>
        )}
      </button>
    </header>
  );
};
