package service;

import model.Booking;
import model.Guest;
import model.Room;
import repository.BookingRepository;
import java.time.LocalDate;
import java.util.List;

public class BookingService {
    // Внедрение зависимости через интерфейс (Dependency Injection)
    private final BookingRepository repository;

    public BookingService(BookingRepository repository) {
        this.repository = repository;
    }

    public Booking bookRoom(String id, model.Guest guest, model.Room room, LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null) {
            throw new IllegalArgumentException("Даты не могут быть пустыми");
        }
        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Дата выезда должна быть позже даты заезда");
        }

        // Проверка индивидуального задания: пересечение дат бронирования одного номера
        List<Booking> existingBookings = repository.findByRoomId(room.getId());
        for (Booking existing : existingBookings) {
            boolean overlaps = !checkIn.isAfter(existing.getCheckOutDate()) &&
                    !checkOut.isBefore(existing.getCheckInDate());
            if (overlaps) {
                throw new IllegalStateException("Номер " + room.getRoomNumber() +
                        " уже забронирован на указанный период c " +
                        existing.getCheckInDate() + " по " + existing.getCheckOutDate());
            }
        }

        Booking newBooking = new Booking(id, guest, room, checkIn, checkOut);
        repository.save(newBooking);
        return newBooking;
    }
}