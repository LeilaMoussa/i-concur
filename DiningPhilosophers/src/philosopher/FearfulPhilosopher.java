package philosopher;

/* This is for part 1: deadlock prevention. */

import static diningphilosophers.DiningPhilosophers.forks;

public class FearfulPhilosopher extends Philosopher {

    public FearfulPhilosopher(int id, int thinking_time, int eating_time) {
        super(id, thinking_time, eating_time);
    }

    public synchronized int occupyForks() {
        /* synchronized keyword guarantees that only one thread executes this function
         * at any given moment, this is important because occupying needs to be
         * atomic to prevent deadlock. */
        
        /* This check is important because while one thread is executing this function,
         * another may be waiting to do the same, but shouldn't be allowed
         * to effectively occupy the forks since the other guy has them. */
        if (forks[this.left_fork].value || forks[this.right_fork].value) {
            return 1;
        }
        forks[this.left_fork].value = true;
        forks[this.right_fork].value = true;
        return 0;
    }

    @Override
    public void acquireForks() {
        int flag;
        /* This is not meant to be an infinite loop, it just allows
         * for a thread to try to occupy the forks again if it failed. */
        while (true) {
            /* Both forks need to be free. */
            if (!forks[this.left_fork].value && !forks[this.right_fork].value) {
                flag = this.occupyForks();
                if (flag == 0) {
                    return;
                }
            }
        }
    }

    @Override
    public void releaseFork(String fork) {
        int idx;
        if (fork.equals("left")) {
            idx = this.left_fork;
        } else {
            idx = this.right_fork;
        }

        if (!forks[idx].value) {
            System.err.println("ERROR. " + fork + " fork already released, in " + this.toString());
        } else {
            forks[idx].value = false;
        }
    }

}
