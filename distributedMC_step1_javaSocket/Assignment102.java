package assignments;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

class PiMonteCarlo {
    AtomicInteger nAtomSuccess;
    int nThrows;
    double value;

    class MonteCarlo implements Runnable {
        @Override
        public void run() {
            double x = Math.random();
            double y = Math.random();
            if (x * x + y * y <= 1)
                nAtomSuccess.incrementAndGet();
        }
    }

    public PiMonteCarlo(int i) {
        this.nAtomSuccess = new AtomicInteger(0);
        this.nThrows = i;
        this.value = 0;
    }

    public double getPi(int nProcessors) {
        ExecutorService executor = Executors.newFixedThreadPool(nProcessors);
        for (int i = 1; i <= nThrows; i++) {
            Runnable worker = new MonteCarlo();
            executor.execute(worker);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        value = 4.0 * nAtomSuccess.get() / nThrows;
        return value;
    }
}

public class Assignment102 {
    public static void main(String[] args) {
        String folderName = "perfs";
        createFolder(folderName);

        String strongScalingCSV = folderName + "/performanceSF_assignment102.csv";
        String weakScalingCSV = folderName + "/performanceWF_assignment102.csv";

        // Initialize CSV files (overwrite if they exist)
        initializeCSV(strongScalingCSV, "numProcessors,totalIterations,timeDurationNs");
        initializeCSV(weakScalingCSV, "numProcessors,totalIterations,timeDurationNs");

        // Strong Scaling: Total iterations fixed at 16 million
        int fixedIterations = 16000000;
        for (int numProcessors = 1; numProcessors <= 16; numProcessors++) {
            long avgDuration = performMultipleTests(fixedIterations, numProcessors, 10);
            saveToCSV(strongScalingCSV, numProcessors, fixedIterations, avgDuration);
        }

        // Weak Scaling: Iterations increase with workers (1 million per processor)
        int iterationsPerProcessor = 1000000;
        for (int numProcessors = 1; numProcessors <= 16; numProcessors++) {
            int totalIterations = iterationsPerProcessor * numProcessors;
            long avgDuration = performMultipleTests(totalIterations, numProcessors, 10);
            saveToCSV(weakScalingCSV, numProcessors, totalIterations, avgDuration);
        }
    }

    private static long performMultipleTests(int totalIterations, int numProcessors, int numRuns) {
        long totalDuration = 0;
        for (int i = 0; i < numRuns; i++) {
            totalDuration += performTest(totalIterations, numProcessors);
        }
        return totalDuration / numRuns; // Calculate average duration
    }

    private static long performTest(int totalIterations, int numProcessors) {
        PiMonteCarlo piMonteCarlo = new PiMonteCarlo(totalIterations);
        long startTime = System.nanoTime();
        piMonteCarlo.getPi(numProcessors);
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    private static void saveToCSV(String csvFile, int numProcessors, int totalIterations, long duration) {
        try (PrintWriter csvWriter = new PrintWriter(new FileWriter(csvFile, true))) {
            csvWriter.printf("%d,%d,%d%n", numProcessors, totalIterations, duration);
        } catch (Exception e) {
            System.err.println("Error writing to CSV: " + e.getMessage());
        }
    }

    private static void initializeCSV(String csvFile, String header) {
        try (PrintWriter csvWriter = new PrintWriter(new FileWriter(csvFile, false))) { // Overwrite mode
            csvWriter.println(header);
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
