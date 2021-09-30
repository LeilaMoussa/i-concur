/*
2 threads work to sum up a variable to 100 using the first mutual exclusion
algorithm developed.

Small bug to be addressed: in some runs, x sums up to 101. Volatility problem?
 */
package dekker;

public class Dekker {

    volatile static int x = 0;

    volatile static int favored_thread;
    static boolean[] intention = new boolean[2];

    public static class MyThread extends Thread {

        public int id;

        public MyThread(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            while (x < 100) {
                intention[id - 1] = true;
                while (intention[id % 2]) {
                    if (favored_thread == id % 2 + 1) {
                        intention[id - 1] = false;
                        while (favored_thread == id % 2 + 1) {
                        }
                        intention[id - 1] = true;
                    }
                }
                // Critical section
                System.out.println("Thread " + this.id + " incrementing.");
                x += 1;

                favored_thread = id % 2 + 1;
                intention[id - 1] = false;
            }

        }
    }

    public static void main(String[] args) throws InterruptedException {
        favored_thread = 1;

        MyThread thread1 = new MyThread(1);
        MyThread thread2 = new MyThread(2);

        thread1.start();
        thread2.start();

        thread1.join();
        thread1.join();

        System.out.println(x);
    }
}
