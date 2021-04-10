package philosopher;

public class StubbornPhilosopher extends Philosopher {

    public StubbornPhilosopher(int id, int thinking_time, int eating_time) {
        super(id, thinking_time, eating_time);
    }

    @Override
    public void acquireForks() {
        // The stubborn philosopher only checks for the left fork
        // if it's free, take it
        // then check for the right
        // wait for it to be free
        while (!this.isForkFree("left")) {
            // wait
        }
        this.occupyFork("left");
        
        while (!this.isForkFree("right")) {
            // wait
        }
        this.occupyFork("right");
    }
    
    public void releaseLeftFork () {
        // if preempted by the main thread, release left only
        // think of resource graph
    }
}
