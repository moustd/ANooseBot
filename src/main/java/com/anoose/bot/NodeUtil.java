package com.anoose.bot;

import com.github.bhlangonijr.chesslib.Board;

public class NodeUtil {
    public static Node createNode(Board board) {
        Node node = NodeCache.boardNodeCache.get(board);
        if (node == null) {
            return new Node(board);
        } else {
            return node;
        }
    }

    public static void createTree(Node node, int depth) {
        if (depth > 0) {
            for (Node child : node.getChildren()) {
                createTree(child, depth - 1);
            }
        }
    }
  

}
