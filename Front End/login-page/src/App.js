import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link,Navigate } from 'react-router-dom';
import LoginPage from './Components/LoginPage';
import RegisterPage from './Components/RegisterPage';
import './App.css';

function App() {
  return (
    <Router>
      <div className="app-container">
        {/* Header */}
        <header className="app-header">
          <div className="header-content">
            <Link to="/" className="logo">
              <span>Skyline Airways</span>
            </Link>
          </div>
        </header>

        {/* Main Content */}
        <main className="app-main">
          <div className="content-container">
            <Routes>
            <Route path="/" element={<Navigate to="/login" />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
            </Routes>
          </div>
        </main>

        {/* Footer */}
        <footer className="app-footer">
          <div className="footer-content">
            <div className="footer-section">
              <h3>Skyline Airways</h3>
              <p>Your journey begins with us</p>
            </div>
            <div className="footer-section">
              <p>&copy; {new Date().getFullYear()} Skyline Airways. All rights reserved.</p>
            </div>
          </div>
        </footer>
      </div>
    </Router>
  );
}

export default App;