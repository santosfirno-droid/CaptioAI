import React from 'react';

interface CaptioLogoProps {
  size?: 'sm' | 'md' | 'lg';
  showSubtitle?: boolean;
}

export const CaptioLogo: React.FC<CaptioLogoProps> = ({ size = 'md', showSubtitle = false }) => {
  const iconSize = size === 'sm' ? 'w-6 h-6' : size === 'lg' ? 'w-12 h-12' : 'w-8 h-8';
  const textSize = size === 'sm' ? 'text-lg' : size === 'lg' ? 'text-3xl' : 'text-xl';

  return (
    <div className="flex flex-col items-center">
      <div className="flex items-center gap-2">
        <img
          src="/logo.jpg"
          alt="CAPTIO Logo"
          className={`${iconSize} rounded-md object-cover shadow-sm shadow-[#7C3AED]/40`}
          onError={(e) => {
            // fallback if image not loaded
            (e.currentTarget as HTMLImageElement).style.display = 'none';
          }}
        />
        <span className={`font-black tracking-wider text-white ${textSize} drop-shadow-[0_2px_10px_rgba(124,58,237,0.3)]`}>
          CAPTIO
        </span>
      </div>
      {showSubtitle && (
        <div className="mt-1 text-center">
          <p className="text-[10px] sm:text-xs font-bold text-[#D1D1E9] tracking-wider uppercase leading-tight">
            Gestão inteligente para profissionais de prótese capilar
          </p>
          <p className="text-xs font-semibold text-[#00FF66] mt-0.5">
            Sua clínica mais organizada, seu negócio mais lucrativo.
          </p>
        </div>
      )}
    </div>
  );
};
