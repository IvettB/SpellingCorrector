package spell;

public class Trie implements ITrie {
    private int wordCount;
    private int nodeCount;
    private final Node root;

    public Trie() {
        wordCount = 0;
        nodeCount = 1;
        root = new Node();
    }

    @Override
    public void add(String word) {
        Node current = root;
        word = word.toLowerCase();

        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            int index = letter - 'a';

            if (current.getChildren()[index] == null) {
                current.getChildren()[index] = new Node();
                nodeCount++;
            }
            current = current.getChildren()[index];
        }
        if (current.getValue() == 0) {
            wordCount++;
        }
        current.incrementValue();
    }

    @Override
    public INode find(String word) {
        Node current = root;
        word = word.toLowerCase();

        for (char letter : word.toCharArray()) {
            int index = letter - 'a';
            Node child = current.getChildren()[index];

            if (child == null) {
                return null;
            }
            current = child;
        }
        return (current.getValue() > 0) ? current : null;
    }

    @Override
    public int getWordCount() {
        return wordCount;
    }

    @Override
    public int getNodeCount() {
        return nodeCount;
    }

    @Override
    public String toString() {
        StringBuilder current = new StringBuilder();
        StringBuilder out = new StringBuilder();

        toStringHelper(root, current, out);

        return out.toString();
    }

    private void toStringHelper(Node n, StringBuilder current, StringBuilder out) {
        if (n.getValue() > 0) {
            out.append(current).append("\n");
        }

        for (int i = 0; i < n.getChildren().length; i++) {
            Node child = n.getChildren()[i];

            if (child != null) {
                char nodeLetter = (char) ('a' + i);
                current.append(nodeLetter);

                toStringHelper(child, current, out);

                current.deleteCharAt(current.length() - 1);
            }
        }
    }

    @Override
    public int hashCode() {
        Node[] rootChild = root.getChildren();
        int sum = 0;

        for (int i = 0; i < rootChild.length; i++) {
            if (rootChild[i] != null) {
                sum += i;
            }
        }
        // key not unique when not multiplied
        return nodeCount + wordCount * sum;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Trie temp)) {
            return false;
        }

        if (temp.wordCount != this.wordCount) {
            return false;
        }

        if (temp.nodeCount != this.nodeCount) {
            return false;
        }
        return equalsHelper(temp.root, this.root);
    }

    private boolean equalsHelper(Node n1, Node n2) {
        if (n1.getValue() != n2.getValue()) {
            return false;
        }

        for (int i = 0; i < n1.getChildren().length; i++) {
            Node child1 = n1.getChildren()[i];
            Node child2 = n2.getChildren()[i];

            if (child1 != null && child2 != null) {
                if (!equalsHelper(child1, child2)) {
                    return false;
                }
            }
            else if (child1 != null || child2 != null) {
                return false;
            }
        }
            return true;
        }
    }
