package com.lullabyhomestay.homestay_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lullabyhomestay.homestay_management.domain.CustomerType;
import com.lullabyhomestay.homestay_management.exception.CannotDeleteException;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.repository.CustomerRepository;
import com.lullabyhomestay.homestay_management.repository.CustomerTypeRepository;
import com.lullabyhomestay.homestay_management.utils.Constants;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CustomerTypeService {
    private final CustomerTypeRepository typeRepository;
    private final CustomerRepository customerRepository;

    public List<CustomerType> getAllCustomerTypes() {
        return typeRepository.findAll();
    }

    public CustomerType getCustomerTypeById(Long id) {
        Optional<CustomerType> typeOpt = typeRepository.findByCustomerTypeId(id);
        if (!typeOpt.isPresent())
            throw new NotFoundException("Phân loại khách hàng");
        return typeOpt.get();
    }

    public CustomerType getCustomerTypeWithLowestMinPoint() {
        Optional<CustomerType> customerType = typeRepository.findFirstByOrderByMinPointAsc();
        if (customerType == null) {
            throw new NotFoundException("Phân loại khách hàng");
        }
        return customerType.get();
    }

    public CustomerType handleSaveCustomerType(CustomerType type) {
        return typeRepository.save(type);
    }

    public Page<CustomerType> searchCustomerTypes(String keyword, int page) {
        Pageable pageable = PageRequest.of(page - 1, Constants.PAGE_SIZE);
        return (keyword != null && !keyword.trim().isEmpty()
                ? typeRepository.findByNameContainingIgnoreCase(keyword, pageable)
                : typeRepository.findAll(pageable));
    }

    public boolean canDeleteType(Long id) {
        return !customerRepository.existsByCustomerType_CustomerTypeId(id);
    }

    @Transactional
    public void deleteByCustomerTypeId(Long id) {
        if (!canDeleteType(id)) {
            throw new CannotDeleteException("Loại khách hàng đang được sử dụng, không thể xóa.");
        }
        typeRepository.deleteByCustomerTypeId(id);
    }

    public boolean existsByName(String name) {
        return typeRepository.existsByNameIgnoreCase(name.trim());
    }

    public boolean existsByNameAndNotId(String name, Long id) {
        return typeRepository.existsByNameIgnoreCaseAndCustomerTypeIdNot(name.trim(), id);
    }
}
