package ru.alexander.graph.render;

import ru.alexander.graph.graph.Graph;
import ru.alexander.graph.graph.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.QuadCurve2D;

public class GraphDrawer extends JPanel {
    private Graph graph = null;


    private double x = 0;
    private double y = 0;
    private final GraphMouseAdapter adapter;

    public GraphDrawer() {
        adapter = new GraphMouseAdapter(this);

        addMouseListener(adapter);
        addMouseWheelListener(adapter);
        addMouseMotionListener(adapter);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (graph != null) {
            g.clearRect(0, 0, getWidth(), getHeight());

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setStroke(new BasicStroke(3));

            for (Node node : graph.getNodes()) {
                Node.Input[] inputs = node.getInputs();
                for (int i = 0; i < inputs.length; i++) {
                    Node.Input input = inputs[i];
                    if (input == null) continue;
                    double nx1 = node.getPinX(i) + node.x - x;
                    double ny1 = node.getPinY(i) + node.y - y;

                    int index = input.pin() + input.node().getInputs().length;
                    double nx2 = input.node().getPinX(index) + input.node().x - x;
                    double ny2 = input.node().getPinY(index) + input.node().y - y;

                    drawCurve(g2d, nx1, nx2, ny1, ny2);
                }
            }

            for (Node node : graph.getNodes())
                node.draw(g2d, x, y);


            adapter.draw(g2d);
        }
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }


    private static void drawCurve(Graphics2D g, double nx1, double nx2, double ny1, double ny2) {
        double half = -Math.abs(nx2 - nx1) * 0.5;
        CubicCurve2D q = new CubicCurve2D.Float();
        q.setCurve(
                nx1, ny1,
                nx1 + half, ny1,
                nx2 - half, ny2,
                nx2, ny2
        );
        g.draw(q);
    }
    private static class GraphMouseAdapter extends MouseAdapter {
        private final GraphDrawer drawer;

        private int prevX, prevY;

        private Node node = null;
        private int pin = -1;

        public GraphMouseAdapter(GraphDrawer drawer) {
            this.drawer = drawer;

        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            double px = drawer.x + e.getX();
            double py = drawer.y + e.getY();

            if (e.getButton() == MouseEvent.BUTTON3) {
                GraphMenuPopup menu = new GraphMenuPopup(drawer);
                menu.x = px;
                menu.y = py;

                for (Node node : drawer.graph.getNodes())
                    if (node.roughInside(px, py)) {
                        if (node.inside(px, py)) {
                            JMenuItem nodeSettings = node.openSettingsMenu(px, py);
                            if (nodeSettings == null) break;
                            menu.add(nodeSettings);
                            break;
                        }
                    }

                menu.show(e.getComponent(), e.getX(), e.getY());
                return;
            }

            for (Node node : drawer.graph.getNodes())
                if (node.roughInside(px, py)) {
                    pin = node.pinSelected(px, py);
                    if (pin != -1 || node.inside(px, py)) {
                        this.node = node;
                        break;
                    }
                }

            updatePrevPos(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) return;
            if (pin == -1) {
                node = null;
                return;
            }
            double px = drawer.x + e.getX();
            double py = drawer.y + e.getY();

            for (Node node : drawer.graph.getNodes())
                if (this.node != node && node.roughInside(px, py)) {
                    int endPin = node.pinSelected(px, py);
                    if (endPin == -1) continue;
                    if (endPin >= node.getInputs().length) {
                        // From input node to output
                        if (pin >= this.node.getInputs().length) continue;
                        endPin -= node.getInputs().length;
                        this.node.getInputs()[pin] = new Node.Input(node, endPin);
                    }
                    else {
                        // From output node to input
                        if (pin < this.node.getInputs().length) continue;
                        pin -= this.node.getInputs().length;
                        node.getInputs()[endPin] = new Node.Input(this.node, pin);
                    }
                }

            node = null;
            pin = -1;

            drawer.repaint();
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {

        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (node != null) {
                if (pin == -1) {
                    node.x += e.getX() - prevX;
                    node.y += e.getY() - prevY;
                }
            }
            else {
                drawer.x -= e.getX() - prevX;
                drawer.y -= e.getY() - prevY;
            }

            updatePrevPos(e);
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }


        private void updatePrevPos(MouseEvent e) {
            prevX = e.getX();
            prevY = e.getY();

            drawer.repaint();
        }

        public void draw(Graphics2D g) {
            if (pin == -1) return;
            double nx = node.getPinX(pin) + node.x - drawer.x;
            double ny = node.getPinY(pin) + node.y - drawer.y;

            drawCurve(g, prevX, nx, prevY, ny);
        }
    }
}
