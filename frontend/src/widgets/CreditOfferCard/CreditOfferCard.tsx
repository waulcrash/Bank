import React from 'react';
import { Button } from '../../shared/ui/Button/Button';
import surpriseImage from '../../shared/assets/images/SurpriseImage.png';
import './CreditOfferCard.css';

interface CreditOfferCardProps {
  offer: {
    statementId: string;
    requestedAmount: number;
    totalAmount: number;
    term: number;
    monthlyPayment: number;
    rate: number;
    isInsuranceEnabled: boolean;
    isSalaryClient: boolean;
  };
  onSelect: () => void;
}

export const CreditOfferCard: React.FC<CreditOfferCardProps> = ({ offer, onSelect }) => {
  return (
    <div className="credit-offer-card">
      <div className="credit-offer-card__image-wrapper">
        <img src={surpriseImage} alt="Surprise" className="credit-offer-card__image" />
      </div>
      
      <div className="credit-offer-card__content">
        <div className="credit-offer-card__field">
          Requested amount: {offer.requestedAmount.toLocaleString()} ₽
        </div>
        
        <div className="credit-offer-card__field">
          Total amount: {offer.totalAmount.toLocaleString()} ₽
        </div>
        
        <div className="credit-offer-card__field">
          For {offer.term} months
        </div>
        
        <div className="credit-offer-card__field">
          Monthly payment: {offer.monthlyPayment.toLocaleString()} ₽
        </div>
        
        <div className="credit-offer-card__field">
          Your rate: {offer.rate}%
        </div>
        
        <div className="credit-offer-card__field credit-offer-card__field--with-icon">
          <span>Insurance included</span>
          <span className={`credit-offer-card__icon credit-offer-card__icon--${offer.isInsuranceEnabled ? 'success' : 'error'}`}>
            {offer.isInsuranceEnabled ? '✓' : '✗'}
          </span>
        </div>
        
        <div className="credit-offer-card__field credit-offer-card__field--with-icon">
          <span>Salary client</span>
          <span className={`credit-offer-card__icon credit-offer-card__icon--${offer.isSalaryClient ? 'success' : 'error'}`}>
            {offer.isSalaryClient ? '✓' : '✗'}
          </span>
        </div>
      </div>
      
      <Button variant="primary" size="medium" onClick={onSelect}>
        Select
      </Button>
    </div>
  );
};