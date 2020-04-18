package Implementations;

public class Node implements Comparable<Node> {

    private int character;
    private int frequency;
    private Node left;
    private Node right;
    private Node parent;


    // Constructors

    public Node(int frequency) {
        this.character = -1;
        this.frequency = frequency;
        this.left = null;
        this.right = null;
        this.parent = null;    }

    public Node(int character, int frequency) {
        this.character = character;
        this.frequency = frequency;
        this.left = null;
        this.right = null;
        this.parent = null;
    }

    public Node(int character, int frequency, Node left, Node right, Node parent) {
        this.character = character;
        this.frequency = frequency;
        this.left = left;
        this.right = right;
        this.parent = parent;
    }



    // Getters and Setters

    public void setCharacter(int character) {
        this.character = character;
    }
    public int getCharacter() {
        return character;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getFrequency() {
        return frequency;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
        left.parent = this;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
        right.parent = this;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }



    // Methods

    @Override
    public int compareTo(Node otherNode) {
        if (this.frequency < otherNode.frequency)
            return -1;
        else if (this.frequency > otherNode.frequency)
            return 1;
        else return 0;

    }
}
