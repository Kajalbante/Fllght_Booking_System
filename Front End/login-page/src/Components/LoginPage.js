// src/pages/LoginPage.js
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login } from "../Service/loginService";
import { jwtDecode } from "jwt-decode";
import "../Style/Login.css";

const LoginPage = () => {
  const [credentials, setCredentials] = useState({
    email: "",
    password: ""
  });
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  const handleChange = (e) => {
    setCredentials({
      ...credentials,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");
    setIsLoading(true);

    try {
      const result = await login(credentials.email, credentials.password);
      if (result.success) {
        const token = result.data.token;
        const decoded = jwtDecode(token);
        const role = decoded.role;

        setSuccess("Login successful!");

        // Redirect based on role
        if (role === "ADMIN") {
          window.location.href ="http://localhost:3001/admin";
        } else if (role === "USER") {
          window.location.href ="http://localhost:3001/";
        } else {
          setError("Unknown role. Contact support.");
        }
      } else {
        setError(result.message || "Invalid credentials");
      }
    } catch (err) {
      setError("An error occurred. Please try again.");
    } finally {
      setIsLoading(false);
    }
  };

  const handleSignUpClick = () => {
    navigate("/register");
  };

  return (
    <div className="login-container">
      <h2>Login</h2>

      <form onSubmit={handleSubmit} className="login-form">
        <div className="form-group">
          <label htmlFor="email">Email</label>
          <input
            id="email"
            type="email"
            name="email"
            value={credentials.email}
            onChange={handleChange}
            required
            placeholder="your@email.com"
          />
        </div>

        <div className="form-group">
          <label htmlFor="password">Password</label>
          <input
            id="password"
            type="password"
            name="password"
            value={credentials.password}
            onChange={handleChange}
            required
            placeholder="••••••••"
          />
          <div className="forgot-password-container">
            <a href="/forgot-password" className="forgot-password">
              Forgot password?
            </a>
          </div>
        </div>

        <button
          type="submit"
          className="login-btn"
          disabled={isLoading}
        >
          {isLoading ? "Logging in..." : "Login"}
        </button>
      </form>

      {error && <div className="message error-message">{error}</div>}
      {success && <div className="message success-message">{success}</div>}

      <div className="signup-link">
        Don't have an account?{" "}
        <button className="signup-btn-link" onClick={handleSignUpClick}>
          Sign up
        </button>
      </div>
    </div>
  );
};

export default LoginPage;
