export const validateTerm = (value: string) => {
    if (!value) return 'Term is required';
    return true;
  };