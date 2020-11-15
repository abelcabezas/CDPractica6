package main;

import us.car.accidents.core.CarAccident;
import us.car.accidents.operations.CarAccidentOperations;
import us.car.accidents.utils.CarAccidentParser;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.Math;

public class Main {

    public static void main(String args[]) {
        mainMenu();
    }


    public static void mainMenu() {

        System.out.println("Por favor, seleccione una opción del menú o pulse \"0\" para salir del programa: \n" +
                "\t1. Obtener la severidad de accidente mas común (severity).\n" +
                "\t2. Obtener la distancia media en la que ocurren los accidentes.\n" +
                "\t3. Obtener el lado de la calle  (side) más común en el que ocurren accidentes.\n" +
                "\t4. Obtener la condición meteorológica (weather_condition) con la que ocurren más accidentes.\n" +
                "\t5. Otener el numero de accidentes que ocurren por debajo de un determinado nivel de visibilidad (visibility (mi)). \n" +
                "\t0. Salir del programa.\n");

        Scanner scanner = new Scanner(System.in);
        try {
            readUserOption(scanner.nextInt());
        } catch (IOException e) {
            System.out.println("Hubo un error leyendo la opción del usuario: \n" + e.getStackTrace());
        }
    }


    public static void visibilityMenu() {

        System.out.println("Por favor, introduzca una distancia umbral en millas (utilizando una coma para los descimales) o pulse \"0\" para volver al menú principal: \n");
        Scanner scanner = new Scanner(System.in);
        try {
            readVisibilityOption(scanner.nextFloat());
        } catch (IOException e) {
            System.out.println("Hubo un error leyendo el umbral de visibiliad: \n" + e.getStackTrace());
        }
    }


    public static void readUserOption(int i) throws IOException {
        CarAccidentOperations carOps= new CarAccidentOperations();
        long startTime, endTime, timeElapsed;
        switch (i) {
            case 0:
                System.out.println("Elegiste salir, ¡hasta pronto!");
                startTime = System.nanoTime();
                endTime = System.nanoTime();
                timeElapsed = endTime - startTime;
                printElapsedTime(timeElapsed);
                break;
            case 1:
                startTime = System.nanoTime();
                String severity = carOps.getMostCommonSeverity(readData());
                endTime = System.nanoTime();
                timeElapsed = endTime - startTime;
                printElapsedTime(timeElapsed);
                System.out.println("La severidad de accidente más común es: "+ severity);
                break;
            case 2:
                startTime = System.nanoTime();
                float mediumDistance = carOps.getMediumDistance(readData());
                endTime = System.nanoTime();
                timeElapsed = endTime - startTime;
                System.out.printf("La distancia media en la que ocurren los accidentes es %.3f%n millas ó %.3f%n kilómetros. \n",  mediumDistance, mediumDistance/0.621);
                printElapsedTime(timeElapsed);
                break;
            case 3:
                startTime = System.nanoTime();
                String streetSide = carOps.getMostCommonSide(readData());
                endTime = System.nanoTime();
                timeElapsed = endTime - startTime;
                printElapsedTime(timeElapsed);
                System.out.println("Los accidentes ocurren con mas frecuencia en el lado "+"\033[0;1m" +streetSide);
                break;
            case 4:
                startTime = System.nanoTime();
                String weatherCondition  = carOps.getMostCommonWeatherCondition(readData());
                endTime = System.nanoTime();
                timeElapsed = endTime - startTime;
                System.out.println("Los accidentes ocurren con mas frecuencia, con la condicion meteorológica: "+weatherCondition);
                printElapsedTime(timeElapsed);
                break;
            case 5:
                visibilityMenu();
                break;

            default:
                System.out.println("valor introducido no válido  (" + i + ") por favor inserte un valor del 0 al 5");
                mainMenu();

        }
    }


    public static void readVisibilityOption(float threshold) throws IOException {
        CarAccidentOperations carOps= new CarAccidentOperations();
        long startTime, endTime, timeElapsed;
        if (threshold == 0){
            mainMenu();
        }else{
                startTime = System.nanoTime();
                int visibilityOcurrences  = carOps.getAccidentsUnderThreshold(readData(),threshold );
                endTime = System.nanoTime();
                timeElapsed = endTime - startTime;
                printElapsedTime(timeElapsed);
                System.out.println("Por debajo del umbral "+threshold+" se producen " +visibilityOcurrences +" accidentes.");
        }
    }

    private static void printElapsedTime(long timeElapsed) {
        System.out.println("Elapsed time = " + timeElapsed + " ns");
        System.out.println("Elapsed time = " + timeElapsed / 1000000 + " ms");
        System.out.println("Elapsed time ≈ " + timeElapsed / 1000000000 + " s");
    }

    public static ArrayList<CarAccident> readData() throws IOException {
        FileInputStream fstream = new FileInputStream("resources/preprocessed_car_accidents.csv");
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        String strLine;
        ArrayList<CarAccident> carAccidents = new ArrayList<>();
        //Read the header and skip it
        strLine = br.readLine();
        //Read File Line By Line
        while ((strLine = br.readLine()) != null) {
            // Print the content on the console
            CarAccidentParser cap = new CarAccidentParser();
            carAccidents.add(cap.csvLineToCarAccident(strLine));
        }
        fstream.close();
        return carAccidents;
    }


}
