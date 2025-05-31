package com.lullabyhomestay.homestay_management.domain.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookingRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private LocalDateTime checkin;
    private LocalDateTime checkout;
    private Integer guestCount;
    private Long roomId;
    private Long customerId;
    private Long bookingId;
    private boolean createdFlag;

    private List<BookingServiceDTO> services;
}
