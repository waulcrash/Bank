export const validateBirthdate = (value: string) => {
    const trimmed = value.trim();
    if (!trimmed) return 'Date of birth is required';
    if (value !== trimmed) return 'Spaces are not allowed';
    const dateRegex = /^(0[1-9]|[12][0-9]|3[01])\.(0[1-9]|1[0-2])\.(19[0-9]{2}|20[0-9]{2})$/;
    if (!dateRegex.test(trimmed)) return 'Use format DD.MM.YYYY';
    
    const [day, month, year] = trimmed.split('.').map(Number);
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