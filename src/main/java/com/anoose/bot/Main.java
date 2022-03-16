package com.anoose.bot;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;

public class Main {
    public static void main(String[] args) {
//        Client lichessClient = Client.auth("lip_lmoscUCGtpF9EHC792Bv");
        // Creates a new chessboard in the standard initial position
        Board board = new Board();
        long start = System.nanoTime();
        Node tree = createTree(board, 4);
        long stop = System.nanoTime();
        System.out.printf("%.2f%n", (stop - start) / 1_000_000_000f);
        System.out.println(tree.getCount());
    }

    public static Node createNode(Board board) {
        Node node = NodeCache.boardNodeCache.get(board);
        if (node == null) {
            return new Node(board, board.legalMoves());
        } else {
            return node;
        }
    }

    public static Node createTree(Board board, int depth) {
        Node node = createNode(board);
        if (depth > 0) {
            for (Move move : node.getMoves()) {
                Board clone = board.clone();
                if (clone.doMove(move)) {
                    node.getChildren().add(createTree(clone, depth - 1));
                }
            }
        }
        return node;
    }
}
