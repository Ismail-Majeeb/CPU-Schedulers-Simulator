import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main {
    public static Process checkPriority(Queue<Process> processQueue, Process p) {
        Queue<Process> processes_cache = new LinkedList<>();
        Process highestPriority = new Process(p);
        while (processQueue.size() != 0) {
            Process tmp = new Process(processQueue.remove());
            processes_cache.add(tmp);
            if (tmp.priority < highestPriority.priority)
                highestPriority = tmp;
        }
        while (processes_cache.size() != 0) {
            Process tmp = new Process(processes_cache.remove());
            if (!tmp.name.equals(highestPriority.name))
                processQueue.add(tmp);
        }
        return highestPriority;
    }

    public static Process SJF(Queue<Process> processQueue, Process p) {
        Queue<Process> processes_cache = new LinkedList<>();
        Process shortestJob = new Process(p);
        while (processQueue.size() != 0) {
            Process tmp = new Process(processQueue.remove());
            processes_cache.add(tmp);
            if (tmp.burst < shortestJob.burst)
                shortestJob = tmp;
        }
        while (processes_cache.size() != 0) {
            Process tmp = new Process(processes_cache.remove());
            if (!tmp.name.equals(shortestJob.name))
                processQueue.add(tmp);
        }
        return shortestJob;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Queue<Process> processes = new LinkedList<>();
        Queue<String> processes_execution_order = new LinkedList<>();
        Queue<Integer> processes_waiting_times = new LinkedList<>();
        HashMap<String, Queue<Double>> quantum_history_processes = new HashMap<>();
        HashMap<String, Integer> waiting_times = new HashMap<>();
        HashMap<String, Integer> turnaround_times = new HashMap<>();
        System.out.print("Enter number of process : ");
        int size = input.nextInt();
        for (int i = 0; i < size; i++) {
            System.out.print("Enter name of process : ");
            String name = input.next();

            System.out.print("Enter arrival time : ");
            int arrival = input.nextInt();
            if (!processes.isEmpty() && arrival < processes.peek().arrival) {
                do {
                    System.out.println(name + "'s arrival time should be >= " + processes.peek().arrival);
                    arrival = input.nextInt();
                } while (arrival < processes.peek().arrival);
            }

            System.out.print("Enter burst ");
            int burst = input.nextInt();

            System.out.print("Enter priority ");
            int priority = input.nextInt();

            System.out.print("Enter quantum ");
            int quantum = input.nextInt();

            Process p = new Process(name, burst, arrival, priority, quantum);
            processes.add(p);
            p.quantum_history.add(p.quantum);
            quantum_history_processes.put(p.name, p.quantum_history);
            waiting_times.put(p.name, p.burst);
        }
        int time = 0;
        Process p = null;
        Queue<Process> readyQueue = new LinkedList<>();
        double remQuantumTime = 0;

        while (true) {
            if ((processes.isEmpty() && readyQueue.isEmpty()) && p == null)
                break;

            // add processes to ready queue
            while (!processes.isEmpty() && processes.peek().arrival <= time)
                readyQueue.add(processes.remove());

            // If CPU is empty, add head of the ready queue to CPU and run FCFS
            if (p == null && !readyQueue.isEmpty()) {
                // add head of the queue
                p = readyQueue.remove();
                processes_execution_order.add(p.name);
                processes_waiting_times.add(time);
                remQuantumTime = p.quantum;

                continue;
            }

            // If CPU is not empty
            else if (p != null) {
                // Current process finishes its job
                if (p.burst == 0) {
                    p.quantum = 0;
                    Queue<Double> tmp4 = quantum_history_processes.get(p.name);
                    tmp4.add(p.quantum);
                    quantum_history_processes.put(p.name, tmp4);
                    waiting_times.put(p.name, time - waiting_times.get(p.name) - p.arrival);
                    turnaround_times.put(p.name, time - p.arrival);
                    p = null;
                    continue;
                }

                // Current process consumes all its quantum time,
                // but it still has job on CPU
                if (remQuantumTime == 0) {
                    p.quantum += 2;
                    Queue<Double> tmp4 = quantum_history_processes.get(p.name);
                    tmp4.add(p.quantum);
                    quantum_history_processes.put(p.name, tmp4);
                    readyQueue.add(p);
                    p = null;
                }

                // Start of process and run 25%
                if (remQuantumTime == p.quantum) {
                    time += Math.min(Math.ceil(p.quantum / 4.0), p.burst);
                    remQuantumTime -= Math.ceil(p.quantum / 4.0);
                    p.burst -= Math.min(Math.ceil(p.quantum / 4.0), p.burst);
                    continue;
                }

                // Check on priority
                else if (remQuantumTime == p.quantum - Math.ceil(p.quantum / 4.0)) {
                    Process tmp = new Process(checkPriority(readyQueue, p));

                    // if current process has the highest priority (the lowest value)
                    // run next 25%
                    if (tmp.name.equals(p.name)) {
                        double half = Math.ceil(p.quantum / 2.0) - Math.ceil(p.quantum / 4.0);
                        time += Math.min(half, p.burst);
                        remQuantumTime -= half;
                        p.burst -= Math.min(half, p.burst);
                    }

                    // add current process to the end of the queue,
                    // and then increase its Quantum time by ceil(the remaining Quantum time/2)
                    // add process has the highest priority (the lowest value)
                    else {
                        p.quantum += Math.ceil(remQuantumTime / 2.0);
                        Queue<Double> tmp4 = quantum_history_processes.get(p.name);
                        tmp4.add(p.quantum);
                        quantum_history_processes.put(p.name, tmp4);

                        readyQueue.add(p);
                        p = new Process(tmp);
                        remQuantumTime = p.quantum;
                        processes_execution_order.add(p.name);
                        processes_waiting_times.add(time);
                    }
                    continue;
                } else if (remQuantumTime <= p.quantum - Math.ceil(p.quantum / 2.0)) {
                    Process tmp = new Process(SJF(readyQueue, p));

                    // each second check for remaining burst time
                    // let process run until 100%
                    if (tmp.name.equals(p.name)) {
                        remQuantumTime--;
                        p.burst--;
                    }

                    // (add this process to the end of the queue, and then increase its Quantum time
                    // by the remaining Quantum time).
                    // add process has the lowest remaining burst time
                    else {
                        p.quantum += remQuantumTime;
                        Queue<Double> tmp4 = quantum_history_processes.get(p.name);
                        tmp4.add(p.quantum);
                        quantum_history_processes.put(p.name, tmp4);

                        readyQueue.add(p);
                        p = new Process(tmp);
                        remQuantumTime = p.quantum;
                        processes_execution_order.add(p.name);
                        processes_waiting_times.add(time);
                        continue;
                    }
                }
            }
            time++;
        }
        while (!processes_execution_order.isEmpty()) {
            String x = processes_execution_order.remove();
            System.out.print(x + "  ");
        }
        while (!processes_waiting_times.isEmpty()) {
            int x = processes_waiting_times.remove();
            System.out.print(x + "  ");
        }
        System.out.println(time);
        System.out.println("\n............................................................................");
        System.out.println("Quantum time history for each process:");
        for (HashMap.Entry<String, Queue<Double>> a : quantum_history_processes.entrySet()) {
            System.out.print("\n" + a.getKey() + " :   ");
            while (!a.getValue().isEmpty()) {
                System.out.print(a.getValue().remove());
                if (!a.getValue().isEmpty()) {
                    System.out.print(" --> ");
                }
            }
        }
        System.out.println("\n............................................................................");
        System.out.println("\nWaiting time for each process:");
        double avgWaiting = 0;
        for (HashMap.Entry<String, Integer> a : waiting_times.entrySet()) {
            avgWaiting += a.getValue();
            System.out.println(a.getKey() + " :   " + a.getValue());

        }
        System.out.println("Average Waiting time = " + avgWaiting / waiting_times.size());
        System.out.println("............................................................................\n");
        double avgTurnaround = 0;
        for (HashMap.Entry<String, Integer> a : turnaround_times.entrySet()) {
            avgTurnaround +=  a.getValue();
            System.out.println(a.getKey() + " :   " + a.getValue());
        }
        System.out.println("Average turnaround time = " + avgTurnaround / turnaround_times.size());
    }
}