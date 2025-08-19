import axios from 'axios';

// Replace with your actual Spring Boot backend URL and credentials
const API_URL = '/api/flights';
const username = 'admin';  // Replace with your username
const password = '1234';  // Replace with your password

// Basic Authentication header
const authHeader = 'Basic ' + btoa(username + ':' + password);

// Function to get all flights
export const getAllFlights = async () => {
    try {
        const response = await axios.get(API_URL, {
            headers: {
                'Authorization': authHeader
            }
        });
        return response.data; // Return the flight data
    } catch (error) {
        console.error("Error fetching flights:", error);
        throw error; // Rethrow the error to be handled later
    }
};

export const searchFlights = async (source, destination, date) => {
    try {
        const params = {
            source,
            destination
        };
        if (date) {
            params.date = date;  // Expecting format YYYY-MM-DD
        }

        const response = await axios.get(`${API_URL}/search`, {
            headers: {
                'Authorization': authHeader
            },
            params
        });

        return response.data;
    } catch (error) {
        console.error("Error searching flights:", error);
        throw error;
    }
};
