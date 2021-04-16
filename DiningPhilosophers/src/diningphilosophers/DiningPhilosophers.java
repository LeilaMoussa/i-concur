package diningphilosophers;

import java.util.ArrayList;
import java.util.Scanner;
import philosopher.FearfulPhilosopher;
import philosopher.Philosopher;
import philosopher.StubbornPhilosopher;

public class DiningPhilosophers {

    /* 5 philosophers and 5 forks, as stated by the assignment. */
    public static final int NUMBER = 5;
    /* These are just arbitary choices for min and max times. */
    public static final int MAX_EATING_TIME = 4;
    public static final int MAX_THINKING_TIME = 3;
    public static final int MIN_TIME = 1;

    /* An array of volatile boolean elements that show fork availability. */
    public static VolatileBoolean[] forks = new VolatileBoolean[NUMBER];

    /* The list of Philosophers to be created. */
    public static ArrayList<Philosopher> philos = new ArrayList<>(NUMBER);

    /* This global variable flags to the main thread whether to launch the 
     * cycle detection method. */
    public static volatile int alloc_flag = 0;
    /* Resource Allocation Graph */
    public static ResourceGraph rag;

    public static void main(String[] args) {
        System.out.println("Which strategy should be used?\nPrevention (part 1)?"
                + " Detection (part 2)?\nEnter 1 or 2.");
        System.out.println("Note that the assignment requirements specify that philosopher"
                + " behavior is an infinite loop,\nso you should "
                + "stop the program yourself in a bit.");
        Scanner in = new Scanner(System.in);
        int strategy = in.nextInt();
        int eat, think;
        for (int i = 0; i < NUMBER; i++) {
            Philosopher p = null;
            eat = (int) (Math.random() * (MAX_EATING_TIME - MIN_TIME + 1) + MIN_TIME);
            think = (int) (Math.random() * (MAX_THINKING_TIME - MIN_TIME + 1) + MIN_TIME);
            if (strategy == 1) {
                p = new FearfulPhilosopher(i, eat, think);
            } else if (strategy == 2) {
                p = new StubbornPhilosopher(i, eat, think);
            }
            philos.add(p);
        }

        /* Initialize the forks with the volatile boolean class we defined. */
        for (int i = 0; i < NUMBER; i++) {
            forks[i] = new VolatileBoolean(false);
        }

        /* Spawn the 5 threads. */
        philos.forEach((p) -> {
            p.start();
        });

        /* This is where the main thread launches the deadlock check
        * in part 2 (deadlock detection). */
        if (strategy == 2) {
            rag = new ResourceGraph();
            while (true) {
                /* Only check if a left fork has been allocated. Just a matter
                 * of efficiency. */
                if (alloc_flag == 1) {
                    /* Reset the flag once checking has started. */
                    alloc_flag = 0;
                    /* The philosopher ID to be preempted. */
                    int preempt_idx = rag.detectCycle();
                    /* No deadlock has occured if the index returned is invalid. */
                    if (preempt_idx != -1) {
                        Philosopher preempt = philos.get(preempt_idx);
                        preempt.releaseFork("left");
                        System.out.println(preempt.toString() + " preempted.");
                    }
                }
            }
        }
        // No Thread.join() because they never stop.
    }

}
