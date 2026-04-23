import React, { useState } from 'react';
import { Container } from '../../shared/ui/Container/Container';
import { Tabs } from '../../shared/ui/Tabs/Tabs';
import { Divider } from '../../shared/ui/Divider/Divider';
import { Accordion } from '../../shared/ui/Accordion/Accordion';
import { CardsSection } from '../CardsSection/CardsSection';
import './TabsSection.css';

const tabs = [
  { id: 'about', label: 'About card' },
  { id: 'rates', label: 'Rates and conditions' },
  { id: 'cashback', label: 'Cashback' },
  { id: 'faq', label: 'FAQ' },
];

// FAQ данные
const issuingFaqItems = [
  {
    id: '1',
    title: 'How to get a card?',
    content: 'We will deliver your card by courier free of charge. Delivery in Moscow and St. Petersburg - 1-2 working days. For other regions of the Russian Federation - 2-5 working days.',
  },
  {
    id: '2',
    title: 'What documents are needed and how old should one be to get a card?',
    content: 'Need a passport. You must be between 20 and 70 years old.',
  },
  {
    id: '3',
    title: 'In what currency can I issue a card?',
    content: 'In rubles, dollars or euro',
  },
  {
    id: '4',
    title: 'How much income do I need to get a credit card?',
    content: 'To obtain a credit card, you will need an income of at least 25,000 rubles per month after taxes.',
  },
  {
    id: '5',
    title: 'How do I find out about the bank\'s decision on my application?',
    content: 'After registration, you will receive an e-mail with a decision on your application.',
  },
];

const usingFaqItems = [
  {
    id: '6',
    title: 'What is an interest free credit card?',
    content: 'A credit card with a grace period is a bank card with an established credit limit, designed for payment, reservation of goods and services, as well as for receiving cash, which allows you to use credit funds free of charge for a certain period.',
  },
  {
    id: '7',
    title: 'How to activate a credit card',
    content: 'You can activate your credit card and generate a PIN code immediately after receiving the card at a bank branch using a PIN pad.',
  },
  {
    id: '8',
    title: 'What is a settlement date?',
    content: 'The settlement date is the date from which you can pay off the debt for the reporting period. The settlement date falls on the first calendar day following the last day of the reporting period. The first settlement date is reported by the bank when transferring the issued credit card to the client, and then in the monthly account statement.',
  },
  {
    id: '9',
    title: 'What do I need to know about interest rates?',
    content: 'For each reporting period from the 7th day of the previous month to the 6th day of the current month inclusive, a statement is generated for the credit card. The statement contains information on the amount and timing of the minimum payment, as well as the total amount of debt as of the date of issue.',
  },
];

// Данные для Rates and conditions
const ratesItems = [
  { label: 'Card currency', value: 'Rubles, dollars, euro' },
  { label: 'Interest free period', value: '0% up to 160 days' },
  { label: 'Payment system', value: 'Mastercard, Visa' },
  { label: 'Maximum credit limit on the card', value: '600 000 ₽' },
  { label: 'Replenishment and withdrawal', value: 'At any ATM. Top up your credit card for free with cash or transfer from other cards' },
  { label: 'Max cashback per month', value: '15 000 ₽' },
  { label: 'Transaction Alert', value: (
    <>60 ₽ — SMS or push notifications <br/>
    0 ₽ — card statement, information about transactions in the online bank</>)},
  
];

// Данные для Cashback карточек
const cashbackCards = [
  
  { id: 1, title: 'For food delivery, cafes and restaurants', value: '5%', bgColor: 'rgba(234, 236, 238, 1)' },
  { id: 2, title: 'In supermarkets with our subscription', value: '5%', bgColor: 'rgba(136, 179, 184, 0.6)' },
  { id: 3, title: 'In clothing stores and children\'s goods', value: '2%', bgColor: 'rgba(234, 236, 238, 1)' },
  
  { id: 4, title: 'Other purchases and payment of services and fines', value: '1%', bgColor: 'rgba(136, 179, 184, 0.6)' },
  { id: 5, title: 'Shopping in online stores', value: 'up to 3%', bgColor: 'rgba(234, 236, 238, 1)' },
  { id: 6, title: 'Purchases from our partners', value: '30%', bgColor: 'rgba(136, 179, 184, 0.6)' },
];

export const TabsSection: React.FC = () => {
  const [activeTab, setActiveTab] = useState('about');

  return (
    <section className="tabs-section">
      <Container>
        <Tabs tabs={tabs} activeTab={activeTab} onTabChange={setActiveTab} />
        
        <Divider variant="solid" color="rgba(128, 128, 128, 0.2)" />
        
        <div className="tabs-section__content">
          {/* About card */}
          {activeTab === 'about' && <CardsSection />}
          
          {/* Rates and conditions */}
          {activeTab === 'rates' && (
            <div className="tabs-section__panel">
              <div className="rates-list">
                {ratesItems.map((item, index) => (
                  <React.Fragment key={item.label}>
                    <div className="rates-list__item">
                      <span className="rates-list__label">{item.label}</span>
                      <span className="rates-list__value">{item.value}</span>
                    </div>
                    {index < ratesItems.length - 1 && (
                      <Divider variant="solid" color="rgba(127, 146, 172, 1)" />
                    )}
                  </React.Fragment>
                ))}
              </div>
            </div>
          )}
          
          {/* Cashback */}
          {activeTab === 'cashback' && (
            <div className="tabs-section__panel">
              <div className="cashback-grid">
                {cashbackCards.map((card) => (
                  <div 
                    key={card.id}
                    className="cashback-card"
                    style={{ backgroundColor: card.bgColor }}
                  >
                    <div className="cashback-card__title">{card.title}</div>
                    <div className="cashback-card__value">{card.value}</div>
                  </div>
                ))}
              </div>
            </div>
          )}
          
          {/* FAQ */}
          {activeTab === 'faq' && (
            <div className="tabs-section__panel">
              <div className="faq-block">
              <h3 className="faq-title">Issuing and receiving a card</h3>
              <Accordion items={issuingFaqItems} />
              </div>

              <div className="faq-block">
              <h3 className="faq-title">Using a credit card</h3>
              <Accordion items={usingFaqItems} />
              </div>
            </div>
          )}
        </div>
      </Container>
    </section>
  );
};