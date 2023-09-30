
public class Process {

	public String name;
	public int Arrival;
	public int Burst;
	public int Completion;
	public int Turnaround;
	public int Waited;
	public int Priority;
	public int starving;

	Process()
    {
        name = "";
        Waited = 0;
        Turnaround = 0;

        Arrival = -1;
        Burst = -1;
        Completion = -1;
        starving = 0;
    }


    public void setTurnaround() {
        Turnaround = Completion - Arrival;
    }
}
