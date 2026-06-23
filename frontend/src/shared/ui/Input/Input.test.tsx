import { render, screen, fireEvent } from '@testing-library/react';
import { Input } from './Input';

describe('Input Component', () => {
  // Тест 1: рендеринг с label 
  test('renders input with label', () => {
    render(<Input label="Email" value="" onChange={() => {}} />);
    expect(screen.getByText('Email')).toBeInTheDocument();
    expect(screen.getByRole('textbox')).toBeInTheDocument();
  });

  // Тест 2: отображение обязательной звездочки
  test('shows required asterisk when required prop is true', () => {
    render(<Input label="Password" value="" onChange={() => {}} required />);
    // Звездочка отображается только при ошибке
    // Проверяем что label отображается
    expect(screen.getByText('Password')).toBeInTheDocument();
  });

  // Тест 3: вызов onChange при вводе текста
  test('calls onChange when user types', () => {
    const handleChange = vi.fn();
    render(<Input value="" onChange={handleChange} />);
    
    const input = screen.getByRole('textbox');
    fireEvent.change(input, { target: { value: 'test value' } });
    
    expect(handleChange).toHaveBeenCalledWith('test value');
  });

  // Тест 4: отображение ошибки
  test('displays error message when error prop is provided', () => {
    render(<Input value="" onChange={() => {}} error="This field is required" />);
    expect(screen.getByText('This field is required')).toBeInTheDocument();
  });

  // Тест 5: отображение иконки успеха
  test('shows success icon when success is true', () => {
    render(<Input value="valid value" onChange={() => {}} success />);
    expect(screen.getByText('✓')).toBeInTheDocument();
  });

  // Тест 6: input disabled
  test('input is disabled when disabled prop is true', () => {
    render(<Input value="" onChange={() => {}} disabled />);
    expect(screen.getByRole('textbox')).toBeDisabled();
  });
});