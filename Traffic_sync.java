/* Assignment prepared by
 * Leila Farah Moussa
 * Imane Boudra
 * Khaoula Qassibi
 * Soukaina El Maysour

This code ran sucecsfully on all three test cases.
*/

package traffic_sync;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Queue;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Traffic_sync {

    public static final int MAX = 3; // Max number of cars that can cross in one direction before switching

    public static ArrayList<Vehicle> arrival_index = new ArrayList<>();
    public static ArrayList<Integer> departure_index = new ArrayList<>();

    public static enum Direction {
        EAST,
        WEST;

        private static final List<Direction> values = Collections.unmodifiableList(Arrays.asList(values()));
        private static final int SIZE = values.size();
        private static final Random rand = new Random();

        public static Direction getRandom() {
            return values.get(rand.nextInt(SIZE));
        }
    }

    public static class Semaphore {

        private int count;
        private Direction direction_semaphore; // The currently allowed direction.
        private Queue<Vehicle> toEastQueue; // Those waiting to go east.
        private Queue<Vehicle> toWestQueue; // Those watiting to go west.

        public Semaphore() {
            this.count = MAX;
            this.direction_semaphore = Direction.EAST; // Arbitrary initial direction.
            this.toEastQueue = new LinkedList<>();
            this.toWestQueue = new LinkedList<>();
        }

        public void switchDirection() {
            if (this.direction_semaphore == Direction.EAST) {
                this.direction_semaphore = Direction.WEST;
            } else {
                this.direction_semaphore = Direction.EAST;
            }
        }

        public Vehicle unblockSide(Direction d) {
            Vehicle unblocked;
            if (d == Direction.EAST) {
                unblocked = this.toEastQueue.remove();
            } else {
                unblocked = this.toWestQueue.remove();
            }
            unblocked.blocked = false;
            return unblocked;
        }

        public void blockVehicle(Vehicle vehicle) {
            // Enqueue a car that's asking to cross but is not allowed yet.
            vehicle.blocked = true;
            if (vehicle.direction == Direction.EAST) {
                this.toEastQueue.add(vehicle);
            } else {
                this.toWestQueue.add(vehicle);
            }
        }

        public boolean queueIsEmpty(boolean same_side) {
            if (same_side) {
                if (this.direction_semaphore == Direction.EAST) {
                    return (this.toEastQueue.isEmpty());
                } else {
                    return (this.toWestQueue.isEmpty());
                }
            } else {
                if (this.direction_semaphore == Direction.EAST) {
                    return (this.toWestQueue.isEmpty());
                } else {
                    return (this.toEastQueue.isEmpty());
                }
            }
        }

        public void acquireAccess(Vehicle requester) { // this is semWait
            System.out.println(sem);
            this.count--;
            if (this.count >= 0) {
                if (requester.blocked == true) {
                    sem.unblockSide(requester.direction);
                }
                // We still haven't maxed out, just continue.
            } else {
                // count < 0, meaning we maxed out
                // so block the car who asked for access,
                // switch direction, and reset the count for the other side
                //System.out.println("Maxed out.");
                if (requester.blocked == false) {
                    this.blockVehicle(requester);
                }
                if (this.queueIsEmpty(false) == false) {
                    // If there is indeed someone on the other side, switch
                    this.switchDirection();
                }
                this.count = MAX;
            }
        }

        public void releaseAccess(Vehicle releaser) { // this is semSignal
            //System.out.println(sem);
            //System.out.println(releaser.vehicle_id + " just released.");
            if (this.queueIsEmpty(true)) {
                // If there's no one here, switch.
                this.count = MAX;
                this.switchDirection();
            }
        }

        @Override
        public String toString() {
            return "(Cnt: " + this.count + ", DIR_SEM: " + this.direction_semaphore
                    + ", EQ: " + this.toEastQueue + ", WQ: " + this.toWestQueue + ")";
        }

    }

    public static class Vehicle extends Thread {

        int vehicle_id;
        Direction direction;
        int time_to_cross;
        boolean blocked;

        public Vehicle(int vehicle_id, Direction direction, int time_to_cross) {
            this.vehicle_id = vehicle_id;
            this.direction = direction;
            this.time_to_cross = time_to_cross;
            this.blocked = false;
        }

        public void OneVehicle() {
            this.Arrive();
            this.Cross();
            this.Exit();
        }

        public void Arrive() {
            System.out.println("Vehicle " + this.toString() + " showed up.");

            while (true) {
                while (this.direction != sem.direction_semaphore) {
                    System.out.print("");
                    
                    if (this.blocked == false) {
                        sem.blockVehicle(this);
                    }
                }
                sem.acquireAccess(this);
                if (this.blocked == false) {
                    return;
                }
            }
        }

        public void Cross() {
            System.out.println("Vehicle " + this.toString() + " is crossing.");
            this.criticalSection(); // just a timeout
        }

        public void Exit() {
            sem.releaseAccess(this);
            System.out.println("Vehicle " + this.toString() + " exited.");
            System.out.println(sem);
            departure_index.add(this.vehicle_id);
        }

        @Override
        public void run() {
            this.OneVehicle();
        }

        public void criticalSection() {
            int time_to_sleep = this.time_to_cross * 1000;
            long start, end, slept_for;
            boolean interrupted_flag = false;

            System.out.println("Entered critical section");
            while (time_to_sleep > 0) {
                start = System.currentTimeMillis();
                try {
                    Vehicle.sleep(time_to_sleep);
                    //System.out.println("Sleeping.");
                    break; // If the sleep was uninterrupted, break out of this while loop.
                } catch (InterruptedException ie) {
                    interrupted_flag = true;
                    // If a thread is interrupted in the middle of its sleep, 
                    // that means it did not sleep for the whole time desired
                    // so we should force it back to sleep for the remaining time
                    // so first, figure out how much sleep time already passed, slept_for
                    end = System.currentTimeMillis();
                    slept_for = end - start;
                    // then go back to sleep for the remainder
                    time_to_sleep -= slept_for;
                }
            }

            if (interrupted_flag) {
                // good idea to rethrow the exception and not ignore it
                Thread.currentThread().interrupt();
            }
        }

        @Override
        public String toString() {
            return "(ID: " + this.vehicle_id + ", DIR: " + this.direction + ", TIME: " + this.time_to_cross
                    + ", BLOCKED: " + this.blocked + ")";
        }

    }

    public static Semaphore sem = new Semaphore();

    public static void main(String[] args) throws InterruptedException {

        int min_time = 1;
        int max_time = 15;
        int choice;

        // create a list of vehicle objects for our 20 vehicles
        List<Vehicle> vehicles = new ArrayList<>();
        // vehicle ids start from 1 to 20 
        // direction is randomly assigned 
        // time to cross is randomly assigend within a range (1,15)
        for (int i = 0; i < 20; i++) {
            vehicles.add(i, new Vehicle(i + 1, Direction.getRandom(), 
                    (int) (Math.random() * (max_time - min_time + 1) + min_time)));
        }

        //get user choice 
        Scanner inp = new Scanner(System.in);
        System.out.println("Enter your selection:\n1 for test case i\n2 for test case ii \n3 for test case iii");
        choice = inp.nextInt();
        switch (choice) {
            case 1:
                for (int i = 0; i < 5; i++){ //start first 5 threads
                    vehicles.get(i).start();
                    arrival_index.add(vehicles.get(i));
                }
                TimeUnit.SECONDS.sleep(10); // delay 10 seconds
                for (int i = 5; i < 10; i++) {
                    vehicles.get(i).start();
                    arrival_index.add(vehicles.get(i));
                }
                TimeUnit.SECONDS.sleep(10);
                for (int i = 10; i < 15; i++) {
                    vehicles.get(i).start();
                    arrival_index.add(vehicles.get(i));
                }
                TimeUnit.SECONDS.sleep(10);
                for (int i = 15; i < 20; i++) {
                    vehicles.get(i).start();
                    arrival_index.add(vehicles.get(i));
                }
                break;
            case 2:
                for (int i = 0; i < 10; i++) {
                    vehicles.get(i).start();
                    arrival_index.add(vehicles.get(i));
                }
                TimeUnit.SECONDS.sleep(10);
                for (int i = 10; i < 20; i++) {
                    vehicles.get(i).start();
                    arrival_index.add(vehicles.get(i));
                }
                break;
            case 3:
                for (int i = 0; i < 20; i++) {
                    vehicles.get(i).start();
                    arrival_index.add(vehicles.get(i));
                }
                break;

            default:
                System.out.println("Invalid Choice");
        }

        // stoping all threads execution
        for (Vehicle v : vehicles) {
            v.join();
        }

        System.out.println("Arrival order: " + arrival_index);
        System.out.println("Departure order: " + departure_index);

        System.out.println("State of the semaphores in the end: " + sem);

    }

}
