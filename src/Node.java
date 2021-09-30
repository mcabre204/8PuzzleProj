public class Node implements Comparable<Node>{

    private String state;
    private Node parent;
    private String move;

    private int hn;
    private int gn;
    private int fn;

    public Node(String state, int hn, int gn){
        this.state = state;
        this.hn = hn;
        this.gn = gn;
        parent = null;
        fn = hn+gn;
    }

    public Node(String state, int hn, int gn, String move, Node parent){
        this.state = state;
        this.hn = hn;
        this.gn = gn;
        this.move = move;
        this.parent = parent;
        fn = hn+gn;
    }

    public String getState(){
        return state;
    }

    public Node getParent() {
        return parent;
    }

    public int getHn() {
        return hn;
    }

    public int getGn() {
        return gn;
    }

    public int getFn(){
        return fn;
    }

    public String getMove() {
        return move;
    }

    @Override
    public int compareTo(Node node) {
        return this.getFn() - node.getFn();
    }
}
