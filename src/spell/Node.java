package spell;

public class Node implements INode {
    Node[] nodes;
    int count;

    public Node() {
        nodes = new Node[26];
        count = 0;
    }
    @Override
    public int getValue() {
        return count;
    }

    @Override
    public void incrementValue() {
        count++;
    }

    @Override
    public Node[] getChildren() {
        return nodes;
    }
}
