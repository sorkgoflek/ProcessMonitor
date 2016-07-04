package ProcessMonitor;

public class ProcessInfomation {
	private String processName;
	private int memory_Kb;
	private int PID;
	
	ProcessInfomation(String processName, int memory_Kb, int PID){
		this.setProcessName(processName);
		this.setMemory_Kb(memory_Kb);
		this.setPID(PID);
	}
	
	void print(){
		System.out.println(getProcessName());
		System.out.println(getMemory_Kb());
		System.out.println(getPID());
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public int getPID() {
		return PID;
	}

	public void setPID(int pID) {
		PID = pID;
	}

	public int getMemory_Kb() {
		return memory_Kb;
	}

	public void setMemory_Kb(int memory_Kb) {
		this.memory_Kb = memory_Kb;
	}
}
