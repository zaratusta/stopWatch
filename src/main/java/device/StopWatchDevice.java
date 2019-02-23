package device;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.time.DurationFormatUtils;
import stopWatch.CountdownStatus;
import stopWatch.StopWatch;
import translator.ScheduledTranslator;
import translator.Translator;
import translator.TriggeredTranslator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StopWatchDevice {
    private final static String ALL_ZERO = "00:00:00:00";
    private final static String TIME_FORMAT = "HH:mm:ss:SSS";
    private final static String TITLE = "StopWatch";
    private final static String START = "Start";
    private final static String STOP = "Stop";
    private final static String PAUSE = "Pause";
    private final static String RUN = "Run";
    private final static String READY = "Ready";

    JLabel displayLabel = new JLabel(ALL_ZERO);
    JLabel clockLabel = new JLabel("");
    JLabel statusLabel = new JLabel(READY);
    JButton startButton = new JButton(START);
    StopWatch stopWatch = new StopWatch();

    Translator labelTranslator = new TriggeredTranslator(() ->
            Optional.of(stopWatch.getStatus()).map(String::valueOf).orElse(""),
            statusLabel::setText);

    ScheduledTranslator mainDisplay = new ScheduledTranslator(() ->
            Optional.of(stopWatch.getTime()).map(t -> DurationFormatUtils.formatDuration(t, TIME_FORMAT).substring(0, 11)).orElse(""),
            displayLabel::setText)
            .setPeriod(10);

    ScheduledTranslator miniDisplay = new ScheduledTranslator(
            () -> LocalDateTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)),
            clockLabel::setText);

    Executor executor = Executors.newFixedThreadPool(2);

    public StopWatchDevice() {
        buildGUI();
        runDisplays();
    }

    private void buildGUI() {
        startButton.addActionListener(startListener);

        JButton stop = new JButton(STOP);
        stop.addActionListener(stopListener);

        statusLabel.setBorder(BorderFactory.createEtchedBorder());
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        clockLabel.setHorizontalAlignment(SwingConstants.CENTER);
        clockLabel.setBorder(BorderFactory.createEtchedBorder());

        GridLayout layout = new GridLayout(4, 0);
        JPanel buttonPane = new JPanel(layout);
        Dimension minimumSize = new Dimension(90, 154);
        buttonPane.setPreferredSize(minimumSize);
        buttonPane.add(startButton);
        buttonPane.add(stop);
        buttonPane.add(statusLabel);
        buttonPane.add(clockLabel);

        displayLabel.setHorizontalAlignment(SwingConstants.CENTER);
        displayLabel.setBorder(BorderFactory.createBevelBorder(1));
        displayLabel.setFont(new Font("Arial", Font.PLAIN, 100));

        JPanel dialView = new JPanel(new BorderLayout());
        dialView.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        dialView.add(BorderLayout.EAST, buttonPane);
        dialView.add(BorderLayout.CENTER, displayLabel);

        JFrame mainFrame = new JFrame(TITLE);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.add(dialView);
        mainFrame.setBounds(300, 300, 750, 154);
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);

        URL path = StopWatch.class.getResource("stopwatch.png");
        Image icon = Toolkit.getDefaultToolkit().getImage(path);
        mainFrame.setIconImage(icon);
    }

    private void runDisplays() {
        executor.execute(mainDisplay);
        executor.execute(miniDisplay.startTranslate());
    }

    ActionListener startListener = event -> {
        switch (stopWatch.getStatus()) {
            case STOP:
            case PAUSE:
                stopWatch.run();
                mainDisplay.startTranslate();
                labelTranslator.translate();
                startButton.setText(PAUSE);
                break;
            case RUN:
                stopWatch.pause();
                mainDisplay.stopTranslate();
                labelTranslator.translate();
                startButton.setText(RUN);
                break;
        }
    };

    ActionListener stopListener = event -> {
        if (stopWatch.getStatus() == CountdownStatus.STOP) {
            displayLabel.setText(ALL_ZERO);
        } else {
            stopWatch.stop();
            mainDisplay.stopTranslate();
            labelTranslator.translate();
            startButton.setText(START);
        }
    };
}