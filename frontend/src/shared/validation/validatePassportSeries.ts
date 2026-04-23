export const validatePassportSeries = (value: string) => {
    const trimmed = value.trim();
    if (!trimmed) return 'Passport series is required';
    if (value !== trimmed) return 'Spaces are not allowed';
    if (!/^\d{4}$/.test(trimmed)) return 'Must be exactly 4 digits';
    return true;
  };