package com.anoose.bot;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Node {
    private Node parent;
    private Board board;
    private Move move;
    private Node node;
    private List<Move> moves = null;
    private List<Node> children = null;
    private Float evaluation = null;


    private Integer alpha = null;
    private Integer beta = null;


    public Node(Board board) {
        this.board = board;

    }

    public Node(Board board, Node node, Move move) {
        this.board = board;
        this.node = node;
        this.move = move;
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
        return getChildren(false);
    }

    public List<Node> getChildren(boolean search) {
        if (children == null && search) {
            children = getMoves().parallelStream().map(move1 -> {
                        Board clone = board.clone();
                        if (clone.doMove(move1)) {
                            //todo add filtering alpha/beta and null moving pruning
                            return new Node(clone, this, move1);
                        } else {
                            return null;
                        }
                    }).filter(Objects::nonNull)
                    .filter(node1 -> NodeUtil.alphaBetaPruning(node1, Float.MIN_VALUE, Float.MAX_VALUE, board.getSideToMove()) >= 0)
                    .sorted(Comparator.comparing(Node::getEvaluation))
                    .collect(Collectors.toList());
        }
        return children;
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


    public float getEvaluation() {
        if (evaluation == null) {
            evaluation = NodeUtil.evaluateBoard(board);
        }
        return evaluation;
    }

    public Move getMove() {
        return move;
    }

}
