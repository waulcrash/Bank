import React from 'react';
import './Card.css';

interface CardProps {
  title: string;
  description: string;
  icon?: string;
  variant?: 'light' | 'dark';
  size?: 'normal' | 'wide';
  className?: string;
}

export const Card: React.FC<CardProps> = ({
  title,
  description,
  icon,
  variant = 'light',
  size = 'normal',
  className = '',
}) => {
  return (
    <div className={`card card--${variant} card--${size} ${className}`}>
      {icon && (
        <div className="card__icon">
          <img src={icon} alt={title} />
        </div>
      )}
      <h3 className="card__title">{title}</h3>
      <p className="card__description">{description}</p>
    </div>
  );
};