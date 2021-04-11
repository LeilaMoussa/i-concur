package philosopher;

import static diningphilosophers.DiningPhilosophers.forks;
import static diningphilosophers.DiningPhilosophers.alloc_flag;
import static diningphilosophers.DiningPhilosophers.rag;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class StubbornPhilosopher extends Philosopher {

    public StubbornPhilosopher(int id, int thinking_time, int eating_time) {
        super(id, thinking_time, eating_time);
    }

    @Override
    public void acquireForks() {
        while (forks[this.left_fork] == new AtomicBoolean(true)) {
            // while occupied, wait
        }
        forks[this.left_fork] = new AtomicBoolean(true); // occupy
        rag.allocationMatrix[this.id][0] = new AtomicInteger(1);
        //rag.allocationMatrix = rag.allocationMatrix;

        // set global flag to launch detection
        alloc_flag = 1;

        while (forks[this.right_fork] == new AtomicBoolean(true)) {
            // wait
        }
        forks[this.right_fork] = new AtomicBoolean(true);
        rag.allocationMatrix[this.id][1] = new AtomicInteger(1);
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

        if (forks[fork_idx] == new AtomicBoolean(false)) {
            System.err.println("ERROR. " + fork + " fork already released, in " + this.toString());
        } else {
            forks[fork_idx] = new AtomicBoolean(false);
        }
        rag.allocationMatrix[this.id][graph_idx] = new AtomicInteger(0); // probably should do the same kind of checking
        // here and in other places
        //rag.allocationMatrix = rag.allocationMatrix;
    }
}
