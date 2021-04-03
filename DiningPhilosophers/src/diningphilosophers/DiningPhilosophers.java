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
    public static final int MAX_EATING_TIME = 5;
    public static final int MAX_THINKING_TIME = 7;
    public static final int MIN_TIME = 2; // just arbitrary choices

    public static boolean[] forks = {false, false, false, false, false}; // bad
    // this is a tentative data structure. In fact, i will need to represent a resource graph
    // with a more sophisticated data structure (thinking 2d array, but we'll see)
    
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
                p = new StubbornPhilosopher(i, eat, think);
            }
            philos.add(p);
        }
        
        // initialize resource graph
        // start checking for a cycle
        if (strategy == 2) {
            while (true) {
                int preempt_idx = check_cycle();
                if (preempt_idx != -1) {
                    Philosopher preempt = philos.get(preempt_idx);
                    preempt.releaseFork("left");
                }
            }
        }
        
        philos.forEach((p) -> {
            p.start();
        });
        
        // No join because they never stop.
        
    }

}
