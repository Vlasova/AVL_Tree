package avlTree;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;


public class AvlTree<T extends Comparable<T>> extends AbstractSet<T> {
    private static class Node<T> {
        Node<T> left = null;
        Node<T> right = null;
        final T value;
        int height = 1;

        Node(T value) {
            this.value = value;
        }

        int getHeight() {
            return height;
        }

        boolean checkBalance() {
            int leftHeight = (left == null) ? 0 : left.getHeight();
            int rightHeight = (right == null) ? 0 : right.getHeight();
            int difference = Math.abs(leftHeight - rightHeight);
            return difference == 1 || difference == 0;
        }

        int getBalance() {
            int leftHeight = (left == null) ? 0 : left.getHeight();
            int rightHeight = (right == null) ? 0 : right.getHeight();
            return rightHeight - leftHeight;
        }

        void fixHeight() {
            int leftHeight = (left == null) ? 0 : left.getHeight();
            int rightHeight = (right == null) ? 0 : right.getHeight();
            this.height = (leftHeight > rightHeight ? leftHeight : rightHeight) + 1;
        }
    }

    private int size = 0;
    private Node<T> root = null;

    @Override
    public boolean add(T t) {
        if (root == null) {
            root = new Node<>(t);
            size++;
            return true;
        }
        Node<T> nearest = findNearest(t);
        if (nearest.value == t) {
            return false;
        }
        root = add(root, t);
        size++;
        return true;
    }

    private Node<T> add(Node<T> node, T t) {
        if (node == null) {
            return new Node<>(t);
        }
        int comparison = t.compareTo(node.value);
        if (comparison == 0) {
            return node;
        }
        else if (comparison < 0) {
            node.left = add(node.left, t);
        }
        else {
            node.right = add(node.right, t);
        }
        return makeBalancing(node);
    }

    @Override
    public boolean remove(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        Node<T> node = findNode(t);
        if (node == null) return false;
        Node<T> parent = findParent(node);
        if (parent == null) {
            root = delete(node);
            size--;
            return true;
        }
        int comparison = node.value.compareTo(parent.value);
        assert comparison != 0;
        if (comparison < 0) {
            parent.left = delete(node);
        }
        else {
            parent.right = delete(node);
        }
        size--;
        return true;
    }

    private Node<T> findNearest(T value) {
        if (root == null) return null;
        return findNearest(root, value);
    }

    private Node<T> findNearest(Node<T> start, T value) {
        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return start;
        }
        else if (comparison < 0) {
            if (start.left == null) return start;
            return findNearest(start.left, value);
        }
        else {
            if (start.right == null) return start;
            return findNearest(start.right, value);
        }
    }

    private Node<T> findParent(Node<T> child) {
        if (root == null || child == null) return null;
        return findParent(root, child.value);
    }

    private Node<T> findParent(Node<T> start, T value) {
        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return null;
        }
        else if (comparison < 0) {
            if (start.left.value == value) return start;
            return findParent(start.left, value);
        }
        else {
            if(start.right.value == value) return start;
            return findParent(start.right, value);
        }
    }

    private Node<T> findNode(T value) {
        if (root == null) return null;
        return findNode(root, value);
    }

    private Node<T> findNode(Node<T> start, T value) {
        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return start;
        }
        else if (comparison < 0) {
            if (start.left == null) return null;
            return findNode(start.left, value);
        }
        else {
            if(start.right == null) return null;
            return findNode(start.right, value);
        }
    }

    private Node<T> makeRightTurn(Node<T> node) {
        Node<T> turnedNode = node.left;
        node.left = turnedNode.right;
        turnedNode.right = node;
        node.fixHeight();
        turnedNode.fixHeight();
        return turnedNode;
    }

    private Node<T> makeLeftTurn(Node<T> node) {
        Node<T> turnedNode = node.right;
        node.right = turnedNode.left;
        turnedNode.left = node;
        node.fixHeight();
        turnedNode.fixHeight();
        return turnedNode;
    }

    private Node<T> makeBalancing(Node<T> node) {
        node.fixHeight();
        if(node.checkBalance()) {
            return node;
        }
        int balance = node.getBalance();
        assert balance < 2 || balance > -2;
        if(balance == 2) {
            if(node.right.getBalance() < 0) {
                node.right = makeRightTurn(node.right);
            }
            return makeLeftTurn(node);
        }
        else {
            if(node.left.getBalance() > 0) {
                node.left = makeLeftTurn(node.left);
            }
            return makeRightTurn(node);
        }
    }

    public boolean checkBalance() {
        return root.checkBalance();
    }

    private Node<T> delete(Node<T> node) {
        assert node.checkBalance();
        if (node.right == null) {
            return node.left;
        }
        else {
            if (node.left == null) {
                return node.right;
            }
            Node<T> minimumLeft = node.right;
            Node<T> minimumLeftParent = node;
            while (minimumLeft.left != null) {
                minimumLeftParent = minimumLeft;
                minimumLeft = minimumLeft.left;
            }
            minimumLeft.left = node.left;
            if (minimumLeftParent != node) {
                minimumLeftParent.left = minimumLeft.right;
                minimumLeft.right = deleteMinimum(node.right);
            }
            return makeBalancing(minimumLeft);
        }
    }

    private Node<T> deleteMinimum(Node<T> node) {
        if (node.left == null) {
           return node;
        }
        node.left = deleteMinimum(node.left);
        return makeBalancing(node);
    }

    public boolean checkInvariant() {
        if (root == null) {
            return true;
        }
        return checkInvariant(root);
    }

    private boolean checkInvariant(Node<T> node) {
        Node<T> left = node.left;
        if (left != null && (left.value.compareTo(node.value) >= 0 || !checkInvariant(left))) return false;
        Node<T> right = node.right;
        return right == null || right.value.compareTo(node.value) > 0 && checkInvariant(right);
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        Node<T> nearest = findNearest(t);
        return nearest != null && nearest.value == t;
    }

    @Override
    public Iterator<T> iterator() {
        return new AvlTreeIterator();
    }

    @Override
    public int size() {
        return size;
    }

    public class AvlTreeIterator implements Iterator<T> {
        private Node<T> current;
        private Stack<Node<T>> stack = new Stack();

        private AvlTreeIterator() {
            Node<T> node = root;
            while (node != null) {
                stack.push(node);
                current = node;
                node = node.left;
            }
        }

        private Node<T> findNext() {
            Node<T> node = current.right;
            while (node != null) {
                stack.push(node);
                node = node.left;
            }
            return stack.pop();
        }

        @Override
        public boolean hasNext() {
            return !stack.empty() || current.right != null;
        }

        @Override
        public T next() {
            current = findNext();
            if (current == null) throw new NoSuchElementException();
            return current.value;
        }
    }
}
