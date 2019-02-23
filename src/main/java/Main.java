import device.StopWatchDevice;

import javax.swing.*;


public class Main {
    public static void main(String... args) {
        SwingUtilities.invokeLater(StopWatchDevice::new);
    }
}