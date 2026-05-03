package structures;
import models.Team;

/**
 * Building block for the AVL tree
 * Acts as a container that holds the Team object and pointers to its child nodes
 */

public class AVLNode {
    private Team team;

    // Pointers to left and right children
    private AVLNode leftChild;
    private AVLNode rightChild;

    // Used by the AVLTree to calculate balance factors
    private int height;

    /**
     * Constructor for a new node
     * When a new node created, it starts as a leaf at the bottom
     * Thus, it has no children and its starting height is 1
     */
    public AVLNode(Team team) {
        this.team = team;
        this.leftChild = null;
        this.rightChild = null;
        this.height = 1;
    }

    // GETTERS
    public Team getTeam() { return team; }

    public AVLNode getLeftChild() { return leftChild; }

    public AVLNode getRightChild() { return rightChild; }

    public int getHeight() { return height; }

    // SETTERS

    /**
     * Overwrites the left child pointer
     * Used break and reattach links during AVL rotations and insertions
     */
    public void setLeftChild(AVLNode leftChild) { this.leftChild = leftChild; }

    /**
     * Overwrites the right child pointer
     * Used break and reattach links during AVL rotations and insertions
     */
    public void setRightChild(AVLNode rightChild) { this.rightChild = rightChild; }

    /**
     * Updates the specific height of the node after insertion or rotation
     */
    public void setHeight(int height) { this.height = height; }

}
