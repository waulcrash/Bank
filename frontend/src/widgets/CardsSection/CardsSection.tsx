import React from 'react';
import { Container } from '../../shared/ui/Container/Container';
import { Card } from '../../shared/ui/Card/Card';
import './CardsSection.css';


import iconMoneyDuotone from '../../shared/assets/images/Money_duotone.png';
import iconCalendarduotone from '../../shared/assets/images/Calendar_duotone.png';
import iconClockduotone from '../../shared/assets/images/Clock_duotone.png';
import iconBagduotone from '../../shared/assets/images/Bag_duotone.png';
import iconCard_duotone from '../../shared/assets/images/Card_duotone.png';

interface CardData {
  id: number;
  title: string;
  description: string;
  icon: string;
  variant: 'light' | 'dark';
  size: 'normal' | 'wide';
}

const cards: CardData[] = [
  {
    id: 0,
    title: 'Up to 50 000 ₽',
    description: 'Cash and transfers without commission and percent',
    icon: iconMoneyDuotone,
    variant: 'light',
    size: 'normal',
  },
  {
    id: 1,
    title: 'Up to 160 days',
    description: 'Without percent on the loan',
    icon: iconCalendarduotone,
    variant: 'dark',
    size: 'normal',
  },
  {
    id: 2,
    title: 'Free delivery',
    description: 'We will deliver your card by courier at a convenient place and time for you',
    icon: iconClockduotone,
    variant: 'light',
    size: 'normal',
  },
  
  {
    id: 3,
    title: 'Up to 12 months',
    description: 'No percent. For equipment, clothes and other purchases in installments',
    icon: iconBagduotone,
    variant: 'dark',
    size: 'wide',
  },
  {
    id: 4,
    title: 'Convenient deposit and withdrawal',
    description: 'At any ATM. Top up your credit card for free with cash or transfer from other cards',
    icon: iconCard_duotone,
    variant: 'light',
    size: 'wide',
  },
];

export const CardsSection: React.FC = () => {
  return (
    <section className="cards-section">
      <Container>
        <div className="cards-section__grid">
          {/* Верхний ряд: 3 карточки normal */}
          <div className="cards-section__row cards-section__row--top">
            {cards.slice(0, 3).map((card) => (
              <Card
                key={card.id}
                title={card.title}
                description={card.description}
                icon={card.icon}
                variant={card.variant}
                size={card.size}
              />
            ))}
          </div>
          
          {/* Нижний ряд: 2 карточки wide */}
          <div className="cards-section__row cards-section__row--bottom">
            {cards.slice(3, 5).map((card) => (
              <Card
                key={card.id}
                title={card.title}
                description={card.description}
                icon={card.icon}
                variant={card.variant}
                size={card.size}
              />
            ))}
          </div>
        </div>
      </Container>
    </section>
  );
};