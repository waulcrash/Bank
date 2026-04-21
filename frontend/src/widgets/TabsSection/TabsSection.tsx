import React, { useState } from 'react';
import { Container } from '../../shared/ui/Container/Container';
import { Tabs } from '../../shared/ui/Tabs/Tabs';
import { Divider } from '../../shared/ui/Divider/Divider';
import './TabsSection.css';

const tabs = [
  { id: 'about', label: 'About card' },
  { id: 'rates', label: 'Rates and conditions' },
  { id: 'cashback', label: 'Cashback' },
  { id: 'faq', label: 'FAQ' },
];

export const TabsSection: React.FC = () => {
    const [activeTab, setActiveTab] = useState('about');
  
    return (
      <section className="tabs-section">
        <Container>
          <Tabs tabs={tabs} activeTab={activeTab} onTabChange={setActiveTab} />

          <Divider variant="solid" color="rgba(128, 128, 128, 0.2)" />
          
        </Container>
      </section>
    
  );
};