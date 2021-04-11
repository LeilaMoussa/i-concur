package diningphilosophers;

import static diningphilosophers.DiningPhilosophers.NUMBER;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class ResourceGraph {
    // whether this represents allocation or request does not matter, just
    // a matter of switching 1s & 0s

    // very simple 5X2 table because each philosopher is only concerned
    // with 2 specific forks (left & right), not the rest
    // and all forks are distinct (fixed position) ==> single-instance reusable resources
    // no need for sophisticated algos or DS's
    public AtomicInteger[][] allocationMatrix;
    //public AtomicIntegerArray allocationMatrix;

    public ResourceGraph() {
        // this.allocationMatrix = new int[NUMBER][2]; // 0s by default
        this.allocationMatrix = new AtomicInteger[NUMBER][2];
        for (int i = 0; i < NUMBER; i++) {
            for (int j = 0; j < 2; j++) {
                allocationMatrix[i][j] = new AtomicInteger();
            }
        }
    }

    public int detectCycle() {
        // deadlock means everyone has their respective left fork and is waiting for the right
        // so all rows have the value (1, 0)
        int cnt = 0;
        for (int i = 0; i < NUMBER; i++) {
            if (this.allocationMatrix[i][0] == new AtomicInteger(1)
                    && this.allocationMatrix[i][1] == new AtomicInteger(0)) {
                cnt++;
            }
        }
        if (cnt == 5) {
            System.out.println("Deadlock detected!");
            this.display();
            // return a random philo id
            return (int) ((Math.random() * (NUMBER - 1))); // range [0; 5[
        }
        // no deadlock
        return -1;
    }

    public void display() {
        for (int i = 0; i < NUMBER; i++) {
            for (int j = 0; j < 2; j++) {
                System.out.print(this.allocationMatrix[i][j] + " ");
            }
            System.out.println("");
        }
    }
}
