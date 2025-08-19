import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { getFlightById, updateFlight } from '../Service/flightService';
import '../Style/updateflight.css';

const UpdateFlight = () => {
    const { id } = useParams();
    const navigate = useNavigate();
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
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const fetchFlight = async () => {
            try {
                const data = await getFlightById(id);
                setFlight({
                    ...data,
                    departureDate: data.departureDate?.slice(0, 16) // Format for datetime-local
                });
            } catch (error) {
                console.error('Error fetching flight:', error);
                setErrors({ fetch: 'Failed to load flight data' });
            } finally {
                setIsLoading(false);
            }
        };
        fetchFlight();
    }, [id]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFlight(prev => ({
            ...prev,
            [name]: value
        }));
        if (errors[name]) {
            setErrors(prev => ({ ...prev, [name]: '' }));
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
            await updateFlight(id, flight);
            navigate('/flights', { state: { success: 'Flight updated successfully!' } });
        } catch (error) {
            console.error('Error updating flight:', error);
            setErrors({ submit: error.response?.data?.message || 'Failed to update flight' });
        } finally {
            setIsSubmitting(false);
        }
    };

    if (isLoading) return <div className="loading-spinner"></div>;
    if (errors.fetch) return <div className="error-message">{errors.fetch}</div>;

    return (
        <div className="update-flight-container">
            <div className="form-header">
                <h2>Update Flight</h2>
                <p>Edit the flight details below</p>
            </div>

            <form onSubmit={handleSubmit} className="flight-form">
                <div className={`form-group ${errors.airline ? 'has-error' : ''}`}>
                    <label htmlFor="airline">Airline</label>
                    <input
                        type="text"
                        id="airline"
                        name="airline"
                        value={flight.airline}
                        onChange={handleChange}
                        placeholder="Enter airline name"
                    />
                    {errors.airline && <span className="error-text">{errors.airline}</span>}
                </div>

                <div className="form-row">
                    <div className={`form-group ${errors.source ? 'has-error' : ''}`}>
                        <label htmlFor="source">Source</label>
                        <input
                            type="text"
                            id="source"
                            name="source"
                            value={flight.source}
                            onChange={handleChange}
                            placeholder="Departure airport"
                        />
                        {errors.source && <span className="error-text">{errors.source}</span>}
                    </div>

                    <div className={`form-group ${errors.destination ? 'has-error' : ''}`}>
                        <label htmlFor="destination">Destination</label>
                        <input
                            type="text"
                            id="destination"
                            name="destination"
                            value={flight.destination}
                            onChange={handleChange}
                            placeholder="Arrival airport"
                        />
                        {errors.destination && <span className="error-text">{errors.destination}</span>}
                    </div>
                </div>

                <div className={`form-group ${errors.departureDate ? 'has-error' : ''}`}>
                    <label htmlFor="departureDate">Departure Date & Time</label>
                    <input
                        type="datetime-local"
                        id="departureDate"
                        name="departureDate"
                        value={flight.departureDate}
                        onChange={handleChange}
                    />
                    {errors.departureDate && <span className="error-text">{errors.departureDate}</span>}
                </div>

                <div className="form-row">
                    <div className={`form-group ${errors.totalSeats ? 'has-error' : ''}`}>
                        <label htmlFor="totalSeats">Total Seats</label>
                        <input
                            type="number"
                            id="totalSeats"
                            name="totalSeats"
                            value={flight.totalSeats}
                            onChange={handleChange}
                            min="1"
                            placeholder="Total capacity"
                        />
                        {errors.totalSeats && <span className="error-text">{errors.totalSeats}</span>}
                    </div>

                    <div className={`form-group ${errors.availableSeats ? 'has-error' : ''}`}>
                        <label htmlFor="availableSeats">Available Seats</label>
                        <input
                            type="number"
                            id="availableSeats"
                            name="availableSeats"
                            value={flight.availableSeats}
                            onChange={handleChange}
                            min="0"
                            placeholder="Seats available"
                        />
                        {errors.availableSeats && <span className="error-text">{errors.availableSeats}</span>}
                    </div>
                </div>

                <div className="form-actions">
                    <button
                        type="button"
                        className="cancel-btn"
                        onClick={() => navigate('/')}
                    >
                        Cancel
                    </button>
                    <button
                        type="submit"
                        className="submit-btn"
                        disabled={isSubmitting}
                    >
                        {isSubmitting ? (
                            <>
                                <span className="spinner"></span>
                                Updating...
                            </>
                        ) : (
                            'Update Flight'
                        )}
                    </button>
                </div>

                {errors.submit && <div className="form-error">{errors.submit}</div>}
            </form>
        </div>
    );
};

export default UpdateFlight;