import javax.swing.*;

public class AppLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Create a new instance of WeatherAppGui
                new WeatherAppGui().setVisible(true);
 //               System.out.println(WeatherApp.getLocationData("Tokyo"));

                // System.out.println(WeatherApp.getCurrentTime());
            }
        });
    }
}
