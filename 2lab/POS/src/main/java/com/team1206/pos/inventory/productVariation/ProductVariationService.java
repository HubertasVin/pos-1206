package com.team1206.pos.inventory.productVariation;

import com.team1206.pos.common.enums.ResourceType;
import com.team1206.pos.exceptions.IllegalStateExceptionWithId;
import com.team1206.pos.exceptions.ResourceNotFoundException;
import com.team1206.pos.inventory.product.AdjustProductQuantityDTO;
import com.team1206.pos.inventory.product.Product;
import com.team1206.pos.inventory.product.ProductService;
import com.team1206.pos.user.user.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ProductVariationService {
    private final ProductVariationRepository productVariationRepository;
    private final ProductService productService;
    private final UserService userService;

    public ProductVariationService(ProductVariationRepository productVariationRepository, ProductService productService, UserService userService) {
        this.productVariationRepository = productVariationRepository;
        this.productService = productService;
        this.userService = userService;
    }

    public ProductVariationResponseDTO createProductVariation(UUID productId, CreateProductVariationBodyDTO productVariationDTO) {
        Product product = productService.getProductEntityById(productId);

        ProductVariation productVariation = new ProductVariation();
        productVariation.setName(productVariationDTO.getName());
        productVariation.setPrice(productVariationDTO.getPrice());
        productVariation.setQuantity(productVariationDTO.getQuantity());
        productVariation.setProduct(product);
        productVariation.setCreatedAt(LocalDateTime.now());

        productVariationRepository.save(productVariation);
        return mapToResponseDTO(productVariation);
    }

    public ProductVariationResponseDTO getProductVariationById (UUID productVariationId)
    {
        ProductVariation productVariation = productVariationRepository.findById(productVariationId)
                .orElseThrow(() -> new ResourceNotFoundException(ResourceType.PRODUCT_VARIATION, productVariationId.toString()));


        return mapToResponseDTO(productVariation);
    }

    public List<ProductVariationResponseDTO> getAllProductVariations(UUID productId) {
        productService.getProductEntityById(productId); // To check whether exists
        UUID merchantId = userService.getMerchantIdFromLoggedInUser();

        List<ProductVariation> productVariations = productVariationRepository.findAllWithFilters(productId, merchantId);

        return productVariations.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    public ProductVariationResponseDTO updateProductVariationById (UUID productVariationId, UpdateProductVariationBodyDTO updateProductVariationBodyDTO)
    {
        ProductVariation productVariation = productVariationRepository.findById(productVariationId)
                .orElseThrow(() -> new ResourceNotFoundException(ResourceType.PRODUCT_VARIATION, productVariationId.toString()));

        if(updateProductVariationBodyDTO.getName() != null && !updateProductVariationBodyDTO.getName().isBlank())
            productVariation.setName(updateProductVariationBodyDTO.getName());
        if(updateProductVariationBodyDTO.getPrice() != null)
            productVariation.setPrice(updateProductVariationBodyDTO.getPrice());
        if(updateProductVariationBodyDTO.getQuantity() != null)
            productVariation.setQuantity(updateProductVariationBodyDTO.getQuantity());
        productVariationRepository.save(productVariation);
        return mapToResponseDTO(productVariation);
    }

    public void deleteProductVariationById(UUID productVariationId)
    {
        if(!productVariationRepository.existsById(productVariationId))
            throw new ResourceNotFoundException(ResourceType.PRODUCT_VARIATION, productVariationId.toString());
        productVariationRepository.deleteById(productVariationId);
    }

    public ProductVariationResponseDTO adjustProductVariationQuantity(UUID id, AdjustProductQuantityDTO adjustDTO) {
        ProductVariation productVariation = productVariationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ResourceType.PRODUCT, id.toString()));

        int newQuantity = productVariation.getQuantity() + adjustDTO.getAdjustment();
        if (newQuantity < 0) {
            throw new IllegalStateExceptionWithId("Product quantity cannot be less than zero", id.toString());
        }

        productVariation.setQuantity(newQuantity);

        ProductVariation updatedProductVariation = productVariationRepository.save(productVariation);

        return mapToResponseDTO(updatedProductVariation);
    }

    // Mappers
    private ProductVariationResponseDTO mapToResponseDTO(ProductVariation productVariation) {
        ProductVariationResponseDTO responseDTO = new ProductVariationResponseDTO();
        responseDTO.setId(productVariation.getId());
        responseDTO.setName(productVariation.getName());
        responseDTO.setPrice(productVariation.getPrice());
        responseDTO.setQuantity(productVariation.getQuantity());
        responseDTO.setCreatedAt(productVariation.getCreatedAt());
        responseDTO.setUpdatedAt(productVariation.getUpdatedAt());
        return responseDTO;
    }
}

