This assignment simulates the behavior of the five philosophers, following
2 scenarios for handling deadlocks:
	- Part 1: Deadlock prevention
Deadlock cannot happen here, because the Philosopher implementation used
is `FearfulPhilosopher`, meaning every philosopher waits until both forks are
free, then picks up both of them at the same time. The atomicity of the 
action of occupying both forks at once is crucial, and is guaranteed by the
`synchronized` keyword.

	- Part 2: Deadlock detection
Deadlock is very likely to happen here because the Philosopher implementation
used here is `StubbornPhilosopher`, meaning every philosopher is only concerned
initially with the left fork. Once it's free, he occupies it then waits for the
right fork. There is a small delay between occupying them, leading to the
possibility that all 5 philosophers at some point each have their left forks
and are waiting for each other circularly.
	In that case, we have a very simply resource allocation graph,
where the rows correspond to the 5 philosophers and the columsn correspond to
"Left fork" & "Right fork", as each philosopher is only concerned with his
own respective left & right forks. If all rows are marked ["allocated", "not
allocated"], i.e. [true, false], that means deadlock has occured. One philosopher
is preempted at random.
	The deadlock detection method is executed by the main thread whenever
a new left fork is allocated to a philosopher, using the static volatile variable
`alloc_flag`.

In both parts, philosphers and forks are numbered 0 to 4, such that:
Philosopher 0 needs forks 0 and 4
Philosopher 1 needs forks 1 and 2
Philosopher 2 needs forks 2 and 3
Philosopher 3 needs forks 3 and 4
Philosopher 4 needs forks 4 and 0

Philosopher behavior is an infinite loop, that starts with `think()`.
Because `FearfulPhilosopher` and `StubbornPhilosopher` behave similarly in 
many ways, we decided to squash the shared behavior in an abstract
`Philosopher` class.

And a boolean array `forks` is used in both parts to keep track of whether a 
particular fork is free (false) or occupied (true).

---- A note about volatility:
Because changes happen so fast, and most operations are dependent on the previous
ones, we cannot allow for default behavior at the level of the Java Memory Model,
consisting usually of delaying writes and allowing for memory inconsistencies.
Hence the need for the `volatile` keyword. However, this is only works for
primitives, and to have a volatile array (an array of volatile elements),
we needed to define a class for a volatile boolean that can be used for `forks`
and `allocationMatrix`.

Students:
Leila Farah Moussa
Ouissal Moumou
