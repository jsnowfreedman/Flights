package tech.freedman.joshua.flights;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import tech.freedman.joshua.flights.data.Aircraft;
import tech.freedman.joshua.flights.data.AircraftReq;

import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Flights implements Runnable {

    private final URL aircraftUrl;
    private final Gson gson;
    /**
     * The array of Data Streamers that will get a list of flights on each execution of `run()`
     */
    private final IDataStreamer[] dataStreamers;

    public Flights(final String domain, final int port, final IDataStreamer... dataStreamers) throws MalformedURLException {
        this.aircraftUrl = new URL("https", domain, port, "/data/aircraft.json");
        this.dataStreamers = dataStreamers;

        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    static Integer averageAltitude(final Aircraft aircraft) {
        Integer altitude = null;
        if (aircraft.getAltitudeGeometric() != null) {
            altitude = aircraft.getAltitudeGeometric();
        }
        if (aircraft.getAltitudeBarometer() != null) {
            if (altitude != null) {
                altitude = (altitude + aircraft.getAltitudeBarometer()) / 2;
            } else {
                altitude = aircraft.getAltitudeGeometric();
            }
        }

        return altitude;
    }

    private static String padLeft(final Object base, final String padding, int count) {
        final String baseStr = String.valueOf(base);
        final StringBuilder output = new StringBuilder(count);

        while (output.length() < count - baseStr.length()) {
            output.append(padding);
        }
        output.append(baseStr);

        return output.toString();
    }

    @Override
    public void run() {
        final List<Aircraft> aircraft = this.getAircraft();
        for (final IDataStreamer dataStreamer : this.dataStreamers) {
            dataStreamer.writeData(aircraft.stream());
        }
    }

    public static void main(String[] args) throws MalformedURLException {

        // We are creating out flights instance. The dataStreamers parameter is varags, and allows for us to have multiple data handlers
        Flights flights = new Flights("flights.joshua.freedman.tech", 443, stream -> {
            stream
                    .filter(aircraft -> aircraft.getFlight() != null)
                    .filter(aircraft -> aircraft.getLongitude() != null && aircraft.getLatitude() != null)
//                    .filter(aircraft -> aircraft.getAltitudeGeometric() != null)
//                    .sorted(Comparator.comparing(Aircraft::getAltitudeGeometric).reversed())
                    .map(aircraft ->
                            String.format("%s: %s, %s, %s knots, %s feet ( GeoAlt: %s, BaroAlt: %s )%s",
                                    Optional.ofNullable(aircraft.getFlight()).orElse("Unknown"),
                                    aircraft.getLatitude(),
                                    aircraft.getLongitude(),
                                    padLeft(aircraft.getGroundSpeed(), " ", 5),
                                    padLeft(averageAltitude(aircraft), " ", 5),
                                    padLeft(aircraft.getAltitudeGeometric(), " ", 5),
                                    padLeft(aircraft.getAltitudeBarometer(), " ", 5),
                                    aircraft.getNavigationModes() != null ? "\n\t" + aircraft.getNavigationModes() : ""))
                    .forEach(System.out::println);
        });

        while (true) {
            flights.run();

            System.out.println();
            System.out.println();
            System.out.println();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Aircraft> getAircraft() {
        try {
            final URLConnection urlConnection = this.aircraftUrl.openConnection();

            try (final InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream())) {
                final AircraftReq aircraftReq = this.gson.fromJson(inputStreamReader, AircraftReq.class);
                return aircraftReq.getAircraft();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>(0);
    }
}
