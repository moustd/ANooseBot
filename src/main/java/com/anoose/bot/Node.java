package com.anoose.bot;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Node {
    private Node parent;
    private Board board;
    private Node node;
    private List<Move> moves = null;
    private List<Node> children = null;

    private Integer alpha = null;
    private Integer beta = null;


    public Node(Board board) {
        this.board = board;
    }

    public Node(Board board, Node node) {
        this.board = board;
        this.node = node;
    }

    public Node(Board board, List<Move> moves) {
        this.board = board;
        this.moves = moves;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public List<Move> getMoves() {
        if (moves == null) {
            moves = board.legalMoves();
        }
        return moves;
    }

    public void setMoves(List<Move> moves) {
        this.moves = moves;
    }


    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public List<Node> getChildren() {
        if (children == null) {
            children = new ArrayList<>();
            for (Move move : getMoves()) {
                Board clone = board.clone();
                if (clone.doMove(move)) {
                    //todo add filtering alpha/beta and null moving pruning
                    children.add(new Node(clone, this));
                }
            }
        }
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(parent, node.parent) && Objects.equals(board, node.board) && Objects.equals(moves, node.moves) && Objects.equals(children, node.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, board, moves, children);
    }

    @Override
    public String toString() {
        return "Node{" +
                "board=" + board +
                ", moves=" + moves +
                '}';
    }

    public int getCount() {
        int count = 0;
        if (children != null) {
            for (Node child : children) {
                count += child.getCount();
            }
            count += children.size();
        }
        return count;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public int getAlpha() {
        return alpha;
    }


    public int getBeta() {
        return beta;
    }


}
