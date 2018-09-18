package pl.us.pawel.cwieka.travellingsalesmanproblem.aco;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.us.pawel.cwieka.travellingsalesmanproblem.resource.AcoInput;
import pl.us.pawel.cwieka.travellingsalesmanproblem.resource.City;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.Random;
import java.util.stream.IntStream;


public class AntColonyOptimization {

    private double c = 1.0;
    private double alpha;
    private double beta;
    private double evaporation;
    private double Q;
    private double antFactor;
    private double randomFactor;
    private int attempts;
    private int maxIterations;

    private int numberOfCities;
    private int numberOfAnts;
    private double graph[][];
    private double trails[][];
    private List<Ant> ants = new ArrayList<>();
    private Random random = new Random();
    private double probabilities[];

    private int currentIndex;

    private int[] bestTourOrder;
    private double bestTourLength;

    private static final Logger logger = LogManager.getLogger(AntColonyOptimization.class);

    public AntColonyOptimization(AcoInput acoInput) {

        this.alpha = acoInput.getAlpha();
        this.beta = acoInput.getBeta();
        this.evaporation = acoInput.getEvaporation();
        this.Q = acoInput.getQ();
        this.antFactor = acoInput.getAntFactor();
        this.randomFactor = acoInput.getRandomFactor();
        this.maxIterations = acoInput.getIterations();
        this.attempts = acoInput.getAttempts();

        graph = generateMatrix(acoInput);
        numberOfCities = graph.length;
        numberOfAnts = (int) (numberOfCities * antFactor);

        trails = new double[numberOfCities][numberOfCities];
        probabilities = new double[numberOfCities];
        IntStream.range(0, numberOfAnts)
                .forEach(i -> ants.add(new Ant(numberOfCities)));
    }

    public double[][] generateMatrix(AcoInput acoInput) {

        List<City> citiesCoordinates = acoInput.getCities();

        int n =  citiesCoordinates.size();

        double[][] matrix = new double[n][n];

        for(City city : citiesCoordinates){

            for(City innerCity : citiesCoordinates){

                matrix[city.getCity()][innerCity.getCity()] = Math.sqrt(Math.pow(city.getX()-innerCity.getX(),2) + Math.pow(city.getY()-innerCity.getY(),2));

            }

        }

        return matrix;
    }

    public String startAntOptimization() {

        String bestTour = null;

        for(int i = 1; i <= attempts; i++) {

            logger.info("Attempt #" + i);
            bestTour = solve();

        }

        return bestTour;
    }


    public String solve() {
        setupAnts();
        clearTrails();
        IntStream.range(0, maxIterations)
                .forEach(i -> {
                    moveAnts();
                    updateTrails();
                    updateBest();
                });

        logger.info("Best tour length: " + (bestTourLength - numberOfCities) + ", best tour order: " + Arrays.toString(bestTourOrder));

        int[] response = new int[bestTourOrder.length+1];
        System.arraycopy(bestTourOrder, 0, response, 0, bestTourOrder.length);
        response[response.length-1] = (int)(bestTourLength - numberOfCities);

        return  Arrays.toString(response);
    }


    private void setupAnts() {
        IntStream.range(0, numberOfAnts)
                .forEach(i -> {
                    ants.forEach(ant -> {
                        ant.clear();
                        ant.visitCity(-1, random.nextInt(numberOfCities));
                    });
                });
        currentIndex = 0;
    }


    private void moveAnts() {
        IntStream.range(currentIndex, numberOfCities - 1)
                .forEach(i -> {
                    ants.forEach(ant -> ant.visitCity(currentIndex, selectNextCity(ant)));
                    currentIndex++;
                });
    }


    private int selectNextCity(Ant ant) {
        int t = random.nextInt(numberOfCities - currentIndex);
        if (random.nextDouble() < randomFactor) {
            OptionalInt cityIndex = IntStream.range(0, numberOfCities)
                    .filter(i -> i == t && !ant.visited(i))
                    .findFirst();
            if (cityIndex.isPresent()) {
                return cityIndex.getAsInt();
            }
        }
        calculateProbabilities(ant);
        double r = random.nextDouble();
        double total = 0;
        for (int i = 0; i < numberOfCities; i++) {
            total += probabilities[i];
            if (total >= r) {
                return i;
            }
        }

        throw new RuntimeException("There are no other cities");
    }


    public void calculateProbabilities(Ant ant) {
        int i = ant.trail[currentIndex];
        double pheromone = 0.0;
        for (int l = 0; l < numberOfCities; l++) {
            if (!ant.visited(l)) {
                pheromone += Math.pow(trails[i][l], alpha) * Math.pow(1.0 / graph[i][l], beta);
            }
        }
        for (int j = 0; j < numberOfCities; j++) {
            if (ant.visited(j)) {
                probabilities[j] = 0.0;
            } else {
                double numerator = Math.pow(trails[i][j], alpha) * Math.pow(1.0 / graph[i][j], beta);
                probabilities[j] = numerator / pheromone;
            }
        }
    }

    private void updateTrails() {
        for (int i = 0; i < numberOfCities; i++) {
            for (int j = 0; j < numberOfCities; j++) {
                trails[i][j] *= evaporation;
            }
        }
        for (Ant a : ants) {
            double contribution = Q / a.trailLength(graph);
            for (int i = 0; i < numberOfCities - 1; i++) {
                trails[a.trail[i]][a.trail[i + 1]] += contribution;
            }
            trails[a.trail[numberOfCities - 1]][a.trail[0]] += contribution;
        }
    }

    private void updateBest() {
        if (bestTourOrder == null) {
            bestTourOrder = ants.get(0).trail;
            bestTourLength = ants.get(0)
                    .trailLength(graph);
        }
        for (Ant a : ants) {
            if (a.trailLength(graph) < bestTourLength) {
                bestTourLength = a.trailLength(graph);
                bestTourOrder = a.trail.clone();
            }
        }
    }

    private void clearTrails() {
        IntStream.range(0, numberOfCities)
                .forEach(i -> {
                    IntStream.range(0, numberOfCities)
                            .forEach(j -> trails[i][j] = c);
                });
    }

}
