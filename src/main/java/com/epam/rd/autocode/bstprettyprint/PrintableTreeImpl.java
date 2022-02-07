package com.epam.rd.autocode.bstprettyprint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrintableTreeImpl implements PrintableTree {
    public static void main(String[] args) {
        PrintableTree printableTree = new PrintableTreeImpl();
        Arrays.asList(52, 415, 951, 97, 489, 656, 559, 387, 733, 765).forEach(printableTree::add);

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

    private static void printNodeToCanvas(List<StringBuffer> canvas, int startX, int startY, Node node) {
        String currentElementString = String.valueOf(node.value);
        print(canvas, startX, startY, currentElementString);

        int connectorY = startY + currentElementString.length();

        {
            String connector = null;

            if (node.top == null && node.down != null) {
                connector = "┐";
            }
            if (node.top != null && node.down == null) {
                connector = "┘";
            }
            if (node.top != null && node.down != null) {
                connector = "┤";
            }
            if (node.top == null && node.down == null) {
                return;
            }

            print(canvas, startX, connectorY, connector);
        }

        if (node.top != null) {
            int pipeCount;
            if (node.top.down == null) {
                pipeCount = 0;
            } else {
                pipeCount = countNodeElements(node.top.down);
            }
            int currentPipeX = startX - 1;
            for (int i = 0; i < pipeCount; i++) {
                print(canvas, currentPipeX, connectorY, "│");
                currentPipeX--;
            }
            print(canvas, currentPipeX, connectorY, "┌");
            printNodeToCanvas(canvas, currentPipeX, connectorY + 1, node.top);
        }
        if (node.down != null) {
            int pipeCount;
            if (node.down.top == null) {
                pipeCount = 0;
            } else {
                pipeCount = countNodeElements(node.down.top);
            }
            int currentPipeX = startX + 1;
            for (int i = 0; i < pipeCount; i++) {
                print(canvas, currentPipeX, connectorY, "│");
                currentPipeX++;
            }
            print(canvas, currentPipeX, connectorY, "└");
            printNodeToCanvas(canvas, currentPipeX, connectorY + 1, node.down);
        }
    }

    @Override
    public String prettyPrint() {
        List<StringBuffer> canvas = new ArrayList<>();
        int firstElementX;
        if (root.top != null) {
            firstElementX = countNodeElements(root.top);
        } else {
            firstElementX = 0;
        }
        printNodeToCanvas(canvas, firstElementX, 0, root);

        StringBuilder result = new StringBuilder();
        for (StringBuffer row : canvas) {
            result.append(row).append("\n");
        }

        // canvas.stream().map(StringBuffer::toString).reduce("", (acc, s) -> acc + s + '\n');   the same as 4 lines above
        return result.toString();
    }

    private static int countNodeElements(Node node) {
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
