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
        while (forks[this.left_fork]) {
            // while occupied, wait
        }
        forks[this.left_fork] = true; // occupy
        rag.allocationMatrix[this.id][0] = 1;
        //rag.allocationMatrix = rag.allocationMatrix;

        // set global flag to launch detection
        alloc_flag = 1;

        while (forks[this.right_fork]) {
            // wait
        }
        forks[this.right_fork] = true;
        rag.allocationMatrix[this.id][1] = 1;
        //rag.allocationMatrix = rag.allocationMatrix;
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

        if (forks[fork_idx] == false) {
            System.err.println("ERROR. " + fork + " fork already released, in " + this.toString());
        } else {
            forks[fork_idx] = false;
        }
        rag.allocationMatrix[this.id][graph_idx] = 0; // probably should do the same kind of checking
        // here and in other places
        //rag.allocationMatrix = rag.allocationMatrix;
    }
}
