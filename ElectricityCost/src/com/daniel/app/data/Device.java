package com.daniel.app.data;

public class Device {

    private String name;
    private double power;
    private double time;

    public Device(String name, double power, double time) {
        this.name = name;
        this.power = power;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
    @Override
    public String toString() {
        return "Device{" +
                "name='" + name + '\'' +
                ", power=" + power +
                ", time=" + time +
                '}';
    }


}
