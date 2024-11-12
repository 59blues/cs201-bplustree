package edu.smu.smusql;

import java.util.*;

public class BPlusTree {
    private static final int ORDER = 4;
    private Node root;
    private Node firstLeaf;

    private class Node {
        List<Integer> keys;
        List<Node> children;
        List<Object> values;  // Only used in leaf nodes
        boolean isLeaf;
        Node next;           // Only used in leaf nodes
        
        Node(boolean isLeaf) {
            this.keys = new ArrayList<>();
            this.children = new ArrayList<>();
            this.isLeaf = isLeaf;
            if (isLeaf) {
                this.values = new ArrayList<>();
            }
        }
    }

    public BPlusTree() {
        root = new Node(true);
        firstLeaf = root;
    }

    public void insert(int key, List<Object> value) {
        if (root.keys.size() == (2 * ORDER - 1)) {
            Node newRoot = new Node(false);
            newRoot.children.add(root);
            splitChild(newRoot, 0);
            root = newRoot;
        }
        insertNonFull(root, key, value);
    }

    private void insertNonFull(Node node, int key, List<Object> value) {
        int i = node.keys.size() - 1;
        
        if (node.isLeaf) {
            while (i >= 0 && key < node.keys.get(i)) {
                i--;
            }
            i++;
            node.keys.add(i, key);
            node.values.add(i, value);
        } else {
            while (i >= 0 && key < node.keys.get(i)) {
                i--;
            }
            i++;
            
            if (node.children.get(i).keys.size() == (2 * ORDER - 1)) {
                splitChild(node, i);
                if (key > node.keys.get(i)) {
                    i++;
                }
            }
            insertNonFull(node.children.get(i), key, value);
        }
    }

    private void splitChild(Node parent, int index) {
        Node child = parent.children.get(index);
        Node newNode = new Node(child.isLeaf);
        
        parent.keys.add(index, child.keys.get(ORDER - 1));
        
        for (int i = 0; i < ORDER - 1; i++) {
            newNode.keys.add(child.keys.get(i + ORDER));
            if (child.isLeaf) {
                newNode.values.add(child.values.get(i + ORDER));
            }
        }
        
        if (!child.isLeaf) {
            for (int i = 0; i < ORDER; i++) {
                newNode.children.add(child.children.get(i + ORDER));
            }
        }
        
        for (int i = 0; i < ORDER; i++) {
            child.keys.remove(child.keys.size() - 1);
            if (child.isLeaf) {
                child.values.remove(child.values.size() - 1);
            } else {
                child.children.remove(child.children.size() - 1);
            }
        }
        
        parent.children.add(index + 1, newNode);
        
        if (child.isLeaf) {
            newNode.next = child.next;
            child.next = newNode;
            if (child == firstLeaf) {
                firstLeaf = child;
            }
        }
    }

    public List<List<Object>> search(int key) {
        return searchNode(root, key);
    }

    private List<List<Object>> searchNode(Node node, int key) {
        int i = 0;
        while (i < node.keys.size() && key > node.keys.get(i)) {
            i++;
        }
        
        if (node.isLeaf) {
            if (i < node.keys.size() && key == node.keys.get(i)) {
                List<List<Object>> result = new ArrayList<>();
                result.add((List<Object>) node.values.get(i));
                return result;
            }
            return new ArrayList<>();
        }
        
        return searchNode(node.children.get(i), key);
    }

    public List<List<Object>> rangeSearch(int startKey, int endKey) {
        List<List<Object>> result = new ArrayList<>();
        Node node = findLeaf(root, startKey);
        
        while (node != null) {
            for (int i = 0; i < node.keys.size(); i++) {
                if (node.keys.get(i) >= startKey && node.keys.get(i) <= endKey) {
                    result.add((List<Object>) node.values.get(i));
                }
                if (node.keys.get(i) > endKey) {
                    return result;
                }
            }
            node = node.next;
        }
        return result;
    }

    private Node findLeaf(Node node, int key) {
        if (node.isLeaf) return node;
        
        int i = 0;
        while (i < node.keys.size() && key > node.keys.get(i)) {
            i++;
        }
        return findLeaf(node.children.get(i), key);
    }

    public void delete(int key) {
        deleteKey(root, key);
    }

    private void deleteKey(Node node, int key) {
        int index = findKeyIndex(node, key);
        
        if (node.isLeaf) {
            if (index < node.keys.size() && node.keys.get(index) == key) {
                node.keys.remove(index);
                node.values.remove(index);
            }
        } else {
            if (index < node.keys.size() && node.keys.get(index) == key) {
                deleteInternalNode(node, key, index);
            } else {
                deleteKey(node.children.get(index), key);
            }
        }
    }

    private void deleteInternalNode(Node node, int key, int index) {
        Node predecessor = getMaxKey(node.children.get(index));
        node.keys.set(index, predecessor.keys.get(predecessor.keys.size() - 1));
        deleteKey(node.children.get(index), predecessor.keys.get(predecessor.keys.size() - 1));
    }

    private Node getMaxKey(Node node) {
        if (node.isLeaf) return node;
        return getMaxKey(node.children.get(node.children.size() - 1));
    }

    private int findKeyIndex(Node node, int key) {
        int index = 0;
        while (index < node.keys.size() && key > node.keys.get(index)) {
            index++;
        }
        return index;
    }
}
