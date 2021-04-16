package diningphilosophers;

import static diningphilosophers.DiningPhilosophers.NUMBER;

public class ResourceGraph {

    /* Very simple 5X2 table because each philosopher is only concerned
     * with 2 specific forks (left & right), not the rest
     * and all forks are distinct (fixed position) ==> single-instance reusable resources
     */
    public VolatileBoolean[][] allocationMatrix;

    public ResourceGraph() {
        this.allocationMatrix = new VolatileBoolean[NUMBER][2];

        for (int i = 0; i < NUMBER; i++) {
            for (int j = 0; j < 2; j++) {
                /* All forks are free at first. */
                this.allocationMatrix[i][j] = new VolatileBoolean(false);
            }
        }
    }

    public int detectCycle() {
        /* Deadlock means everyone has their respective left fork and is waiting for the right
         * so all rows have the value (true, false) */
        int cnt = 0;
        for (int i = 0; i < NUMBER; i++) {
            if (this.allocationMatrix[i][0].value == true
                    && this.allocationMatrix[i][1].value == false) {
                cnt++;
            }
        }
        if (cnt == 5) {
            System.out.println("Deadlock detected!");
            return (int) ((Math.random() * (NUMBER - 1)));
        }
        /* otherwise, no deadlock (not all rows are [T, F]) */
        return -1;
    }

    public void display() {
        /* This method is useful for debugging. */
        for (int i = 0; i < NUMBER; i++) {
            for (int j = 0; j < 2; j++) {
                System.out.print(this.allocationMatrix[i][j] + " ");
            }
            System.out.println("");
        }
    }
}
