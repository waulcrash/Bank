import React, { useState } from 'react';
import './Accordion.css';

interface AccordionItem {
  id: string;
  title: string;
  content: React.ReactNode;
}

interface AccordionProps {
  items: AccordionItem[];
  allowMultiple?: boolean;
}

export const Accordion: React.FC<AccordionProps> = ({ items, allowMultiple = false }) => {
  const [openItems, setOpenItems] = useState<string[]>([]);

  const toggleItem = (id: string) => {
    if (allowMultiple) {
      setOpenItems((prev) =>
        prev.includes(id) ? prev.filter((item) => item !== id) : [...prev, id]
      );
    } else {
      setOpenItems((prev) => (prev.includes(id) ? [] : [id]));
    }
  };

  return (
    <div className="accordion">
      {items.map((item) => (
        <div key={item.id} className="accordion__item">
          <button
            className={`accordion__header ${openItems.includes(item.id) ? 'accordion__header--open' : ''}`}
            onClick={() => toggleItem(item.id)}
          >
            <span className="accordion__title">{item.title}</span>
            <span className="accordion__icon">{openItems.includes(item.id) ? '−' : '+'}</span>
          </button>
          {openItems.includes(item.id) && (
            <div className="accordion__content">{item.content}</div>
          )}
        </div>
      ))}
    </div>
  );
};