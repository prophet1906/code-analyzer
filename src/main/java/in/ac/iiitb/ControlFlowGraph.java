package in.ac.iiitb;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import sootup.core.graph.StmtGraph;
import sootup.core.jimple.common.stmt.Stmt;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ControlFlowGraph {
    private static int nodeCounter = 0;
    private static int edgeCounter = 0;
    private String graphLabel;
    private StmtGraph<?> statementGraph;
    private List<Node> nodes;
    private List<Edge> directedEdges;
    private Graph visualCFG;

    public ControlFlowGraph(String graphTitle, StmtGraph<?> statementGraph) {
        this.graphLabel = graphTitle;
        this.statementGraph = statementGraph;
        this.nodes = new ArrayList<>();
        this.directedEdges = new ArrayList<>();
        this.visualCFG = new MultiGraph(this.graphLabel);
        System.setProperty("org.graphstream.ui", "swing");
        try {
            // Try loading stylesheet - don't crash if it is missing
            String styleSheetPath = getClass().getClassLoader().getResource("styles.css").getPath();
            visualCFG.setAttribute("ui.stylesheet", String.format("url('file://%s')", styleSheetPath));
        } catch (Exception ignored) {
        }
        visualCFG.setStrict(false);
        visualCFG.setAutoCreate(true);
        visualCFG.setAttribute("ui.title", graphTitle);
        visualCFG.setAttribute("ui.quality");
        visualCFG.setAttribute("ui.antialias");
        visualCFG.setAttribute("layout.force");
//        visualCFG.setAttribute("layout.quality", 4);
//        visualCFG.setAttribute("layout.stabilization-limit", 1);
//        visualCFG.setAttribute("layout.weight", 2);
        this.populateNodes();
        this.populateEdges();
    }

    private static String getNodeLabel() {
        String nodeLabel = "node_" + ControlFlowGraph.nodeCounter;
        ControlFlowGraph.nodeCounter++;
        return nodeLabel;
    }

    private static String getEdgeLabel() {
        String edgeLabel = "edge_" + ControlFlowGraph.edgeCounter;
        ControlFlowGraph.edgeCounter++;
        return edgeLabel;
    }

    private void populateNodes() {
        for (Stmt node : statementGraph.getNodes()) {
            Node n = new Node(node);
            nodes.add(n);
            visualCFG.addNode(n.getLabel());
            org.graphstream.graph.Node visualNode = visualCFG.getNode(n.getLabel());
            visualNode.setAttribute("ui.label", n.getStatement());
            String code = n.getStatement().toString();
            if (statementGraph.getStartingStmt().equals(node)) {
                visualNode.setAttribute("ui.class", "start");
            }
            if (code.startsWith("return")) {
                visualNode.setAttribute("ui.class", "end");
            }
            if (n.getStatement().branches()) {
                visualNode.setAttribute("ui.class", "branch");
            }
        }
    }

    private void populateEdges() {
        for (int i = 0; i < nodes.size(); i++) {
            Stmt src = nodes.get(i).getStatement();
            for (Node node : nodes) {
                Stmt dest = node.getStatement();
                if (statementGraph.hasEdgeConnecting(src, dest)) {
                    String branchCondition = statementGraph.outDegree(src) > 1 ? src.toString() : "";
                    Edge e = new Edge(nodes.get(i), node);
                    directedEdges.add(e);
                    visualCFG.addEdge(e.getLabel(), nodes.get(i).getLabel(), node.getLabel(), true);
                    if (!branchCondition.isEmpty()) {
                        String isTarget = Boolean.toString(statementGraph.isStmtBranchTarget(dest));
                        org.graphstream.graph.Edge visualEdge = visualCFG.getEdge(e.getLabel());
                        visualEdge.setAttribute("ui.label", isTarget);
                        visualEdge.setAttribute("ui.class", "branch");
                    }
                }
            }
        }
    }

    public void show() {
        visualCFG.display();
    }

    public void hide() {
        visualCFG.display(false);
    }

    @Data
    static class Node {
        private String label;
        private Stmt statement;

        public Node(Stmt statement) {
            this.statement = statement;
            this.label = ControlFlowGraph.getNodeLabel();
        }
    }

    @Data
    static class Edge {
        private String label;
        private Node source;
        private Node destination;

        public Edge(Node source, Node destination) {
            this.label = ControlFlowGraph.getEdgeLabel();
            this.source = source;
            this.destination = destination;
        }
    }
}
