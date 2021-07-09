package com.technerds.racelogger.Utils;

public class TemperatureConverter {


// Instance variable
    
    private double degreesKelvin; // degrees in Kelvin

// Constructor method: initialize degreesKelvin to zero
    
    public TemperatureConverter() {
        degreesKelvin = 0;
    }

// Convert and save degreesCelius in the Kelvin scale
    
    public void setKelvin(double degreesKelvin) {
        this.degreesKelvin = degreesKelvin;
    }
    
    // Convert and save degreesCelius in the Kelvin scale
    
    public void setCelsius(double degreesCelsius) {
        degreesKelvin = degreesCelsius + 273.16;
    }

// Convert degreesKelvin to Celsius and return the value
    
    public double getCelsius() {
        double c = degreesKelvin - 273.16;
        return c;
    }

// Convert and save degreesFahrenheit in the Kelvin scale
    
    public void setFahrenheit(double degreesFahrenheit) {
        degreesKelvin = (5d / 9 * (degreesFahrenheit - 32) + 273);
    }

// Convert degreesKelvin to Fahrenheit and return the value
    
    public double getFahrenheit() {
        double f = (((degreesKelvin - 273) * 9d / 5) + 32);
        return f;
    }
    
    
}
