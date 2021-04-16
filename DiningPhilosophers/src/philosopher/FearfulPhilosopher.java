package philosopher;

import static diningphilosophers.DiningPhilosophers.forks;

public class FearfulPhilosopher extends Philosopher {

    public FearfulPhilosopher(int id, int thinking_time, int eating_time) {
        super(id, thinking_time, eating_time);
    }

    public synchronized int occupyForks() {
        // synchronized == mutual exclusion
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
        while (true) {
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
