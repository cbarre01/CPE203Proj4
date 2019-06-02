import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy
{


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {

        // Choose/know starting and ending points of the path
        List<Point> path = new LinkedList<>();
        ArrayList<Node> openList = new ArrayList<>();
        ArrayList<Node> closedList = new ArrayList<>();




        //2. Add start node to the open list and mark it as the current node
        Node startNode = new Node(start, 0, hdist(start, end), null);
        Node current = startNode;
        openList.add(startNode);


        while (!current.getPoint().adjacent(end) && closedList.size() < 100)
        {
            //3. Analyze all valid adjacent nodes that are not on the closed list
            List<Point> neighbors = potentialNeighbors.apply(current.getPoint())
                    .filter(canPassThrough)
                    .collect(Collectors.toList());

            ArrayList<Node> neighborNodes = new ArrayList<>();
            for (Point p : neighbors)
            {
                neighborNodes.add(new Node(p,current.getG() + 1,hdist(p, end), current));
            }


            /*
            List<Node> neighbors = potentialNeighbors.apply(start)
                    .filter(canPassThrough)
                    .map(p -> new Node(p,current.getG() + 1,hdist(p, end), current))
                    .collect(Collectors.toList());


             */

            for (Node curAdjacent : neighborNodes)
            {

                //3. Analyze all valid adjacent nodes that are not on the closed list

                boolean openContainsNode = false;
                for (Node cur: openList)
                {
                    if (cur.samePoint(curAdjacent))
                    {
                        openContainsNode = true;
                    }
                }
                if (!containsNode(closedList, curAdjacent))
                {

                    //a. Add to Open List if not already in it
                    if (!containsNode(openList, curAdjacent))
                    {
                        openList.add(curAdjacent);
                    }
                    for (Node curOL : openList) {
                        if (curOL.getPoint().equals(curAdjacent.getPoint())) {
                            //c. If the calculated g value is better than a previously calculated g value, replace
                            //the old g value with the new one and:
                            //i. If necessary, estimate distance of adjacent node to the end point (h)
                            //1. This is the called the heuristic. It can be Euclidian distance,
                            //Manhattan distance, etc.
                            //ii. Add g and h to get an f value
                            //iii.Mark the adjacent node’s prior vertex as the current node
                            if (curAdjacent.getG() < curOL.getG()) {
                                curOL.setG(curAdjacent.getG());
                                curOL.setH(hdist(curAdjacent.getPoint(), end));
                                curOL.setF(curAdjacent.getG() + hdist(curAdjacent.getPoint(), end));
                                curAdjacent.setPrior(current);
                            }
                        }
                    }

                }

            }

            //4. Move the current node to the closed list
            openList.remove(current);
            closedList.add(current);


            //5. Choose a node from the open list with the smallest f value and make it the current
            //    node

            Node smallestF = new Node(current.getPoint(),0, 0, current.getPrior() );
            smallestF.setF(9999999);
            for (Node curOL : openList) {
                if (curOL.getF() < smallestF.getF()) {
                    smallestF = curOL;
                }
            }
            current = smallestF;

        }
        while (!current.getPoint().equals(start))
        {
            path.add(current.getPoint());
            current = current.getPrior();
            //System.out.println(path);
        }
        Collections.reverse(path);
        return path;

    }

    public static int hdist(Point p, Point end)
    {
        return Math.abs(end.getX() - p.getX()) + Math.abs(end.getY() - p.getY());
    }

    public boolean containsNode(List<Node> nodeList, Node node)
    {
        for (Node cur: nodeList)
        {
            if (cur.samePoint(node))
            {
                return true;
            }
        }
        return false;
    }
}
