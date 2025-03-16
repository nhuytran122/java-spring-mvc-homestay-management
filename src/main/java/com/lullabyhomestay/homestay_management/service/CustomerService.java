package com.lullabyhomestay.homestay_management.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lullabyhomestay.homestay_management.domain.Customer;
import com.lullabyhomestay.homestay_management.domain.CustomerType;
import com.lullabyhomestay.homestay_management.domain.dto.CustomerDTO;
import com.lullabyhomestay.homestay_management.domain.dto.SearchCustomerCriterialDTO;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.repository.BookingRepository;
import com.lullabyhomestay.homestay_management.repository.CustomerRepository;
import com.lullabyhomestay.homestay_management.repository.ReportRepository;
import com.lullabyhomestay.homestay_management.service.specifications.CustomerSpecifications;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;
    private final BookingRepository bookingRepository;
    private final ReportRepository reportRepository;
    private final CustomerTypeService customerTypeService;

    public CustomerDTO handleSaveCustomer(CustomerDTO requestDTO) {
        Customer customer = mapper.map(requestDTO, Customer.class);
        customer.setPassword(this.passwordEncoder.encode("lullabyhomestay"));

        List<CustomerType> customerTypes = customerTypeService.getAllCustomerTypes();
        for (CustomerType type : customerTypes) {
            if (customer.getRewardPoints() >= type.getMinPoint()) {
                customer.setCustomerType(type);
            }
        }
        Customer savedCustomer = customerRepository.save(customer);
        return mapper.map(savedCustomer, CustomerDTO.class);
    }

    public Page<CustomerDTO> getAllCustomers(Pageable pageable) {
        Page<Customer> customerPage = customerRepository.findAll(pageable);
        return customerPage.map(customer -> mapper.map(customer, CustomerDTO.class));
    }

    public Page<CustomerDTO> searchCustomers(SearchCustomerCriterialDTO criteria, int page) {
        Pageable pageable = PageRequest.of(page - 1, 1,
                "asc".equals(criteria.getSort()) ? Sort.by("RewardPoints").ascending()
                        : "desc".equals(criteria.getSort()) ? Sort.by("RewardPoints").descending() : Sort.unsorted());
        Specification<Customer> spec = Specification.where(null);

        if (criteria.getKeyword() != null && !criteria.getKeyword().trim().isEmpty()) {
            spec = spec.and(Specification.where(CustomerSpecifications.nameLike(criteria.getKeyword()))
                    .or(CustomerSpecifications.addressLike(criteria.getKeyword()))
                    .or(CustomerSpecifications.phoneEqual(criteria.getKeyword())));
        }
        if (criteria.getIsLocked() != null) {
            spec = spec.and(CustomerSpecifications.isLocked(criteria.getIsLocked()));
        }
        if (criteria.getCustomerTypeID() != null) {
            spec = spec.and(CustomerSpecifications.hasType(criteria.getCustomerTypeID()));
        }
        return customerRepository.findAll(spec, pageable).map(customer -> mapper.map(customer, CustomerDTO.class));
    }

    public CustomerDTO getCustomerDTOByID(Long customerID) {
        Optional<Customer> customerOpt = customerRepository.findByCustomerID(customerID);
        if (!customerOpt.isPresent()) {
            throw new NotFoundException("Khách hàng");
        }
        return mapper.map(customerOpt.get(), CustomerDTO.class);
    }

    public Customer getCustomerByID(Long customerID) {
        Optional<Customer> customerOpt = customerRepository.findByCustomerID(customerID);
        if (!customerOpt.isPresent()) {
            throw new NotFoundException("Khách hàng");
        }
        return customerOpt.get();
    }

    public boolean canDeleteCustomer(Long customerID) {
        boolean hasBooking = bookingRepository.existsByCustomer_CustomerID(customerID);
        boolean hasReport = reportRepository.existsByCustomer_CustomerID(customerID);
        return !(hasBooking || hasReport);
    }

    public boolean checkEmailExistForOther(String email, Long id) {
        Optional<Customer> customerOpt = customerRepository.findByEmail(email);
        if (customerOpt.isEmpty()) {
            return false;
        }
        Customer customer = customerOpt.get();
        return id == null || !customer.getCustomerID().equals(id);
    }

    @Transactional
    public void deleteByCustomerID(Long customerID) {
        if (canDeleteCustomer(customerID)) {
            customerRepository.deleteByCustomerID(customerID);
        }
    }
}
