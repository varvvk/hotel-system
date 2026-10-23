package repository;

import model.Booking;
import java.util.ArrayList;
import java.util.List;

public class InMemoryBookingRepository implements BookingRepository {
    private final List<Booking> bookings = new ArrayList<>();

    @Override
    public void save(Booking booking) {
        bookings.add(booking);
    }

    @Override
    public List<Booking> findByRoomId(String roomId) {
        List<Booking> result = new ArrayList<>();
        for (Booking b : bookings) {
            if (b.getRoom().getId().equals(roomId)) {
                result.add(b);
            }
        }
        return result;
    }

    @Override
    public List<Booking> findAll() {
        return new ArrayList<>(bookings);
    }
}