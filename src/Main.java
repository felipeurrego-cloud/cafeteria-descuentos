import javax.swing.SwingUtilities;
import vista.VentanaCafeteria;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaCafeteria app = new VentanaCafeteria();
            app.setVisible(true);
        });
    }
}