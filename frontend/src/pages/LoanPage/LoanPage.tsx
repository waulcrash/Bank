import React from 'react';
import { Container } from '../../shared/ui/Container/Container';
import { Header } from '../../widgets/Header/Header';
import { Hero } from '../../widgets/Hero/Hero';
import { TabsSection } from '../../widgets/TabsSection/TabsSection';
import { StepsSection } from '../../widgets/StepsSection/StepsSection';
import { LoanApplicationForm } from '../../widgets/LoanApplicationForm/LoanApplicationForm';
import { OffersSection } from '../../widgets/OffersSection/OffersSection';
import { SuccessPage } from '../../widgets/SuccessPage/SuccessPage';
import { Footer } from '../../widgets/Footer/Footer';
import { useLoanStore } from '../../shared/store/loanStore';
import './LoanPage.css';

export const LoanPage: React.FC = () => {
  const { step } = useLoanStore();

  const renderContent = () => {
    switch (step) {
      case 'offers':
        return (
        <>
        <Hero />
            <TabsSection />
            <StepsSection />
        <OffersSection />
      </>  
      );
      case 'success':
        return <SuccessPage />;
      default:
        return (
          <>
            <Hero />
            <TabsSection />
            <StepsSection />
            <LoanApplicationForm />
          </>
        );
    }
  };

  return (
    <div className="loan-page">
      
      <main>
        
        <Container>
        <Header />
          {renderContent()}
        </Container>
      </main>
      <Footer />
    </div>
  );
};