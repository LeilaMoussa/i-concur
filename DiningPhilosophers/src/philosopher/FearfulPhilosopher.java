package philosopher;

import static diningphilosophers.DiningPhilosophers.NUMBER;
import static diningphilosophers.DiningPhilosophers.forks;
import static diningphilosophers.DiningPhilosophers.philos;

public class FearfulPhilosopher extends Philosopher {
    
    public FearfulPhilosopher (int id, int thinking_time, int eating_time) {
        super(id, thinking_time, eating_time);
    }
    
    public synchronized int occupyForks() {
        // synchronized looks like it solves the problem of racing for forks
        if (forks[this.left_fork] || forks[this.right_fork]) return 1;
        // this is just an extra safety cushion
        forks[this.left_fork] = true;
        forks[this.right_fork] = true;
        return 0;
    }
    
    @Override
    public void acquireForks() {
        int flag;
        while (true) {
            if (forks[this.left_fork] == false && forks[this.right_fork] == false) {
                flag = this.occupyForks();
                if (flag == 0) return;
            }
        }
    }
    
}
