export const validateFirstName = (value: string) => {
    if (!value) return 'First name is required';
    return true;
  };