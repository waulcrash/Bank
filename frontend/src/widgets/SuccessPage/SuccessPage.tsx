import React from 'react';
import { Container } from '../../shared/ui/Container/Container';
import { Button } from '../../shared/ui/Button/Button';
import { useLoanStore } from '../../shared/store/loanStore';
import surpriseImage from '../../shared/assets/images/SurpriseImage.png';
import './SuccessPage.css';

export const SuccessPage: React.FC = () => {
  const { reset } = useLoanStore();

  const handleViewOtherOffers = () => {
    reset();
  };

  return (
    <section className="success-page">
      <Container>
        <div className="success-page__card">
          <img src={surpriseImage} alt="Success" className="success-page__image" />
          
          <h1 className="success-page__title">
            Congratulations! You have completed your new credit card.
          </h1>
          
          <p className="success-page__message">
            Your credit card will arrive soon. Thank you for choosing us!
          </p>
          
          <Button variant="outline" size="large" onClick={handleViewOtherOffers}>
            View other offers of our bank
          </Button>
        </div>
      </Container>
    </section>
  );
};