public class Node {
    private Point point;
    private int g;
    private int h;
    private int f;
    private Node prior;

    public Node(Point pointIn, int gIn, int hIn, Node priorIn)
    {
        point = pointIn;
        g = gIn;
        h = hIn;
        f = g + h;
        prior = priorIn;
    }

    public Node(Point pointIn, int hIn, Node priorIn)
    {
        point = pointIn;
        g = 9999999;
        h = hIn;
        f = g + h;
        prior = priorIn;
    }

    public String toString()
    {
        return ("Node x: " + point.getX() + ", y: " + point.getY());
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public Node getPrior() {
        return prior;
    }

    public void setPrior(Node prior) {
        this.prior = prior;
    }

    public boolean samePoint(Node other)
    {
        return this.point.equals(other.point);
    }
}
