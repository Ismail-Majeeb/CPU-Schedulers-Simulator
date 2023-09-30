import java.util.LinkedList;
import java.util.Queue;

public class Process {
    public String name;
    public int burst;
    public int arrival;
    public int priority;
    public double quantum;


    public Queue<Double> quantum_history = new LinkedList<>();
    public Process(String name, int burst, int arrival, int priority, int quantum) {
        this.name = name;
        this.burst = burst;
        this.arrival = arrival;
        this.priority = priority;
        this.quantum = quantum;
    }

    public Process(Process p) {
        this.name = p.name;
        this.burst = p.burst;
        this.arrival = p.arrival;
        this.priority = p.priority;
        this.quantum = p.quantum;
    }
}