import React, { useState } from 'react';
import { Container } from '../../shared/ui/Container/Container';
import { Logo } from '../../shared/ui/Logo/Logo';
import { NavLink } from '../../shared/ui/NavLink/NavLink';
import { Button } from '../../shared/ui/Button/Button';
import './Header.scss';

interface NavItem {
  id: string;
  label: string;
  path: string;
}

const navItems: NavItem[] = [
  { id: 'credit-card', label: 'Credit card', path: '/credit-card' },
  { id: 'product', label: 'Product', path: '/product' },
  { id: 'account', label: 'Account', path: '/account' },
  { id: 'resources', label: 'Resources', path: '/resources' },
];

export const Header: React.FC = () => {
  const [activeNav, setActiveNav] = useState('credit-card');

  return (
    <header className="header">
        <div className="header__inner">
          <Logo variant="dark" size="medium" />
          
          <nav className="header__nav">
            {navItems.map((item) => (
              <NavLink
                key={item.id}
                to={item.path}
                active={activeNav === item.id}
                onClick={() => setActiveNav(item.id)}
              >
                {item.label}
              </NavLink>
            ))}
          </nav>
          
          <div className="header__buttons">
            <Button variant="primary" size="medium">
              Online Bank
            </Button>
          </div>
        </div>
    </header>
  );
};