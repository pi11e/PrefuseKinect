import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Table;

public class GraphFactory 
{
	public static Graph makeGraph() {

        // Create tables for node and edge data, and configure their columns.
        Table nodeData = new Table();
        Table edgeData = new Table(0,1);
        nodeData.addColumn("flag", boolean.class);
        edgeData.addColumn(Graph.DEFAULT_SOURCE_KEY, int.class);
        edgeData.addColumn(Graph.DEFAULT_TARGET_KEY, int.class);
        edgeData.addColumn("label", String.class);
        // Need more data in your nodes or edges?  Just add more
        // columns.

        // Create Graph backed by those tables.  Note that I'm
        // creating a directed graph here also.
        Graph g = new Graph(nodeData, edgeData, true);

        // Create some nodes and edges, each carrying some data.
        // There are surely prettier ways to do this, but for the
        // example it gets the job done.
        for ( int i=0; i<3; ++i ) {
            Node n1 = g.addNode();
            Node n2 = g.addNode();
            Node n3 = g.addNode();
            n1.setBoolean("flag", false);
            n2.setBoolean("flag", true);
            n3.setBoolean("flag", true);
            Edge e1 = g.addEdge(n1, n2);
            Edge e2 = g.addEdge(n1, n3);
            Edge e3 = g.addEdge(n2, n3);
            e1.setString("label", "a");
            e2.setString("label", "a");
            e3.setString("label", "a");
        }
        Edge e4 = g.getEdge(g.addEdge(0, 3));
        Edge e5 = g.getEdge(g.addEdge(3, 6));
        Edge e6 = g.getEdge(g.addEdge(6, 0));
        e4.setString("label", "b");
        e5.setString("label", "b");
        e6.setString("label", "b");
        return g;
    }
	
	

}
