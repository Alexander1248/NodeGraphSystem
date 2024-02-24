import org.junit.Test;
import ru.alexander.graph.graph.Graph;
import ru.alexander.graph.graph.Node;
import ru.alexander.graph.render.GraphDrawer;
import ru.alexander.graph.render.GraphMenuPopup;

import javax.swing.*;
import java.awt.*;

public class GraphTest {

    @Test
    public void test() {
        GraphMenuPopup.tree.add(new GraphMenuPopup.PopupTreeNode("test", TestNode.class));
        GraphDrawer drawer = new GraphDrawer();
        drawer.setGraph(new Graph());
        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(drawer);
        frame.setSize(1280, 720);
        frame.setVisible(true);
        while (true) frame.repaint();
    }
}
