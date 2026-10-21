package br.com.eduardothomaz.algorithms;

import java.util.Arrays;
import java.util.Objects;

public final class AlgorithmTest {
    private int assertions;

    public static void main(String[] args) {
        AlgorithmTest suite = new AlgorithmTest();
        suite.hashUsesDivisionAndSeparateChaining();
        suite.hashUpdatesAndRemovesKeys();
        suite.hashResizesWithoutLosingEntries();
        suite.avlHandlesFourRotationCases();
        suite.avlSupportsOrderedOperations();
        System.out.println("PASS - " + suite.assertions + " assertions");
    }

    private void hashUsesDivisionAndSeparateChaining() {
        DivisionHashTable<Integer, String> hash = new DivisionHashTable<Integer, String>(5);
        hash.put(1, "one");
        hash.put(6, "six");
        hash.put(11, "eleven");

        equal(1, hash.bucketIndex(1), "division method for key 1");
        equal(1, hash.bucketIndex(6), "collision for key 6");
        equal(1, hash.bucketIndex(11), "collision for key 11");
        equal("six", hash.get(6), "retrieve from collision chain");
        equal(3, hash.size(), "hash size");
    }

    private void hashUpdatesAndRemovesKeys() {
        DivisionHashTable<Integer, String> hash = new DivisionHashTable<Integer, String>(8);
        hash.put(2, "old");
        equal("old", hash.put(2, "new"), "returns previous value");
        equal(1, hash.size(), "update does not duplicate key");
        equal("new", hash.remove(2), "remove returns value");
        check(!hash.containsKey(2), "removed key must be absent");
    }

    private void hashResizesWithoutLosingEntries() {
        DivisionHashTable<Integer, Integer> hash = new DivisionHashTable<Integer, Integer>(4);
        for (int value = 0; value < 100; value++) {
            hash.put(value, value * value);
        }
        check(hash.capacity() > 4, "hash must resize");
        equal(Integer.valueOf(9801), hash.get(99), "entry survives resize");
        equal(100, hash.size(), "all entries counted after resize");
    }

    private void avlHandlesFourRotationCases() {
        equal(Integer.valueOf(20), treeWith(30, 20, 10).rootKey(), "LL rotation");
        equal(Integer.valueOf(20), treeWith(10, 20, 30).rootKey(), "RR rotation");
        equal(Integer.valueOf(20), treeWith(30, 10, 20).rootKey(), "LR rotation");
        equal(Integer.valueOf(20), treeWith(10, 30, 20).rootKey(), "RL rotation");
    }

    private void avlSupportsOrderedOperations() {
        AvlTree<Integer, String> tree = treeWith(40, 20, 60, 10, 30, 50, 70, 25);
        equal(Arrays.asList(10, 20, 25, 30, 40, 50, 60, 70), tree.keysInOrder(), "in-order traversal");
        equal(Integer.valueOf(25), tree.floor(27), "floor");
        equal(Integer.valueOf(30), tree.ceiling(27), "ceiling");
        equal(4, tree.rank(40), "rank");
        equal("value-50", tree.get(50), "lookup");
        check(tree.isBalanced(), "AVL invariants");
        check(tree.height() <= 3, "logarithmic height for sample");
    }

    private AvlTree<Integer, String> treeWith(int... keys) {
        AvlTree<Integer, String> tree = new AvlTree<Integer, String>();
        for (int key : keys) {
            tree.put(key, "value-" + key);
        }
        check(tree.isBalanced(), "tree must remain balanced");
        return tree;
    }

    private void check(boolean condition, String message) {
        assertions++;
        if (!condition) throw new AssertionError(message);
    }

    private void equal(Object expected, Object actual, String message) {
        assertions++;
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError(message + ": expected=" + expected + ", actual=" + actual);
        }
    }
}
