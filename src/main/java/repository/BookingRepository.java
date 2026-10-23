package repository;

import model.Booking;
import java.util.List;

public interface BookingRepository {
    void save(Booking booking);
    List<Booking> findByRoomId(String roomId);
    List<Booking> findAll();
}