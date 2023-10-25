package ru.alexander.graph.graph;

import java.util.*;

public class Graph {
    private final List<Node> nodes = new ArrayList<>();
    public List<Node> getNodes() {
        return nodes;
    }

    public void orderGraph() {
        Stack<Node> reverseOrder = new Stack<>();

        Queue<Node> queue = new LinkedList<>();
        for (Node node : nodes)
            if (node.getOutputs().length == 0)
                queue.add(node);

        while (!queue.isEmpty()) {
            Node node = queue.poll();

            if (reverseOrder.contains(node)) continue;

            reverseOrder.add(node);

            Node.Input[] inputs = node.getInputs();
            for (int i = 0; i < inputs.length; i++)
                if (inputs[i] != null)
                    queue.add(inputs[i].node());
        }

        nodes.clear();

        while (!reverseOrder.isEmpty())
            nodes.add(reverseOrder.pop());
    }

    public void calculate() {
        for (Node node : nodes)
            node.calculate();
    }
}
