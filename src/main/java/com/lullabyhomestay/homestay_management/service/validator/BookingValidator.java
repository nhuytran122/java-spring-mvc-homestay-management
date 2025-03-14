package com.lullabyhomestay.homestay_management.service.validator;

// import com.lullabyhomestay.homestay_management.config.ContextProvider;
import com.lullabyhomestay.homestay_management.domain.Booking;
import com.lullabyhomestay.homestay_management.domain.Room;
import com.lullabyhomestay.homestay_management.service.RoomService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class BookingValidator implements ConstraintValidator<ValidBooking, Booking> {

    @Override
    public boolean isValid(Booking booking, ConstraintValidatorContext context) {
        if (booking == null || booking.getCheckIn() == null || booking.getCheckOut() == null) {
            return false;
        }
        if (booking.getRoom() == null || booking.getRoom().getRoomID() == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Thông tin phòng không hợp lệ.")
                    .addPropertyNode("room")
                    .addConstraintViolation();
            return false;
        }
        if (!booking.getCheckOut().isAfter(booking.getCheckIn())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Thời gian check-out phải sau thời gian check-in.")
                    .addPropertyNode("checkOut")
                    .addConstraintViolation();
            return false;
        }
        if (booking.getRoom() != null && booking.getRoom().getRoomID() != null) {
            if (booking.getGuestCount() > booking.getRoom().getRoomType().getMaxGuest()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        "Số lượng khách không được vượt quá số lượng khách tối đa.")
                        .addPropertyNode("guestCount")
                        .addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}