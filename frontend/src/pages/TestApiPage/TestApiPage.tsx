import React from 'react';
import { Container } from '../../shared/ui/Container/Container';
import { Button } from '../../shared/ui/Button/Button';
import { CreditOfferCard } from '../../widgets/CreditOfferCard/CreditOfferCard';
import { SuccessPage } from '../../widgets/SuccessPage/SuccessPage';
import { useLoanStore } from '../../shared/store/loanStore';
import './TestApiPage.css';

// Тестовые данные 
const testOffers = [
  {
    statementId: '1',
    requestedAmount: 200000,
    totalAmount: 216619,
    term: 24,
    monthlyPayment: 9026,
    rate: 15,
    isInsuranceEnabled: false,
    isSalaryClient: false,
  },
  {
    statementId: '2',
    requestedAmount: 200000,
    totalAmount: 215640,
    term: 24,
    monthlyPayment: 8985,
    rate: 14.5,
    isInsuranceEnabled: true,
    isSalaryClient: false,
  },
  {
    statementId: '3',
    requestedAmount: 200000,
    totalAmount: 214608,
    term: 24,
    monthlyPayment: 8942,
    rate: 14,
    isInsuranceEnabled: false,
    isSalaryClient: true,
  },
  {
    statementId: '4',
    requestedAmount: 200000,
    totalAmount: 213240,
    term: 12,
    monthlyPayment: 8885,
    rate: 12,
    isInsuranceEnabled: true,
    isSalaryClient: true,
  },
];

export const TestApiPage: React.FC = () => {
  const { step, reset } = useLoanStore();

  
  const handleShowOffers = () => {
    
    useLoanStore.setState({ offers: testOffers, step: 'offers' });
  };

  const handleReset = () => {
    reset();
  };

  return (
    <div className="test-api-page">
      <Container>
        <h1 className="test-api-page__title">Test API Components</h1>
        
        <div className="test-api-page__controls">
          <Button variant="primary" onClick={handleShowOffers}>
            Show Credit Offers (4 cards)
          </Button>
          <Button variant="outline" onClick={handleReset}>
            Reset to Form
          </Button>
        </div>

        <div className="test-api-page__content">
          {step === 'offers' && (
            <div className="test-api-page__offers">
              <h2 className="test-api-page__subtitle">Credit Offers</h2>
              <div className="test-api-page__grid">
                {testOffers.map((offer) => (
                  <CreditOfferCard
                    key={offer.statementId}
                    offer={offer}
                    onSelect={() => console.log('Selected offer:', offer)}
                  />
                ))}
              </div>
            </div>
          )}

          {step === 'success' && <SuccessPage />}
        </div>
      </Container>
    </div>
  );
};