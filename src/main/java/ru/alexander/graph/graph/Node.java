package ru.alexander.graph.graph;

import javax.swing.*;
import java.awt.*;

public abstract class Node {
    public double x = 0;
    public double y = 0;
    private final Input[] inputs;
    private final Object[] outputs;

    public Node(int inCount, int outCount) {
        inputs = new Input[inCount];
        outputs = new Object[outCount];
    }

    public Node(int inCount, int outCount, double x, double y) {
        this(inCount, outCount);
        this.x = x;
        this.y = y;
    }

    public Input[] getInputs() {
        return inputs;
    }

    public Object[] getOutputs() {
        return outputs;
    }

    public abstract void calculate();


    public abstract void draw(Graphics2D g, double screenX, double screenY);

    public boolean roughInside(double pointX, double pointY) {
        return true;
    }
    public abstract boolean inside(double pointX, double pointY);
    public abstract int pinSelected(double pointX, double pointY);
    public abstract double getPinX(int index);
    public abstract double getPinY(int index);
    public abstract JMenuItem openSettingsMenu(double x, double y);



    public record Input(Node node, int pin) {}
}
