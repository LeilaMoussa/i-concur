package philosopher;

import static diningphilosophers.DiningPhilosophers.forks;
import static diningphilosophers.DiningPhilosophers.NUMBER;

public abstract class Philosopher extends Thread {

    public final int id;
    public Status status;
    private final int thinking_time;
    private final int eating_time;
    private final int left_fork;
    private final int right_fork;

    public Philosopher(int id, int thinking_time, int eating_time) {
        this.id = id;
        this.thinking_time = thinking_time;
        this.eating_time = eating_time;
        this.left_fork = this.id;
        this.right_fork = (this.id + 1) % NUMBER;
    }

    public void eat() {
        this.status = Status.EAT;
        this.timeOut();
    }

    public void think() {
        this.status = Status.THINK;
        this.timeOut();
    }

    public void timeOut() {
        int sleep_time;
        if (this.status == Status.EAT) {
            sleep_time = this.eating_time;
        } else {
            sleep_time = this.thinking_time;
        }

        try {
            Thread.sleep(sleep_time);
        } catch (InterruptedException ie) {
            System.err.println(ie);
        }
    }
    
    public boolean isForkFree(String fork) {
        if (fork.equals("left")) {
            return !forks[this.left_fork];
        } else {
            return !forks[this.right_fork];
        }
    }
    
    public void occupyFork(String fork) {
        int idx;
        if (fork.equals("left")) {
            idx = this.left_fork;
        } else {
            idx = this.right_fork;
        }
        
        if(forks[idx] == true) {
            System.err.println("ERROR." + fork + " fork already occupied, " + this.toString());
        } else {
            forks[idx] = true;
            System.out.println(this.toString() + " occupied fork.");
        }
    }

    public abstract void acquireForks();

    public void releaseFork(String fork) {
        // need to check that it's true first
        // otherwise, error
        // this function is similar to occupy, may need to squash them
        // something like toggleFork, maybe
        int idx;
        if (fork.equals("left")) {
            idx = this.left_fork;
        } else {
            idx = this.right_fork;
        }
        
        if(forks[idx] == false) {
            System.err.println("ERROR." + fork + " fork already released.");
        } else {
            forks[idx] = false;
        }
    }

    @Override
    public void run() {
        System.out.println(this.toString() + " just started eating.");
        while (true) {
            this.think();
            this.acquireForks();
            this.eat(); // Critical section.
            this.releaseFork("left");
            this.releaseFork("right");
        }
    }
    
    @Override
    public String toString() {
        return "Philosopher " + this.id + " with status " + this.status +
                " (Left: " + this.left_fork + ", right: " + this.right_fork + ").";
    }
}
