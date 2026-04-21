import React, { useState } from 'react';
import { Container } from '../../shared/ui/Container/Container';
import { Button } from '../../shared/ui/Button/Button';
import { Input } from '../../shared/ui/Input/Input';
import { Select } from '../../shared/ui/Select/Select';
import { RangeSlider } from '../../shared/ui/RangeSlider/RangeSlider';
import './LoanApplicationForm.css';

interface FormErrors {
  lastName?: string;
  firstName?: string;
  email?: string;
  dateOfBirth?: string;
  passportSeries?: string;
  passportNumber?: string;
  amount?: string;
  term?: string;
}

interface TouchedFields {
  lastName: boolean;
  firstName: boolean;
  email: boolean;
  dateOfBirth: boolean;
  passportSeries: boolean;
  passportNumber: boolean;
  amount: boolean;
  term: boolean;
}

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

  const [errors, setErrors] = useState<FormErrors>({});
  const [touched, setTouched] = useState<TouchedFields>({
    lastName: false,
    firstName: false,
    email: false,
    dateOfBirth: false,
    passportSeries: false,
    passportNumber: false,
    amount: false,
    term: false,
  });

  const termOptions = [
    { value: '6', label: '6 months' },
    { value: '12', label: '12 months' },
    { value: '18', label: '18 months' },
    { value: '24', label: '24 months' },
  ];

  // Валидаторы
  const validateEmail = (value: string): string => {
    if (!value) return 'Email is required';
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(value)) return 'Enter a valid email address';
    return '';
  };

  const validateBirthdate = (value: string): string => {
    if (!value) return 'Date of birth is required';
    const dateRegex = /^(0[1-9]|[12][0-9]|3[01])\.(0[1-9]|1[0-2])\.(19[0-9]{2}|20[0-9]{2})$/;
    if (!dateRegex.test(value)) return 'Use format DD.MM.YYYY';
    
    const [day, month, year] = value.split('.').map(Number);
    const date = new Date(year, month - 1, day);
    const today = new Date();
    let age = today.getFullYear() - date.getFullYear();
    const monthDiff = today.getMonth() - date.getMonth();
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < date.getDate())) {
      age--;
    }
    if (age < 18) return 'You must be at least 18 years old';
    return '';
  };

  const validatePassportSeries = (value: string): string => {
    if (!value) return 'Passport series is required';
    if (!/^\d{4}$/.test(value)) return 'Must be exactly 4 digits';
    return '';
  };

  const validatePassportNumber = (value: string): string => {
    if (!value) return 'Passport number is required';
    if (!/^\d{6}$/.test(value)) return 'Must be exactly 6 digits';
    return '';
  };

  const validateAmount = (value: number): string => {
    if (value < 15000) return 'Minimum amount is 15 000 ₽';
    if (value > 600000) return 'Maximum amount is 600 000 ₽';
    return '';
  };

  const validateField = (name: string, value: string | number): string => {
    switch (name) {
      case 'lastName':
        if (!value) return 'Last name is required';
        return '';
      case 'firstName':
        if (!value) return 'First name is required';
        return '';
      case 'email':
        return validateEmail(value as string);
      case 'dateOfBirth':
        return validateBirthdate(value as string);
      case 'passportSeries':
        return validatePassportSeries(value as string);
      case 'passportNumber':
        return validatePassportNumber(value as string);
      case 'amount':
        return validateAmount(value as number);
      case 'term':
        if (!value) return 'Term is required';
        return '';
      default:
        return '';
    }
  };

  const handleChange = (field: string, value: string) => {
    setFormData({ ...formData, [field]: value });
    const error = validateField(field, value);
    setErrors(prev => ({ ...prev, [field]: error }));
  };

  const handleBlur = (field: string) => {
    setTouched(prev => ({ ...prev, [field]: true }));
    const error = validateField(field, formData[field as keyof typeof formData]);
    setErrors(prev => ({ ...prev, [field]: error }));
  };

  const handleAmountChange = (newAmount: number) => {
    setAmount(newAmount);
    const error = validateAmount(newAmount);
    setErrors(prev => ({ ...prev, amount: error }));
    setTouched(prev => ({ ...prev, amount: true }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    // Отмечаем все поля как touched
    const allTouched = Object.keys(touched).reduce((acc, key) => {
      acc[key as keyof TouchedFields] = true;
      return acc;
    }, {} as TouchedFields);
    setTouched(allTouched);
    
    // Валидируем все поля
    const newErrors: FormErrors = {};
    newErrors.lastName = validateField('lastName', formData.lastName);
    newErrors.firstName = validateField('firstName', formData.firstName);
    newErrors.email = validateField('email', formData.email);
    newErrors.dateOfBirth = validateField('dateOfBirth', formData.dateOfBirth);
    newErrors.passportSeries = validateField('passportSeries', formData.passportSeries);
    newErrors.passportNumber = validateField('passportNumber', formData.passportNumber);
    newErrors.amount = validateAmount(amount);
    newErrors.term = validateField('term', formData.term);
    setErrors(newErrors);
    
    const isValid = !Object.values(newErrors).some(error => error);
    if (isValid) {
      console.log('Form submitted:', { amount, ...formData });
      alert('Application submitted successfully!');
    }
  };

  const isFieldValid = (field: keyof FormErrors, value: string | number) => {
    return touched[field as keyof TouchedFields] && !errors[field] && value !== '';
  };

  return (
    <section className="loan-form">
      <Container>
        <div className="loan-form__card">
          
          <div className="loan-form__two-columns">
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
                  onChange={handleAmountChange}
                />
                {errors.amount && touched.amount && (
                  <span className="loan-form__error">{errors.amount}</span>
                )}
              </div>
            </div>

            <div className="loan-form__right-column">
              <div className="loan-form__chosen-section">
                <span className="loan-form__chosen-label">You have chosen the amount</span>
                <span className="loan-form__chosen-value">{amount.toLocaleString()} ₽</span>
                <div className="loan-form__divider-horizontal"></div>
              </div>
            </div>
          </div>

          <div className="loan-form__contact">
            <h3 className="loan-form__contact-title">Contact Information</h3>
          </div>

          <form onSubmit={handleSubmit} className="loan-form__form">
            <div className="loan-form__row">
              <Input
                label="Your last name"
                value={formData.lastName}
                onChange={(v) => handleChange('lastName', v)}
                onBlur={() => handleBlur('lastName')}
                placeholder="Doe"
                required
                error={errors.lastName && touched.lastName ? errors.lastName : undefined}
                success={isFieldValid('lastName', formData.lastName)}
              />
              <Input
                label="Your first name"
                value={formData.firstName}
                onChange={(v) => handleChange('firstName', v)}
                onBlur={() => handleBlur('firstName')}
                placeholder="John"
                required
                error={errors.firstName && touched.firstName ? errors.firstName : undefined}
                success={isFieldValid('firstName', formData.firstName)}
              />
              <Input
                label="Your patronymic"
                value={formData.patronymic}
                onChange={(v) => handleChange('patronymic', v)}
                placeholder="Victorovich"
                required={false}
              />
              <Select
                label="Select term"
                value={formData.term}
                onChange={(v) => handleChange('term', v)}
                options={termOptions}
                placeholder="Select term"
                required
                error={errors.term && touched.term ? errors.term : undefined}
              />
            </div>

            <div className="loan-form__row">
              <Input
                label="Your email"
                type="email"
                value={formData.email}
                onChange={(v) => handleChange('email', v)}
                onBlur={() => handleBlur('email')}
                placeholder="test@gmail.com"
                required
                error={errors.email && touched.email ? errors.email : undefined}
                success={isFieldValid('email', formData.email)}
              />
              <Input
                label="Your date of birth"
                type="text"
                value={formData.dateOfBirth}
                onChange={(v) => handleChange('dateOfBirth', v)}
                onBlur={() => handleBlur('dateOfBirth')}
                placeholder="DD.MM.YYYY"
                required
                error={errors.dateOfBirth && touched.dateOfBirth ? errors.dateOfBirth : undefined}
                success={isFieldValid('dateOfBirth', formData.dateOfBirth)}
              />
              <Input
                label="Your passport series"
                value={formData.passportSeries}
                onChange={(v) => handleChange('passportSeries', v)}
                onBlur={() => handleBlur('passportSeries')}
                placeholder="0000"
                required
                error={errors.passportSeries && touched.passportSeries ? errors.passportSeries : undefined}
                success={isFieldValid('passportSeries', formData.passportSeries)}
              />
              <Input
                label="Your passport number"
                value={formData.passportNumber}
                onChange={(v) => handleChange('passportNumber', v)}
                onBlur={() => handleBlur('passportNumber')}
                placeholder="000000"
                required
                error={errors.passportNumber && touched.passportNumber ? errors.passportNumber : undefined}
                success={isFieldValid('passportNumber', formData.passportNumber)}
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