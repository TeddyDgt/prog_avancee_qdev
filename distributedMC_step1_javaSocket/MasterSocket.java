import java.io.*;
import java.net.*;

/** Master is a client. It makes requests to numWorkers.
 *
 */
public class MasterSocket {
	static int maxServer = 8;
	static final int[] tab_port = {25545, 25546, 25547, 25548, 25549, 25550, 25551, 25552};
	static String[] tab_total_workers = new String[maxServer];
	static final String ip = "127.0.0.1";
	static BufferedReader[] reader = new BufferedReader[maxServer];
	static PrintWriter[] writer = new PrintWriter[maxServer];
	static Socket[] sockets = new Socket[maxServer];

	public static void main(String[] args) throws Exception {

		// MC parameters
		int totalCount = 16000000; // total number of throws on a Worker
		int total = 0; // total number of throws inside quarter of disk
		double pi;

		int numWorkers = maxServer;
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		String s; // for bufferRead

		System.out.println("#########################################");
		System.out.println("# Computation of PI by MC method        #");
		System.out.println("#########################################");

		System.out.println("\n How many workers for computing PI (< maxServer): ");
		try {
			s = bufferRead.readLine();
			numWorkers = Integer.parseInt(s);
			System.out.println(numWorkers);
		} catch (IOException ioE) {
			ioE.printStackTrace();
		}

		// Enter worker ports
		for (int i = 0; i < numWorkers; i++) {
			System.out.println("Enter worker" + i + " port : ");
			try {
				s = bufferRead.readLine();
				tab_port[i] = Integer.parseInt(s); // Update port array
				System.out.println("You select " + s);
			} catch (IOException ioE) {
				ioE.printStackTrace();
			}
		}

		// Create worker's socket
		for (int i = 0; i < numWorkers; i++) {
			sockets[i] = new Socket(ip, tab_port[i]);
			System.out.println("SOCKET = " + sockets[i]);

			reader[i] = new BufferedReader(new InputStreamReader(sockets[i].getInputStream()));
			writer[i] = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sockets[i].getOutputStream())), true);
		}

		String message_to_send = String.valueOf(totalCount);
		String message_repeat = "y";

		long stopTime, startTime;

		// Create CSV file and write headers
		try (PrintWriter csvWriter = new PrintWriter(new FileWriter("performanceMasterSocket.csv"))) {
			csvWriter.println("numWorkers,pi,error,totalPoints,duration (ns)");

			while (message_repeat.equals("y")) {

				startTime = System.nanoTime(); // Start time in nanoseconds
				// Initialize workers
				for (int i = 0; i < numWorkers; i++) {
					writer[i].println(message_to_send); // Send a message to each worker
				}

				// Listen to workers' messages
				total = 0;
				for (int i = 0; i < numWorkers; i++) {
					tab_total_workers[i] = reader[i].readLine(); // Read message from server
					System.out.println("Client received: " + tab_total_workers[i]);
					total += Integer.parseInt(tab_total_workers[i]);
				}

				// Compute PI with the results from workers
				pi = 4.0 * total / totalCount / numWorkers;
				stopTime = System.nanoTime(); // Stop time in nanoseconds

				long duration = stopTime - startTime;
				double error = Math.abs((pi - Math.PI)) / Math.PI;

				System.out.println("\nPi : " + pi);
				System.out.println("Error: " + error + "\n");
				System.out.println("Ntot: " + totalCount * numWorkers);
				System.out.println("Available processors: " + numWorkers);
				System.out.println("Time Duration (ns): " + duration + "\n");

				// Write performance data to CSV
				csvWriter.printf("%d,%.6f,%.6f,%d,%d%n", numWorkers, pi, error, totalCount * numWorkers, duration);
				csvWriter.flush();

				System.out.println("\n Repeat computation (y/N): ");
				try {
					message_repeat = bufferRead.readLine();
					System.out.println(message_repeat);
				} catch (IOException ioE) {
					ioE.printStackTrace();
				}
			}
		}

		// Close sockets and streams
		for (int i = 0; i < numWorkers; i++) {
			System.out.println("END"); // Send ending message
			writer[i].println("END");
			reader[i].close();
			writer[i].close();
			sockets[i].close();
		}

		System.out.println("Performance data saved to performance.csv");
	}
}
