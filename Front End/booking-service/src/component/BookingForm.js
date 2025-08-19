import React, { useState } from 'react';
import { useParams ,useNavigate} from 'react-router-dom';
import { createBooking } from '../service/bookingService';
import '../style/BookingForm.css'; // Optional: add matching CSS

const BookingForm = () => {
    const { flightId } = useParams(); // gets flightId from URL
    const [formData, setFormData] = useState({
        userId: '',
        passengerName: '',
        passengerEmail: '',
    });
    const [message, setMessage] = useState('');
    const navigate = useNavigate();


    const handleChange = (e) => {
        setFormData(prev => ({
            ...prev,
            [e.target.name]: e.target.value,
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const bookingData = {
                id: Number(flightId),
                userId: Number(formData.userId),
                passengerName: formData.passengerName,
                passengerEmail: formData.passengerEmail,
            };
            const response = await createBooking(bookingData);

            // Assuming response contains: userId, totalFare, bookingId, etc.
            navigate(`/payment`, {
                state: {
                    userId: response.userId,
                    totalFare: response.totalFare,
                    bookingId: response.id,
                }
            });
        } catch (error) {
            console.error(error);
            alert('Booking failed');
        }
    };

    return (
        <div className="booking-container">
            <h2>Book Flight</h2>
            <form className="booking-form" onSubmit={handleSubmit}>
                <label>Flight ID:
                    <input type="number" value={flightId} readOnly />
                </label>
                <label>User ID:
                    <input
                        type="number"
                        name="userId"
                        value={formData.userId}
                        onChange={handleChange}
                        required
                    />
                </label>
                <label>Passenger Name:
                    <input
                        type="text"
                        name="passengerName"
                        value={formData.passengerName}
                        onChange={handleChange}
                        required
                    />
                </label>
                <label>Passenger Email:
                    <input
                        type="email"
                        name="passengerEmail"
                        value={formData.passengerEmail}
                        onChange={handleChange}
                        required
                    />
                </label>
                <button type="submit">Confirm Booking</button>
            </form>
            {message && <p className="message">{message}</p>}
        </div>
    );
};

export default BookingForm;
