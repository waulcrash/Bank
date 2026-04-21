import React from 'react';
import { Container } from '../../shared/ui/Container/Container';
import { Step } from '../../shared/ui/Step/Step';
import './StepsSection.css';

interface StepData {
  id: number;
  number: number;
  description: string;
}

const steps: StepData[] = [
  {
    id: 0,
    number: 1,
    description: 'Fill out an online application - you do not need to visit the bank',
  },
  {
    id: 1,
    number: 2,
    description: "Find out the bank's decision immediately after filling out the application",
  },
  {
    id: 2,
    number: 3,
    description: 'The bank will deliver the card free of charge, wherever convenient, to your city',
  },
];

export const StepsSection: React.FC = () => {
  return (
    <section className="steps-section">
      <Container>
        <h2 className="steps-section__title">How to get a card</h2>
        <div className="steps-section__grid">
          {steps.map((step) => (
            <Step
              key={step.id}
              number={step.number}
              description={step.description}
            />
          ))}
        </div>
      </Container>
    </section>
  );
};