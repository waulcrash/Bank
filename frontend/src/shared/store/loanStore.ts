import { create } from 'zustand';
import type { CreditOffer, LoanApplicationData } from '../api/loanApi';
import { loanApi } from '../api/loanApi';

interface LoanState {
  // Состояние
  isSubmitting: boolean;
  isLoading: boolean;
  offers: CreditOffer[];
  selectedOffer: CreditOffer | null;
  error: string | null;
  step: 'form' | 'offers' | 'success';
  
  // Действия
  submitApplication: (data: LoanApplicationData) => Promise<void>;
  selectOffer: (offer: CreditOffer) => Promise<void>;
  reset: () => void;
  clearError: () => void;
}

export const useLoanStore = create<LoanState>((set) => ({
  isSubmitting: false,
  isLoading: false,
  offers: [],
  selectedOffer: null,
  error: null,
  step: 'form',
  
  submitApplication: async (data) => {
    set({ isSubmitting: true, error: null });
    
    try {
      const offers = await loanApi.submitApplication(data);
      
      set({
        offers,
        step: 'offers',
        isSubmitting: false,
      });
    } catch (error) {
      set({
        error: error instanceof Error ? error.message : 'Failed to submit application',
        isSubmitting: false,
      });
    }
  },
  
  selectOffer: async (offer) => {
    set({ isLoading: true, error: null });
    
    try {
      await loanApi.selectOffer(offer);
      
      set({
        selectedOffer: offer,
        step: 'success',
        isLoading: false,
      });
    } catch (error) {
      set({
        error: error instanceof Error ? error.message : 'Failed to select offer',
        isLoading: false,
      });
    }
  },
  
  reset: () => {
    set({
      isSubmitting: false,
      isLoading: false,
      offers: [],
      selectedOffer: null,
      error: null,
      step: 'form',
    });
  },
  
  clearError: () => {
    set({ error: null });
  },
}));