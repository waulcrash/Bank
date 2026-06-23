import React from 'react';
import { Button } from '../../shared/ui/Button/Button';
import './Hero.css';
import cardImage from '../../shared/assets/images/card.png';

export const Hero: React.FC = () => {
  return (
    <section className="hero">
      
        <div className="hero__gradient-bg">
          
          {/* Левая колонка с текстом */}
          <div className="hero__content">
            <h1 className="hero__title">
              Platinum digital credit card
            </h1>

            <p className="hero__description">
              Our best credit card. Suitable for everyday spending and shopping.<br/>
              Cash withdrawals and transfers without commission and interest.
            </p>

            <div className="hero__stats-grid">
              <div className="hero__stat">
                <span className="hero__stat-value">Up to 160 days</span>
                <span className="hero__stat-label">No percent</span>
              </div>
              <div className="hero__stat">
                <span className="hero__stat-value">Up to 600 000 ₽</span>
                <span className="hero__stat-label">Credit limit</span>
              </div>
              <div className="hero__stat">
                <span className="hero__stat-value">0 ₽</span>
                <span className="hero__stat-label">Card service is free</span>
              </div>
            </div>

            <div className="hero__button">
              <Button variant="primary" size="medium">
                Apply for card
              </Button>
            </div>
          </div>

          <div className="hero__card-wrapper">
          <img src={cardImage} alt="Credit card" className="hero__card" />
        </div>

        </div>
      
    </section>
  );
};