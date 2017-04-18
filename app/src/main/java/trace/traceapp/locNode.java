package trace.traceapp;

/**
 * Created by david on 4/11/2017.
 */

public class locNode {

    private String locName = "";
    private String locAddress = "";
    private float locLatCoord;
    private float locLongCoord;
    private int timesVisit = 0;

    public locNode() {
    }


    public void setLocName(String name) {
        this.locName = name;
    }

    public void setLocAddress(String address) {
        this.locAddress = address;
    }

    public void setTimesVisit(int visits) {
        this.timesVisit = visits;
    }

    public void setLocLatCoord(float latitude) {
        this.locLatCoord = latitude;
    }

    public void setLocLongCoord(float longitude) {
        this.locLongCoord = longitude;
    }

    /*public void setLocationCoord(float latitude, float longitude) {
        this.locLatCoord = latitude;
        this.locLongCoord = longitude;
    }*/

    
}
