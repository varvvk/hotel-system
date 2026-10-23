
import model.Guest;
import model.Room;
import repository.BookingRepository;
import repository.InMemoryBookingRepository;
import service.BookingService;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        // Создание зависимости
        BookingRepository repository = new InMemoryBookingRepository();

        // Внедрение зависимости в сервис (Dependency Injection)
        BookingService service = new BookingService(repository);

        Guest guest1 = new Guest("g1", "Иван Иванов");
        Guest guest2 = new Guest("g2", "Анна Смирнова");
        Room room101 = new Room("r1", "101");

        // 1. Успешное бронирование
        service.bookRoom("b1", guest1, room101,
                LocalDate.of(2026, 10, 1),
                LocalDate.of(2026, 10, 5));
        System.out.println("Первое бронирование успешно создано!");

        // 2. Попытка бронирования на пересекающиеся даты
        try {
            service.bookRoom("b2", guest2, room101,
                    LocalDate.of(2026, 10, 3),
                    LocalDate.of(2026, 10, 8));
        } catch (IllegalStateException e) {
            System.out.println("Ошибка при повторном бронировании: " + e.getMessage());
        }
    }
}