import javax.swing.*;

public class AppLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Create a new instance of WeatherAppGui
                new WeatherAppGui().setVisible(true);
            }
        });
    }
}
