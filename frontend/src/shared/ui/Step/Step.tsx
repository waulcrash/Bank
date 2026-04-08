import React from 'react';
import './Step.css';

interface StepProps {
  number: number;
  description: string;
}

export const Step: React.FC<StepProps> = ({ number, description }) => {
  return (
    <div className="step">
      <div className="step__line">
        <div className="step__circle">
          <span className="step__number">{number}</span>
        </div>
      </div>
      <p className="step__description">{description}</p>
    </div>
  );
};