package model;

public class Room {
    private final String id;
    private final String roomNumber;

    public Room(String id, String roomNumber) {
        this.id = id;
        this.roomNumber = roomNumber;
    }

    public String getId() {
        return id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }
}