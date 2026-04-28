import { Routes, Route } from 'react-router-dom';
import { LoanPage } from '../../pages/LoanPage/LoanPage';
import { TestApiPage } from '../../pages/TestApiPage/TestApiPage';

export const AppRouter = () => {
  return (
    <Routes>
      <Route path="/" element={<LoanPage />} />
      <Route path="/loan" element={<LoanPage />} />
      <Route path="/test-api" element={<TestApiPage />} />
    </Routes>
  );
};