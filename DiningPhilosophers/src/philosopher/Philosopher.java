package philosopher;

import static diningphilosophers.DiningPhilosophers.NUMBER;

public abstract class Philosopher extends Thread {

    public final int id;
    public Status status;
    private final int thinking_time;
    private final int eating_time;
    public final int left_fork;
    public final int right_fork;

    public Philosopher(int id, int thinking_time, int eating_time) {
        this.id = id;
        this.thinking_time = thinking_time;
        this.eating_time = eating_time;
        this.left_fork = this.id;
        this.right_fork = (this.id + 1) % NUMBER;
    }

    public void eat() {
        this.status = Status.EAT;
        System.out.println(this.toString() + " started eating.");
        this.timeOut();
    }

    public void think() {
        this.status = Status.THINK;
        System.out.println(this.toString() + " started thinking.");
        this.timeOut();
    }

    public void timeOut() {
        /* Because Thread.sleep() takes time in ms, need to multiply eat/sleep
         * time by 1000. */
        int sleep_time = 1000;
        if (this.status == Status.EAT) {
            sleep_time *= this.eating_time;
        } else {
            sleep_time *= this.thinking_time;
        }

        try {
            Thread.sleep(sleep_time);
        } catch (InterruptedException ie) {
            System.err.println(ie);
        }
    }

    /* Fearful and Stubborn philosopher behave differently
     * when acquiring or releasing forks, so there are 2 slightly different
     * implementations. */
    public abstract void acquireForks();

    public abstract void releaseFork(String fork);

    @Override
    public void run() {
        /* All threads follow the same pattern of an infinite loops of
         * thinking & eating. */
        while (true) {
            this.think();
            this.acquireForks();
            this.eat();
            this.releaseFork("left");
            this.releaseFork("right");
        }
    }

    @Override
    public String toString() {
        return "Philosopher " + this.id + " with status " + this.status
                + " (Left fork: " + this.left_fork + ", right fork: " + this.right_fork + ")";
    }
}
