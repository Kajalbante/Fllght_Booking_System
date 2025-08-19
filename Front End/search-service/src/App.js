import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import ListFlight from './component/FlightList';
import AddFlight from './component/AddFlight';
import UpdateFlight from './component/UpdateFlight';
import SearchForm from './component/SearchForm';
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
              <Route path="/admin" element={<ListFlight />} />
              <Route path="/" element={<SearchForm />} />
              <Route path="/add" element={<AddFlight />} />
              <Route path="/update/:id" element={<UpdateFlight />} />
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