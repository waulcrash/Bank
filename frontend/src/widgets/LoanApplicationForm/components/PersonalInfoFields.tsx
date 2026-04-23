import React from 'react';
import { useFormContext, Controller } from 'react-hook-form';
import { Input } from '../../../shared/ui/Input/Input';
import { Select } from '../../../shared/ui/Select/Select';
import { Tooltip } from '../../../shared/ui/Tooltip/Tooltip';
import { termOptions } from '../../../shared/constants/formConstants';
import {
  validateLastName,
  validateFirstName,
  validateTerm,
} from '../../../shared/validation';

const removeSpaces = (value: string) => value.replace(/\s/g, '');

export const PersonalInfoFields: React.FC = () => {
  const { control, formState: { errors, touchedFields } } = useFormContext();

  return (
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
              onChange={(v) => field.onChange(removeSpaces(v))}
              onBlur={field.onBlur}
              placeholder="Doe"
              required
              error={errors.lastName?.message as string | undefined}
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
              onChange={(v) => field.onChange(removeSpaces(v))}
              onBlur={field.onBlur}
              placeholder="John"
              required
              error={errors.firstName?.message as string | undefined}
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
            onChange={(v) => field.onChange(removeSpaces(v))}
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
              error={errors.term?.message as string | undefined}
            />
          </Tooltip>
        )}
      />
    </div>
  );
};