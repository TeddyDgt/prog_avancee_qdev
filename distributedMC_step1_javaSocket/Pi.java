import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Approximates PI using the Monte Carlo method. Demonstrates
 * use of Callables, Futures, and thread pools.
 */
public class Pi {
    public static void main(String[] args) throws Exception {
        String folderName = "perfs";
        createFolder(folderName);

        String strongScalingCSV = folderName + "/performanceSS_pi.csv";
        String weakScalingCSV = folderName + "/performanceWS_pi.csv";

        // Initialize CSV files (overwrite if they exist)
        initializeCSV(strongScalingCSV);
        initializeCSV(weakScalingCSV);

        // Strong Scaling: Total iterations fixed at 16 million
        int fixedIterations = 16000000;
        for (int numWorkers = 1; numWorkers <= 16; numWorkers++) {
            long avgDuration = performMultipleTests(fixedIterations, numWorkers, 10);
            saveToCSV(strongScalingCSV, numWorkers, fixedIterations, avgDuration);
        }

        // Weak Scaling: Iterations increase with workers (1 million per processor)
        int iterationsPerWorker = 1000000;
        for (int numWorkers = 1; numWorkers <= 16; numWorkers++) {
            int totalIterations = iterationsPerWorker * numWorkers;
            long avgDuration = performMultipleTests(totalIterations, numWorkers, 10);
            saveToCSV(weakScalingCSV, numWorkers, totalIterations, avgDuration);
        }
    }

    private static long performMultipleTests(int totalIterations, int numWorkers, int numRuns) throws InterruptedException, ExecutionException {
        long totalDuration = 0;
        for (int i = 0; i < numRuns; i++) {
            totalDuration += performTest(totalIterations, numWorkers);
        }
        return totalDuration / numRuns; // Calculate average duration
    }

    private static long performTest(int totalIterations, int numWorkers) throws InterruptedException, ExecutionException {
        long startTime = System.nanoTime();
        Master master = new Master();
        master.doRun(totalIterations, numWorkers);
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    private static void saveToCSV(String csvFile, int numWorkers, int totalIterations, long duration) {
        try (PrintWriter csvWriter = new PrintWriter(new FileWriter(csvFile, true))) {
            csvWriter.printf("%d,%d,%d%n", numWorkers, totalIterations, duration);
            System.out.println("Saved to CSV: " + numWorkers + " processors, " + totalIterations + " iterations.");
        } catch (Exception e) {
            System.err.println("Error writing to CSV: " + e.getMessage());
        }
    }

    private static void initializeCSV(String csvFile) {
        try (PrintWriter csvWriter = new PrintWriter(new FileWriter(csvFile, false))) { // Overwrite mode
            csvWriter.println("numProcessors,totalIterations,timeDurationNs");
            System.out.println("Initialized CSV file: " + csvFile);
        } catch (Exception e) {
            System.err.println("Error initializing CSV: " + e.getMessage());
        }
    }

    private static void createFolder(String folderName) {
        File folder = new File(folderName);
        if (!folder.exists()) {
            if (folder.mkdirs()) {
                System.out.println("Folder created: " + folderName);
            } else {
                System.err.println("Failed to create folder: " + folderName);
            }
        }
    }
}

/**
 * Creates workers to run the Monte Carlo simulation
 * and aggregates the results.
 */
class Master {
    public void doRun(int totalCount, int numWorkers) throws InterruptedException, ExecutionException {
        List<Callable<Long>> tasks = new ArrayList<>();
        for (int i = 0; i < numWorkers; ++i) {
            tasks.add(new Worker(totalCount / numWorkers));
        }

        ExecutorService exec = Executors.newFixedThreadPool(numWorkers);
        exec.invokeAll(tasks);
        exec.shutdown();
    }
}

/**
 * Task for running the Monte Carlo simulation.
 */
class Worker implements Callable<Long> {
    private final int numIterations;

    public Worker(int num) {
        this.numIterations = num;
    }

    @Override
    public Long call() {
        long circleCount = 0;
        for (int j = 0; j < numIterations; j++) {
            double x = Math.random();
            double y = Math.random();
            if ((x * x + y * y) < 1) ++circleCount;
        }
        return circleCount;
    }
}
