package structures;

import models.Team;

/**
 * A self-balancing binary search tree that stores Team objects using their TeamID as keys
 * The self-balancing nature of the tree guarantees an O(log N) time complexity for searching and inserting records
 */

public class AVLTree {
    private AVLNode root;
    private int size;

    // Constructor -- Creates a tree and sets root to null to indicate it is empty
    public AVLTree() {
        this.root = null;
        this.size = 0;
    }

    // Extracts height of a given node
    private int height(AVLNode node) {
        if (node == null) { return 0; }
        return node.getHeight();
    }

    // Calculates the balance factor by finding the numerical difference between the left and right subtrees
    private int getBalance(AVLNode node) {
        if (node == null) { return 0; }
        return height(node.getLeftChild()) - height(node.getRightChild());
    }

    // Public wrapper - Begins recursive search process starting at the root to find a specific team
    public Team search(int teamID) {
        return search(root, teamID);
    }

    // Private engine - Traverses tree in O(log N) time to locate the Team object
    // Returns null if the ID does not exist in the database
    private Team search(AVLNode node, int teamID) {
        if (node == null) { return null; }  // Base Case: not found

        if (teamID == node.getTeam().getID()) {
            return node.getTeam();  // Base Case: team found
        }

        // Move to the left or right subtree depending on ID
        if (teamID < node.getTeam().getID()) {
            return search(node.getLeftChild(), teamID);
        }
        else {
            return search(node.getRightChild(), teamID);
        }
    }


    public Team[] toArray() {
        Team[] teamArray = new Team[size];

        int[] currentIndex = {0};

        fillArray(root, teamArray, currentIndex);

        return teamArray;
    }

    private void fillArray(AVLNode node, Team[] teamArray, int[] index) {
        if (node == null) { return; }
        fillArray(node.getLeftChild(), teamArray, index);

        teamArray[index[0]] = node.getTeam();
        index[0]++;

        fillArray(node.getRightChild(), teamArray, index);
     }

    // Returns the number of teams currently stored
    public int size() { return size; }

    // Public wrapper - Begins the recursive insertion process starting at the root
    // Returns false (and changes nothing) if a team with the same ID already exists,
    // so size always matches the number of nodes actually in the tree
    public boolean insert(Team team) {
        if (search(team.getID()) != null) { return false; }
        root = insert(root, team);
        size++;
        return true;
    }

    // Private engine - Compares IDs to dive left or right until it finds an empty spot
    // During the recursive "unwind" phase, it automatically recalculates heights and triggers rotation if unbalanced
    private AVLNode insert(AVLNode node, Team team) {
        // Base Case: Found empty spot, create the new leaf
        if (node == null) {
            return new AVLNode(team);
        }

        if (team.getID() < node.getTeam().getID()) {
            node.setLeftChild(insert(node.getLeftChild(), team));
        }
        else if (team.getID() > node.getTeam().getID()) {
            node.setRightChild(insert(node.getRightChild(), team));
        }
        else { return node; }   // Duplicate IDs are not allowed

        // Unwind phase: Recalculate height and check balance
        node.setHeight(1 + Math.max(height(node.getLeftChild()), height(node.getRightChild())));
        int balance = getBalance(node);

        // Rotation Logic

        // Left-Left Case
        if (balance > 1 && team.getID() < node.getLeftChild().getTeam().getID()) {
            return rotateRight(node);
        }

        // Right-Right case
        if (balance < -1 && team.getID() > node.getRightChild().getTeam().getID()) {
            return rotateLeft(node);
        }

        // Left-Right case
        if (balance > 1 && team.getID() > node.getLeftChild().getTeam().getID()) {
            node.setLeftChild(rotateLeft(node.getLeftChild()));
            return rotateRight(node);
        }

        // Right-Left case
        if (balance < -1 && team.getID() < node.getRightChild().getTeam().getID()) {
            node.setRightChild(rotateRight(node.getRightChild()));
            return rotateLeft(node);
        }

        return node;
    }

    /**
     * Triggers during "Left-Heavy" case
     * Left child becomes new parent and original parent is pushed down to the right
    */
    private AVLNode rotateRight(AVLNode y) {
        // Get nodes
        AVLNode x = y.getLeftChild();
        AVLNode T2 = x.getRightChild();

        // Perform rotation
        x.setRightChild(y);
        y.setLeftChild(T2);

        // Update heights
        y.setHeight(Math.max(height(y.getLeftChild()), height(y.getRightChild())) + 1);
        x.setHeight(Math.max(height(x.getLeftChild()), height(x.getRightChild())) + 1);

        return x;
    }

    /**
     * Triggers during "Right-Heavy" case
     * Right child becomes new parent and original parent is pushed down to the left
     */
    private AVLNode rotateLeft(AVLNode x) {
        // Get nodes
        AVLNode y = x.getRightChild();
        AVLNode T2 = y.getLeftChild();

        // Perform rotation
        y.setLeftChild(x);
        x.setRightChild(T2);

        // Update heights
        x.setHeight(Math.max(height(x.getLeftChild()), height(x.getRightChild())) + 1);
        y.setHeight(Math.max(height(y.getLeftChild()), height(y.getRightChild())) + 1);

        return y;
    }

}
