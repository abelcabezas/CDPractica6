package us.car.accidents.utils;


import us.car.accidents.core.CarAccident;


public class CarAccidentParser {


    /**
     * Converts the CSV input file to objects of the class CarAccident
     */
    public CarAccident csvLineToCarAccident(String line) {
        CarAccident carAccident = new CarAccident();


            String[] splitLine = line.split(",");
            //parse regular line
            if(splitLine.length == 5) {
            carAccident = parseCsvLine(splitLine);
            }
        return carAccident;
    }

    private CarAccident parseCsvLine(String[] splitLine) {
        CarAccident carAccident = new CarAccident() ;
        carAccident.setSeverity(splitLine[0]);
        carAccident.setDistance(Float.parseFloat(splitLine[1]));
        carAccident.setSide(splitLine[2]);
        carAccident.setW_condition(splitLine[3]);
        carAccident.setVisibility(Float.parseFloat(splitLine[4]));
        return carAccident;


    }


}
