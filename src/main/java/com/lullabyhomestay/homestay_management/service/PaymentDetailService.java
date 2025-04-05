package com.lullabyhomestay.homestay_management.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lullabyhomestay.homestay_management.domain.BookingExtension;
import com.lullabyhomestay.homestay_management.domain.BookingServices;
import com.lullabyhomestay.homestay_management.domain.Payment;
import com.lullabyhomestay.homestay_management.domain.PaymentDetail;
import com.lullabyhomestay.homestay_management.repository.BookingServiceRepository;
import com.lullabyhomestay.homestay_management.repository.PaymentDetailRepository;
import com.lullabyhomestay.homestay_management.utils.DiscountUtil;
import com.lullabyhomestay.homestay_management.utils.PaymentPurpose;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class PaymentDetailService {
    private final PaymentDetailRepository paymentDetailRepo;
    private final BookingService bookingService;
    private final BookingServiceRepository bookingServiceRepo;
    private final BookingExtensionService bookingExtensionService;

    public void handleSavePaymentDetail(Payment payment, PaymentPurpose paymentPurpose) {
        Long bookingID = payment.getBooking().getBookingID();

        if (paymentPurpose == PaymentPurpose.ROOM_BOOKING) {
            handleRoomBookingPayment(payment, bookingID);
        } else if (paymentPurpose == PaymentPurpose.ADDITIONAL_SERVICE) {
            handleAdditionalServicePayment(payment, bookingID);
        } else if (paymentPurpose == PaymentPurpose.EXTENDED_HOURS) {
            handleExtendedHoursPayment(payment, bookingID);
        }

    }

    private void handleRoomBookingPayment(Payment payment, Long bookingID) {
        PaymentDetail paymentDetail = new PaymentDetail();
        paymentDetail.setPayment(payment);
        paymentDetail.setPaymentPurpose(PaymentPurpose.ROOM_BOOKING);
        paymentDetail.setBaseAmount(bookingService.calculateRawTotalAmountBookingRoom(payment.getBooking()));
        paymentDetail.setFinalAmount(bookingService.calculateTotalAmountBookingRoom(payment.getBooking(),
                payment.getBooking().getCustomer()));
        paymentDetailRepo.save(paymentDetail);

        if (bookingServiceRepo.existsByBooking_BookingID(bookingID)) {
            List<BookingServices> listBookingServices = bookingServiceRepo
                    .findBookingServicesWithoutPaymentDetail(bookingID);
            for (BookingServices bService : listBookingServices) {
                if (bService.getService().getIsPrepaid()) {
                    PaymentDetail paymentDetailOfService = new PaymentDetail();
                    paymentDetailOfService.setPayment(payment);
                    paymentDetailOfService.setBookingService(bService);
                    paymentDetailOfService.setPaymentPurpose(PaymentPurpose.PREPAID_SERVICE);
                    com.lullabyhomestay.homestay_management.domain.Service service = bService.getService();
                    Double rawTotalAmount = service.getPrice() * bService.getQuantity();
                    paymentDetailOfService.setBaseAmount(rawTotalAmount);
                    paymentDetailOfService.setFinalAmount(
                            bService.getRawTotalAmount() - DiscountUtil.calculateDiscountAmount(rawTotalAmount,
                                    payment.getBooking().getCustomer()));
                    paymentDetailRepo.save(paymentDetailOfService);
                }
            }
        }
    }

    private void handleAdditionalServicePayment(Payment payment, Long bookingID) {
        if (bookingServiceRepo.existsByBooking_BookingID(bookingID)) {
            List<BookingServices> listBookingServices = bookingServiceRepo
                    .findBookingServicesWithoutPaymentDetail(bookingID);
            for (BookingServices bService : listBookingServices) {
                PaymentDetail paymentDetail = new PaymentDetail();
                paymentDetail.setPayment(payment);

                paymentDetail.setPaymentPurpose(PaymentPurpose.ADDITIONAL_SERVICE);
                paymentDetail.setBookingService(bService);

                Double rawTotalAmount = bService.getRawTotalAmount();
                paymentDetail.setBaseAmount(rawTotalAmount);
                paymentDetail.setFinalAmount(rawTotalAmount - DiscountUtil
                        .calculateDiscountAmount(rawTotalAmount, payment.getBooking().getCustomer()));
                paymentDetailRepo.save(paymentDetail);
            }
        }
    }

    private void handleExtendedHoursPayment(Payment payment, Long bookingID) {
        BookingExtension bookingExtension = bookingExtensionService.getLatestBookingExtensionByBookingID(bookingID);
        PaymentDetail paymentDetail = new PaymentDetail();
        paymentDetail.setPayment(payment);
        paymentDetail.setPaymentPurpose(PaymentPurpose.EXTENDED_HOURS);
        paymentDetail.setBookingExtension(bookingExtension);

        Double rawTotalAmount = bookingExtension.getTotalAmount();
        paymentDetail.setBaseAmount(rawTotalAmount);
        paymentDetail.setFinalAmount(rawTotalAmount
                - DiscountUtil.calculateDiscountAmount(rawTotalAmount, bookingExtension.getBooking().getCustomer()));
        paymentDetailRepo.save(paymentDetail);
    }
}
