/**
 * This class is an unbalanced BST of records
 * 
 * It has insert,remove,search,getHeight,getMinimum,getMaximum and print methods
 */
public class RecordBST {

    protected Node root;// root node of BST

    /**
     * Constructs an empty BST
     */
    public RecordBST() {
        root = null;
    }

    /**
     * inner node class that hold the record and the left and right nodes
     */
    public class Node {
        public Record value; // holds the record within the node
        public Node left; //left node
        public Node right; //right node

        protected int balanceFactor;//height of left subtree - height of right subtree

        /**
         * constructs a node with a new record
         * both of its children are null
         * and its balance factor is set to 0
         * 
         * @param record the value the node is to store
         */
        public Node(Record value) {
            this.value = value;
            this.left = null;
            this.right = null;
            this.balanceFactor = 0;
        }

        /**
         * returns the height of the leaf node and it children
         * 
         * @return the height of the subtree and its node 
         */
        public int getHeight() {
            int leftHeight = (left == null) ? -1 : left.getHeight();
            int rightHeight = (right == null) ? -1 : right.getHeight();
            return 1 + Math.max(leftHeight, rightHeight);
        }

        /**
         * updates the balance factor
         */
        public void updateNode() {
            int leftHeight = (left == null) ? -1 : left.getHeight();
            int rightHeight = (right == null) ? -1 : right.getHeight();
            this.balanceFactor = leftHeight - rightHeight;
        }
    }

    /**
     *  Inserts a record into the BST
     * 
     *  @param record is the record being inserted
     */
    public void insert(Record record) {
        root = insertRec(root, record);
    }

    /** 
     * recursive insertion helper
     * Maintains sort order
     * doesnt allow duplicates
     * 
     * @param node is the current subtree root
     * @param record is the record being inserted
     * 
     * @return updated subtree root
     */
    protected Node insertRec(Node node, Record record) {
        if (node == null) return new Node(record);

        int comparisonNum = record.compareTo(node.value);

        if (comparisonNum < 0) {
            node.left = insertRec(node.left, record);
        } else if (comparisonNum > 0) {
            node.right = insertRec(node.right, record);
        }

        return node;
    }
    /**
     *  Removes a record into the BST
     * 
     *  @param record is the record being removed
     */
    public void remove(Record record) {
        root = removeRec(root, record);
    }

    /** 
     * recursive helper for remove
     * 
     * @param node is the current subtree root
     * @param record is the record being removed
     * 
     * @return updated subtree root
     */    
    protected Node removeRec(Node node, Record record) {
        if (node == null) return null;

        int comparisonNum = record.compareTo(node.value);

        if (comparisonNum < 0) {
            node.left = removeRec(node.left, record);
        } else if (comparisonNum > 0) {
            node.right = removeRec(node.right, record);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            Node successor = findMin(node.right);
            node.value = successor.value;
            node.right = removeRec(node.right, successor.value);
        }

        return node;
    }

    /**
     * Searchs the tree for a record
     * 
     * @param record is the record being searched for
     * @return true if the record is found, return false if not
     */
    public boolean search(Record record) {
        return searchRec(root, record);
    }

    /**
     * recursive helper for search
     * 
     * @param node is the current subtree root
     * @param record is the record being searched for
     * 
     * @return true if the record is found, return false if not
     */
    private boolean searchRec(Node node, Record record) {
        if (node == null) return false;

        int comparisonNum = record.compareTo(node.value);

        if (comparisonNum == 0) return true;
        if (comparisonNum < 0) return searchRec(node.left, record);
        return searchRec(node.right, record);
    }

    /**
     * returns total height of bst
     * 
     * @return bst height or 0 if empty
     */
    public int getHeight() {
        return (root == null) ? -1 : root.getHeight();
    }

    /**
     * finds the left most record in bst(minimum)
     * 
     * @return left most record or null if tree is empty
     */
    public Record getMinimum() {
        if (root == null) return null;
        return findMin(root).value;
    }

    /**
     * finds left most node in subtree
     * is also called from remove node
     * 
     * @return left most node in subtree
     */
    protected Node findMin(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    /**
     * Gets the right most node in the bst
     * 
     * @return maximum record or null if tree is empty
     */
    public Record getMaximum() {
        if (root == null) return null;
        Node current = root;
        while (current.right != null) current = current.right;
        return current.value;
    }
    
    /**
     * prints all records in the bst in ascending order
     */
    public void print() {
        printRec(root);
    }

    /**
     * recursivly prints all records
     * 
     *  @param node current subtree root
     */
    private void printRec(Node node) {
        if (node == null) return;

        printRec(node.left);
        System.out.println(node.value);
        printRec(node.right);
    }

    /**
     * prints all records with the given genre, in acending year order
     * 
     * @param genre to filter by
     */
    public void printGenre(String genre) {
        printGenreRec(root, genre);
    }

    /**
     * Recursive help function for printing by genre
     * ignores irralevant sub trees
     * 
     * @param node current subtree root
     * @param  genre to filter by
     */
    private void printGenreRec(Node node, String genre) {
        if (node == null) return;

        int genreCN = genre.compareToIgnoreCase(node.value.getGenre());

        if (genreCN < 0) {
            printGenreRec(node.left, genre);
        } else if (genreCN >0) {
            printGenreRec(node.right, genre);
        } else {
            printGenreRec(node.left, genre);
           System.out.println(node.value);
            printGenreRec(node.right, genre);
        }
    }

    /**
     * Prints all records in the given genre with a earlist and latest year
     * 
     *  @param  genre to filter by
     * @param earliest year to filter by
     *  @param latest year to filter by
     */
    public void printGenreWithYearRange(String genre, int earliest, int latest) {
        printGenreYearRangeRec(root, genre, earliest, latest);
    }

    /**
     * recursive helper for printGenreWithYearRange
     * ignores irrelevant subtree
     * 
     * @param node is the current subtree root
     * @param  genre to filter by
     * @param earliest year to filter by
     *  @param latest year to filter by
     */
    private void printGenreYearRangeRec(Node node, String genre, int earliest, int latest) {
        if (node == null) return;

        int genreCN = genre.compareToIgnoreCase(node.value.getGenre());

        if (genreCN < 0) {
            printGenreYearRangeRec(node.left, genre, earliest, latest);
        } else if (genreCN > 0) {
            printGenreYearRangeRec(node.right, genre, earliest, latest);
        } else {
            printGenreYearRangeRec(node.left, genre, earliest, latest);

            int year = node.value.getYear();
            if (year > earliest && year < latest) {
                System.out.println(node.value);
            }

            printGenreYearRangeRec(node.right, genre, earliest, latest);
        }
    }

    /**
     * prints all records in a genre before a certain year in ascending year order
     * 
     * @param  genre to filter by
     * @param latest upper bound year to filter by
     */
    public void printGenreBelowYear(String genre, int latest) {
        printGenreBelowYearRec(root, genre, latest);
    }

    /**
     * Recursive helper function for printGenreBelowYear
     * 
     * @param node current subtree root
     * @param  genre to filter by
     * @param latest upper bound year to filter by
     * 
     */
    private void printGenreBelowYearRec(Node node, String genre, int latest) {
        if (node == null) return;

        int genreCN = genre.compareToIgnoreCase(node.value.getGenre());

        if (genreCN < 0) {
            printGenreBelowYearRec(node.left, genre,latest);
        } else if (genreCN > 0) {
            printGenreBelowYearRec(node.right, genre,latest);
        } else {
            printGenreBelowYearRec(node.left, genre,latest);

            int year = node.value.getYear();
            if (year < latest) {
                System.out.println(node.value);
            }

            printGenreBelowYearRec(node.right, genre, latest);
        }
    }
}
