package avlTree;

import org.junit.Test;

import java.util.Iterator;
import java.util.Random;

import static org.junit.Assert.*;

public class AvlTreeTest {

    @Test
    public void addTest() {
        AvlTree<Integer> tree = new AvlTree<>();
        tree.add(10);
        tree.add(3);
        tree.add(15);
        tree.add(5);
        tree.add(7);
        tree.add(2);
        assertTrue(tree.checkBalance());
        assertTrue(tree.contains(7));
        assertEquals(6, tree.size());
        tree.add(15);
        assertEquals(6, tree.size());
        assertTrue(tree.contains(15));
        assertTrue(tree.checkInvariant());
        tree.add(12);
        tree.add(17);
        tree.add(16);
        tree.add(20);
        assertTrue(tree.checkBalance());
        assertEquals(10, tree.size());
        tree.add(25);
        tree.add(23);
        tree.add(24);
        assertTrue(tree.checkBalance());
        assertTrue(tree.checkInvariant());
        assertEquals(13, tree.size());
    }

    @Test
    public void addRandomTest() {
        Random r = new Random();
        AvlTree<Integer> tree = new AvlTree<>();
        int SIZE = 100;
        int[] arr = new int[SIZE];
        for (int i=0; i<SIZE; i++) {
            arr[i] = r.nextInt();
            tree.add(arr[i]);
        }
        assertEquals(100, tree.size());
        for (int i=0; i<SIZE; i++) {
            assertTrue(tree.contains(arr[i]));
        }
        assertTrue(tree.checkBalance());
        assertTrue(tree.checkInvariant());
    }

    @Test
    public void removeTest() {
        AvlTree<Integer> tree = new AvlTree<>();
        tree.add(10);
        tree.add(5);
        tree.add(15);
        tree.add(3);
        tree.add(7);
        tree.add(2);
        tree.remove(3);
        assertFalse(tree.contains(3));
        assertEquals(5, tree.size());
        assertTrue(tree.checkBalance());
        tree.add(12);
        tree.add(17);
        tree.add(16);
        tree.add(20);
        tree.remove(15);
        assertFalse(tree.contains(15));
        assertEquals(8, tree.size());
        assertTrue(tree.checkBalance());
        tree.add(25);
        tree.add(23);
        tree.add(24);
        tree.remove(20);
        assertFalse(tree.contains(20));
        assertEquals(10, tree.size());
        assertTrue(tree.checkBalance());
    }

    @Test
    public void containsTest() {
        AvlTree<Integer> tree = new AvlTree<>();
        tree.add(20);
        tree.add(30);
        assertTrue(tree.contains(20));
        assertTrue(tree.contains(30));
        assertFalse(tree.contains(10));
        tree.remove(30);
        assertFalse(tree.contains(30));
    }

    @Test
    public void iteratorTest() {
        AvlTree<Integer> tree = new AvlTree<>();
        tree.add(10);
        tree.add(3);
        tree.add(15);
        tree.add(5);
        tree.add(7);
        tree.add(2);
        Iterator<Integer> iterator = tree.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(3, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(5, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(7, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(10, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(15, iterator.next().intValue());
        assertFalse(iterator.hasNext());
    }
}
