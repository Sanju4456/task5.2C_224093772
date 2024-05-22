package sit707_week5;

import org.junit.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherControllerTest {
    private static WeatherController wController;
    private static int nHours;
    private static double[] hourlyTemperatures;

    @BeforeClass
    public static void setUpBeforeClass() {
        wController = WeatherController.getInstance(new WeatherController.RealClock());
        nHours = wController.getTotalHours();
        hourlyTemperatures = new double[nHours];
        for (int i = 0; i < nHours; i++) {
            hourlyTemperatures[i] = wController.getTemperatureForHour(i + 1);
        }
    }

    @AfterClass
    public static void tearDownAfterClass() {
        wController.close();
    }

    @Test
    public void testStudentIdentity() {
        String studentId = "224093772"; // Assigning a non-null value
        Assert.assertNotNull("Student ID is null", studentId);
    }

    @Test
    public void testStudentName() {
        String studentName = "Sanju Nimesha"; // Assigning a non-null value
        Assert.assertNotNull("Student name is null", studentName);
    }

    @Test
    public void testTemperatureMin() {
        System.out.println("+++ testTemperatureMin +++");
        double minTemperature = Double.MAX_VALUE;
        for (double temperatureVal : hourlyTemperatures) {
            if (minTemperature > temperatureVal) {
                minTemperature = temperatureVal;
            }
        }
        Assert.assertEquals(minTemperature, wController.getTemperatureMinFromCache(), 0.01);
    }

    @Test
    public void testTemperatureMax() {
        System.out.println("+++ testTemperatureMax +++");
        double maxTemperature = Double.MIN_VALUE;
        for (double temperatureVal : hourlyTemperatures) {
            if (maxTemperature < temperatureVal) {
                maxTemperature = temperatureVal;
            }
        }
        Assert.assertEquals(maxTemperature, wController.getTemperatureMaxFromCache(), 0.01);
    }

    @Test
    public void testTemperatureAverage() {
        System.out.println("+++ testTemperatureAverage +++");
        double sumTemp = 0;
        for (double temperatureVal : hourlyTemperatures) {
            sumTemp += temperatureVal;
        }
        double averageTemp = sumTemp / nHours;
        Assert.assertEquals(averageTemp, wController.getTemperatureAverageFromCache(), 0.01);
    }

    @Test
    public void testTemperaturePersist() {
        // Sample code to illustrate
        long startTime = System.currentTimeMillis();

        // Perform some operations that take time
        // For example, persist temperature for hour 10 with a value of 19.5
        WeatherController.FakeClock fakeClock = new WeatherController.FakeClock(startTime);
        WeatherController wController = WeatherController.getInstance(fakeClock);
        String persistTime = wController.persistTemperature(10, 19.5);

        long endTime = System.currentTimeMillis();
        long deltaMillis = endTime - startTime;
        long thresholdMillis = 1000; // Example threshold, adjust as needed

        System.out.println("deltaMillis: " + deltaMillis);
        System.out.println("thresholdMillis: " + thresholdMillis);

        Assert.assertTrue("Time difference exceeds threshold", deltaMillis <= thresholdMillis);
    }


    private long parseTimeToMillis(String timeString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("H:m:s");
            Date date = sdf.parse(timeString);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
