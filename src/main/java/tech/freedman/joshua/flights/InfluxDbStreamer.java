package tech.freedman.joshua.flights;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import tech.freedman.joshua.flights.data.Aircraft;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InfluxDbStreamer implements IDataStreamer {
    private final InfluxDBClient influxDBClient;
    private final Flights flights;
    private final String org;
    private final String bucket;

    public InfluxDbStreamer(final Flights flights, final String org, final String bucket, String hostStr, final String token) {
        this.flights = flights;
        this.org = org;
        this.bucket = bucket;
        this.influxDBClient = InfluxDBClientFactory.create(hostStr, token.toCharArray());

    }

    @Override
    public void writeData(final Stream<Aircraft> stream) {
        final List<Point> points = stream.map(aircraft -> Point.measurement("aircraft")
                        .addTag("flight", aircraft.getFlight().trim())
                        .addField("latitude", Double.valueOf(aircraft.getLatitude()))
                        .addField("longitude", Double.valueOf(aircraft.getLongitude()))
                        .addField("groundSpeed", aircraft.getGroundSpeed())
                        .addField("altitude", Flights.averageAltitude(aircraft))
                        .addField("magneticHeading", aircraft.getMagneticHeading())
                        .addField("trueAirSpeed", aircraft.getTrueAirSpeed())
                        .time(Instant.now(), WritePrecision.NS))
                .collect(Collectors.toList());

        try (WriteApi writeApi = this.influxDBClient.getWriteApi()) {
            writeApi.writePoints(this.bucket, this.org, points);
        }
    }
}
