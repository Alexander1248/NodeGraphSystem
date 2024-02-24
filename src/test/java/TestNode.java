import ru.alexander.graph.graph.Node;

import javax.swing.*;
import java.awt.*;

public class TestNode extends Node {
    public TestNode(double x, double y) {
        super(2, 2, x, y);
    }

    @Override
    public void calculate() {

    }

    @Override
    public void draw(Graphics2D g, double screenX, double screenY) {
        g.drawRoundRect((int) (x - screenX), (int) (y - screenY), 100, 50, 10, 10);
    }

    @Override
    public boolean inside(double pointX, double pointY) {
        return Math.max(Math.abs(x - pointX), Math.abs(y - pointY) * 2) < 100;
    }

    @Override
    public int pinSelected(double pointX, double pointY) {
        double dx = x - pointX;
        double dy = y + 10 - pointY;

        if (dx * dx + dy * dy < 7) return 0;
        dy += 30;
        if (dx * dx + dy * dy < 7) return 1;
        dy -= 30;
        dx += 100;
        if (dx * dx + dy * dy < 7) return 2;
        dy += 30;
        if (dx * dx + dy * dy < 7) return 3;
        return -1;
    }

    @Override
    public double getPinX(int index) {
        switch (index) {
            case 0, 1 -> {
                return 0;
            }
            case 2, 3 -> {
                return 100;
            }
        }
        return -100;
    }

    @Override
    public double getPinY(int index) {
        switch (index) {
            case 0, 2 -> {
                return 10;
            }
            case 1, 3 -> {
                return 40;
            }
        }
        return -100;
    }

    @Override
    public JMenuItem openSettingsMenu(double x, double y) {
        return null;
    }
}
