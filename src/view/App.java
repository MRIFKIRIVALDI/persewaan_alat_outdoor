package view;

import javax.swing.SwingUtilities;
import view.panel.MenuUtama;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuUtama().setVisible(true));
    }
}
