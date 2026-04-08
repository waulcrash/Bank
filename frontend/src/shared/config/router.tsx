import { Routes, Route } from 'react-router-dom';
import { LoanPage } from '../../pages/LoanPage/LoanPage';

export const AppRouter = () => {
  return (
    <Routes>
      <Route path="/" element={<LoanPage />} />
      <Route path="/loan" element={<LoanPage />} />
    </Routes>
  );
};