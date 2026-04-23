export const validatePassportNumber = (value: string) => {
    const trimmed = value.trim();
    if (!trimmed) return 'Passport number is required';
    if (value !== trimmed) return 'Spaces are not allowed';
    if (!/^\d{6}$/.test(trimmed)) return 'Must be exactly 6 digits';
    return true;
  };