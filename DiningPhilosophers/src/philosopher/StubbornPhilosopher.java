package philosopher;

import static diningphilosophers.DiningPhilosophers.forks;
import static diningphilosophers.DiningPhilosophers.alloc_flag;
import static diningphilosophers.DiningPhilosophers.rag;

public class StubbornPhilosopher extends Philosopher {

    public StubbornPhilosopher(int id, int thinking_time, int eating_time) {
        super(id, thinking_time, eating_time);
    }

    @Override
    public void acquireForks() {
        // should check before setting?
        
        while (forks[this.left_fork].value) { }
        forks[this.left_fork].value = true; // occupy
        rag.allocationMatrix[this.id][0].value = true;

        // set global flag to launch detection
        alloc_flag = 1;

        // small delay, the bigger the delay the higher the chance for deadlock
        try {
            Thread.sleep(2100);
        } catch (InterruptedException ie) {
            // Not really important.
        }
        
        while (forks[this.right_fork].value) { }
        forks[this.right_fork].value = true;
        rag.allocationMatrix[this.id][1].value = true;
    }

    @Override
    public void releaseFork(String fork) {
        int fork_idx;
        int graph_idx;
        if (fork.equals("left")) {
            fork_idx = this.left_fork;
            graph_idx = 0;
        } else {
            fork_idx = this.right_fork;
            graph_idx = 1;
        }

        if (forks[fork_idx].value == false 
                || rag.allocationMatrix[this.id][graph_idx].value == false) { // shouldn't happen
            System.err.println("ERROR. " + fork + " fork already released, in " + this.toString());
        } else {
            forks[fork_idx].value = false;
            rag.allocationMatrix[this.id][graph_idx].value = false;
            //rag.allocationMatrix = rag.allocationMatrix;
        }
    }
}
