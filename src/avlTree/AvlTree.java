package avlTree;

import java.util.AbstractSet;
import java.util.Iterator;


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
        root = add(root, t);
        size++;
        return true;
    }

    private Node<T> add(Node<T> node, T t) {
        if(node == null) {
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
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
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

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public int size() {
        return size;
    }
}
