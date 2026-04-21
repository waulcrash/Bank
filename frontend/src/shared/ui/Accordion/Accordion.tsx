import React, { useState } from 'react';
import './Accordion.css';

interface AccordionItem {
  id: string;
  title: string;
  content: React.ReactNode;
}

interface AccordionProps {
  items: AccordionItem[];
}

export const Accordion: React.FC<AccordionProps> = ({ items }) => {
  const [openId, setOpenId] = useState<string | null>(null);

  const toggle = (id: string) => {
    setOpenId(openId === id ? null : id);
  };

  return (
    <div className="accordion">
      {items.map((item) => (
        <div key={item.id} className="accordion__item">
          <button
            className={`accordion__header ${openId === item.id ? 'accordion__header--open' : ''}`}
            onClick={() => toggle(item.id)}
          >
            <span className="accordion__title">{item.title}</span>
            <span className="accordion__icon">{openId === item.id ? '−' : '+'}</span>
          </button>
          {openId === item.id && (
            <div className="accordion__content">{item.content}</div>
          )}
        </div>
      ))}
    </div>
  );
};