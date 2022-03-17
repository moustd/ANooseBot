package com.anoose.bot;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;

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
            for (Node child : node.getChildren(true)) {
                createTree(child, depth - 1);
            }
        }
    }

    public static float minimax(Node node, int depth, Side maximizingPlayer) {
        if (depth == 0 || node.getChildren() == null) {
            return node.getEvaluation();
        }
        Side sideToMove = node.getBoard().getSideToMove();
        if (sideToMove.equals(maximizingPlayer)) {
            float value = Float.MIN_VALUE;
            for (Node child : node.getChildren(false)) {
                value = Math.max(value, minimax(child, depth--, maximizingPlayer.flip()));
            }
            return value;
        } else {
            float value = Float.MAX_VALUE;
            for (Node child : node.getChildren(false)) {
                value = Math.min(value, minimax(child, depth--, maximizingPlayer));
            }
            return value;
        }
    }


}
