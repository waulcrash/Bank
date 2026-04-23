import React, { useState, useEffect } from 'react';
import { useForm, Controller } from 'react-hook-form';
import { Container } from '../../shared/ui/Container/Container';
import { Button } from '../../shared/ui/Button/Button';
import { Input } from '../../shared/ui/Input/Input';
import { Select } from '../../shared/ui/Select/Select';
import { RangeSlider } from '../../shared/ui/RangeSlider/RangeSlider';
import { Tooltip } from '../../shared/ui/Tooltip/Tooltip';
import './LoanApplicationForm.css';

interface FormData {
  lastName: string;
  firstName: string;
  patronymic: string;
  term: string;
  email: string;
  dateOfBirth: string;
  passportSeries: string;
  passportNumber: string;
  amount: number;
}

export const LoanApplicationForm: React.FC = () => {
  const [localAmount, setLocalAmount] = useState<string>('150000');
  const [isAmountFocused, setIsAmountFocused] = useState(false);

  const {
    control,
    handleSubmit,
    watch,
    setValue,
    formState: { errors, isValid, touchedFields },
  } = useForm<FormData>({
    mode: 'onChange',
    defaultValues: {
      lastName: '',
      firstName: '',
      patronymic: '',
      term: '6',
      email: '',
      dateOfBirth: '',
      passportSeries: '',
      passportNumber: '',
      amount: 150000,
    },
  });

  const amount = watch('amount');
  const termOptions = [
    { value: '6', label: '6 months' },
    { value: '12', label: '12 months' },
    { value: '18', label: '18 months' },
    { value: '24', label: '24 months' },
  ];

  // Синхронизация localAmount при изменении amount
  useEffect(() => {
    if (!isAmountFocused) {
      setLocalAmount(amount.toString());
    }
  }, [amount, isAmountFocused]);

  const removeSpaces = (value: string) => value.replace(/\s/g, '');

  const validateLastName = (value: string) => {
    if (!value) return 'Last name is required';
    return true;
  };

  const validateFirstName = (value: string) => {
    if (!value) return 'First name is required';
    return true;
  };

  const validateEmail = (value: string) => {
    if (!value) return 'Email is required';
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(value)) return 'Enter a valid email address';
    return true;
  };

  const validateBirthdate = (value: string) => {
    const trimmed = value.trim();
    if (!trimmed) return 'Date of birth is required';
    if (value !== trimmed) return 'Spaces are not allowed';
    const dateRegex = /^(0[1-9]|[12][0-9]|3[01])\.(0[1-9]|1[0-2])\.(19[0-9]{2}|20[0-9]{2})$/;
    if (!dateRegex.test(trimmed)) return 'Use format DD.MM.YYYY';
    
    const parts = trimmed.split('.');
    const day = Number(parts[0]);
    const month = Number(parts[1]);
    const year = Number(parts[2]);
    const date = new Date(year, month - 1, day);
    const today = new Date();
    let age = today.getFullYear() - date.getFullYear();
    const monthDiff = today.getMonth() - date.getMonth();
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < date.getDate())) {
      age--;
    }
    if (age < 18) return 'You must be at least 18 years old';
    return true;
  };

  const validatePassportSeries = (value: string) => {
    const trimmed = value.trim();
    if (!trimmed) return 'Passport series is required';
    if (value !== trimmed) return 'Spaces are not allowed';
    if (!/^\d{4}$/.test(trimmed)) return 'Must be exactly 4 digits';
    return true;
  };

  const validatePassportNumber = (value: string) => {
    const trimmed = value.trim();
    if (!trimmed) return 'Passport number is required';
    if (value !== trimmed) return 'Spaces are not allowed';
    if (!/^\d{6}$/.test(trimmed)) return 'Must be exactly 6 digits';
    return true;
  };

  const validateTerm = (value: string) => {
    if (!value) return 'Term is required';
    if (value.trim() === '') return 'Term cannot be only spaces';
    return true;
  };

  const handleAmountChange = (newAmount: number) => {
    setValue('amount', newAmount, { shouldValidate: true });
  };

  const handleAmountFocus = () => {
    setIsAmountFocused(true);
    setLocalAmount('');
  };

  const handleAmountBlur = () => {
    setIsAmountFocused(false);
    let value = parseInt(localAmount);
    if (isNaN(value)) {
      value = 15000;
    }
    const clampedValue = Math.min(600000, Math.max(15000, value));
    setLocalAmount(clampedValue.toString());
    handleAmountChange(clampedValue);
  };

  const handleAmountInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const rawValue = e.target.value;
    setLocalAmount(rawValue);
    
    const parsed = parseInt(rawValue);
    if (!isNaN(parsed) && parsed >= 15000 && parsed <= 600000) {
      handleAmountChange(parsed);
    }
  };

  const onSubmit = (data: FormData) => {
    console.log('Form submitted:', data);
    alert('Application submitted successfully!');
  };

  // Отображаемое значение: при фокусе - localAmount, иначе - форматированное число
  const displayValue = isAmountFocused ? localAmount : amount.toLocaleString();

  return (
    <section className="loan-form">
      <Container>
        <div className="loan-form__card">
          
          <Controller
            name="amount"
            control={control}
            rules={{
              required: 'Amount is required',
              min: { value: 15000, message: 'Minimum amount is 15 000 ₽' },
              max: { value: 600000, message: 'Maximum amount is 600 000 ₽' },
            }}
            render={({ field }) => <input type="hidden" {...field} />}
          />

          <div className="loan-form__two-columns">
            <div className="loan-form__left-column">
              <div className="loan-form__header-row">
                <h2 className="loan-form__title">Customize your card</h2>
                <span className="loan-form__step">Step 1 of 5</span>
              </div>
              
              <div className="loan-form__slider-card">
                <Tooltip content="Available amount from 15 000 ₽ to 600 000 ₽" position="top">
                  <label className="loan-form__slider-label">Select amount</label>
                </Tooltip>
                
                <input
                  type="text"
                  value={displayValue}
                  onChange={handleAmountInputChange}
                  onFocus={handleAmountFocus}
                  onBlur={handleAmountBlur}
                  className="loan-form__current-amount-input"
                  inputMode="numeric"
                  pattern="[0-9]*"
                />
                
                <RangeSlider
                  min={15000}
                  max={600000}
                  value={amount}
                  onChange={handleAmountChange}
                />
                
                {errors.amount && (
                  <span className="loan-form__error">{errors.amount.message}</span>
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

          <form onSubmit={handleSubmit(onSubmit)} className="loan-form__form">
            <div className="loan-form__row">
              <Controller
                name="lastName"
                control={control}
                rules={{ validate: validateLastName }}
                render={({ field }) => (
                  <Tooltip content="Enter your last name as in passport" position="top">
                    <Input
                      label="Your last name"
                      value={field.value}
                      onChange={(value: string) => {
                        const newValue = removeSpaces(value);
                        field.onChange(newValue);
                      }}
                      onBlur={field.onBlur}
                      placeholder="Doe"
                      required
                      error={errors.lastName?.message}
                      success={touchedFields.lastName && !errors.lastName && field.value !== ''}
                    />
                  </Tooltip>
                )}
              />
              
              <Controller
                name="firstName"
                control={control}
                rules={{ validate: validateFirstName }}
                render={({ field }) => (
                  <Tooltip content="Enter your first name as in passport" position="top">
                    <Input
                      label="Your first name"
                      value={field.value}
                      onChange={(value: string) => {
                        const newValue = removeSpaces(value);
                        field.onChange(newValue);
                      }}
                      onBlur={field.onBlur}
                      placeholder="John"
                      required
                      error={errors.firstName?.message}
                      success={touchedFields.firstName && !errors.firstName && field.value !== ''}
                    />
                  </Tooltip>
                )}
              />
              
              <Controller
                name="patronymic"
                control={control}
                render={({ field }) => (
                  <Input
                    label="Your patronymic"
                    value={field.value}
                    onChange={(value: string) => {
                      const newValue = removeSpaces(value);
                      field.onChange(newValue);
                    }}
                    placeholder="Victorovich"
                    required={false}
                  />
                )}
              />
              
              <Controller
                name="term"
                control={control}
                rules={{ validate: validateTerm }}
                render={({ field }) => (
                  <Tooltip content="Choose loan term: 6, 12, 18, or 24 months" position="top">
                    <Select
                      label="Select term"
                      value={field.value}
                      onChange={field.onChange}
                      options={termOptions}
                      placeholder="Select term"
                      required
                      error={errors.term?.message}
                    />
                  </Tooltip>
                )}
              />
            </div>

            <div className="loan-form__row">
              <Controller
                name="email"
                control={control}
                rules={{ validate: validateEmail }}
                render={({ field }) => (
                  <Tooltip content="We will send confirmation to this email" position="top">
                    <Input
                      label="Your email"
                      type="email"
                      value={field.value}
                      onChange={(value: string) => {
                        const newValue = removeSpaces(value);
                        field.onChange(newValue);
                      }}
                      onBlur={field.onBlur}
                      placeholder="test@gmail.com"
                      required
                      error={errors.email?.message}
                      success={touchedFields.email && !errors.email && field.value !== ''}
                    />
                  </Tooltip>
                )}
              />
              
              <Controller
                name="dateOfBirth"
                control={control}
                rules={{ validate: validateBirthdate }}
                render={({ field }) => (
                  <Tooltip content="Format: DD.MM.YYYY. You must be at least 18 years old" position="top">
                    <Input
                      label="Your date of birth"
                      type="text"
                      value={field.value}
                      onChange={(value: string) => {
                        let newValue = removeSpaces(value);
                        if (newValue.length === 2 || newValue.length === 5) {
                          newValue += '.';
                        }
                        field.onChange(newValue);
                      }}
                      onBlur={field.onBlur}
                      placeholder="DD.MM.YYYY"
                      required
                      error={errors.dateOfBirth?.message}
                      success={touchedFields.dateOfBirth && !errors.dateOfBirth && field.value !== ''}
                    />
                  </Tooltip>
                )}
              />
              
              <Controller
                name="passportSeries"
                control={control}
                rules={{ validate: validatePassportSeries }}
                render={({ field }) => (
                  <Tooltip content="First 4 digits of your passport" position="top">
                    <Input
                      label="Your passport series"
                      value={field.value}
                      onChange={(value: string) => {
                        const newValue = removeSpaces(value);
                        if (newValue.length <= 4) {
                          field.onChange(newValue);
                        }
                      }}
                      onBlur={field.onBlur}
                      placeholder="0000"
                      required
                      error={errors.passportSeries?.message}
                      success={touchedFields.passportSeries && !errors.passportSeries && field.value !== ''}
                    />
                  </Tooltip>
                )}
              />
              
              <Controller
                name="passportNumber"
                control={control}
                rules={{ validate: validatePassportNumber }}
                render={({ field }) => (
                  <Tooltip content="Last 6 digits of your passport" position="top">
                    <Input
                      label="Your passport number"
                      value={field.value}
                      onChange={(value: string) => {
                        const newValue = removeSpaces(value);
                        if (newValue.length <= 6) {
                          field.onChange(newValue);
                        }
                      }}
                      onBlur={field.onBlur}
                      placeholder="000000"
                      required
                      error={errors.passportNumber?.message}
                      success={touchedFields.passportNumber && !errors.passportNumber && field.value !== ''}
                    />
                  </Tooltip>
                )}
              />
            </div>

            <div className="loan-form__actions">
              <Button type="submit" variant="primary" size="large" disabled={!isValid}>
                Continue
              </Button>
            </div>
          </form>
        </div>
      </Container>
    </section>
  );
};