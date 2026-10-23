package model;

public class Guest {
    private final String id;
    private final String fullName;

    public Guest(String id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }
}