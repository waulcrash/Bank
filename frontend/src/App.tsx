import { BrowserRouter } from 'react-router-dom';
import { AppRouter } from './shared/config/router';
import './App.css';

function App() {
  return (
    <BrowserRouter>
      <AppRouter />
    </BrowserRouter>
  );
}

export default App;