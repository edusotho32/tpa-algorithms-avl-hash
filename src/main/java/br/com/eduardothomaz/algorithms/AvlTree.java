package br.com.eduardothomaz.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Ordered symbol table backed by an AVL tree. */
public final class AvlTree<K extends Comparable<K>, V> {
    private Node<K, V> root;

    public int size() {
        return size(root);
    }

    public boolean isEmpty() {
        return root == null;
    }

    /** Height of an empty tree is -1 and height of a leaf is 0. */
    public int height() {
        return height(root);
    }

    public K rootKey() {
        return root == null ? null : root.key;
    }

    public void put(K key, V value) {
        Objects.requireNonNull(key, "key");
        root = put(root, key, value);
    }

    public V get(K key) {
        Objects.requireNonNull(key, "key");
        Node<K, V> current = root;
        while (current != null) {
            int comparison = key.compareTo(current.key);
            if (comparison < 0) {
                current = current.left;
            } else if (comparison > 0) {
                current = current.right;
            } else {
                return current.value;
            }
        }
        return null;
    }

    public boolean containsKey(K key) {
        Objects.requireNonNull(key, "key");
        Node<K, V> current = root;
        while (current != null) {
            int comparison = key.compareTo(current.key);
            if (comparison < 0) {
                current = current.left;
            } else if (comparison > 0) {
                current = current.right;
            } else {
                return true;
            }
        }
        return false;
    }

    public K floor(K key) {
        Objects.requireNonNull(key, "key");
        Node<K, V> node = floor(root, key);
        return node == null ? null : node.key;
    }

    public K ceiling(K key) {
        Objects.requireNonNull(key, "key");
        Node<K, V> node = ceiling(root, key);
        return node == null ? null : node.key;
    }

    /** Number of keys strictly smaller than the argument. */
    public int rank(K key) {
        Objects.requireNonNull(key, "key");
        return rank(root, key);
    }

    public List<K> keysInOrder() {
        List<K> result = new ArrayList<K>(size());
        collectInOrder(root, result);
        return result;
    }

    public boolean isBalanced() {
        return checkBalance(root) != Integer.MIN_VALUE;
    }

    private Node<K, V> put(Node<K, V> node, K key, V value) {
        if (node == null) {
            return new Node<K, V>(key, value);
        }

        int comparison = key.compareTo(node.key);
        if (comparison < 0) {
            node.left = put(node.left, key, value);
        } else if (comparison > 0) {
            node.right = put(node.right, key, value);
        } else {
            node.value = value;
            return node;
        }

        update(node);
        return rebalance(node);
    }

    private Node<K, V> rebalance(Node<K, V> node) {
        int balance = balanceFactor(node);
        if (balance > 1) {
            if (balanceFactor(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }
        if (balance < -1) {
            if (balanceFactor(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }
        return node;
    }

    private Node<K, V> rotateRight(Node<K, V> rootNode) {
        Node<K, V> newRoot = rootNode.left;
        rootNode.left = newRoot.right;
        newRoot.right = rootNode;
        update(rootNode);
        update(newRoot);
        return newRoot;
    }

    private Node<K, V> rotateLeft(Node<K, V> rootNode) {
        Node<K, V> newRoot = rootNode.right;
        rootNode.right = newRoot.left;
        newRoot.left = rootNode;
        update(rootNode);
        update(newRoot);
        return newRoot;
    }

    private Node<K, V> floor(Node<K, V> node, K key) {
        if (node == null) return null;
        int comparison = key.compareTo(node.key);
        if (comparison == 0) return node;
        if (comparison < 0) return floor(node.left, key);
        Node<K, V> candidate = floor(node.right, key);
        return candidate == null ? node : candidate;
    }

    private Node<K, V> ceiling(Node<K, V> node, K key) {
        if (node == null) return null;
        int comparison = key.compareTo(node.key);
        if (comparison == 0) return node;
        if (comparison > 0) return ceiling(node.right, key);
        Node<K, V> candidate = ceiling(node.left, key);
        return candidate == null ? node : candidate;
    }

    private int rank(Node<K, V> node, K key) {
        if (node == null) return 0;
        int comparison = key.compareTo(node.key);
        if (comparison < 0) return rank(node.left, key);
        if (comparison > 0) return 1 + size(node.left) + rank(node.right, key);
        return size(node.left);
    }

    private void collectInOrder(Node<K, V> node, List<K> result) {
        if (node == null) return;
        collectInOrder(node.left, result);
        result.add(node.key);
        collectInOrder(node.right, result);
    }

    private int checkBalance(Node<K, V> node) {
        if (node == null) return -1;
        int leftHeight = checkBalance(node.left);
        if (leftHeight == Integer.MIN_VALUE) return Integer.MIN_VALUE;
        int rightHeight = checkBalance(node.right);
        if (rightHeight == Integer.MIN_VALUE) return Integer.MIN_VALUE;
        if (Math.abs(leftHeight - rightHeight) > 1) return Integer.MIN_VALUE;

        int expectedHeight = 1 + Math.max(leftHeight, rightHeight);
        int expectedSize = 1 + size(node.left) + size(node.right);
        if (node.height != expectedHeight || node.size != expectedSize) {
            return Integer.MIN_VALUE;
        }
        return expectedHeight;
    }

    private void update(Node<K, V> node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
        node.size = 1 + size(node.left) + size(node.right);
    }

    private int balanceFactor(Node<K, V> node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private int height(Node<K, V> node) {
        return node == null ? -1 : node.height;
    }

    private int size(Node<K, V> node) {
        return node == null ? 0 : node.size;
    }

    private static final class Node<K, V> {
        private final K key;
        private V value;
        private int height;
        private int size = 1;
        private Node<K, V> left;
        private Node<K, V> right;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
