package tech.freedman.joshua.flights.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AircraftReq {

    /**
     * since epoch
     */
    @SerializedName("now")
    private Double nowTime;
    @SerializedName("aircraft")
    private List<Aircraft> aircraft;
    @SerializedName("messages")
    private Integer messages;

    public Double getNowTime() {
        return this.nowTime;
    }

    public List<Aircraft> getAircraft() {
        return this.aircraft;
    }

    public Integer getMessages() {
        return this.messages;
    }
}
