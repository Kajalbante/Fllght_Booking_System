import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useLocation } from 'react-router-dom';
import '../style/RazorpayCheckout.css';

const RazorpayCheckout = () => {
  const { state } = useLocation();
  const [amount, setAmount] = useState('');
  const [isProcessing, setIsProcessing] = useState(false);
  const [error, setError] = useState(null);
  const [successMsg, setSuccessMsg] = useState(null); // NEW

  useEffect(() => {
    const script = document.createElement("script");
    script.src = "https://checkout.razorpay.com/v1/checkout.js";
    script.async = true;
    script.onerror = () => {
      setError("Failed to load payment gateway. Please refresh the page.");
    };
    document.body.appendChild(script);

    return () => {
      document.body.removeChild(script);
    };
  }, []);

  useEffect(() => {
    if (state && state.totalFare) {
      setAmount(state.totalFare);
    } else {
      setError("Missing booking information. Please start over.");
    }
  }, [state]);

  const startPayment = async () => {
    setIsProcessing(true);
    setError(null);

    try {
      const response = await axios.post(
        'http://localhost:8082/api/payment/create-order',
        null,
        {
          params: { amount: amount },
          timeout: 10000
        }
      );

      const orderData = response.data;
      const options = {
        key: 'rzp_test_UN0tEmDj823X1c',
        amount: orderData.amount,
        currency: orderData.currency,
        name: "Flight Booking Payment",
        description: `Payment for user ID: ${state.userId}`,
        order_id: orderData.id,
        handler: function (response) {
          setSuccessMsg(`✅ Flight Booking Successful! Payment ID: ${response.razorpay_payment_id}`);
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

      rzp.on('payment.failed', function (response) {
        setError(`Payment failed: ${response.error.description}`);
      });

    } catch (err) {
      const errorMsg = err.response?.data?.message || err.message || "Payment initiation failed";
      setError(errorMsg);
    } finally {
      setIsProcessing(false);
    }
  };

  if (error) {
    return (
      <div className="razorpay-container error-state">
        <h2>Payment Error</h2>
        <p className="error-message">{error}</p>
        <button
          className="payment-button"
          onClick={() => window.location.reload()}
        >
          Try Again
        </button>
      </div>
    );
  }

  return (
    <div className="payment-box">
      <h2>Proceed to Payment</h2>

      <div className="payment-details">
        <p>
          <strong>Total Amount:</strong>
          <span>₹{amount}</span>
        </p>
      </div>

      {successMsg && (
        <div className="success-message">
          <h3>{successMsg}</h3>
        </div>
      )}

      {!successMsg && (
        <button
          className={`payment-button ${isProcessing ? 'processing' : ''}`}
          onClick={startPayment}
          disabled={isProcessing}
        >
          {isProcessing ? (
            <>
              <svg className="spinner" viewBox="0 0 50 50">
                <circle cx="25" cy="25" r="20" fill="none" strokeWidth="4"></circle>
              </svg>
              Processing Payment...
            </>
          ) : (
            `Pay ₹${amount} Now`
          )}
        </button>
      )}

      <div className="payment-security">
        <svg width="16" height="16" viewBox="0 0 24 24">
          <path fill="currentColor" d="M12,1L3,5v6c0,5.55,3.84,10.74,9,12c5.16-1.26,9-6.45,9-12V5L12,1z M12,11.99h7c-0.53,4.12-3.28,7.79-7,8.94V12H5V6.3l7-3.11V11.99z" />
        </svg>
        <span>Secure payment powered by Razorpay</span>
      </div>
    </div>
  );
};

export default RazorpayCheckout;
