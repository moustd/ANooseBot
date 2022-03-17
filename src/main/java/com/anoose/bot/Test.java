package com.anoose.bot;

import com.github.bhlangonijr.chesslib.Board;

public class Test {
    public static void main(String[] args) {
        Board board = new Board();
        long start = System.nanoTime();
        Node node = NodeUtil.createNode(board);
        NodeUtil.createTree(node, 4);
        long stop = System.nanoTime();
        System.out.printf("%.3f%n", (stop - start) / 1_000_000_000f);
        System.out.println(node.getCount());
    }
}
