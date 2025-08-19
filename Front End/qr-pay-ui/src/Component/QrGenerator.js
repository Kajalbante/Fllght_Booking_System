import React, { useState } from 'react';
import axios from 'axios';

const RazorpayCheckout = () => {
  const [amount, setAmount] = useState('');

  const startPayment = async () => {
    try {
      const response = await axios.post(
        'http://localhost:8080/api/payment/create-order',
        null,
        { params: { amount: amount } }
      );

      const orderData = response.data;
      const options = {
        key: 'rzp_test_UN0tEmDj823X1c', // Replace with your Razorpay Key ID
        amount: orderData.amount,
        currency: orderData.currency,
        name: "Flight Booking Payment",
        description: "Real-Time UPI Payment",
        order_id: orderData.id,
        handler: function (response) {
          alert("✅ Payment Successful!\nPayment ID: " + response.razorpay_payment_id);
        },
        prefill: {
          name: "User",
          email: "user@example.com",
          contact: "9999999999"
        },
        theme: {
          color: "#3399cc"
        }
      };

      const rzp = new window.Razorpay(options);
      rzp.open();

    } catch (err) {
      console.error(err);
      alert("Payment initiation failed");
    }
  };

  return (
    <div style={{ padding: '2rem' }}>
      <h2>Pay Using Razorpay (Real-Time)</h2>
      <input
        type="number"
        placeholder="Enter amount"
        value={amount}
        onChange={(e) => setAmount(e.target.value)}
        min="1"
        required
        style={{ marginRight: '1rem' }}
      />
      <button onClick={startPayment}>Pay Now</button>
    </div>
  );
};

export default RazorpayCheckout;
