import stopWatch.DisplayImpl;
import stopWatch.StopWatchImpl;

import javax.swing.*;

/**
 * Created by Yan on 27.05.2015.
 */
public class Main {
    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DisplayImpl(new StopWatchImpl());
            }
        });
    }
}
