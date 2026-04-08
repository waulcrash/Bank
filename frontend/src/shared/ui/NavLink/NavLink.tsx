import React from 'react';
import { Link } from 'react-router-dom';
import './NavLink.scss';

interface NavLinkProps {
  to: string;
  children: React.ReactNode;
  active?: boolean;
  onClick?: () => void;
  className?: string;
}

export const NavLink: React.FC<NavLinkProps> = ({ 
  to, 
  children, 
  active = false, 
  onClick, 
  className = '' 
}) => {
  return (
    <Link 
      to={to} 
      className={`nav-link ${active ? 'nav-link--active' : ''} ${className}`}
      onClick={onClick}
    >
      {children}
    </Link>
  );
};