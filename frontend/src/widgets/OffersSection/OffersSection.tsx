import React from 'react';
import { Container } from '../../shared/ui/Container/Container';
import { CreditOfferCard } from '../CreditOfferCard/CreditOfferCard';
import { useLoanStore } from '../../shared/store/loanStore';
import './OffersSection.css';

export const OffersSection: React.FC = () => {
  const { offers, selectOffer, isLoading } = useLoanStore();

  if (isLoading) {
    return (
      <div className="offers-section__loader">
        <div className="loader">Loading...</div>
      </div>
    );
  }

  return (
    <section className="offers-section">
      <Container>
        <div className="offers-section__grid">
          {offers.map((offer, index) => (
            <CreditOfferCard
              key={offer.statementId || index}
              offer={offer}
              onSelect={() => selectOffer(offer)}
            />
          ))}
        </div>
      </Container>
    </section>
  );
};