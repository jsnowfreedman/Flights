package tech.freedman.joshua.flights.data;

import com.google.gson.annotations.SerializedName;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;
import java.util.List;
import java.util.StringJoiner;

@Measurement(name = "aircraft")
public class Aircraft {
    @Column(timestamp = true)
    private final Instant timestamp = Instant.now();
    /**
     * the 24-bit ICAO identifier of the aircraft, as 6 hex digits
     */
    @SerializedName("hex")
    @Column
    private String icaoHexIdentifier;
    @SerializedName("alt_baro")
    @Column
    private Object altitudeBarometer;
    @SerializedName("alt_geom")
    @Column
    private Integer altitudeGeometric;
    /**
     * Feet/Minute
     */
    @SerializedName("geom_rate")
    @Column
    private Double altitudeGeometricRate;
    /**
     * Feet/Minute
     */
    @SerializedName("baro_rate")
    @Column
    private Double altitudeBarometricRate;
    /**
     * Knots
     */
    @SerializedName("gs")
    @Column
    private Double groundSpeed;
    /**
     * Knots
     */
    @SerializedName("ias")
    @Column
    private Double indicatedAirSpeed;
    /**
     * Knots
     */
    @SerializedName("tas")
    @Column
    private Double trueAirSpeed;
    @SerializedName("mach")
    @Column
    private Double mach;
    /**
     * Degrees
     */
    @SerializedName("track")
    @Column
    private Double trackOverGround;
    /**
     * degrees/second
     */
    @SerializedName("track_rate")
    @Column
    private Double trackOverGroundRate;
    /**
     * degrees, negative is left roll
     */
    @SerializedName("roll")
    @Column
    private Double roll;
    /**
     * 0, 1, 2 (3-7 are reserved)
     */
    @SerializedName("version")
    @Column
    private Integer adsbVersion;
    @SerializedName("nac_p")
    @Column
    private Integer positionNavAccuracy;
    @SerializedName("nac_v")
    @Column
    private Integer velocityNavAccuracy;
    @SerializedName("sil")
    @Column
    private Integer sourceIntegrityLevel;
    @SerializedName("sil_type")
    @Column
    private String sourceIntegrityLevelType;
    @SerializedName("messages")
    @Column
    private Integer messagesReceived;
    @SerializedName("seen")
    @Column
    private Double secondsSinceLastMessage;
    /**
     * dbFS. Will be negative
     */
    @SerializedName("rssi")
    @Column
    private Double rssi;
    /**
     * hPa
     */
    @SerializedName("nav_qnh")
    @Column
    private Double altimeterSetting;
    @SerializedName("nav_altitude_mcp")
    @Column
    private Double modeControlPanelAltitude;
    @SerializedName("nav_altitude_fms")
    @Column
    private Double flightManagementSystemAltitude;
    /**
     * Usually magnetic, although not defined
     */
    @SerializedName("nav_heading")
    @Column
    private Double selectedHeading;
    @SerializedName("nic_baro")
    @Column
    private Integer navigationIntegrityCategoryBarometricAltitude;
    @SerializedName("gva")
    @Column
    private Integer geometricVerticalAccuracy;
    @SerializedName("sda")
    @Column
    private Integer systemDesignAssurance;
    @SerializedName("squawk")
    @Column
    private String squawk;
    @SerializedName("emergency")
    @Column
    private String emergencyStatus;
    @SerializedName("lat")
    @Column
    private String latitude;
    @SerializedName("lon")
    @Column
    private String longitude;
    @SerializedName("nic")
    @Column
    private Integer navigationIntegrityCategory;
    /**
     * meters
     */
    @SerializedName("rc")
    @Column
    private Integer radiusOfContainment;
    @SerializedName("seen_pod")
    @Column
    private Double secondsSinceLastPosition;
    /**
     * callsign, flight name, or aircraft registration
     */
    @SerializedName("flight")
    @Column(tag = true)
    private String flight;
    /**
     * aircraft or vehicle classes (values A0 - D7)
     */
    @SerializedName("category")
    @Column
    private String emitterCategory;
    /**
     * adsb_icao: messages from a Mode S or ADS-B transponder, using a 24-bit ICAO address
     * adsb_icao_nt: messages from an ADS-B equipped "non-transponder" emitter e.g. a ground vehicle, using a 24-bit ICAO address
     * adsr_icao: rebroadcast of ADS-B messages originally sent via another data link e.g. UAT, using a 24-bit ICAO address
     * tisb_icao: traffic information about a non-ADS-B target identified by a 24-bit ICAO address, e.g. a Mode S target tracked by secondary radar
     * adsb_other: messages from an ADS-B transponder using a non-ICAO address, e.g. anonymized address
     * adsr_other: rebroadcast of ADS-B messages originally sent via another data link e.g. UAT, using a non-ICAO address
     * tisb_other: traffic information about a non-ADS-B target using a non-ICAO address
     * tisb_trackfile: traffic information about a non-ADS-B target using a track/file identifier, typically from primary or Mode A/C radar
     */
    @SerializedName("type")
    @Column
    private String messageType;
    /**
     * list of fields derived from TIS-B data
     */
    @SerializedName("tisb")
    @Column
    private List<String> fieldsFromTisb;
    /**
     * list of fields derived from MLAT data
     */
    @SerializedName("mlat")
    @Column
    private List<String> fieldsFromMlat;
    /**
     * degrees clockwise from magnetic north
     */
    @SerializedName("mag_heading")
    @Column
    private Double magneticHeading;
    /**
     * set of engaged automation modes: 'autopilot', 'vnav', 'althold', 'approach', 'lnav', 'tcas'
     */
    @SerializedName("nav_modes")
    @Column
    private List<String> navigationModes;

    public String getIcaoHexIdentifier() {
        return this.icaoHexIdentifier;
    }

    public Integer getAltitudeBarometer() {
        if (this.altitudeBarometer instanceof Double)
            return ((Double) this.altitudeBarometer).intValue();
        return null;
    }

    public Integer getAltitudeGeometric() {
        return this.altitudeGeometric;
    }

    public Double getAltitudeGeometricRate() {
        return this.altitudeGeometricRate;
    }

    public Double getAltitudeBarometricRate() {
        return this.altitudeBarometricRate;
    }

    public Double getGroundSpeed() {
        return this.groundSpeed;
    }

    public Double getIndicatedAirSpeed() {
        return this.indicatedAirSpeed;
    }

    public Double getTrueAirSpeed() {
        return this.trueAirSpeed;
    }

    public Double getMach() {
        return this.mach;
    }

    public Double getTrackOverGround() {
        return this.trackOverGround;
    }

    public Double getTrackOverGroundRate() {
        return this.trackOverGroundRate;
    }

    public Double getRoll() {
        return this.roll;
    }

    public Integer getAdsbVersion() {
        return this.adsbVersion;
    }

    public Integer getPositionNavAccuracy() {
        return this.positionNavAccuracy;
    }

    public Integer getVelocityNavAccuracy() {
        return this.velocityNavAccuracy;
    }

    public Integer getSourceIntegrityLevel() {
        return this.sourceIntegrityLevel;
    }

    public String getSourceIntegrityLevelType() {
        return this.sourceIntegrityLevelType;
    }

    public Integer getMessagesReceived() {
        return this.messagesReceived;
    }

    public Double getSecondsSinceLastMessage() {
        return this.secondsSinceLastMessage;
    }

    public Double getRssi() {
        return this.rssi;
    }

    public Double getAltimeterSetting() {
        return this.altimeterSetting;
    }

    public Double getModeControlPanelAltitude() {
        return this.modeControlPanelAltitude;
    }

    public Double getFlightManagementSystemAltitude() {
        return this.flightManagementSystemAltitude;
    }

    public Double getSelectedHeading() {
        return this.selectedHeading;
    }

    public Integer getNavigationIntegrityCategoryBarometricAltitude() {
        return this.navigationIntegrityCategoryBarometricAltitude;
    }

    public Integer getGeometricVerticalAccuracy() {
        return this.geometricVerticalAccuracy;
    }

    public Integer getSystemDesignAssurance() {
        return this.systemDesignAssurance;
    }

    public String getSquawk() {
        return this.squawk;
    }

    public String getEmergencyStatus() {
        return this.emergencyStatus;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public Integer getNavigationIntegrityCategory() {
        return this.navigationIntegrityCategory;
    }

    public Integer getRadiusOfContainment() {
        return this.radiusOfContainment;
    }

    public Double getSecondsSinceLastPosition() {
        return this.secondsSinceLastPosition;
    }

    public String getFlight() {
        return this.flight;
    }

    public String getEmitterCategory() {
        return this.emitterCategory;
    }

    public String getMessageType() {
        return this.messageType;
    }

    public List<String> getFieldsFromTisb() {
        return this.fieldsFromTisb;
    }

    public List<String> getFieldsFromMlat() {
        return this.fieldsFromMlat;
    }

    public Double getMagneticHeading() {
        return this.magneticHeading;
    }

    public List<String> getNavigationModes() {
        return this.navigationModes;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Aircraft.class.getSimpleName() + "[", "]")
                .add("hexColor='" + this.icaoHexIdentifier + "'")
                .add("altitudeBarometer='" + this.altitudeBarometer + "'")
                .add("altitudeGeometric='" + this.altitudeGeometric + "'")
                .add("altitudeGeometricRate=" + this.altitudeGeometricRate)
                .add("altitudeBarometricRate=" + this.altitudeBarometricRate)
                .add("groundSpeed=" + this.groundSpeed)
                .add("indicatedAirSpeed=" + this.indicatedAirSpeed)
                .add("trueAirSpeed=" + this.trueAirSpeed)
                .add("mach=" + this.mach)
                .add("trackOverGround=" + this.trackOverGround)
                .add("trackOverGroundRate=" + this.trackOverGroundRate)
                .add("roll=" + this.roll)
                .add("adsbVersion=" + this.adsbVersion)
                .add("positionNavAccuracy=" + this.positionNavAccuracy)
                .add("velocityNavAccuracy=" + this.velocityNavAccuracy)
                .add("sourceIntegrityLevel=" + this.sourceIntegrityLevel)
                .add("sourceIntegrityLevelType='" + this.sourceIntegrityLevelType + "'")
                .add("messagesReceived=" + this.messagesReceived)
                .add("secondsSinceLastMessage=" + this.secondsSinceLastMessage)
                .add("rssi=" + this.rssi)
                .add("altimeterSetting=" + this.altimeterSetting)
                .add("modeControlPanelAltitude=" + this.modeControlPanelAltitude)
                .add("flightManagementSystemAltitude=" + this.flightManagementSystemAltitude)
                .add("selectedHeading=" + this.selectedHeading)
                .add("navigationIntegrityCategoryBarometricAltitude=" + this.navigationIntegrityCategoryBarometricAltitude)
                .add("geometricVerticalAccuracy=" + this.geometricVerticalAccuracy)
                .add("systemDesignAssurance=" + this.systemDesignAssurance)
                .add("squawk='" + this.squawk + "'")
                .add("emergencyStatus='" + this.emergencyStatus + "'")
                .add("latitude='" + this.latitude + "'")
                .add("longitude='" + this.longitude + "'")
                .add("navigationIntegrityCategory=" + this.navigationIntegrityCategory)
                .add("radiusOfContainment=" + this.radiusOfContainment)
                .add("secondsSinceLastPosition=" + this.secondsSinceLastPosition)
                .add("flight='" + this.flight + "'")
                .add("emitterCategory='" + this.emitterCategory + "'")
                .add("messageType='" + this.messageType + "'")
                .add("fieldsFromTisb=" + this.fieldsFromTisb)
                .add("fieldsFromMlat=" + this.fieldsFromMlat)
                .add("magneticHeading=" + this.magneticHeading)
                .add("navigationModes=" + this.navigationModes)
                .toString();
    }
}
// ^\s+"type": .+,?\s