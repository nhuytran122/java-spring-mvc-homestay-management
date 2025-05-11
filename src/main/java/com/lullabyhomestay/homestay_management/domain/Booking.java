package com.lullabyhomestay.homestay_management.domain;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import com.lullabyhomestay.homestay_management.service.validator.AdminValidation;
import com.lullabyhomestay.homestay_management.service.validator.ValidBooking;
import com.lullabyhomestay.homestay_management.utils.BookingStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@ValidBooking
@DynamicUpdate
@Table(name = "Bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BookingID")
    private Long bookingID;

    @NotNull(message = "Vui lòng nhập giờ checkin")
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    @Column(name = "CheckIn")
    private LocalDateTime checkIn;

    @NotNull(message = "Vui lòng nhập giờ checkout")
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    @Column(name = "CheckOut")
    private LocalDateTime checkOut;

    @NotNull(message = "Vui lòng nhập số lượng khách")
    @Min(value = 1, message = "Số lượng khách phải lớn hơn hoặc bằng 1")
    @Column(name = "GuestCount")
    private Integer guestCount;

    @Column(name = "Status")
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Column(name = "CreatedAt", insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;

    @Column(name = "TotalAmount")
    private Double totalAmount;

    @Column(name = "PaidAmount")
    private Double paidAmount;

    @OneToMany(mappedBy = "booking")
    List<BookingExtension> bookingExtensions;

    @OneToMany(mappedBy = "booking")
    List<BookingServices> bookingServices;

    @OneToOne(mappedBy = "booking")
    private Review review;

    @OneToMany(mappedBy = "booking")
    List<Payment> payments;

    @ManyToOne
    @JoinColumn(name = "CustomerID")
    @NotNull(message = "Vui lòng chọn khách hàng đặt phòng", groups = AdminValidation.class)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "RoomID")
    @NotNull(message = "Vui lòng chọn phòng", groups = AdminValidation.class)
    private Room room;

    @OneToMany(mappedBy = "booking")
    List<RoomStatusHistory> roomStatusHistories;

    public long getNumberOfHours() {
        if (checkIn != null && checkOut != null) {
            return ChronoUnit.HOURS.between(checkIn, checkOut);
        }
        return 0;
    }

}
