import React from 'react';
import './Loader.css';

interface LoaderProps {
  size?: 'small' | 'medium' | 'large';
  text?: string;
}

export const Loader: React.FC<LoaderProps> = ({ size = 'medium', text }) => {
  return (
    <div className={`loader loader--${size}`}>
      <div className="loader__spinner"></div>
      {text && <span className="loader__text">{text}</span>}
    </div>
  );
};