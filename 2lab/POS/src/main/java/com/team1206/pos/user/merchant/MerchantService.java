package com.team1206.pos.user.merchant;

import com.team1206.pos.enums.ResourceType;
import com.team1206.pos.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MerchantService {
    private final MerchantRepository merchantRepository;

    public MerchantService(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    // Create a new merchant
    public MerchantResponseDTO createMerchant(MerchantRequestDTO request) {
        Merchant merchant = new Merchant();
        setMerchantFieldsFromRequest(merchant, request);

        Merchant savedMerchant = merchantRepository.save(merchant);
        return mapToResponseDTO(savedMerchant);
    }

    // Get all merchants
    public List<MerchantResponseDTO> getAllMerchants() {
        return merchantRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Retrieve merchant by ID
    public MerchantResponseDTO getMerchantById(UUID merchantId) {
        return merchantRepository.findById(merchantId)
                .map(this::mapToResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException(ResourceType.MERCHANT, merchantId.toString()));
    }

    // Update merchant by ID
    @Transactional
    public MerchantResponseDTO updateMerchantById(UUID merchantId, MerchantRequestDTO request) {
        Merchant retrievedMerchant = merchantRepository
                .findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException(ResourceType.MERCHANT, merchantId.toString()));

        setMerchantFieldsFromRequest(retrievedMerchant, request);

        Merchant updatedMerchant = merchantRepository.save(retrievedMerchant);
        return mapToResponseDTO(updatedMerchant);
    }

    // Delete merchant by ID
    public void deleteMerchantById(UUID merchantId) {
        Optional<Merchant> merchant = merchantRepository.findById(merchantId);
        if (merchant.isPresent()) {
            try {
                merchantRepository.deleteById(merchantId);
            } catch (Exception e) {
                throw new RuntimeException("An error occurred while deleting the merchant.", e);
            }
        } else {
            throw new ResourceNotFoundException(ResourceType.MERCHANT, merchantId.toString());
        }
    }

    // Map Merchant entity to Response DTO
    private MerchantResponseDTO mapToResponseDTO(Merchant merchant) {
        MerchantResponseDTO response = new MerchantResponseDTO();
        response.setId(merchant.getId());
        response.setName(merchant.getName());
        response.setPhone(merchant.getPhone());
        response.setEmail(merchant.getEmail());
        response.setCurrency(merchant.getCurrency());
        response.setAddress(merchant.getAddress());
        response.setCity(merchant.getCity());
        response.setCountry(merchant.getCountry());
        response.setPostcode(merchant.getPostcode());
        response.setCreatedAt(merchant.getCreatedAt());
        return response;
    }

    // Set merchant fields
    private void setMerchantFieldsFromRequest(Merchant merchant, MerchantRequestDTO request) {
        merchant.setName(request.getName());
        merchant.setPhone(request.getPhone());
        merchant.setEmail(request.getEmail());
        merchant.setCurrency(request.getCurrency());
        merchant.setAddress(request.getAddress());
        merchant.setCity(request.getCity());
        merchant.setCountry(request.getCountry());
        merchant.setPostcode(request.getPostcode());
    }
}
