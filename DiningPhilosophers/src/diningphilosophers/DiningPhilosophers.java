/*
 * 
 */
package diningphilosophers;

import java.util.ArrayList;
import java.util.Scanner;
import philosopher.FearfulPhilosopher;
import philosopher.Philosopher;
import philosopher.Status;
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

    public static int check_cycle() {
        // applies a cycle finding algorithm on the resource graph
        // if a cycle is found, try to return the id of the philospoher to be preempted
        return 0; // temporary
    }

    public static void main(String[] args) {
        System.out.println("Which strategy should be used?\nPrevention (part 1)?"
                + " Detection (part 2)?\nEnter 1 or 2.");
        System.out.println("Note that the assignment requirements specify that philosopher"
                + " behavior is an infinite loop, so you should stop the program in a bit.");
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
                System.out.println("part 2 not ready yet.");
                p = new StubbornPhilosopher(i, eat, think);
                return; // temp return
            }
            philos.add(p);
        }
        
        philos.forEach((p) -> {
            p.start();
        });

        // initialize resource graph
        // start checking for a cycle
        if (strategy == 2) {
            while (true) {
                // i know this isn't the best we can do: checking blindly in a while true loop
                // better: launch a check whenever some new philosopher takes a fork
                // could use a global static flag for that
                
                // note: pages 306-7 of the textbook talk about a deadlock detection
                // algorithm that shows which 2 processes are deadlocked
                int preempt_idx = check_cycle();
                if (preempt_idx != -1) {
                    Philosopher preempt = philos.get(preempt_idx);
                    preempt.releaseFork("left");
                }
            }
        }

        // No join because they never stop.
    }

}
