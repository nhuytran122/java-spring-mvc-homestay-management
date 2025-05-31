package com.lullabyhomestay.homestay_management.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lullabyhomestay.homestay_management.domain.Customer;
import com.lullabyhomestay.homestay_management.domain.CustomerType;
import com.lullabyhomestay.homestay_management.domain.User;
import com.lullabyhomestay.homestay_management.domain.dto.CustomerDTO;
import com.lullabyhomestay.homestay_management.domain.dto.RegisterDTO;
import com.lullabyhomestay.homestay_management.domain.dto.SearchCustomerCriterialDTO;
import com.lullabyhomestay.homestay_management.domain.dto.UserDTO;
import com.lullabyhomestay.homestay_management.exception.CannotDeleteException;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.repository.BookingRepository;
import com.lullabyhomestay.homestay_management.repository.CustomerRepository;
import com.lullabyhomestay.homestay_management.service.specifications.CustomerSpecifications;
import com.lullabyhomestay.homestay_management.utils.Constants;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final ModelMapper mapper;
    private final BookingRepository bookingRepository;
    private final CustomerTypeService customerTypeService;
    private final UserService userService;

    @Transactional
    public CustomerDTO handleRegisterAccount(RegisterDTO registerDTO) throws Exception {
        User user = userService.registerUserWithVerification(registerDTO);

        Customer customer = mapper.map(registerDTO, Customer.class);
        customer.setCustomerType(customerTypeService.getCustomerTypeWithLowestMinPoint());
        customer.setUser(user);
        Customer savedCustomer = customerRepository.save(customer);

        return mapper.map(savedCustomer, CustomerDTO.class);
    }

    // public UserDTO mapFromRegisterDTO(RegisterDTO dto) {
    // UserDTO userDTO = new UserDTO();
    // userDTO.setFullName(dto.getFullName());
    // userDTO.setPhone(dto.getPhone());
    // userDTO.setEmail(dto.getEmail());
    // return userDTO;
    // }

    public CustomerDTO handleCreateCustomer(CustomerDTO customerDTO) {
        Customer customer = mapper.map(customerDTO, Customer.class);
        // UserDTO userDTO = mapFromEmployeeDTO(employeeDTO);
        UserDTO userDTO = mapper.map(customerDTO, UserDTO.class);
        User user = userService.createUserForPerson(userDTO, null, null);
        customer.setCustomerType(customerTypeService.getCustomerTypeWithLowestMinPoint());
        customer.setUser(user);
        Customer savedCustomer = customerRepository.save(customer);
        return mapper.map(savedCustomer, CustomerDTO.class);
    }

    public Customer handleSaveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Transactional
    public void updateRewardPointsAndCustomerType(Long customerId, double amount) {
        Customer customer = getCustomerById(customerId);

        double points = (amount / 100000) * 10;
        double updatedPoints = customer.getRewardPoints() + points;
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

    public Page<CustomerDTO> getAllCustomers(Pageable pageable) {
        Page<Customer> customerPage = customerRepository.findAll(pageable);
        return customerPage.map(this::mapToCustomerDTO);
    }

    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(this::mapToCustomerDTO)
                .collect(Collectors.toList());
    }

    public Page<CustomerDTO> searchCustomers(SearchCustomerCriterialDTO criteria, int page) {
        Pageable pageable = PageRequest.of(page - 1, Constants.PAGE_SIZE,
                "asc".equals(criteria.getSort()) ? Sort.by("RewardPoints").ascending()
                        : "desc".equals(criteria.getSort()) ? Sort.by("RewardPoints").descending() : Sort.unsorted());

        Specification<Customer> spec = Specification.where(null);

        if (criteria.getKeyword() != null && !criteria.getKeyword().trim().isEmpty()) {
            spec = spec.and(Specification.where(CustomerSpecifications.nameLike(criteria.getKeyword()))
                    .or(CustomerSpecifications.addressLike(criteria.getKeyword()))
                    .or(CustomerSpecifications.phoneEqual(criteria.getKeyword())));
        }
        if (criteria.getCustomerTypeId() != null) {
            spec = spec.and(CustomerSpecifications.hasType(criteria.getCustomerTypeId()));
        }

        return customerRepository.findAll(spec, pageable).map(this::mapToCustomerDTO);
    }

    public CustomerDTO getCustomerDTOById(Long customerId) {
        Customer customer = customerRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new NotFoundException("Khách hàng"));

        return mapToCustomerDTO(customer);
    }

    public Customer getCustomerById(Long customerId) {
        Optional<Customer> customerOpt = customerRepository.findByCustomerId(customerId);
        if (!customerOpt.isPresent()) {
            throw new NotFoundException("Khách hàng");
        }
        return customerOpt.get();
    }

    public boolean canDeleteCustomer(Long customerId) {
        return !bookingRepository.existsByCustomer_CustomerId(customerId);
    }

    @Transactional
    public void deleteByCustomerId(Long customerId) {
        if (!canDeleteCustomer(customerId)) {
            throw new CannotDeleteException("Khách hàng");
        }
        customerRepository.deleteByCustomerId(customerId);
    }

    public CustomerDTO mapToCustomerDTO(Customer customer) {
        if (customer == null || customer.getUser() == null) {
            return null;
        }

        User user = customer.getUser();

        CustomerDTO dto = new CustomerDTO();
        dto.setCustomerId(customer.getCustomerId());
        dto.setRewardPoints(customer.getRewardPoints());
        dto.setCustomerType(customer.getCustomerType());

        dto.setUserId(user.getUserId());
        dto.setFullName(user.getFullName());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setAddress(user.getAddress());
        dto.setAvatar(user.getAvatar());
        dto.setRole(user.getRole());
        dto.setIsEnabled(user.getIsEnabled());

        return dto;
    }

}
