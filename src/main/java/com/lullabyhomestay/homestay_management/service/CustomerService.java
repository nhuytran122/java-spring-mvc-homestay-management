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
import com.lullabyhomestay.homestay_management.domain.dto.RegisterDTO;
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
        Customer customer;
        if (requestDTO.getCustomerID() == null) {
            customer = mapper.map(requestDTO, Customer.class);
            customer.setPassword(this.passwordEncoder.encode("lullabyhomestay"));
            customer.setCustomerType(customerTypeService.getCustomerTypeWithLowestMinPoint());
        } else {
            customer = getCustomerByID(requestDTO.getCustomerID());
            customer = updateCustomerInfo(customer, requestDTO);
        }

        Customer savedCustomer = customerRepository.save(customer);
        return mapper.map(savedCustomer, CustomerDTO.class);
    }

    private Customer updateCustomerInfo(Customer customer, CustomerDTO requestDTO) {
        customer.setFullName(requestDTO.getFullName());
        customer.setAddress(requestDTO.getAddress());
        customer.setPhone(requestDTO.getPhone());
        customer.setEmail(requestDTO.getEmail());
        customer.setRewardPoints(requestDTO.getRewardPoints());
        return customer;
    }

    @Transactional
    public void updateRewardPointsAndCustomerType(Long customerID, double amount, boolean isAdd) {
        Customer customer = getCustomerByID(customerID);

        double points = (amount / 100000) * 10;
        double updatedPoints = isAdd ? customer.getRewardPoints() + points : customer.getRewardPoints() - points;
        customer.setRewardPoints(updatedPoints);

        List<CustomerType> customerTypes = customerTypeService.getAllCustomerTypes();
        customerTypes.sort((a, b) -> Float.compare(b.getMinPoint(), a.getMinPoint()));

        CustomerType bestCustomerType = customerTypeService.getCustomerTypeWithLowestMinPoint();
        for (CustomerType type : customerTypes) {
            if (customer.getRewardPoints() >= type.getMinPoint()) {
                bestCustomerType = type;
                break;
            }
        }
        customer.setCustomerType(bestCustomerType);
        customerRepository.save(customer);
    }

    public CustomerDTO handleRegisterCustomer(RegisterDTO registerDTO) {
        Customer customer = mapper.map(registerDTO, Customer.class);
        customer.setPassword(this.passwordEncoder.encode(registerDTO.getPassword()));
        customer.setCustomerType(customerTypeService.getCustomerTypeWithLowestMinPoint());
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

    public boolean checkEmailExistWhenRegister(String email) {
        return this.customerRepository.existsByEmail(email);
    }

    public Customer getCustomerByEmail(String email) {
        Optional<Customer> customerOptional = customerRepository.findByEmail(email);
        return customerOptional.orElse(null);
    }

    public CustomerDTO getCustomerDTOByEmail(String email) {
        Optional<Customer> customerOpt = customerRepository.findByEmail(email);
        if (!customerOpt.isPresent()) {
            throw new NotFoundException("Khách hàng");
        }
        return mapper.map(customerOpt.get(), CustomerDTO.class);
    }

    @Transactional
    public void deleteByCustomerID(Long customerID) {
        if (canDeleteCustomer(customerID)) {
            customerRepository.deleteByCustomerID(customerID);
        }
    }
}
