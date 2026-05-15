import { render, screen, fireEvent } from '@testing-library/react';
import { Select } from './Select';

const options = [
  { value: '6', label: '6 months' },
  { value: '12', label: '12 months' },
  { value: '18', label: '18 months' },
  { value: '24', label: '24 months' },
];

describe('Select Component', () => {
  // Тест 1: рендеринг селекта с лейблом
  test('renders select with label', () => {
    render(<Select label="Select term" value="6" onChange={() => {}} options={options} />);
    expect(screen.getByText('Select term')).toBeInTheDocument();
    expect(screen.getByRole('combobox')).toBeInTheDocument();
  });

  // Тест 2: отображение всех опций
  test('renders all options', () => {
    render(<Select value="6" onChange={() => {}} options={options} />);
    
    options.forEach(option => {
      expect(screen.getByText(option.label)).toBeInTheDocument();
    });
  });

  // Тест 3: вызов onChange при выборе опции
  test('calls onChange when selecting option', () => {
    const handleChange = vi.fn();
    render(<Select value="6" onChange={handleChange} options={options} />);
    
    fireEvent.change(screen.getByRole('combobox'), { target: { value: '12' } });
    expect(handleChange).toHaveBeenCalledWith('12');
  });

  // Тест 4: отображение ошибки
  test('displays error message when error prop is provided', () => {
    render(<Select value="6" onChange={() => {}} options={options} error="This field is required" />);
    expect(screen.getByText('This field is required')).toBeInTheDocument();
  });
});