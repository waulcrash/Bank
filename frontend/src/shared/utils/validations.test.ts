import {
    validateEmail,
    validateBirthdate,
    validatePassportSeries,
    validatePassportNumber,
    validateLastName,
    validateFirstName,
  } from '../validation';
  
  describe('Validation Functions', () => {
    describe('validateEmail', () => {
      test('returns error for empty email', () => {
        expect(validateEmail('')).toBe('Email is required');
      });
  
      test('returns error for invalid email format', () => {
        expect(validateEmail('invalid')).toBe('Enter a valid email address');
        expect(validateEmail('test@')).toBe('Enter a valid email address');
        expect(validateEmail('test@gmail')).toBe('Enter a valid email address');
      });
  
      test('returns true for valid email', () => {
        expect(validateEmail('test@gmail.com')).toBe(true);
        expect(validateEmail('user@domain.ru')).toBe(true);
      });
    });
  
    describe('validateBirthdate', () => {
      test('returns error for empty date', () => {
        expect(validateBirthdate('')).toBe('Date of birth is required');
      });
  
      test('returns error for invalid format', () => {
        expect(validateBirthdate('01/01/2000')).toBe('Use format DD.MM.YYYY');
        expect(validateBirthdate('2000-01-01')).toBe('Use format DD.MM.YYYY');
      });
  
      test('returns error for age under 18', () => {
        const today = new Date();
        const year = today.getFullYear() - 17;
        const underage = `01.01.${year}`;
        expect(validateBirthdate(underage)).toBe('You must be at least 18 years old');
      });
  
      test('returns true for valid date over 18', () => {
        expect(validateBirthdate('01.01.1990')).toBe(true);
      });
    });
  
    describe('validatePassportSeries', () => {
      test('returns error for empty series', () => {
        expect(validatePassportSeries('')).toBe('Passport series is required');
      });
  
      test('returns error for non-4-digit series', () => {
        expect(validatePassportSeries('123')).toBe('Must be exactly 4 digits');
        expect(validatePassportSeries('12345')).toBe('Must be exactly 4 digits');
      });
  
      test('returns true for valid series', () => {
        expect(validatePassportSeries('1234')).toBe(true);
      });
    });
  
    describe('validatePassportNumber', () => {
      test('returns error for empty number', () => {
        expect(validatePassportNumber('')).toBe('Passport number is required');
      });
  
      test('returns error for non-6-digit number', () => {
        expect(validatePassportNumber('12345')).toBe('Must be exactly 6 digits');
        expect(validatePassportNumber('1234567')).toBe('Must be exactly 6 digits');
      });
  
      test('returns true for valid number', () => {
        expect(validatePassportNumber('123456')).toBe(true);
      });
    });
  
    describe('validateLastName', () => {
      test('returns error for empty last name', () => {
        expect(validateLastName('')).toBe('Last name is required');
      });
  
      test('returns true for non-empty last name', () => {
        expect(validateLastName('Smith')).toBe(true);
      });
    });
  
    describe('validateFirstName', () => {
      test('returns error for empty first name', () => {
        expect(validateFirstName('')).toBe('First name is required');
      });
  
      test('returns true for non-empty first name', () => {
        expect(validateFirstName('John')).toBe(true);
      });
    });
  });