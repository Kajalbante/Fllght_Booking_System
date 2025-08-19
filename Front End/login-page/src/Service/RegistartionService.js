import axios from "axios";

const REGISTER_API_URL = "http://localhost:8080/api/auth/register";

export const register = async (firstName, lastName, email, password) => {
  try {
    const response = await axios.post(REGISTER_API_URL, {
      firstName: firstName,
      lastName: lastName,
      email: email,
      password: password,
      role: "USER", // manually set role here
    });

    return { success: true, data: response.data };
  } catch (error) {
    console.error("Registration error:", error.response);
    return {
      success: false,
      message:
        error.response?.data?.message || "Registration failed. Please try again.",
    };
  }
};
