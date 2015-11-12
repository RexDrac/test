package CVRP;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// The values here are overwritten if a configuration file is given.
public class Configuration {

    public static int EMPLOYED_BEE_NUM =25;//25
    public static int ONLOOKER_BEE_NUM=25;
    public static int ITERATION_LIM=12450;//249*50=12450
    public static long EPOCH_LIM=2000000000;
    public static int RUNTIME_LIM=60;//minute
    public static double ALPHA = 300;//weight for the violation of capacity in the fitness function
    public static int CAPACITY_REDUNDANCY =0;
    public static double RANDOM_EXPLORE_RATE=0.5;//0.5
    public static double INSERT_DEPOT_RATE=0.1;//25
    public static double REMOVE_DEPOT_RATE=0.3;

    public Configuration(String fileName) {
        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);

            String line;

            while ((line = br.readLine()) != null) {
                String[] lineSplit = line.split("=");

                try {
                    if (lineSplit[0].equals("EMPLOYED_BEE_NUM")) {
                    	EMPLOYED_BEE_NUM = Integer.parseInt(lineSplit[1]);
                    } else if (lineSplit[0].equals("ONLOOKER_BEE_NUM")) {
                    	ONLOOKER_BEE_NUM = Integer.parseInt(lineSplit[1]);
                    } else if (lineSplit[0].equals("ITERATION_LIM")) {
                    	ITERATION_LIM = Integer.parseInt(lineSplit[1]);
                    } else if (lineSplit[0].equals("EPOCH_LIM")) {
                    	EPOCH_LIM = Long.parseLong(lineSplit[1]);
                    } else {
                        printError(line);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Could not read config file");
        }
    }

    // Default configuration
    public Configuration() {
    }

    private void printError(String line) {
        System.out.println("Error reading config file. " + line + " not understood");
    }
/*
    public boolean getGraphics() {
        return graphics;
    }

    public int getMaxJourneys() {
        return maxJourneys;
    }

    public int getMaxGenerations() {
        return maxGenerations;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public int getMaxSwaps() {
        return maxSwaps;
    }

    public double getCrossoverRate() {
        return crossoverRate;
    }

    public double getMutationRate() {
        return mutationRate;
    }

    public double getMixRate() {
        return mixRate;
    }

    public double getMoveRate() {
        return moveRate;
    }

    public int getSearchLength() {
        return searchLength;
    }

    public boolean getNearestNeighbour() {
        return nearestNeighbour;
    }

    public int getGenerationsBeforeReset() {
        return generationsBeforeReset;
    }

    public long getMaxTime() {
        return maxTime;
    }
    */
}
