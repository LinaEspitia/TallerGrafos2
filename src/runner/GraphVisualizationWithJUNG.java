package runner;
import logic.GraphPanel;

import javax.swing.*;

public class GraphVisualizationWithJUNG {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Planificación de Citas Médicas");
            GraphPanel graphPanel = new GraphPanel();
            frame.add(graphPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setVisible(true);
        });
    }
}
