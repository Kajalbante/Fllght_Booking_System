import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom'; // ⬅️ import navigate
import { addFlight } from '../Service/flightService';
import '../Style/addflight.css';

const AddFlight = ({ onFlightAdded }) => {
    const navigate = useNavigate(); // ⬅️ create navigate instance

    const [flight, setFlight] = useState({
        airline: '',
        source: '',
        destination: '',
        departureDate: '',
        totalSeats: '',
        availableSeats: ''
    });

    const [errors, setErrors] = useState({});
    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFlight(prev => ({
            ...prev,
            [name]: value
        }));
        // Clear error when user types
        if (errors[name]) {
            setErrors(prev => ({
                ...prev,
                [name]: ''
            }));
        }
    };

    const validateForm = () => {
        const newErrors = {};
        if (!flight.airline.trim()) newErrors.airline = 'Airline is required';
        if (!flight.source.trim()) newErrors.source = 'Source is required';
        if (!flight.destination.trim()) newErrors.destination = 'Destination is required';
        if (!flight.departureDate) newErrors.departureDate = 'Departure date is required';
        if (!flight.totalSeats || flight.totalSeats < 1) newErrors.totalSeats = 'Must have at least 1 seat';
        if (!flight.availableSeats || flight.availableSeats < 0) newErrors.availableSeats = 'Cannot be negative';
        if (flight.availableSeats > flight.totalSeats) newErrors.availableSeats = 'Cannot exceed total seats';

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!validateForm()) return;

        setIsSubmitting(true);
        
        try {
            await addFlight(flight);
            if (onFlightAdded) onFlightAdded();
            setFlight({
                airline: '',
                source: '',
                destination: '',
                departureDate: '',
                totalSeats: '',
                availableSeats: ''
            });
            alert('Flight added successfully!');
        } catch (error) {
            console.error('Error adding flight:', error);
            setErrors({
                submit: error.message || 'Failed to add flight'
            });
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div className="add-flight-container">
            <h2>Add New Flight</h2>
            <form onSubmit={handleSubmit} className="add-flight-form">
                <div className={`form-group ${errors.airline ? 'error' : ''}`}>
                    <label>Airline:</label>
                    <input 
                        type="text" 
                        name="airline" 
                        value={flight.airline} 
                        onChange={handleChange} 
                        required 
                        placeholder="e.g., Delta Airlines"
                    />
                    {errors.airline && <span className="error-message">{errors.airline}</span>}
                </div>
                
                <div className={`form-group ${errors.source ? 'error' : ''}`}>
                    <label>Source:</label>
                    <input 
                        type="text" 
                        name="source" 
                        value={flight.source} 
                        onChange={handleChange} 
                        required 
                        placeholder="e.g., JFK"
                    />
                    {errors.source && <span className="error-message">{errors.source}</span>}
                </div>
                
                <div className={`form-group ${errors.destination ? 'error' : ''}`}>
                    <label>Destination:</label>
                    <input 
                        type="text" 
                        name="destination" 
                        value={flight.destination} 
                        onChange={handleChange} 
                        required 
                        placeholder="e.g., LAX"
                    />
                    {errors.destination && <span className="error-message">{errors.destination}</span>}
                </div>
                
                <div className={`form-group ${errors.departureDate ? 'error' : ''}`}>
                    <label>Departure Date:</label>
                    <input 
                        type="datetime-local" 
                        name="departureDate" 
                        value={flight.departureDate} 
                        onChange={handleChange} 
                        required 
                    />
                    {errors.departureDate && <span className="error-message">{errors.departureDate}</span>}
                </div>
                
                <div className={`form-group ${errors.totalSeats ? 'error' : ''}`}>
                    <label>Total Seats:</label>
                    <input 
                        type="number" 
                        name="totalSeats" 
                        value={flight.totalSeats} 
                        onChange={handleChange} 
                        required 
                        min="1"
                        placeholder="e.g., 150"
                    />
                    {errors.totalSeats && <span className="error-message">{errors.totalSeats}</span>}
                </div>
                
                <div className={`form-group ${errors.availableSeats ? 'error' : ''}`}>
                    <label>Available Seats:</label>
                    <input 
                        type="number" 
                        name="availableSeats" 
                        value={flight.availableSeats} 
                        onChange={handleChange} 
                        required 
                        min="0"
                        placeholder="e.g., 150"
                    />
                    {errors.availableSeats && <span className="error-message">{errors.availableSeats}</span>}
                </div>
                
                <button 
                    type="submit" 
                    className="submit-btn"
                    disabled={isSubmitting}
                >
                    {isSubmitting ? 'Adding...' : 'Add Flight'}
                </button>

                <button
                        type="button"
                        className="cancel-btn"
                        onClick={() => navigate('/')}
                        style={{ marginLeft: '1rem', backgroundColor: '#888', color: '#fff' }}
                    >
                        Cancel
                    </button>
                
                {errors.submit && (
                    <div className="error-message" style={{ textAlign: 'center', marginTop: '1rem' }}>
                        {errors.submit}
                    </div>
                )}
            </form>
        </div>
    );
};

export default AddFlight;