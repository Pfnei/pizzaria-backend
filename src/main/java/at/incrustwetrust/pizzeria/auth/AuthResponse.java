package at.incrustwetrust.pizzeria.auth;

import at.incrustwetrust.pizzeria.dto.UserResponseLightDTO;

public record AuthResponse(String accessToken, UserResponseLightDTO user) {}
