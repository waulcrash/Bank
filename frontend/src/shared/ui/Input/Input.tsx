import React from 'react';
import './Input.css';

interface InputProps {
  label?: string;
  type?: string;
  value: string;
  onChange: (value: string) => void;
  onBlur?: () => void;
  placeholder?: string;
  error?: string;
  required?: boolean;
  disabled?: boolean;
  name?: string;
  success?: boolean;
}

export const Input: React.FC<InputProps> = ({
  label,
  type = 'text',
  value,
  onChange,
  onBlur,
  placeholder,
  error,
  required = false,
  disabled = false,
  name,
  success = false,
}) => {
  return (
    <div className="input">
      {label && (
        <label className="input__label">
          {label}
          {required && <span className="input__required">*</span>}
        </label>
      )}
      <div className="input__wrapper">
        <input
          type={type}
          className={`input__field 
            ${error ? 'input__field--error' : ''} 
            ${success ? 'input__field--success' : ''}`}
          value={value}
          onChange={(e) => onChange(e.target.value)}
          onBlur={onBlur}
          placeholder={placeholder}
          disabled={disabled}
          name={name}
        />
        {success && !error && (
          <span className="input__success-icon">✓</span>
        )}
      </div>
      {error && <span className="input__error">{error}</span>}
    </div>
  );
};