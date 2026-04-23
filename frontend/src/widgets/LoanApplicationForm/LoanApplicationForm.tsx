import React, { useState, useEffect } from 'react';
import { FormProvider, useForm, Controller } from 'react-hook-form';
import { Container } from '../../shared/ui/Container/Container';
import { Button } from '../../shared/ui/Button/Button';
import { RangeSlider } from '../../shared/ui/RangeSlider/RangeSlider';
import { Tooltip } from '../../shared/ui/Tooltip/Tooltip';
import { defaultFormValues } from '../../shared/constants/formConstants';
import { PersonalInfoFields } from './components/PersonalInfoFields';
import { ContactInfoFields } from './components/ContactInfoFields';
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

  const methods = useForm<FormData>({
    mode: 'onChange',
    defaultValues: defaultFormValues,
  });

  const { watch, setValue, formState: { errors, isValid } } = methods;
  const amount = watch('amount');

  useEffect(() => {
    if (!isAmountFocused) {
      setLocalAmount(amount.toString());
    }
  }, [amount, isAmountFocused]);

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
    if (isNaN(value)) value = 15000;
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
    <FormProvider {...methods}>
      <section className="loan-form">
        <Container>
          <div className="loan-form__card">
            
            <Controller
              name="amount"
              control={methods.control}
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

            <form onSubmit={methods.handleSubmit(onSubmit)} className="loan-form__form">
              <PersonalInfoFields />
              <ContactInfoFields />
              <div className="loan-form__actions">
                <Button type="submit" variant="primary" size="large" disabled={!isValid}>
                  Continue
                </Button>
              </div>
            </form>
          </div>
        </Container>
      </section>
    </FormProvider>
  );
};