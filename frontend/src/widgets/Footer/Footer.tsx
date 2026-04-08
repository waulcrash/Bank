import React from 'react';
import { Container } from '../../shared/ui/Container/Container';
import './Footer.css';

// Импорт PNG логотипа
import logoImage from '../../shared/assets/images/Logo.png';

interface FooterLink {
  id: number;
  title: string;
  url: string;
}

const footerLinks: FooterLink[] = [
  { id: 1, title: 'About bank', url: '/about' },
  { id: 2, title: 'Ask a Question', url: '/questions' },
  { id: 3, title: 'Quality of service', url: '/quality' },
  { id: 4, title: 'Requisites', url: '/requisites' },
  { id: 5, title: 'Press center', url: '/press' },
  { id: 6, title: 'Bank career', url: '/career' },
  { id: 7, title: 'Investors', url: '/investors' },
  { id: 8, title: 'Analytics', url: '/analytics' },
  { id: 9, title: 'Business and processes', url: '/business' },
  { id: 10, title: 'Compliance and business ethics', url: '/compliance' },
];

export const Footer: React.FC = () => {
  return (
    <footer className="footer">
      <Container>
        <div className="footer__content">
          
          {/* Левая колонка с логотипом */}
          <div className="footer__left">
            <img src={logoImage} alt="NeoBank" className="footer__logo" />
          </div>

          {/* Правая колонка с контактами */}
          <div className="footer__right">
            <a href="tel:+74951234567" className="footer__phone">
              +7 (495) 123-45-67
            </a>
            <a href="mailto:info@neobank.ru" className="footer__email">
              info@neobank.ru
            </a>
          </div>

        </div>

        {/* Ссылки */}
        <div className="footer__links">
          {footerLinks.map((link) => (
            <a key={link.id} href={link.url} className="footer__link">
              {link.title}
            </a>
          ))}
        </div>

        {/* Разделительная линия */}
        <div className="footer__divider"></div>

        {/* Текст про cookies */}
        <div className="footer__cookies">
          <p>
            We use cookies to personalize our services and improve the user experience of our website. 
            Cookies are small files containing information about previous visits to a website. 
            If you do not want to use cookies, please change your browser settings.
          </p>
        </div>

      </Container>
    </footer>
  );
};