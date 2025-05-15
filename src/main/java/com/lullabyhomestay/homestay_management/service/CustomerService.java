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
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.repository.BookingRepository;
import com.lullabyhomestay.homestay_management.repository.CustomerRepository;
import com.lullabyhomestay.homestay_management.service.specifications.CustomerSpecifications;

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

    // public void handleUpdateCustomer(CustomerDTO customerDTO) {
    // Customer customer = mapper.map(customerDTO, Customer.class);
    // User user = userService.getUserByUserID(customer.getUser().getUserID());

    // user.setFullName(customerDTO.getFullName());
    // user.setAddress(customerDTO.getAddress());
    // user.setPhone(customerDTO.getPhone());
    // user.setAvatar(customerDTO.getAvatar() != null ? customerDTO.getAvatar() :
    // user.getAvatar());
    // userService.handleSaveUser(user);

    // this.handleSaveCustomer(customer);
    // }

    public Customer handleSaveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    // private Customer updateCustomerInfo(Customer customer, CustomerDTO
    // requestDTO) {
    // customer.setFullName(requestDTO.getFullName());
    // customer.setAddress(requestDTO.getAddress());
    // customer.setPhone(requestDTO.getPhone());
    // customer.setEmail(requestDTO.getEmail());
    // customer.setAvatar(requestDTO.getAvatar());
    // customer.setRewardPoints(requestDTO.getRewardPoints());
    // return customer;
    // }

    @Transactional
    public void updateRewardPointsAndCustomerType(Long customerID, double amount) {
        Customer customer = getCustomerByID(customerID);

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
        return customerPage.map(customer -> mapper.map(customer, CustomerDTO.class));
    }

    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(customer -> mapper.map(customer, CustomerDTO.class))
                .collect(Collectors.toList());
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

        Customer customer = customerOpt.get();
        User user = customer.getUser();
        CustomerDTO customerDTO = mapper.map(customerOpt.get(), CustomerDTO.class);
        customerDTO.setFullName(user.getFullName());
        customerDTO.setEmail(user.getEmail());
        customerDTO.setPhone(user.getPhone());
        customerDTO.setAddress(user.getAddress());
        customerDTO.setAvatar(user.getAvatar());
        customerDTO.setRole(user.getRole());
        return customerDTO;
    }

    public Customer getCustomerByID(Long customerID) {
        Optional<Customer> customerOpt = customerRepository.findByCustomerID(customerID);
        if (!customerOpt.isPresent()) {
            throw new NotFoundException("Khách hàng");
        }
        return customerOpt.get();
    }

    public boolean canDeleteCustomer(Long customerID) {
        return !bookingRepository.existsByCustomer_CustomerID(customerID);
    }

    @Transactional
    public void deleteByCustomerID(Long customerID) {
        if (canDeleteCustomer(customerID)) {
            customerRepository.deleteByCustomerID(customerID);
        }
    }
}
