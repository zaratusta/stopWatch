package stopWatch;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import org.apache.commons.lang3.time.DurationFormatUtils;
import stopWatch.StopWatch.CountdownStatus;
import stopWatch.Translator.Input;


public class Device {
	private final static String ALL_ZERO = "00:00:00:00";
	private final static String TIME_FORMAT = "HH:mm:ss:SSS";
	private final static String TITLE = "StopWatch";
	private final static String START = "Start";
	private final static String STOP = "Stop";
	private final static String PAUSE = "Pause";
	private final static String RUN = "Run";
	private final static String READY = "Ready";

	private JLabel displayLabel;
	private JLabel clockLabel;
	private JButton startButton;
	private JLabel statusLabel;
	private StopWatch stopWatch;
	private Translator mainDisplay;

	public Device () {
		buildGUI();
		setDisplays();
	}

	private void buildGUI() {
		startButton = new JButton(START);
		startButton.addActionListener(new StartListener());

		JButton stop = new JButton(STOP);
		stop.addActionListener(new StopListener());

		statusLabel = new JLabel(READY);
		statusLabel.setBorder(BorderFactory.createEtchedBorder());
		statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

		clockLabel = new JLabel("");
		clockLabel.setHorizontalAlignment(SwingConstants.CENTER);
		clockLabel.setBorder(BorderFactory.createEtchedBorder());

		GridLayout layout = new GridLayout(4, 0);
		JPanel buttonPane = new JPanel(layout);
		Dimension minimumSize = new Dimension(70, 154);
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

		URL path = StopWatch.class.getResource("stopwatch.png");
		Image icon = Toolkit.getDefaultToolkit().getImage(path);
		mainFrame.setIconImage(icon);
	}

	private void setDisplays() {
		stopWatch = new StopWatch();
		mainDisplay = new Translator(displayLabel::setText, new Input<>(stopWatch::getTime, t -> DurationFormatUtils.formatDuration(t, TIME_FORMAT).substring(0, 11)));
		mainDisplay.setPeriod(10);
		Thread stopWatchThread = new Thread(mainDisplay);
		stopWatchThread.start();

		Translator miniDisplay = new Translator(clockLabel::setText, new Input<>(() -> LocalDateTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)), t -> t));
		Thread clock = new Thread(miniDisplay);
		clock.setDaemon(true);
		clock.start();
		miniDisplay.translate();
	}

	private class StartListener implements ActionListener {

		public void actionPerformed(ActionEvent ae) {
			switch (stopWatch.getStatus()) {
				case STOP:
				case PAUSE:
					stopWatch.run();
					mainDisplay.translate();
					statusLabel.setText(CountdownStatus.RUN.toString());
					startButton.setText(PAUSE);
					break;
				case RUN:
					stopWatch.pause();
					mainDisplay.stopTranslate();
					statusLabel.setText(CountdownStatus.PAUSE.toString());
					startButton.setText(RUN);
					break;
			}
		}
	}

	private class StopListener implements ActionListener {

		public void actionPerformed(ActionEvent ae) {
			switch (stopWatch.getStatus()) {
				case STOP:
					displayLabel.setText(ALL_ZERO);
					break;
				default:
					stopWatch.stop();
					mainDisplay.stopTranslate();
					statusLabel.setText(CountdownStatus.STOP.toString());
					startButton.setText(START);
			}
		}
	}
}