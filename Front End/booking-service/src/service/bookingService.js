import axios from 'axios';

const BASE_URL = 'http://localhost:8084/api/bookings';

// Replace these with actual backend credentials
const username = 'admin';
const password = '1234';

// Encode to Base64 for Basic Auth
const basicAuthHeader = 'Basic ' + btoa(`${username}:${password}`);

/**
 * Sends a booking request with Basic Authentication
 */
export const createBooking = async (bookingData) => {
  try {
    const response = await axios.post(BASE_URL, bookingData, {
      headers: {
        Authorization: basicAuthHeader,
        'Content-Type': 'application/json'
      }
    });
    return response.data;
  } catch (error) {
    console.error('Booking failed:', error);
    throw error;
  }
};
