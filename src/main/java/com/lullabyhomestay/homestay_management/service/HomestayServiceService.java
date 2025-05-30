package com.lullabyhomestay.homestay_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.lullabyhomestay.homestay_management.domain.Service;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.repository.BookingServiceRepository;
import com.lullabyhomestay.homestay_management.repository.ServiceRepository;
import com.lullabyhomestay.homestay_management.utils.Constants;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@org.springframework.stereotype.Service
public class HomestayServiceService {
    private final ServiceRepository serviceRepository;
    private final BookingServiceRepository bookingServiceRepository;

    public List<Service> getAllServices() {
        return this.serviceRepository.findAll();
    }

    public List<Service> getServiceByIsPrepaid(boolean isPrepaid) {
        return this.serviceRepository.findByIsPrepaid(isPrepaid);
    }

    public Page<Service> getAllServices(Pageable pageable) {
        return this.serviceRepository.findAll(pageable);
    }

    public Page<Service> searchServices(String keyword, int page, String sortOrder) {
        Pageable pageable = PageRequest.of(page - 1, Constants.PAGE_SIZE,
                "asc".equals(sortOrder) ? Sort.by("Price").ascending()
                        : "desc".equals(sortOrder) ? Sort.by("Price").descending() : Sort.unsorted());

        return keyword.isEmpty()
                ? this.serviceRepository.findAll(pageable)
                : this.serviceRepository.findByServiceNameContainingIgnoreCase(keyword, pageable);
    }

    public void handleSaveService(Service service) {
        this.serviceRepository.save(service);
    }

    public Service getServiceById(long serviceId) {
        Optional<Service> serviceOpt = serviceRepository.findByServiceId(serviceId);
        if (!serviceOpt.isPresent()) {
            throw new NotFoundException("Dịch vụ");
        }
        return serviceOpt.get();
    }

    public boolean canDeleteService(long serviceId) {
        return !bookingServiceRepository.existsByService_ServiceId(serviceId);
    }

    @Transactional
    public void deleteByServiceId(long serviceId) {
        if (canDeleteService(serviceId))
            this.serviceRepository.deleteByServiceId(serviceId);
    }

}
