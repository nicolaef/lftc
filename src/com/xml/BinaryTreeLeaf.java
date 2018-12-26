package com.xml;

public class BinaryTreeLeaf {
    private BinaryTreeLeaf left;
    private BinaryTreeLeaf right;
    private String info;
    private BinaryTreeLeaf parent;
    private int id;

    public BinaryTreeLeaf(BinaryTreeLeaf left, BinaryTreeLeaf right, String info, int id, BinaryTreeLeaf parent) {
        this.left = left;
        this.right = right;
        this.info = info;
        this.parent = parent;
        this.id = id;
    }

    public BinaryTreeLeaf() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BinaryTreeLeaf getLeft() {
        return left;
    }

    public void setLeft(BinaryTreeLeaf left) {
        this.left = left;
    }

    public BinaryTreeLeaf getRight() {
        return right;
    }

    public void setRight(BinaryTreeLeaf right) {
        this.right = right;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public BinaryTreeLeaf getParent() {
        return parent;
    }

    public void setParent(BinaryTreeLeaf parent) {
        this.parent = parent;
    }
}
