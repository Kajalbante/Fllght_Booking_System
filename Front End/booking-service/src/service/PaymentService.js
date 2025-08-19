// src/services/PaymentService.js
import axios from 'axios';

export const createOrder = async (amount) => {
  return await axios.post(
    'http://localhost:8082/api/payment/create-order',
    null,
    { 
      params: { amount },
      timeout: 10000
    }
  );
};
