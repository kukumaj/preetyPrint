package com.epam.rd.autocode.bstprettyprint;

import java.util.ArrayList;
import java.util.List;

public class PrintableTreeImpl implements PrintableTree {
    public static void main(String[] args) {
        PrintableTree printableTree = new PrintableTreeImpl();
        printableTree.add(123);
        printableTree.add(11);
        printableTree.add(200);
        printableTree.add(1);
        printableTree.add(100);
        printableTree.add(150);
        printableTree.add(2000);

        System.out.println(printableTree.prettyPrint());

    }

    Node root = null;

    private Node addRecursive(Node current, int value) {
        if (current == null) {
            return new Node(value);
        }

        if (value > current.value) {
            current.down = addRecursive(current.down, value);
        } else if (value < current.value) {
            current.top = addRecursive(current.top, value);
        }
        return current;
    }

    @Override
    public void add(int i) {
        if (root == null) {
            root = new Node(i);
            return;
        }
        addRecursive(root, i);

    }

    private static void print(List<StringBuffer> canvas, int x, int y, String string) {
        while (canvas.size() - 1 < x) {
            canvas.add(new StringBuffer());
        }

        StringBuffer row = canvas.get(x);
        while (row.length() - 1 < y - 1) {
            row.append(' ');
        }

        row.insert(y, string);
    }

    @Override
    public String prettyPrint() {
        List<StringBuffer> canvas = new ArrayList<>();
        String firstElementString = String.valueOf(root.value);
        int firstElementX = countNodeElements(root.top);
        print(canvas, firstElementX, 0, firstElementString);

        int connectorY = firstElementString.length();

        {
            String connector = null;

            if (root.top == null && root.down != null) {
                connector = "┐";
            }
            if (root.top != null && root.down == null) {
                connector = "┘";
            }
            if (root.top != null && root.down != null) {
                connector = "┤";
            }
            if (root.top == null && root.down == null) {
                return null;
            }


            print(canvas, firstElementX, connectorY, connector);
        }

        if (root.top != null) {
            int pipeCount;
            if (root.top.down == null) {
                pipeCount = 0;
            } else {
                pipeCount = countNodeElements(root.top.down);
            }
            int currentPipeX = firstElementX - 1;
            for (int i = 0; i < pipeCount; i++) {
                print(canvas, currentPipeX, connectorY, "│");
                currentPipeX--;
            }
            print(canvas, currentPipeX, connectorY, "┌");
        }
        if (root.down != null) {
            int pipeCount;
            if (root.down.top == null) {
                pipeCount = 0;
            } else {
                pipeCount = countNodeElements(root.down.top);
            }
            int currentPipeX = firstElementX +1;
            for (int i = 0; i < pipeCount; i++) {
                print(canvas, currentPipeX, connectorY, "│");
                currentPipeX++;
            }
            print(canvas, currentPipeX, connectorY, "└");
        }

        canvas.forEach(System.out::println);
        return null;
    }

    private int countNodeElements(Node node) {
        int sum = 1;

        if (node.top != null) {
            sum += countNodeElements(node.top);
        }

        if (node.down != null) {
            sum += countNodeElements(node.down);
        }

        return sum;
    }

    private static class Node {
        int value;
        Node top, down;

        Node(int value) {
            this.value = value;
        }
    }
}
//more to be
