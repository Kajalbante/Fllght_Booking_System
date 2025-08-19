package com.auth.controller;

import com.auth.controller.AuthController;
import com.auth.dto.*;
import com.auth.entity.Role;
import com.auth.entity.User;
import com.auth.service.UserService;
import com.auth.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser() {
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setRole("USER");

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(Role.USER);

        when(userService.registerUser(any(User.class))).thenReturn(user);

        ResponseEntity<User> response = authController.registerUser(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("test@example.com", response.getBody().getEmail());
        verify(userService, times(1)).registerUser(any(User.class));
    }

    @Test
    void testLoginUser() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("test@example.com");
        request.setPassword("password123");

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword("hashed_password");
        user.setRole(Role.USER);

        String token = "mocked_jwt_token";

        when(userService.authenticate(request.getEmail(), request.getPassword())).thenReturn(user);
        when(jwtUtil.generateToken(user.getEmail(), user.getRole().name())).thenReturn(token);

        ResponseEntity<JwtResponseDTO> response = authController.loginUser(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(token, response.getBody().getToken());
        assertEquals("test@example.com", response.getBody().getEmail());
        assertEquals("USER", response.getBody().getRole());
        verify(userService).authenticate(request.getEmail(), request.getPassword());
        verify(jwtUtil).generateToken(user.getEmail(), "USER");
    }
    
    @Test
    void testRegisterUserWithInvalidRole() {
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setRole("INVALID_ROLE");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            authController.registerUser(request);
        });

        assertTrue(exception.getMessage().contains("No enum constant"));
    }

    
    @Test
    void testLoginUserInvalidCredentials() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("wrong@example.com");
        request.setPassword("wrongpass");

        when(userService.authenticate(request.getEmail(), request.getPassword()))
            .thenThrow(new RuntimeException("Invalid credentials"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            authController.loginUser(request);
        });

        assertEquals("Invalid credentials", exception.getMessage());
    }

    
    
    @Test
    void testRegisterUserWithNullFields() {
        RegisterRequestDTO request = new RegisterRequestDTO();
        // all fields are null

        Exception exception = assertThrows(NullPointerException.class, () -> {
            authController.registerUser(request);
        });

        // Exact exception may vary depending on service implementation
    }

    
    
    @Test
    void testLoginUserTokenGenerationFails() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("test@example.com");
        request.setPassword("password123");

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword("hashed_password");
        user.setRole(Role.USER);

        when(userService.authenticate(request.getEmail(), request.getPassword())).thenReturn(user);
        when(jwtUtil.generateToken(user.getEmail(), "USER")).thenReturn(null); // Simulate failure

        ResponseEntity<JwtResponseDTO> response = authController.loginUser(request);

        assertNull(response.getBody().getToken());
    }

}
