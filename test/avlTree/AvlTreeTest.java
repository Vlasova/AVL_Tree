package avlTree;

import org.junit.Test;

import static org.junit.Assert.*;

public class AvlTreeTest {

    @Test
    public void addTest() {
        AvlTree<Integer> tree = new AvlTree<>();
        tree.add(10);
        tree.add(5);
        tree.add(15);
        tree.add(3);
        tree.add(7);
        tree.add(2);
        tree.checkBalance();
        assertEquals(tree.size(), 6);
    }
}
