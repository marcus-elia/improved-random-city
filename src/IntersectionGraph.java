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

    // Returns the Road connecting these two intersections, and null if no such Road exists.
    public Road getConnectingRoad(Intersection intsec1, Intersection intsec2)
    {
        if(!hasEdge(intsec1, intsec2))
        {
            return null;
        }

        for(Road r : intsec1.getRoads())
        {
            if(r.goesBetweenIntersections(intsec1, intsec2))
            {
                return r;
            }
        }
        System.out.println("No road goes between these intersections.");
        return null;
    }

    // This applies Breadth First Search to find the shortest path of Intersections between start
    // and end. The output is stored in reverse. This is just a helper method.
    public ArrayList<Intersection> getShortestPathBackwards(Intersection start, Intersection end)
    {
        ArrayList<Intersection> output = new ArrayList<Intersection>();

        // Keep track of which intersection led to each one we visit
        HashMap<Intersection, Intersection> nodeToPrev = new HashMap<Intersection, Intersection>();
        nodeToPrev.put(start, null);

        // Keep track of the previous and current levels of nodes
        ArrayList<Intersection> prevLevel;
        ArrayList<Intersection> curLevel;

        curLevel = new ArrayList<Intersection>();
        curLevel.add(start);

        boolean hasBeenFound = false;
        while(!hasBeenFound)
        {
            prevLevel = curLevel;
            curLevel = new ArrayList<Intersection>();
            for(Intersection curInt : prevLevel)
            {
                // Look at each neighbor
                for(Intersection intsec : edgeList.get(curInt))
                {
                    // If we haven't discovered it yet
                    if(!nodeToPrev.containsKey(intsec))
                    {
                        nodeToPrev.put(intsec, curInt);
                        curLevel.add(intsec);
                    }
                    if(intsec.equals(end))
                    {
                        hasBeenFound = true;
                        break;
                    }
                }
            }
        }

        // Trace backwards and add the path to the output
        Intersection curInt = end;
        while(curInt != null)
        {
            output.add(curInt);
            curInt = nodeToPrev.get(curInt);
        }
        return output;
    }

    // Wrapper function
    public ArrayList<Road> getShortestPath(Intersection start, Intersection end)
    {
        ArrayList<Intersection> backwardIntersections = getShortestPathBackwards(start, end);
        ArrayList<Road> output = new ArrayList<Road>();
        for(int i = backwardIntersections.size() - 1; i > 0; i--)
        {
            output.add(getConnectingRoad(backwardIntersections.get(i), backwardIntersections.get(i-1)));
        }
        return output;
    }

}