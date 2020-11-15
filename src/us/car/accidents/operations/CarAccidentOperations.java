package us.car.accidents.operations;

import us.car.accidents.core.CarAccident;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Functions representing the operations shown in the main menu
 */
public class CarAccidentOperations {
    /**
     * @param carAccidents
     * @return recclass with most occurrences on the list
     */
    public String getMostCommonSeverity(ArrayList<CarAccident> carAccidents) {
        Map<String, Integer> map = new HashMap<>();

        for (CarAccident t : carAccidents) {
            if (t.getSeverity() != null) {
                Integer val = map.get(t.getSeverity());
                map.put(t.getSeverity(), val == null ? 1 : val + 1);
            }
        }

        Map.Entry<String, Integer> max = null;

        for (Map.Entry<String, Integer> e : map.entrySet()) {
            System.out.println("Tipo de severidad: "+e.getKey()+" Numero de ocurrencias: "+e.getValue());
            if (max == null || e.getValue() > max.getValue())
                max = e;
        }

        return max.getKey();
    }

    public float getMediumDistance(ArrayList<CarAccident> carAccidents) {
        float media = 0;
        for (CarAccident t : carAccidents) {
                media  = media + t.getDistance();
        }
        media = media /carAccidents.size();

        return media;
    }

    public String getMostCommonSide(ArrayList<CarAccident> carAccidents) {
        Map<String, Integer> map = new HashMap<>();

        for (CarAccident t : carAccidents) {
            if (t.getSide() != null) {
                Integer val = map.get(t.getSide());
                map.put(t.getSide(), val == null ? 1 : val + 1);
            }
        }

        Map.Entry<String, Integer> max = null;

        for (Map.Entry<String, Integer> e : map.entrySet()) {
            if (max == null || e.getValue() > max.getValue())
                max = e;
        }

        return max.getKey().equals("R")  ? "derecho" : "izquierdo";
    }

    public String getMostCommonWeatherCondition(ArrayList<CarAccident> carAccidents) {
        Map<String, Integer> map = new HashMap<>();

        for (CarAccident t : carAccidents) {
            if (t.getW_condition() != null) {
                Integer val = map.get(t.getW_condition());
                map.put(t.getW_condition(), val == null ? 1 : val + 1);
            }
        }

        Map.Entry<String, Integer> max = null;

        for (Map.Entry<String, Integer> e : map.entrySet()) {
            //System.out.println("Tipo de condicion climatológica: "+e.getKey()+". Número de ocurrencias: "+e.getValue());
            if (max == null || e.getValue() > max.getValue())
                max = e;
        }

        return "'"+max.getKey() + "' con ("+max.getValue() +" ocurrencias)." ;
    }

    public int getAccidentsUnderThreshold(ArrayList<CarAccident> carAccidents, float threshold ) {
        int count = 0;
        for (CarAccident t : carAccidents) {
           if(t.getVisibility() <= threshold){
               count ++;
           }
        }
        return count;
    }





}
