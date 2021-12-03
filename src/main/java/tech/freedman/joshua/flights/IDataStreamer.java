package tech.freedman.joshua.flights;

import tech.freedman.joshua.flights.data.Aircraft;

import java.util.stream.Stream;

public interface IDataStreamer {
    void writeData(Stream<Aircraft> stream);
}
