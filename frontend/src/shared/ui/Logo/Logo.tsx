import React from 'react';
import './Logo.scss';

interface LogoProps {
  variant?: 'light' | 'dark';
  size?: 'small' | 'medium' | 'large';
}

export const Logo: React.FC<LogoProps> = ({ variant = 'dark', size = 'medium' }) => {
  return (
    <div className={`logo logo--${variant} logo--${size}`}>
      <span className="logo__text">NeoBank</span>
    </div>
  );
};