import axios from 'axios';

const API_URL = '/api/flights';  // Proxy is assumed to be set in package.json
const username = 'admin';
const password = '1234';

// Basic Authentication header
const authHeader = 'Basic ' + btoa(username + ':' + password);

// Get all flights
export const getAllFlights = async () => {
    try {
        const response = await axios.get(API_URL, {
            headers: { Authorization: authHeader }
        });
        return response.data;
    } catch (error) {
        console.error("Error fetching flights:", error);
        throw error;
    }
};

// Add a new flight
export const addFlight = async (flightData) => {
    try {
        const response = await axios.post(API_URL, flightData, {
            headers: {
                'Content-Type': 'application/json',
                Authorization: authHeader
            }
        });
        return response.data;
    } catch (error) {
        console.error("Error adding flight:", error);
        throw error;
    }
};


// Update an existing flight
export const updateFlight = async (id, flightData) => {
    try {
        const response = await axios.put(`${API_URL}/${id}`, flightData, {
            headers: {
                'Content-Type': 'application/json',
                Authorization: authHeader
            }
        });
        return response.data;
    } catch (error) {
        console.error("Error updating flight:", error);
        throw error;
    }
};

// Delete a flight
export const deleteFlight = async (id) => {
    try {
        await axios.delete(`${API_URL}/${id}`, {
            headers: {
                Authorization: authHeader
            }
        });
    } catch (error) {
        console.error("Error deleting flight:", error);
        throw error;
    }
};

// Get a single flight by ID
export const getFlightById = async (id) => {
  try {
      const response = await axios.get(`${API_URL}/${id}`, {
          headers: {
              Authorization: authHeader
          }
      });
      return response.data;
  } catch (error) {
      console.error("Error fetching flight by ID:", error);
      throw error;
  }
};

