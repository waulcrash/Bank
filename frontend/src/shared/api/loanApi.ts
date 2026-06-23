export type LoanApplicationData = {
  amount: number;
  term: number;
  firstName: string;
  lastName: string;
  middleName?: string;
  email: string;
  birthdate: string;
  passportSeries: string;
  passportNumber: string;
}

export type CreditOffer = {
  statementId: string;
  requestedAmount: number;
  totalAmount: number;
  term: number;
  monthlyPayment: number;
  rate: number;
  isInsuranceEnabled: boolean;
  isSalaryClient: boolean;
}

const API_BASE = 'http://localhost:8081';

export const loanApi = {
  // Отправка заявки
  submitApplication: async (data: LoanApplicationData): Promise<CreditOffer[]> => {
    const { amount, term, firstName, lastName, middleName, email, birthdate, passportSeries, passportNumber } = data;
    
    const response = await fetch(`${API_BASE}/deal/statement`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        amount,
        term,
        firstName,
        lastName,
        middleName,
        email,
        birthdate,
        passportSeries,
        passportNumber,
      }),
    });
    
    if (!response.ok) {
      throw new Error('Failed to submit application');
    }
    
    return response.json();
  },

  // Выбор предложения
  selectOffer: async (offer: CreditOffer): Promise<void> => {
    const { statementId, requestedAmount, totalAmount, term, monthlyPayment, rate, isInsuranceEnabled, isSalaryClient } = offer;
    
    const response = await fetch(`${API_BASE}/deal/offer/select`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        statementId,
        requestedAmount,
        totalAmount,
        term,
        monthlyPayment,
        rate,
        isInsuranceEnabled,
        isSalaryClient,
      }),
    });
    
    if (!response.ok) {
      throw new Error('Failed to select offer');
    }
  },
};