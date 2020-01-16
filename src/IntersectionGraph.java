import java.util.ArrayList;
import java.util.HashMap;

public class IntersectionGraph
{
    private HashMap<Intersection, ArrayList<Intersection>> edgeList;

    public IntersectionGraph()
    {
        edgeList = new HashMap<Intersection, ArrayList<Intersection>>();
    }

    // ==========================================
    //
    //             Graph Functions
    //
    // ==========================================

    // Return true if this Graph has intsec as a node
    public boolean hasNode(Intersection intsec)
    {
        return edgeList.containsKey(intsec);
    }

    // Add the given intersection as a node, unless it is already in the graph
    public void addNode(Intersection intsec)
    {
        if(!this.hasNode(intsec))
        {
            edgeList.put(intsec, new ArrayList<Intersection>());
        }
    }

    // Returns true if the graph has an edge from intsec1 to intsec2. This only checks one direction,
    // since our addEdge function is symmetric and there is no remove edge functionality.
    public boolean hasEdge(Intersection intsec1, Intersection intsec2)
    {
        if(!this.hasNode(intsec1))
        {
            return false;
        }
        return edgeList.get(intsec1).contains(intsec2);
    }

    // Add an edge between the two given intersections by adding them to each others'
    // neighbor lists. If either node is not already in the graph, add it.
    public void addEdge(Intersection intsec1, Intersection intsec2)
    {
        if(!hasEdge(intsec1, intsec2))
        {
            // Add the nodes to the graph if they are not already there.
            this.addNode(intsec1);
            this.addNode(intsec2);

            edgeList.get(intsec1).add(intsec2);
            edgeList.get(intsec2).add(intsec1);
        }
    }

}