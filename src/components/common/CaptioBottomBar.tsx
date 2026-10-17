import React from 'react';
import { Home, Users, BarChart3, Menu, Plus } from 'lucide-react';
import { Screen } from '../../types';
import { useCaptio } from '../../context/CaptioContext';

interface CaptioBottomBarProps {
  onQuickAdd: () => void;
}

export const CaptioBottomBar: React.FC<CaptioBottomBarProps> = ({ onQuickAdd }) => {
  const { currentScreen, setCurrentScreen } = useCaptio();

  const navItems: { screen: Screen; label: string; icon: React.ReactNode }[] = [
    { screen: 'HOME', label: 'Início', icon: <Home className="w-5 h-5" /> },
    { screen: 'CLIENTS', label: 'Clientes', icon: <Users className="w-5 h-5" /> },
    // Center FAB is handled separately
    { screen: 'REPORTS', label: 'Relatórios', icon: <BarChart3 className="w-5 h-5" /> },
    { screen: 'MORE', label: 'Mais', icon: <Menu className="w-5 h-5" /> },
  ];

  return (
    <div className="fixed bottom-0 left-0 right-0 max-w-md mx-auto z-40">
      <div className="relative bg-[#181038] border-t border-[#7C3AED]/20 rounded-t-2xl px-3 py-2 flex items-center justify-around shadow-[0_-8px_25px_rgba(0,0,0,0.5)]">
        {/* Início */}
        <button
          onClick={() => setCurrentScreen('HOME')}
          data-testid="nav_home"
          className={`flex flex-col items-center justify-center py-1 px-3 transition-colors ${
            currentScreen === 'HOME' ? 'text-[#8B5CF6] font-bold' : 'text-[#A2A2BF] hover:text-white'
          }`}
        >
          {navItems[0].icon}
          <span className="text-[11px] mt-0.5">{navItems[0].label}</span>
        </button>

        {/* Clientes */}
        <button
          onClick={() => setCurrentScreen('CLIENTS')}
          data-testid="nav_clients"
          className={`flex flex-col items-center justify-center py-1 px-3 transition-colors ${
            currentScreen === 'CLIENTS' ? 'text-[#8B5CF6] font-bold' : 'text-[#A2A2BF] hover:text-white'
          }`}
        >
          {navItems[1].icon}
          <span className="text-[11px] mt-0.5">{navItems[1].label}</span>
        </button>

        {/* Center Floating Neon Green Add Button */}
        <div className="relative -top-5">
          <button
            onClick={onQuickAdd}
            data-testid="nav_add_fab"
            aria-label="Ação rápida"
            className="w-12 h-12 rounded-full bg-gradient-to-tr from-[#00CC52] via-[#00FF66] to-[#33FF85] text-black font-black flex items-center justify-center shadow-[0_0_20px_rgba(0,255,102,0.45)] hover:scale-105 active:scale-95 transition-all"
          >
            <Plus className="w-7 h-7 text-black stroke-[2.5]" />
          </button>
        </div>

        {/* Relatórios */}
        <button
          onClick={() => setCurrentScreen('REPORTS')}
          data-testid="nav_reports"
          className={`flex flex-col items-center justify-center py-1 px-3 transition-colors ${
            currentScreen === 'REPORTS' ? 'text-[#8B5CF6] font-bold' : 'text-[#A2A2BF] hover:text-white'
          }`}
        >
          {navItems[2].icon}
          <span className="text-[11px] mt-0.5">{navItems[2].label}</span>
        </button>

        {/* Mais */}
        <button
          onClick={() => setCurrentScreen('MORE')}
          data-testid="nav_more"
          className={`flex flex-col items-center justify-center py-1 px-3 transition-colors ${
            currentScreen === 'MORE' ? 'text-[#8B5CF6] font-bold' : 'text-[#A2A2BF] hover:text-white'
          }`}
        >
          {navItems[3].icon}
          <span className="text-[11px] mt-0.5">{navItems[3].label}</span>
        </button>
      </div>
    </div>
  );
};
