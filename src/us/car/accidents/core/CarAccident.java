package us.car.accidents.core;



public class CarAccident {

    private  String severity;
    private float distance;
    private String side;
    private String w_condition;
    private float visibility;

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
