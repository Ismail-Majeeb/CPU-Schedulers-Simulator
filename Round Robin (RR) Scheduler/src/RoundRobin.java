import java.util.Scanner;
public class RoundRobin {
  public static void main(String args[]) {

    int temporary, aq = 0;
    int n, i, ok, count = 0;
    int bt[], wt[], tation[], Rbt[];
    float awt = 0, atation = 0;

    Rbt = new int[15];
    bt = new int[15];
    tation = new int[15];
    wt = new int[15];

    Scanner s = new Scanner(System.in);
    System.out.print("[max: 10] Enter the No. of processes = ");
    n = s.nextInt();
    System.out.print("Enter Burst time for each proccess\n");
    
    for (i = 0; i < n; i++) {
      System.out.print("P" + i + ": ");
      bt[i] = s.nextInt();
      Rbt[i] = bt[i];
    }

    System.out.print("Quantum time: ");
    ok = s.nextInt();

    while (true) {

      for (count = 0, i = 0; i < n; i++) {
        temporary = ok;

        if (Rbt[i] == 0) {
          count++;
          continue;
        }

        if (Rbt[i] > ok)
          Rbt[i] = Rbt[i] - ok;
        else

        if (Rbt[i] >= 0) {
          temporary = Rbt[i];
          Rbt[i] = 0;
        }
        aq = aq + temporary;
        tation[i] = aq;
      }

      if (n == count)
        break;
    }
    
    
    System.out.print("\nProcess\t      Burst Time\t       Time in system\t          \tWaiting Time\n");
    for (i = 0; i < n; i++) {
      wt[i] = tation[i] - bt[i];
      awt = awt + wt[i];
      atation = atation + tation[i];
      System.out.print("\n P" + (i + 1) + "\t " + bt[i] + "\t\t " + tation[i] + "\t\t " + wt[i] + "\n");
    }

    atation = atation / n;
    awt = awt / n;
    System.out.println("\n\nAverage turnaround time= " + atation + "\nAverage waiting Time= " + awt + "\n");
  }
}