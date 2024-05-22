package sit707_week5;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

public class WeatherController {
    public static final int HOURS_PER_DAY = 10;
    private static WeatherController instance;
    private Clock clock;

    public static WeatherController getInstance(Clock clock) {
        if (instance == null) {
            instance = new WeatherController(clock);
        } else {
            instance.setClock(clock);
        }
        return instance;
    }

    // Overloaded factory method for backward compatibility
    public static WeatherController getInstance() {
        return getInstance(new RealClock());
    }

    private void setClock(Clock clock) {
        this.clock = clock;
    }

    // Initialise 10 hourly temperature
    private static double[] todaysHourlyTemperature = new double[HOURS_PER_DAY];

    /**
     * Private constructor prevents to create new instance manually.
     * A factory method needs to be used.
     * @param clock 
     */
    WeatherController(Clock clock) {
        this.clock = clock;
        System.out.println("Creating new weather controller.");

        // Sleep to simulate a delay
        sleep(2 + new Random().nextInt(5));

        // Initialize hourly temperatures
        Random random = new Random();
        for (int i = 0; i < HOURS_PER_DAY; i++) {
            todaysHourlyTemperature[i] = 1 + random.nextInt(30);
        }
        System.out.println(Arrays.toString(todaysHourlyTemperature));
    }

    public void close() {
        System.out.println("Closing weather controller.");
        instance = null;
        sleep(2 + new Random().nextInt(5));
    }

    /**
     * Calculate minimum of today's hourly temperatures.
     * @return
     */
    public double getTemperatureMinFromCache() {
        double minVal = 1000;
        for (int i = 0; i < todaysHourlyTemperature.length; i++) {
            if (minVal > todaysHourlyTemperature[i]) {
                minVal = todaysHourlyTemperature[i];
            }
        }
        return minVal;
    }

    /**
     * Calculate maximum of today's hourly temperatures.
     * @return
     */
    public double getTemperatureMaxFromCache() {
        double maxVal = -1;
        for (int i = 0; i < todaysHourlyTemperature.length; i++) {
            if (maxVal < todaysHourlyTemperature[i]) {
                maxVal = todaysHourlyTemperature[i];
            }
        }
        return maxVal;
    }

    /**
     * Calculate average of today's hourly temperatures.
     * @return
     */
    public double getTemperatureAverageFromCache() {
        double sumVal = 0;
        for (int i = 0; i < todaysHourlyTemperature.length; i++) {
            sumVal += todaysHourlyTemperature[i];
        }
        return sumVal / todaysHourlyTemperature.length;
    }

    /**
     * Return temperature for given hour of current day.
     * @param hour
     * @return
     */
    public double getTemperatureForHour(int hour) {
        // sleep a while to simulate a delay
        sleep(1 + new Random().nextInt(5));

        // Let's return a randomly selected temperature from hourly list if hour does not exist.
        if (hour > todaysHourlyTemperature.length) {
            hour = 1 + new Random().nextInt(todaysHourlyTemperature.length);
        }

        // Hour index starts from 0 instead of 1 due to array indexing.
        return todaysHourlyTemperature[hour - 1];
    }

    /**
     * Persist reported temperature to data store and return recorded time. 
     * @param hour
     * @param temperature
     * @return
     */
    public String persistTemperature(int hour, double temperature) {
        SimpleDateFormat sdf = new SimpleDateFormat("H:m:s");
        String savedTime = sdf.format(new Date(clock.currentTimeMillis()));
        System.out.println("Temperature: " + temperature + " of hour: " + hour + ", saved at " + savedTime);
        // sleep(1 + new Random().nextInt(2)); // Removed delay for testing
        return savedTime;
    }

    /**
     * Calculated the number of hours temperature data is available for today.
     * @return
     */
    public int getTotalHours() {
        return todaysHourlyTemperature.length;
    }

    // Define the Clock interface
    public interface Clock {
        long currentTimeMillis();
    }

    // RealClock implementation retrieves the current time from the system clock
    public static class RealClock implements Clock {
        @Override
        public long currentTimeMillis() {
            return System.currentTimeMillis();
        }
    }

    // FakeClock implementation returns a predefined time value
    public static class FakeClock implements Clock {
        private long currentTimeMillis;

        public FakeClock(long currentTimeMillis) {
            this.currentTimeMillis = currentTimeMillis;
        }

        public void setTime(long currentTimeMillis) {
            this.currentTimeMillis = currentTimeMillis;
        }

        @Override
        public long currentTimeMillis() {
            return currentTimeMillis;
        }
    }

    /**
     * Sleep for specified seconds.
     * @param sec
     */
    public static void sleep(int sec) {
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
