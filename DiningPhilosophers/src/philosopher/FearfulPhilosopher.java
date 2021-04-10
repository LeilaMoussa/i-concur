package philosopher;

import static diningphilosophers.DiningPhilosophers.NUMBER;
import static diningphilosophers.DiningPhilosophers.philos;

public class FearfulPhilosopher extends Philosopher {
    
    public FearfulPhilosopher (int id, int thinking_time, int eating_time) {
        super(id, thinking_time, eating_time);
    }
    
    @Override
    public void acquireForks() {
        // The fearful philosopher checks that both forks are free
        // only then does he acquire both of them at once
        // otherwise, he keeps waiting for both to be free
        // this way, deadlock can't happen
//        while (!this.isForkFree("left") && !this.isForkFree("right")) {
//            // nothing, just wait
//        }
//        // not good, need to make these 2 operations atomic somehow
//        // need a semaphore responsible for these 2 forks
          // first paragraph on page 313 talks about exactly this problem and solves it somehow
//        this.occupyFork("left");
//        this.occupyFork("right");

        // alternative to the above: only eat if the 2 guys next to me aren't eating
        // but i noticed that this starves 1 or 2 philosophers...
        int left_philo = (this.id - 1) % NUMBER;
        if (left_philo < 0) { // doing this because -1 % 5 = -1 in java lol
            left_philo += NUMBER;
        }
        int right_philo = (this.id + 1) % NUMBER;
        
        while (philos.get(left_philo).status == Status.EAT ||
                philos.get(right_philo).status == Status.EAT) {
            //System.out.println(this.toString() + " is waiting"); // this line makes some things clear
        }
        this.status = Status.EAT; // superfluous?
        System.out.println(this.toString() + " restarted eating.");
    }
    
}
