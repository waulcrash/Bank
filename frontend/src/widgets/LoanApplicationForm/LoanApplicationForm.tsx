import React, { useState } from 'react';
import { Container } from '../../shared/ui/Container/Container';
import { Button } from '../../shared/ui/Button/Button';
import { Input } from '../../shared/ui/Input/Input';
import { Select } from '../../shared/ui/Select/Select';
import { RangeSlider } from '../../shared/ui/RangeSlider/RangeSlider';
import './LoanApplicationForm.css';

export const LoanApplicationForm: React.FC = () => {
  const [amount, setAmount] = useState(150000);
  const [formData, setFormData] = useState({
    lastName: '',
    firstName: '',
    patronymic: '',
    term: '6',
    email: '',
    dateOfBirth: '',
    passportSeries: '',
    passportNumber: '',
  });

  const termOptions = [
    { value: '6', label: '6 months' },
    { value: '12', label: '12 months' },
    { value: '18', label: '18 months' },
    { value: '24', label: '24 months' },
  ];

  const handleInputChange = (field: string, value: string) => {
    setFormData({ ...formData, [field]: value });
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    console.log('Form submitted:', { amount, ...formData });
  };

  return (
    <section className="loan-form">
      <Container>
        <div className="loan-form__card">
          
          {/* Основной блок с двумя колонками и пунктирной линией */}
          <div className="loan-form__two-columns">
            
            {/* Левая колонка */}
            <div className="loan-form__left-column">
              <div className="loan-form__header-row">
                <h2 className="loan-form__title">Customize your card</h2>
                <span className="loan-form__step">Step 1 of 5</span>
              </div>
              
              <div className="loan-form__slider-card">
                <label className="loan-form__slider-label">Select amount</label>
                <div className="loan-form__current-amount">{amount.toLocaleString()}</div>
                <RangeSlider
                    min={15000}
                    max={600000}
                    value={amount}
                    onChange={setAmount}
                />
                </div>
            </div>

            {/* Правая колонка */}
            <div className="loan-form__right-column">
            <div className="loan-form__chosen-section">
                <span className="loan-form__chosen-label">You have chosen the amount</span>
                <span className="loan-form__chosen-value">{amount.toLocaleString()} ₽</span>
                <div className="loan-form__divider-horizontal"></div>
            </div>
            </div>

          </div>

          {/* Contact Information */}
          <div className="loan-form__contact">
            <h3 className="loan-form__contact-title">Contact Information</h3>
          </div>

          {/* Форма с 8 полями */}
          <form onSubmit={handleSubmit} className="loan-form__form">
            <div className="loan-form__row">
              <Input
                label="Your last name"
                value={formData.lastName}
                onChange={(v) => handleInputChange('lastName', v)}
                placeholder="Doe"
                required
              />
              <Input
                label="Your first name"
                value={formData.firstName}
                onChange={(v) => handleInputChange('firstName', v)}
                placeholder="John"
                required
              />
              <Input
                label="Your patronymic"
                value={formData.patronymic}
                onChange={(v) => handleInputChange('patronymic', v)}
                placeholder="Victorovich"
                required={false}
              />
              <Select
                label="Select term"
                value={formData.term}
                onChange={(v) => handleInputChange('term', v)}
                options={termOptions}
                placeholder="Select term"
                required
              />
            </div>

            <div className="loan-form__row">
              <Input
                label="Your email"
                type="email"
                value={formData.email}
                onChange={(v) => handleInputChange('email', v)}
                placeholder="test@gmail.com"
                required
              />
             <Input
                label="Your date of birth"
                type="text"
                value={formData.dateOfBirth}
                onChange={(v) => handleInputChange('dateOfBirth', v)}
                placeholder="DD.MM.YYYY"
                required
                />
              <Input
                label="Your passport series"
                value={formData.passportSeries}
                onChange={(v) => handleInputChange('passportSeries', v)}
                placeholder="0000"
                required
              />
              <Input
                label="Your passport number"
                value={formData.passportNumber}
                onChange={(v) => handleInputChange('passportNumber', v)}
                placeholder="000000"
                required
              />
            </div>

            <div className="loan-form__actions">
              <Button type="submit" variant="primary" size="large">
                Continue
              </Button>
            </div>
          </form>
        </div>
      </Container>
    </section>
  );
};