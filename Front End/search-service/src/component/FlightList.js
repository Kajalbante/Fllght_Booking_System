import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getAllFlights, deleteFlight } from '../Service/flightService';
import '../Style/Flightlist.css';

const ListFlight = () => {
    const [flights, setFlights] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        loadFlights();
    }, []);

    const loadFlights = async () => {
        setIsLoading(true);
        setError(null);
        try {
            const data = await getAllFlights();
            setFlights(data);
        } catch (err) {
            console.error('Failed to load flights:', err);
            setError('Failed to load flights. Please try again later.');
        } finally {
            setIsLoading(false);
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm("Are you sure you want to delete this flight?")) {
            try {
                await deleteFlight(id);
                loadFlights();
            } catch (err) {
                console.error('Failed to delete flight:', err);
                setError('Failed to delete flight. Please try again.');
            }
        }
    };

    if (isLoading) return <div className="loading-state">Loading flights...</div>;
    if (error) return <div className="error-message">{error}</div>;

    return (
        <div className="flight-list-container">
            <div className="flight-list-header">
                <h2 className="flight-list-title">Flight List</h2>
                <button 
                    className="action-button primary-button"
                    onClick={() => navigate('/add')}
                >
                    + Add New Flight
                </button>
            </div>

            {flights.length === 0 ? (
                <div className="empty-state">No flights found. Add a new flight to get started.</div>
            ) : (
                <table className="flights-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Airline</th>
                            <th>Source</th>
                            <th>Destination</th>
                            <th>Departure Date</th>
                            <th>Total Seats</th>
                            <th>Available Seats</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {flights.map((flight) => (
                            <tr key={flight.id}>
                                <td>{flight.id}</td>
                                <td>{flight.airline}</td>
                                <td>{flight.source}</td>
                                <td>{flight.destination}</td>
                                <td>{new Date(flight.departureDate).toLocaleString()}</td>
                                <td>{flight.totalSeats}</td>
                                <td>{flight.availableSeats}</td>
                                <td className="action-cell">
                                    <button 
                                        className="update-button"
                                        onClick={() => navigate(`/update/${flight.id}`)}
                                    >
                                        Update
                                    </button>
                                    <button 
                                        className="delete-button"
                                        onClick={() => handleDelete(flight.id)}
                                    >
                                        Delete
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}
        </div>
    );
};

export default ListFlight;