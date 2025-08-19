import axios from "axios";

const AUTH_API_URL = "http://localhost:8080/api/auth/login";

export const login = async (email, password) => {
  try {
    const response = await axios.post(AUTH_API_URL, {
      email: email,
      password: password,
    });

    // Store token in localStorage
    if (response.data && response.data.token) {
      localStorage.setItem("token", response.data.token);
      return { success: true, data: response.data };
    } else {
      return { success: false, message: "Token not received." };
    }
  } catch (error) {
    console.error("Login error:", error.response);
    return {
      success: false,
      message:
        error.response?.data?.message || "Login failed. Please check credentials.",
    };
  }
};
