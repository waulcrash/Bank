export const validateLastName = (value: string) => {
    if (!value) return 'Last name is required';
    return true;
  };