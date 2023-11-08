package ru.alexander.graph.render;

import ru.alexander.graph.graph.Node;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class GraphMenuPopup extends JPopupMenu {
    public static final ArrayList<PopupTreeItem> tree = new ArrayList<>();
    private final GraphDrawer drawer;

    public double x, y;
    public GraphMenuPopup(GraphDrawer drawer) {
        this.drawer = drawer;
        JMenu addNode = new JMenu("Add Node...");

        loadTree(tree, addNode);

        add(addNode);
    }

    private void loadTree(ArrayList<PopupTreeItem> tree, JMenuItem menu) {
        for (PopupTreeItem o : tree) {
            JMenuItem branch;
            if (o instanceof PopupTreeNode node) {
                branch = new JMenuItem(node.name());
                branch.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            Constructor<? extends Node> constructor = node
                                    .node().getDeclaredConstructor(double.class, double.class);
                            drawer.getGraph().getNodes().add(constructor.newInstance(x, y));
                            drawer.repaint();
                        } catch (NoSuchMethodException | InvocationTargetException |
                                 InstantiationException | IllegalAccessException ignored) {}
                    }
                });
            }
            else if (o instanceof PopupTreeBranch t) {
                branch = new JMenu(t.name());
                loadTree(t.childs(), branch);
            }
            else throw new IllegalStateException("Illegal tree node!");
            menu.add(branch);
        }
    }

    public static class PopupTreeItem {
        private final String name;

        public PopupTreeItem(String name) {
            this.name = name;
        }

        public String name() {
            return name;
        }
    }
    public static final class PopupTreeNode extends PopupTreeItem {
        private final Class<? extends Node> node;

        public PopupTreeNode(String name, Class<? extends Node> node) {
            super(name);
            this.node = node;
        }

        public Class<? extends Node> node() {
            return node;
        }
    }
    public static class PopupTreeBranch extends PopupTreeItem {
        private final ArrayList<PopupTreeItem> childs = new ArrayList<>();

        public PopupTreeBranch(String name) {
            super(name);
        }

        public ArrayList<PopupTreeItem> childs() {
            return childs;
        }
    }

}
