
public class CPU {

	public Process CurrentP;
	public int Timer;

	CPU()
    {
        setTimer(0);
        this.CurrentP = new Process();
    }


    public void setCurrentP(Process currentP) {
        CurrentP = currentP;
    }

    public void setTimer(int timer) {
        Timer = timer;
    }
}
