import React from 'react';
import './Divider.css';

interface DividerProps {
  orientation?: 'horizontal' | 'vertical';
  variant?: 'solid' | 'dashed' | 'dotted';
  color?: string;
  className?: string;
}

export const Divider: React.FC<DividerProps> = ({
  orientation = 'horizontal',
  variant = 'solid',
  color = 'rgba(128, 128, 128, 0.2)',
  className = '',
}) => {
  return (
    <div
      className={`divider divider--${orientation} divider--${variant} ${className}`}
      style={{ backgroundColor: variant === 'solid' ? color : undefined, borderColor: color }}
    />
  );
};