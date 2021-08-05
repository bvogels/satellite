import java.util.*;
import java.util.stream.Collectors;

class Satellite {

    int preorderIndex = 0; // An index variable for every entry in preorder.
    Tree tree = new Tree(null); // Is returned if preorder/inorder is empty

    /* This method communicates with the test cases. It takes the two lists, from which the tree can be
    re-build. The body is only executed if the return value of the isValid method is true. preorderIndex has
    to be reset to 0, otherwise the value from the previous test cases will interfere with the results.
    If preorder and inorder contain 0 elements, the second if statements becomes false. preorderIndex is then
    equal to the size of preorder, hence there will be no nodes and an empty tree is returned.
    If there is a valid list, createNode will build the tree.
     */

    public Tree treeFromTraversals(List<Character> preorder, List<Character> inorder) {
        if (isValid(preorder, inorder)) {
            preorderIndex = 0;
            if (preorderIndex < preorder.size()) {
                return new Tree(createNode(preorder, inorder));
            }
        }
        return tree;
    }

    /* The binary tree can be reconstructed from its preorder and inorder representation. The first element in the
    preorder list is invariably the root. The position of this element in the inorder list informs us about whats
    left and right of the root. The next element in the preorder list thus gives an element of the left or the right,
    which is node that has (or has not) further sub-nodes. Therefore a recursive algorithm is to be used.

    The method returns a node, since this is the output of the operation on the nodes. The recursion works its way down
    the lists, creating node objects. To get the recursion working, we need a base case to stop the recursion and valid
    inputs. The base case is reached when there is no further element to the left or to the right. Then the current node
    will be returned. If there still are elements, the remainder of the list (left or right) is the input for the
    next iteration of the recursion.
    */

    private Node createNode(List<Character> preorder, List<Character> inorder) {

        /* c stores the current character of preorder. preorderIndex is incremented subsequently. inorderIndex
        * holds the position of this character c in the inorder list. */
        Character c = preorder.get(preorderIndex);
        preorderIndex++;
        int inorderIndex = inorder.indexOf(c);

        Node n = new Node(c); // A new node is created.

        /* The remaining lists are stored in a new ArrayList. This is because operations on the actual list will alter
        it and make it useless for further operations.
         */
        ArrayList<Character> left = new ArrayList<>(inorder.subList(0, inorderIndex));
        ArrayList<Character> right = new ArrayList<>(inorder.subList(inorderIndex+1, inorder.size()));

        /* If there are elements to the left or to the right, the recursion has to go on. In this case, an empty list
        shows that there are no more characters.
         */
        if (!left.isEmpty() && !right.isEmpty()) {
            n.left = createNode(preorder, left);
            n.right = createNode(preorder, right);
        }

        return n; // The node is returned.
    }

    /* These are the exceptions that can occur and are tested. Most of the use Java API calls.*/

    private boolean isValid(List<Character> preorder, List<Character> inorder) {
        if (preorder.size() != inorder.size()) {
            throw new IllegalArgumentException("traversals must have the same length");
        }
        else if (!preorder.containsAll(inorder)) {
            throw new IllegalArgumentException("traversals must have the same elements");
        }
        else if (!uniqueItems(preorder, inorder)) { // This is a tougher one and is therefore outsourced to a method.
            throw new IllegalArgumentException("traversals must contain unique items");
        }
        else {
            return true;
        }
    }

    /* It is not that easy to determine if an elements appears multiple times in a list. A way to find out is to put
    them into a set, which holds only unique items, and, as a side-effect, will return false if an element is added more
    than once. In this case it is solved via a stream. I test both lists, although only preorder contains double
    elements, but it could be very well be the other way around.
     */
    private boolean uniqueItems(List<Character> preorder, List<Character> inorder) {
        List<List<Character>> orders = new ArrayList<>(Arrays.asList(preorder, inorder));
        for (List<Character> l : orders) {
            Set<Character> items = new HashSet<>();
            l.stream()
                    .filter(n -> !items.add(n))
                    .collect(Collectors.toSet());
            if (items.size() != l.size()) {
                return false;
            }
        }
        return true;
    }
}


