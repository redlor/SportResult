package com.example.android.sportresult;

/**
 * Created by Hp on 13/10/2017.
 */

public class Event {
    private String name;
    private String location;
    private String duration;

    public Event(String name, String location, String duration) {
        this.name = name;
        this.location = location;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }
}
