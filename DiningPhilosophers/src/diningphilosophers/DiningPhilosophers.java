/*
 * 
 */
package diningphilosophers;

import java.util.ArrayList;
import java.util.Scanner;
import philosopher.FearfulPhilosopher;
import philosopher.Philosopher;
import philosopher.StubbornPhilosopher;

public class DiningPhilosophers {

    public static final int NUMBER = 5;
    public static final int MAX_EATING_TIME = 3;
    public static final int MAX_THINKING_TIME = 2;
    public static final int MIN_TIME = 1; // just arbitrary choices

    public volatile static boolean[] forks = {false, false, false, false, false};
    // volatile keyword is important! somehow without it, starvation happens (or seems to)
    // I think it's fine to keep just a boolean array for part 1, it seems to work decently
    // and the textbook has a similar solution
    // part 2 will have a completely different set of DS's

    public static ArrayList<Philosopher> philos = new ArrayList<>(NUMBER);

    public static volatile int alloc_flag = 0; // flags to the main thread whether to launch cycle detection
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

        philos.forEach((p) -> {
            p.start();
        });

        if (strategy == 2) {
            rag = new ResourceGraph();
            while (true) {
                if (alloc_flag == 1) {
                    int preempt_idx = rag.detectCycle();
                    if (preempt_idx != -1) {
                        Philosopher preempt = philos.get(preempt_idx);
                        preempt.releaseFork("left");
                        System.out.println(preempt.toString() + " preempted.");
                    }
                }
            }
        }

        // No join because they never stop.
    }

}
