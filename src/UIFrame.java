import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import ProcessMonitor.ProcessMonitor;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

@SuppressWarnings("serial")
public class UIFrame extends JFrame{
	enum Status {PLAY, PAUSE, STOP;}
	
	private JTextField processName_textField;
	private JTextField PID_textField;
	private JTextField minGap_textField;
	private JLabel msgLabel;
	
	ProcessMonitor pm = null;
	UIThread ui;
	
	public UIFrame() {
		setBounds(200, 210, 210, 179);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				ui.status = Status.STOP;
				if(pm != null) pm.setStop();
				release();
			}
		});
		
		setTitle("PM");
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{97, 97, 0};
		gridBagLayout.rowHeights = new int[]{28, 28, 28, 28, 28, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JLabel lblProcessName = new JLabel("Process Name");
		GridBagConstraints gbc_lblProcessName = new GridBagConstraints();
		gbc_lblProcessName.fill = GridBagConstraints.BOTH;
		gbc_lblProcessName.insets = new Insets(0, 5, 5, 5);
		gbc_lblProcessName.gridx = 0;
		gbc_lblProcessName.gridy = 0;
		getContentPane().add(lblProcessName, gbc_lblProcessName);
		
		processName_textField = new JTextField();
		processName_textField.setText("Test.exe");
		GridBagConstraints gbc_processName_textField = new GridBagConstraints();
		gbc_processName_textField.fill = GridBagConstraints.BOTH;
		gbc_processName_textField.insets = new Insets(0, 0, 5, 0);
		gbc_processName_textField.gridx = 1;
		gbc_processName_textField.gridy = 0;
		getContentPane().add(processName_textField, gbc_processName_textField);
		processName_textField.setColumns(10);
		
		JLabel lblPid = new JLabel("PID");
		GridBagConstraints gbc_lblPid = new GridBagConstraints();
		gbc_lblPid.anchor = GridBagConstraints.WEST;
		gbc_lblPid.fill = GridBagConstraints.VERTICAL;
		gbc_lblPid.insets = new Insets(0, 5, 5, 5);
		gbc_lblPid.gridx = 0;
		gbc_lblPid.gridy = 1;
		getContentPane().add(lblPid, gbc_lblPid);
		
		PID_textField = new JTextField();
		GridBagConstraints gbc_PID_textField = new GridBagConstraints();
		gbc_PID_textField.fill = GridBagConstraints.BOTH;
		gbc_PID_textField.insets = new Insets(0, 0, 5, 0);
		gbc_PID_textField.gridx = 1;
		gbc_PID_textField.gridy = 1;
		getContentPane().add(PID_textField, gbc_PID_textField);
		PID_textField.setColumns(10);
		
		JLabel lblMinGap = new JLabel("Min Gap");
		GridBagConstraints gbc_lblMinGap = new GridBagConstraints();
		gbc_lblMinGap.fill = GridBagConstraints.BOTH;
		gbc_lblMinGap.insets = new Insets(0, 5, 5, 5);
		gbc_lblMinGap.gridx = 0;
		gbc_lblMinGap.gridy = 2;
		getContentPane().add(lblMinGap, gbc_lblMinGap);
		
		minGap_textField = new JTextField();
		minGap_textField.setText("4");
		GridBagConstraints gbc_minGap_textField = new GridBagConstraints();
		gbc_minGap_textField.fill = GridBagConstraints.BOTH;
		gbc_minGap_textField.insets = new Insets(0, 0, 5, 0);
		gbc_minGap_textField.gridx = 1;
		gbc_minGap_textField.gridy = 2;
		getContentPane().add(minGap_textField, gbc_minGap_textField);
		minGap_textField.setColumns(10);
		
		JLabel lblStatus = new JLabel("Status: ");
		GridBagConstraints gbc_lblStatus = new GridBagConstraints();
		gbc_lblStatus.fill = GridBagConstraints.BOTH;
		gbc_lblStatus.insets = new Insets(0, 5, 5, 5);
		gbc_lblStatus.gridx = 0;
		gbc_lblStatus.gridy = 3;
		getContentPane().add(lblStatus, gbc_lblStatus);
		
		msgLabel = new JLabel("Ready");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.fill = GridBagConstraints.BOTH;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 3;
		getContentPane().add(msgLabel, gbc_lblNewLabel);
		
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean isFirstAction = false;
				
				if(pm == null){
					pm = new ProcessMonitor();
					isFirstAction = true;
				}
				
				pm.setTargetProcessName(processName_textField.getText());
				pm.setTargetPID(PID_textField.getText());
				pm.setMinGap(minGap_textField.getText());
				
				if(pm.catchTask() == null){
					setStatusMsg("NOT EXIST");
					return;
				}
				
				if(isFirstAction){
					pm.start();
				}
				else{
					pm.setPlay();
				}
				
				ui.status = Status.PLAY;
			}
		});
		GridBagConstraints gbc_btnStart = new GridBagConstraints();
		gbc_btnStart.fill = GridBagConstraints.BOTH;
		gbc_btnStart.insets = new Insets(0, 0, 0, 5);
		gbc_btnStart.gridx = 0;
		gbc_btnStart.gridy = 4;
		getContentPane().add(btnStart, gbc_btnStart);
		
		JButton btnPause = new JButton("Pause");
		btnPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(pm != null){
					pm.setPause();
					setStatusMsg("Paused.");
					ui.status = Status.PAUSE;
				}
			}
		});
		GridBagConstraints gbc_btnPause = new GridBagConstraints();
		gbc_btnPause.fill = GridBagConstraints.BOTH;
		gbc_btnPause.gridx = 1;
		gbc_btnPause.gridy = 4;
		getContentPane().add(btnPause, gbc_btnPause);
		
		ui = new UIThread();
		ui.start();
	}
	
	void setStatusMsg(String text){
		msgLabel.setText(text);
		msgLabel.repaint();
		repaint();
	}
	
	void release(){
		this.dispose();
		try {
			this.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	class UIThread extends Thread{
		private Status status = Status.PAUSE;
		
		public void run(){
			int indicator = 0;
			while(true){
				if(status != Status.STOP){
					switch(status){
					case PLAY:
						String str = "Running.";
						for(int i=0; i<indicator%3; i++){
							str+='.';
						}
						setStatusMsg(str);
						indicator++;
						break;
						
					case PAUSE:
						break;
					default:
						break;
					}
	
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				else{
					setStatusMsg("Stopped.");
					break;
				}
			}
			
			System.out.println("UI done");
		}

	}
}
