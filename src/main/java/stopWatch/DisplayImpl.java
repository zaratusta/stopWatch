package stopWatch;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class DisplayImpl implements Display{

	private JLabel displayLabel;
	private JLabel clockLabel;
	
	private JButton startButton;
	private JLabel statusLabel;
	private StopWatch stopWatch;

    private final String ALL_ZERO = "00:00:00:00";

    private final String TITLE = "StopWatch";

    private final String START = "Start";
    private final String STOP = "Stop";
    private final String PAUSE = "Pause";
    private final String RUN = "Run";
    private final String READY = "Ready";

	public DisplayImpl(StopWatch stopWatch) {
        buildGUI();
        setThreads(stopWatch);
    }

    private void setThreads(StopWatch stopWatch) {
        this.stopWatch = stopWatch;
        stopWatch.setDisplay(this);
        Thread clock = new Thread(new Clock(this));
        clock.setDaemon(true);
        clock.start();
    }

    @Override
	public void setDisplay(long in){
        displayLabel.setText(digitToString(in));
	}

    @Override
	public void setClock(long in){
		clockLabel.setText(digitToString3(in));
	}
	
	private void buildGUI() {

//		URL path = StopWatch.class.getResource("stopwatch.png");
//      Image icon = Toolkit.getDefaultToolkit().getImage(path);
//      mainFrame.setIconImage(icon);

        startButton = new JButton(START);
        startButton.addActionListener(new StartListener());

        JButton stop = new JButton(STOP);
        stop.addActionListener(new StopListener());

        statusLabel = new JLabel(READY);
        statusLabel.setBorder(BorderFactory.createEtchedBorder());
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        clockLabel = new JLabel();
        clockLabel.setHorizontalAlignment(SwingConstants.CENTER);
        clockLabel.setBorder(BorderFactory.createEtchedBorder());

        GridLayout layout = new GridLayout(4,0);
        JPanel buttonPane = new JPanel(layout);
        Dimension minimumSize = new Dimension(70,154);
        buttonPane.setPreferredSize(minimumSize);
        buttonPane.add(startButton);
        buttonPane.add(stop);
        buttonPane.add(statusLabel);
        buttonPane.add(clockLabel);

        displayLabel = new JLabel(ALL_ZERO);
        displayLabel.setHorizontalAlignment(SwingConstants.CENTER);
        displayLabel.setBorder(BorderFactory.createBevelBorder(1));
        displayLabel.setFont(new Font("Arial", 0, 100));

        JPanel dialView = new JPanel(new BorderLayout());
        dialView.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        dialView.add(BorderLayout.EAST, buttonPane);
        dialView.add(BorderLayout.CENTER, displayLabel);

        JFrame mainFrame = new JFrame(TITLE);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.add(dialView);
        mainFrame.setBounds(300, 300, 630, 154);
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);
	}

        private class StartListener implements ActionListener{
        	
        	public void actionPerformed(ActionEvent ae){
        		
        		CountdownStatus status = stopWatch.getStatus();

                switch (status) {
                    case STOP:
                        Thread stopWatchThread = new Thread(stopWatch);
                        stopWatchThread.start();
                        statusLabel.setText(CountdownStatus.RUN.toString());
                        startButton.setText(PAUSE);
                        break;
                    case RUN:
                        stopWatch.pause();
                        statusLabel.setText(CountdownStatus.PAUSE.toString());
                        startButton.setText(RUN);
                        break;
                    case PAUSE:
                        stopWatch.continueRun();
                        statusLabel.setText(CountdownStatus.RUN.toString());
                        startButton.setText(PAUSE);
                        break;
                }
        	}
        }
        
        private class StopListener implements ActionListener{
        	
        		public void actionPerformed(ActionEvent ae){
        			CountdownStatus status = stopWatch.getStatus();

                    switch (status) {
                        case STOP:
                            displayLabel.setText(ALL_ZERO);
                            break;
                        default:
                            stopWatch.stop();
                            statusLabel.setText(CountdownStatus.STOP.toString());
                            startButton.setText(START);
                    }
        	}
        }
        
        private String fXX(long digit){
    		if(digit<10) return "0"+digit;
    		else return String.valueOf(digit); 
    	}
        
        private String digitToString3(long digit){
    		
    		long hours = digit/3600000;
    		long minutes = (digit%3600000)/60000;
    		long seconds = (digit%60000)/1000;

    		return fXX(hours)+":"+fXX(minutes)+":"+fXX(seconds);
    	}
        
        private String digitToString(long digit){
    		
    		long hours = digit/3600000;
    		long minutes = (digit%3600000)/60000;
    		long second = (digit%60000)/1000;
    		long milliSeconds = (digit%1000)/10;
    				
    		return fXX(hours)+":"+fXX(minutes)+":"+fXX(second)+":"+fXX(milliSeconds);
    	}
}
