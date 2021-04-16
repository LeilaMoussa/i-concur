package philosopher;

/* This is for part 2: deadlock detection. */

import static diningphilosophers.DiningPhilosophers.forks;
import static diningphilosophers.DiningPhilosophers.alloc_flag;
import static diningphilosophers.DiningPhilosophers.rag;

public class StubbornPhilosopher extends Philosopher {

    public StubbornPhilosopher(int id, int thinking_time, int eating_time) {
        super(id, thinking_time, eating_time);
    }

    @Override
    public void acquireForks() {

        while (forks[this.left_fork].value) {
        }
        forks[this.left_fork].value = true;
        rag.allocationMatrix[this.id][0].value = true;

        /* Set global flag to launch detection */
        alloc_flag = 1;

        /* Small delay of 2s, the bigger the delay the higher the chance for deadlock */
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ie) {
            // Not really important.
        }

        while (forks[this.right_fork].value) {
        }
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
        
        forks[fork_idx].value = false;
        rag.allocationMatrix[this.id][graph_idx].value = false;
    }
}
