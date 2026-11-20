package service;

import java.time.LocalDate;
import java.util.List;
import model.Booking;
import model.Guest;
import model.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.BookingRepository;
import java.util.Date;

public class BookingService {
    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);
    private final BookingRepository repository;

    public BookingService(BookingRepository repository) {
        this.repository = repository;
    }

    public Booking bookRoom(String id, Guest guest, Room room, LocalDate checkIn, LocalDate checkOut) {
        logger.info("Booking request received for room {} (id={}) from {} to {}",
                room.getRoomNumber(), room.getId(), checkIn, checkOut);

        if (checkIn == null || checkOut == null) {
            logger.error("Booking {} failed: check-in or check-out date is null", id);
            throw new IllegalArgumentException("Даты не могут быть пустыми");
        }
        if (!checkOut.isAfter(checkIn)) {
            logger.error("Booking {} failed: check-out date ({}) must be after check-in date ({})",
                    id, checkOut, checkIn);
            throw new IllegalArgumentException("Дата выезда должна быть позже даты заезда");
        }

        List<Booking> existingBookings = repository.findByRoomId(room.getId());
        for (Booking existing : existingBookings) {
            boolean overlaps = !checkIn.isAfter(existing.getCheckOutDate())
                    && !checkOut.isBefore(existing.getCheckInDate());
            if (overlaps) {
                logger.warn("Room {} is already booked from {} to {}",
                        room.getRoomNumber(), existing.getCheckInDate(), existing.getCheckOutDate());
                throw new IllegalStateException("Номер " + room.getRoomNumber()
                        + " уже забронирован на указанный период c "
                        + existing.getCheckInDate() + " по " + existing.getCheckOutDate());
            }
        }

        Booking newBooking = new Booking(id, guest, room, checkIn, checkOut);
        repository.save(newBooking);
        logger.info("Booking {} for room {} created successfully", id, room.getRoomNumber());
        return newBooking;
    }
}