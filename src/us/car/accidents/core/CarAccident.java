package us.car.accidents.core;

import us.car.accidents.types.Fall;
import us.car.accidents.types.GeoLocation;
import us.car.accidents.types.NameType;

public class CarAccident {

    String severity;
    float distance;
    String side;
    String w_condition;
    float visibility;

    public CarAccident() {

    }
    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getW_condition() {
        return w_condition;
    }

    public void setW_condition(String w_condition) {
        this.w_condition = w_condition;
    }

    public float getVisibility() {
        return visibility;
    }

    public void setVisibility(float visibility) {
        this.visibility = visibility;
    }




}
