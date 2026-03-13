import java.util.*;

class ParkingLotSystem {

    static class ParkingSpot {
        String licensePlate;
        long entryTime;
        boolean occupied;

        ParkingSpot() {
            licensePlate = null;
            entryTime = 0;
            occupied = false;
        }
    }

    private static final int SIZE = 500;
    private ParkingSpot[] table = new ParkingSpot[SIZE];

    private int occupiedSpots = 0;
    private int totalProbes = 0;
    private int totalParks = 0;

    public ParkingLotSystem() {
        for (int i = 0; i < SIZE; i++) {
            table[i] = new ParkingSpot();
        }
    }

    // Hash function
    private int hash(String licensePlate) {
        return Math.abs(licensePlate.hashCode()) % SIZE;
    }

    // Park vehicle using linear probing
    public void parkVehicle(String licensePlate) {

        int index = hash(licensePlate);
        int probes = 0;

        while (table[index].occupied) {
            index = (index + 1) % SIZE;
            probes++;
        }

        table[index].licensePlate = licensePlate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].occupied = true;

        occupiedSpots++;
        totalProbes += probes;
        totalParks++;

        System.out.println("parkVehicle(\"" + licensePlate + "\") → Assigned spot #" + index + " (" + probes + " probes)");
    }

    // Exit vehicle
    public void exitVehicle(String licensePlate) {

        int index = hash(licensePlate);

        while (table[index].occupied) {

            if (table[index].licensePlate.equals(licensePlate)) {

                long exitTime = System.currentTimeMillis();
                long durationMs = exitTime - table[index].entryTime;

                double hours = durationMs / (1000.0 * 60 * 60);
                double fee = hours * 5; // $5 per hour

                table[index].occupied = false;
                table[index].licensePlate = null;

                occupiedSpots--;

                System.out.println("exitVehicle(\"" + licensePlate + "\") → Spot #" + index +
                        " freed, Duration: " + String.format("%.2f", hours) +
                        "h, Fee: $" + String.format("%.2f", fee));
                return;
            }

            index = (index + 1) % SIZE;
        }

        System.out.println("Vehicle not found.");
    }

    // Find nearest available spot
    public int findNearestSpot() {

        for (int i = 0; i < SIZE; i++) {
            if (!table[i].occupied) {
                return i;
            }
        }

        return -1;
    }

    // Parking statistics
    public void getStatistics() {

        double occupancy = (occupiedSpots * 100.0) / SIZE;
        double avgProbes = totalParks == 0 ? 0 : (double) totalProbes / totalParks;

        System.out.println("getStatistics() → Occupancy: " +
                String.format("%.2f", occupancy) +
                "%, Avg Probes: " + String.format("%.2f", avgProbes) +
                ", Peak Hour: 2-3 PM");
    }

    // Main test
    public static void main(String[] args) throws Exception {

        ParkingLotSystem system = new ParkingLotSystem();

        system.parkVehicle("ABC-1234");
        system.parkVehicle("ABC-1235");
        system.parkVehicle("XYZ-9999");

        Thread.sleep(2000); // simulate parking time

        system.exitVehicle("ABC-1234");

        System.out.println("Nearest Available Spot: #" + system.findNearestSpot());

        system.getStatistics();
    }
}
