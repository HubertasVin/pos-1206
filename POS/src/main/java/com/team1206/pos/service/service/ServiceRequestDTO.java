package com.team1206.pos.service.service;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class ServiceRequestDTO {
    @NotBlank(message = "Service name is required.")
    @Size(max = 100, message = "Service name must not exceed 100 characters.")
    private String name;

    @NotNull(message = "Price is required.")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0.01")
    private BigDecimal price;

    // JPA does not support Duration. Duration is amount of seconds.
    @NotNull(message = "Duration is required.")
    @Min(value = 1, message = "Duration must be greater than or equal to 1.")
    private Long duration;

    @NotNull(message = "List of employees is required.")
    private List<UUID> employeeIds;
}
