import React, { useState } from 'react';
import { searchFlights } from '../Service/SearchService';
import '../Style/searchform.css'; // Import the CSS file

const SearchForm = () => {
    const [flights, setFlights] = useState([]);
    const [source, setSource] = useState('');
    const [destination, setDestination] = useState('');
    const [departureDate, setDepartureDate] = useState('');

    const handleSearch = async (e) => {
        e.preventDefault();
        try {
            const results = await searchFlights(source, destination, departureDate);
            setFlights(results);
        } catch (error) {
            console.error("Search error:", error);
        }
    };

    const handleBook = (flightId) => {
        window.location.href = `http://localhost:3002/booking/${flightId}`;
    };

    return (
        <div className="search-container">
            <div className="search-header">
                <h2>Search Flights</h2>
            </div>
            
            <form className="search-form" onSubmit={handleSearch}>
                <input
                    type="text"
                    placeholder="Source City"
                    value={source}
                    onChange={(e) => setSource(e.target.value)}
                    required
                />
                <input
                    type="text"
                    placeholder="Destination City"
                    value={destination}
                    onChange={(e) => setDestination(e.target.value)}
                    required
                />
                <input
                    type="date"
                    value={departureDate}
                    onChange={(e) => setDepartureDate(e.target.value)}
                />
                <button type="submit">Search Flights</button>
            </form>

            <div className="results-container">
                <div className="results-header">
                    <h3>Available Flights</h3>
                </div>
                
                <table className="flights-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Airline</th>
                            <th>From</th>
                            <th>To</th>
                            <th>Departure</th>
                            <th>Total Seats</th>
                            <th>Available</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        {flights.length > 0 ? (
                            flights.map((flight) => (
                                <tr key={flight.id}>
                                    <td>{flight.id}</td>
                                    <td>{flight.airline}</td>
                                    <td>{flight.source}</td>
                                    <td>{flight.destination}</td>
                                    <td>{new Date(flight.departureDate).toLocaleString()}</td>
                                    <td>{flight.totalSeats}</td>
                                    <td>{flight.availableSeats}</td>
                                    <td>
                                        <button 
                                            className="book-btn"
                                            onClick={() => handleBook(flight.id)}
                                        >
                                            Book Now
                                        </button>
                                    </td>
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td colSpan="8" className="no-flights">
                                    No flights found. Try different search criteria.
                                </td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default SearchForm;