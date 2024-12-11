package com.team1206.pos.inventory.product;

import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class UpdateProductRequestDTO {
    private String name;

    @Positive(message = "Price must be a positive value")
    private BigDecimal price;

    private UUID categoryId;

    private List<UUID> chargeIds;
}
