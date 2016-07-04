package ProcessMonitor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProcessMonitor extends Thread{
	public enum Status {PLAY, PAUSE, STOP;}
	
	private static final String LogDirectoryPath = "c:\\Process Monitor\\";

	private Status status;
	private String targetProcessName;
	private int targetPID;
	private int beforeMemory_Kb;
	private int minGap;
	
	public void setStop() {
		this.status = Status.STOP;
	}
	
	public void setPlay() {
		this.status = Status.PLAY;
	}
	
	public void setPause() {
		this.status = Status.PAUSE;
	}

	public String getTargetProcessName() {
		return targetProcessName;
	}

	public void setTargetProcessName(String targetProcessName) {
		if(targetProcessName.trim().length() != 0){
			this.targetProcessName = targetProcessName;
		}
	}

	public int getTargetPID() {
		return targetPID;
	}

	public void setTargetPID(int targetPID) {
		this.targetPID = targetPID;
	}

	public void setTargetPID(String targetPID) {
		if(targetPID.trim().length() != 0){
			this.targetPID = Integer.parseInt(targetPID);
		}
	}

	public int getMinGap() {
		return minGap;
	}

	public void setMinGap(int minGap) {
		this.minGap = (minGap < 0) ? -minGap : minGap;
	}
	
	public void setMinGap(String minGap) {
		if(minGap.trim().length() != 0){
			int n = 0;
			
			try{
				n = Integer.parseInt(minGap);
			}
			catch(Exception e){
			}
			finally{
				setMinGap(n);
			}
		}
	}

	public ProcessMonitor(){
		targetProcessName = null;
		targetPID = -1;
		status = Status.PAUSE;
		beforeMemory_Kb = 0;
		minGap = 0;
	}
	
	public ProcessMonitor(String targetProcessName, int targetPID){
		this();
		setTargetProcessName(targetProcessName);
		setTargetPID(targetPID);
	}
	
	public void run(){
		status = Status.PLAY;
		
		while (status != Status.STOP){
			switch(status){
			case PLAY:
				String task = catchTask();
				
				if(task != null){
					String[] data = parseInfo(task);
					ProcessInfomation p = getProcessInfomation(data);
					
					//p.print();
					
					writeLogFile(p);
				}
				else{
					//System.out.println("TARGET process is NOT RUNNING.");
					status = Status.PAUSE;
				}
				
				break;
				
			case PAUSE:
				break;
			default:
				break;
			}
			
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("PM done");
	}
	
	public String catchTask(){
		String line = null;
		boolean isRunning = false;
		
		//모니터링
		try {
		    Process p = Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe");
		    //Process p = Runtime.getRuntime().exec("tasklist /v"); //위와 결과가 다름.
		    BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		    while ((line = input.readLine()) != null) {
		    	//System.out.println(line);
		    	
		    	if(targetProcessName != null && targetPID != -1){
		    		if(line.contains(targetProcessName) && line.contains(String.valueOf(targetPID))){
			    		isRunning = true;
			    		break;
			    	}
		    	}
		    	else if(targetProcessName != null && targetPID == -1){
		    		if(line.contains(targetProcessName)){
			    		isRunning = true;
			    		break;
			    	}
		    	}
		    	else if(targetProcessName == null && targetPID != -1){
		    		if(line.contains(String.valueOf(targetPID))){
			    		isRunning = true;
			    		break;
			    	}
		    	}
		    }
		    
		    input.close();
		} catch (Exception err) {
		    err.printStackTrace();
		}
		
		if(!isRunning){
			line = null;
		}
		
		return line;
	}

	String[] parseInfo(String task){
		String[] token = task.split(" ");
		String[] availableData = new String[6];
		int idx = 0;
		
		for(String s : token){

			if(s.length() != 0 && idx < 6){
				availableData[idx++] = s;
			}
		}
		
		return availableData;
	}

	ProcessInfomation getProcessInfomation(String[] data){
		String[] str = data[4].split(",");
		for(int i=1; i < str.length; i++){
		str[0]+=str[i];
		}
		
		return new ProcessInfomation(data[0], Integer.parseInt(str[0]), Integer.parseInt(data[1]));
	}

	String makeLog(ProcessInfomation p, int changeAmount){
		String time = new SimpleDateFormat ("HH:mm:ss").format(new Date());
		
        return String.format("[%s] %s\tPID: %5d\tMemory(Kb): %12d (%+7dKb) \r\n", time, p.getProcessName(), p.getPID(), p.getMemory_Kb(), changeAmount); 
	}
	
	void writeLogFile(ProcessInfomation p){
		String date = new SimpleDateFormat ("yyyyMMdd").format(new Date());
		String LogFilePath  = LogDirectoryPath + date + ".log";
		
		if(getUnsignedGap(p.getMemory_Kb(), beforeMemory_Kb) < minGap){
			return;
		}
		
        String log = makeLog(p, p.getMemory_Kb()-beforeMemory_Kb);
        beforeMemory_Kb = p.getMemory_Kb();
        FileWriter fw = null;
        
        String dir = LogDirectoryPath;
		new File(dir).mkdirs();
        
        try{
        	fw = new FileWriter(LogFilePath, true);
        	fw.write(log);
        	fw.write("\r\n");
        }catch(IOException e){
        	e.printStackTrace();
        }
        finally
        {
        	try{
        		if(fw != null) fw.close();
        	}catch(Exception e1){}
        }
	}
	
	int getUnsignedGap(int a, int b){
		return (a-b)>0 ? (a-b): -(a-b);
	}
	
}
