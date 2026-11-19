package service;

import java.time.LocalDate;
import java.util.List;
import model.Booking;
import model.Guest;
import model.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.BookingRepository;

public class BookingService {
    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);
    private final BookingRepository repository;

    public BookingService(BookingRepository repository) {
        this.repository = repository;
    }

    public Booking bookRoom(String id, Guest guest, Room room, LocalDate checkIn, LocalDate checkOut) {
        logger.info("Запрос на бронирование номера {} (id={}) с {} по {}",
                room.getRoomNumber(), room.getId(), checkIn, checkOut);

        if (checkIn == null || checkOut == null) {
            logger.error("Ошибка бронирования {}: даты не могут быть пустыми", id);
            throw new IllegalArgumentException("Даты не могут быть пустыми");
        }
        if (!checkOut.isAfter(checkIn)) {
            logger.error("Ошибка бронирования {}: дата выезда ({}) должна быть позже заезда ({})",
                    id, checkOut, checkIn);
            throw new IllegalArgumentException("Дата выезда должна быть позже даты заезда");
        }

        List<Booking> existingBookings = repository.findByRoomId(room.getId());
        for (Booking existing : existingBookings) {
            boolean overlaps = !checkIn.isAfter(existing.getCheckOutDate())
                    && !checkOut.isBefore(existing.getCheckInDate());
            if (overlaps) {
                logger.warn("Номер {} уже забронирован на период с {} по {}",
                        room.getRoomNumber(), existing.getCheckInDate(), existing.getCheckOutDate());
                throw new IllegalStateException("Номер " + room.getRoomNumber()
                        + " уже забронирован на указанный период c "
                        + existing.getCheckInDate() + " по " + existing.getCheckOutDate());
            }
        }

        Booking newBooking = new Booking(id, guest, room, checkIn, checkOut);
        repository.save(newBooking);
        logger.info("Бронирование {} для номера {} успешно создано", id, room.getRoomNumber());
        return newBooking;
    }
}