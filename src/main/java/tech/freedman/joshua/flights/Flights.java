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

public class Flights {

    final String ip;

    final short port;
    final URL aircraftUrl;
    final Gson gson;

    private final IDataStreamer dataStreamer;

    public Flights() throws MalformedURLException {
        this.ip = "flights.joshua.freedman.tech";
        this.port = 443;
        this.aircraftUrl = new URL("https", this.ip, this.port, "/data/aircraft.json");
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        // this.dataStreamer = new InfluxDbStreamer(this, "ORG", "BUCKET", "HOST", "TOKEN");
        this.dataStreamer = stream -> { // NOOP
        };
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

    public static void main(String[] args) throws MalformedURLException {

        final Flights flights = new Flights();

        while (true) {
            final List<Aircraft> aircrafts = flights.getAircraft();

            flights.dataStreamer.writeData(aircrafts.stream());

            aircrafts.stream()
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
