package pl.us.pawel.cwieka.travellingsalesmanproblem.aco;

import org.junit.Test;

import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class AntColonyOptimizationTest {

    @Test
    public void generateRandomMatrix() {

        int n = 5;

        Random random = new Random();

        double[][] randomMatrix = new double[n][n];
        IntStream.range(0, n)
                .forEach(i -> IntStream.range(0, n)
                        .forEach(j ->  System.out.print(Math.abs(random.nextInt(100) + 1) + " ")));

    }

    @Test
    public void startAntOptimization() {
    }

    @Test
    public void solve() {
    }

    @Test
    public void calculateProbabilities() {
    }
}