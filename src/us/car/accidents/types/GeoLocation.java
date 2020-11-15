package us.car.accidents.types;

/**
 * TODO javadoc
 */
public class GeoLocation {

    double reclat;
    double reclong;

    public GeoLocation(double reclat, double reclong){
        this.reclat = reclat;
        this.reclong = reclong;
    }
    public double getReclat() {
        return reclat;
    }

    public void setReclat(float reclat) {
        this.reclat = reclat;
    }

    public double getReclong() {
        return reclong;
    }

    public void setReclong(float reclong) {
        this.reclong = reclong;
    }




}
