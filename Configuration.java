package CVRP;

import static CVRP.Print.print;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// The values here are overwritten if a configuration file is given.
public class Configuration {
    public static boolean graphics = false;
    public static int maxJourneys = 30;
    public static int maxGenerations = Integer.MAX_VALUE;
    public static int populationSize = 100;
    public static int maxSwaps = 2;
    public static double crossoverRate = 1.0;
    public static double mutationRate = 0.1;
    public static double mixRate = 0.3;
    public static double moveRate = 0.5;
    public static int searchLength = 1000;
    public static boolean nearestNeighbour = true;
    public static int generationsBeforeReset = 10000;
    public static long maxTime = 60 * 60; // 1 hour
    public static int EMPLOYED_BEE_NUM =25;
    public static int ONLOOKER_BEE_NUM=25;
    public static int ITERATION_LIM=(CVRPData.NUM_NODES-1)*50;
    public static long EPOCH_LIM=30000000;
    public static int RUNTIME_LIM=30;//minute
    public static double ALPHA = 300;//weight for the violation of capacity in the fitness function
    public static int CAPACITY_REDUNDANCY =50;
    public static double INSERT_DEPOT_RATE=0.1;
    public static double REMOVE_DEPOT_RATE=0.1;

    public Configuration(String fileName) {
        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);

            String line;

            while ((line = br.readLine()) != null) {
                String[] lineSplit = line.split("=");

                try {
                    if (lineSplit[0].equals("GRAPHICS")) {
                        graphics = Boolean.parseBoolean(lineSplit[1]);
                    } else if (lineSplit[0].equals("MIX_RATE")) {
                        mixRate = Double.parseDouble(lineSplit[1]);
                    } else if (lineSplit[0].equals("CROSSOVER_RATE")) {
                        crossoverRate = Double.parseDouble(lineSplit[1]);
                    } else if (lineSplit[0].equals("MUTATION_RATE")) {
                        mutationRate = Double.parseDouble(lineSplit[1]);
                    } else if (lineSplit[0].equals("MAX_SWAPS")) {
                        maxSwaps = Integer.parseInt(lineSplit[1]);
                    } else if (lineSplit[0].equals("MAX_GENERATIONS")) {
                        maxGenerations = Integer.parseInt(lineSplit[1]);
                    } else if (lineSplit[0].equals("MAX_JOURNEYS")) {
                        maxJourneys = Integer.parseInt(lineSplit[1]);
                    } else if (lineSplit[0].equals("NEAREST_NEIGHBOUR")) {
                        nearestNeighbour = Boolean.parseBoolean(lineSplit[1]);
                    } else if (lineSplit[0].equals("SEARCH_LENGTH")) {
                        searchLength = Integer.parseInt(lineSplit[1]);
                    } else if (lineSplit[0].equals("POPULATION_SIZE")) {
                        populationSize = Integer.parseInt(lineSplit[1]);
                    } else if (lineSplit[0].equals("GENERATIONS_BEFORE_RESET")) {
                        generationsBeforeReset = Integer.parseInt(lineSplit[1]);  
                    } else if (lineSplit[0].equals("MAX_TIME")) {
                        maxTime = Long.parseLong(lineSplit[1]);
                    } else {
                        printError(line);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            br.close();
        } catch (IOException e) {
            print("Could not read config file");
        }
    }

    // Default configuration
    public Configuration() {
    }

    private void printError(String line) {
        print("Error reading config file. " + line + " not understood");
    }

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
}
