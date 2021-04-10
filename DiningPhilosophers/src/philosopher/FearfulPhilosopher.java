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
        // The fearful philosopher checks that both forks are free
        // only then does he acquire both of them at once
        // otherwise, he keeps waiting for both to be free
        // this way, deadlock can't happen
        int flag;
        while (true) {
            if (forks[this.left_fork] == false && forks[this.right_fork] == false) {
                flag = this.occupyForks();
                if (flag == 0) return;
            }
            //System.out.println(this.toString() + " is waiting");
            // But if you print this line, it doesn't look like anyone is waiting for too long
            // so idk what's really happening
        }

        // alternative to the above: only eat if the 2 guys next to me aren't eating
        // but i noticed that this starves 1 or 2 philosophers...
//        int left_philo = (this.id - 1) % NUMBER;
//        if (left_philo < 0) { // doing this because -1 % 5 = -1 in java lol
//            left_philo += NUMBER;
//        }
//        int right_philo = (this.id + 1) % NUMBER;
//        
//        while (philos.get(left_philo).status == Status.EAT ||
//                philos.get(right_philo).status == Status.EAT) {
//            //System.out.println(this.toString() + " is waiting"); // this line makes some things clear
//        }
    }
    
}
