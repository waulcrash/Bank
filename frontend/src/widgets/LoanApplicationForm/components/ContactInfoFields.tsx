import React from 'react';
import { useFormContext, Controller } from 'react-hook-form';
import { Input } from '../../../shared/ui/Input/Input';
import { Tooltip } from '../../../shared/ui/Tooltip/Tooltip';
import {
  validateEmail,
  validateBirthdate,
  validatePassportSeries,
  validatePassportNumber,
} from '../../../shared/validation';

const removeSpaces = (value: string) => value.replace(/\s/g, '');

export const ContactInfoFields: React.FC = () => {
  const { control, formState: { errors, touchedFields } } = useFormContext();

  return (
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
              onChange={(v) => field.onChange(removeSpaces(v))}
              onBlur={field.onBlur}
              placeholder="test@gmail.com"
              required
              error={errors.email?.message as string | undefined}
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
              onChange={(v) => {
                let newValue = removeSpaces(v);
                if (newValue.length === 2 || newValue.length === 5) {
                  newValue += '.';
                }
                field.onChange(newValue);
              }}
              onBlur={field.onBlur}
              placeholder="DD.MM.YYYY"
              required
              error={errors.dateOfBirth?.message as string | undefined}
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
              onChange={(v) => {
                const newValue = removeSpaces(v);
                if (newValue.length <= 4) field.onChange(newValue);
              }}
              onBlur={field.onBlur}
              placeholder="0000"
              required
              error={errors.passportSeries?.message as string | undefined}
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
              onChange={(v) => {
                const newValue = removeSpaces(v);
                if (newValue.length <= 6) field.onChange(newValue);
              }}
              onBlur={field.onBlur}
              placeholder="000000"
              required
              error={errors.passportNumber?.message as string | undefined}
              success={touchedFields.passportNumber && !errors.passportNumber && field.value !== ''}
            />
          </Tooltip>
        )}
      />
    </div>
  );
};