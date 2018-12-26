package com.xml;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BinaryTree {
    private BinaryTreeLeaf root;
    private int current = 0;

    public BinaryTree() {
    }

    public BinaryTreeLeaf poz(String atom) {
        if (root == null) {
            root = new BinaryTreeLeaf(null, null, atom, current, null);
            return root;
        }
        return recPoz(root, atom);
    }

    private BinaryTreeLeaf recPoz(BinaryTreeLeaf root, String atom) {
        if (root.getInfo().equals(atom)) {
            return root;
        }
        if (root.getInfo().compareTo(atom) < 0) {
            if (root.getLeft() != null)
                return recPoz(root.getLeft(), atom);
            root.setLeft(new BinaryTreeLeaf(null, null, atom, current, root));
            return root.getLeft();
        }
        if (root.getRight() != null) {
            return recPoz(root.getRight(), atom);
        }
        root.setRight(new BinaryTreeLeaf(null, null, atom, current, root));
        return root.getRight();
    }

    private List<BinaryTreeLeaf> middle(BinaryTreeLeaf l) {
        if (l == null) {
            return new ArrayList<>();
        }
        List<BinaryTreeLeaf> list = middle(l.getRight());
        list.add(l);
        list.addAll(middle(l.getLeft()));
        return list;
    }

    public List<Pair<Integer, String>> ordered() {
        List<BinaryTreeLeaf> names = middle(root);
        return names.stream().map(i -> new Pair<>(i.getId(), i.getInfo())).collect(Collectors.toList());
    }
}
