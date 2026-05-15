import { render, screen } from '@testing-library/react';
import { Loader } from './Loader';

describe('Loader Component', () => {
  // Тест 1: рендеринг с размером по умолчанию
  test('renders loader with default size (medium)', () => {
    render(<Loader />);
    const loader = document.querySelector('.loader--medium');
    expect(loader).toBeInTheDocument();
  });

  // Тест 2: рендеринг с маленьким размером
  test('renders loader with small size', () => {
    render(<Loader size="small" />);
    const loader = document.querySelector('.loader--small');
    expect(loader).toBeInTheDocument();
  });

  // Тест 3: рендеринг с большим размером
  test('renders loader with large size', () => {
    render(<Loader size="large" />);
    const loader = document.querySelector('.loader--large');
    expect(loader).toBeInTheDocument();
  });

  // Тест 4: отображение текста
  test('renders loader with text', () => {
    render(<Loader text="Loading..." />);
    expect(screen.getByText('Loading...')).toBeInTheDocument();
  });

  // Тест 5: спиннер имеет правильные CSS классы
  test('spinner has correct classes', () => {
    render(<Loader size="medium" />);
    const spinner = document.querySelector('.loader__spinner');
    expect(spinner).toBeInTheDocument();
  });
});