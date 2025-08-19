import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import BookingForm from './component/BookingForm';
import RazorpayCheckout from './component/RazorpayCheckout';

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/booking/:flightId" element={<BookingForm />} />
                <Route path="/payment" element={<RazorpayCheckout />} />
            </Routes>
        </Router>
    );
}

export default App;
