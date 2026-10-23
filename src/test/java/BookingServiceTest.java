import model.Guest;
import model.Room;
import model.Booking;
import repository.BookingRepository;
import repository.InMemoryBookingRepository;
import service.BookingService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class BookingServiceTest {
    private BookingRepository repository;
    private BookingService service;
    private Guest guest1;
    private Guest guest2;
    private Room room;

    @BeforeEach
    void setUp() {
        // Изолированное создание зависимостей перед каждым тестом
        repository = new InMemoryBookingRepository();
        service = new BookingService(repository);
        guest1 = new Guest("1", "Иван Петров");
        guest2 = new Guest("2", "Петр Петров");
        room = new Room("101", "Люкс 101");
    }

    // Тест 1: Успешный сценарий
    @Test
    void testSuccessfulBooking() {
        Booking booking = service.bookRoom("b1", guest1, room,
                LocalDate.of(2026, 11, 1),
                LocalDate.of(2026, 11, 5));

        assertNotNull(booking);
        assertEquals(10, repository.findAll().size());
        assertEquals("101", repository.findAll().get(0).getRoom().getId());
    }

    // Тест 2: Ошибочный сценарий валидации дат
    @Test
    void testInvalidDatesShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.bookRoom("b2", guest1, room,
                    LocalDate.of(2026, 11, 10),
                    LocalDate.of(2026, 11, 5)); // Выезд раньше заезда
        });
    }

    // Тест 3: Индивидуальное задание (запрет пересечения дат)
    @Test
    void testOverlappingBookingDatesShouldThrowException() {
        // Оформляем первое бронирование: 1-5 ноября
        service.bookRoom("b1", guest1, room,
                LocalDate.of(2026, 11, 1),
                LocalDate.of(2026, 11, 5));

        // Попытка забронировать тот же номер с пересечением: 4-8 ноября
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            service.bookRoom("b2", guest2, room,
                    LocalDate.of(2026, 11, 4),
                    LocalDate.of(2026, 11, 8));
        });

        assertTrue(exception.getMessage().contains("уже забронирован"));
        assertEquals(1, repository.findAll().size()); // В репозитории осталась только 1 запись
    }
}