package ch.test_viewpager.model.data;

/**
 * Created by CH on 2016-07-16.
 */
public class GPSData {

    private double tm_x;
    private double tm_y;

    private double wgs_x;
    private double wgs_y;

    public double getTm_x() {
        return tm_x;
    }

    public void setTm_x(double tm_x) {
        this.tm_x = tm_x;
    }

    public double getTm_y() {
        return tm_y;
    }

    public void setTm_y(double tm_y) {
        this.tm_y = tm_y;
    }

    public double getWgs_x() {
        return wgs_x;
    }

    public void setWgs_x(double wgs_x) {
        this.wgs_x = wgs_x;
    }

    public double getWgs_y() {
        return wgs_y;
    }

    public void setWgs_y(double wgs_y) {
        this.wgs_y = wgs_y;
    }

    public GPSData(){
        tm_x = 0;
        tm_y = 0;
        wgs_x = 0;
        wgs_y = 0;
    }

}
