import javax.swing.SwingUtilities;
import stopWatch.Device;


public class Main {
	public static void main(String args[]) {
		SwingUtilities.invokeLater(Device::new);
	}
}