package diningphilosophers;

/* This class is needed to allow for arrays of volatile elements.
 * It's used for the forks array and the allocationMatrix. */

public class VolatileBoolean {
    public volatile boolean value;
    
    public VolatileBoolean(boolean val) {
        this.value = val;
    }

}
