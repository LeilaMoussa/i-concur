
package diningphilosophers;

import static diningphilosophers.DiningPhilosophers.NUMBER;

public class ResourceGraph {

    // very simple 5X2 table because each philosopher is only concerned
    // with 2 specific forks (left & right), not the rest
    // and all forks are distinct (fixed position) ==> single-instance reusable resources
    // no need for sophisticated algos or DS's
    
    //public volatile int[][] allocationMatrix; 
    public VolatileBoolean [][] allocationMatrix;

    public ResourceGraph() {
        //this.allocationMatrix = new int[NUMBER][2]; // 0s by default
        this.allocationMatrix = new VolatileBoolean[NUMBER][2];
        
        for (int i = 0; i < NUMBER; i++) {
            for (int j = 0; j < 2; j++) {
                this.allocationMatrix[i][j] = new VolatileBoolean(false);
            }
        }
    }

    public int detectCycle() {
        // deadlock means everyone has their respective left fork and is waiting for the right
        // so all rows have the value (1, 0)
        int cnt = 0;
        for (int i = 0; i < NUMBER; i++) {
            if (this.allocationMatrix[i][0].value == true
                    && this.allocationMatrix[i][1].value == false) {
                cnt++;
            }
        }
        if (cnt == 5) {
            System.out.println("Deadlock detected!");
            // this.display();
            return (int) ((Math.random() * (NUMBER - 1)));
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
