import React from 'react';
import './Loader.css';

interface LoaderProps {
  size?: 'small' | 'medium' | 'large';
}

export const Loader: React.FC<LoaderProps> = ({ size = 'medium' }) => {
  return (
    <div className={`loader loader--${size}`}>
      <div className="loader__spinner"></div>
    </div>
  );
};