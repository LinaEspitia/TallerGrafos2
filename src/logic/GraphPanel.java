package logic;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import org.apache.commons.collections15.Transformer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphPanel extends JPanel {
    private JComboBox<String> sourceComboBox;
    private JComboBox<String> targetComboBox;
    private JLabel distanceLabel;
    private JLabel jumpsLabel;
    private BasicVisualizationServer<String, Integer> vv;
    private Graph<String, Integer> graph;
    private DijkstraShortestPath<String, Integer> dijkstra;

    public GraphPanel() {
        graph = new DirectedSparseGraph<>();
        graph.addEdge(1, "A", "B");
        graph.addEdge(2, "A", "C");
        graph.addEdge(3, "B", "C");
        graph.addEdge(4, "B", "D");
        graph.addEdge(5, "C", "D");
        graph.addEdge(6, "D", "E");
        graph.addEdge(7, "E", "F");
        graph.addEdge(8, "F", "G");
        graph.addEdge(9, "G", "B");
        graph.addEdge(10, "E", "G");
        graph.addEdge(11, "B", "E");
        graph.addEdge(12, "F", "A");
        graph.addEdge(13, "E", "C");
        Map<Integer, Double> edgeDistances = new HashMap<>();

        edgeDistances.put(1, 10.0);
        edgeDistances.put(2, 15.0);
        edgeDistances.put(3, 12.0);
        edgeDistances.put(4, 8.0);
        edgeDistances.put(5, 20.0);
        edgeDistances.put(6, 18.0);
        edgeDistances.put(7, 17.0);
        edgeDistances.put(8, 38.0);
        edgeDistances.put(9, 25.0);
        edgeDistances.put(10, 52.0);
        edgeDistances.put(11, 72.0);
        edgeDistances.put(12, 92.0);
        edgeDistances.put(13, 120.0);


        dijkstra = new DijkstraShortestPath<>(graph);
        sourceComboBox = new JComboBox<>(graph.getVertices().toArray(new String[0]));
        targetComboBox = new JComboBox<>(graph.getVertices().toArray(new String[0]));
        JButton calculateButton = new JButton("Camino mas Corto");
        distanceLabel = new JLabel("Distancia: ");
        jumpsLabel = new JLabel(" Número de Saltos: ");

        vv = new BasicVisualizationServer<>(new FRLayout<>(graph));
        vv.setPreferredSize(new Dimension(600, 400));
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<>());
        vv.getRenderContext().setEdgeDrawPaintTransformer(edge -> Color.BLACK);
        vv.getRenderContext().setEdgeLabelTransformer(new Transformer<Integer, String>() {
            public String transform(Integer edge) {
                return edgeDistances.get(edge).toString();
            }
        });

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String source = (String) sourceComboBox.getSelectedItem();
                String target = (String) targetComboBox.getSelectedItem();
                List<Integer> shortestEdgePath = dijkstra.getPath(source, target);

                if (shortestEdgePath.size() != 0){
                    double totalDistance = 0.0;
                    for (int i = 0; i < shortestEdgePath.size(); i++) {
                        totalDistance += edgeDistances.get(shortestEdgePath.get(i));
                    }
                    distanceLabel.setText("Distancia: " + totalDistance);
                    jumpsLabel.setText("Número de Saltos: " + (shortestEdgePath.size()));
                } else {
                    distanceLabel.setText("No se encontro una ruta ");
                }

                vv.getRenderContext().setEdgeDrawPaintTransformer(edge -> {
                    if (shortestEdgePath.contains(edge)) {
                        return Color.RED;
                    }
                    return Color.BLACK;
                });

                vv.repaint();
            }
        });

        setLayout(new BorderLayout());
        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Paciente:"));
        controlPanel.add(sourceComboBox);
        controlPanel.add(new JLabel("Especialista:"));
        controlPanel.add(targetComboBox);
        controlPanel.add(calculateButton);
        controlPanel.add(distanceLabel);
        controlPanel.add(jumpsLabel);

        add(controlPanel, BorderLayout.NORTH);
        add(vv, BorderLayout.CENTER);
    }
}