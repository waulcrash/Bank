import React from 'react';
import { Container } from '../../shared/ui/Container/Container';
import { Header } from '../../widgets/Header/Header';
import { Hero } from '../../widgets/Hero/Hero';
import { TabsSection } from '../../widgets/TabsSection/TabsSection';
import { CardsSection } from '../../widgets/CardsSection/CardsSection';
import { StepsSection } from '../../widgets/StepsSection/StepsSection';
import { LoanApplicationForm } from '../../widgets/LoanApplicationForm/LoanApplicationForm';
import { Footer } from '../../widgets/Footer/Footer';

import './LoanPage.css';

export const LoanPage: React.FC = () => {
  return (
    <div className="loan-page">
      <main>
      <Container>
            <Header />
            <Hero />
            <TabsSection />
            <CardsSection/>
            <StepsSection/>
            <LoanApplicationForm/>
        </Container>
        <Footer/>
      </main>
    </div>
  );
};