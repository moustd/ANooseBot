package com.anoose.bot;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.PieceType;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.move.Move;

import java.util.*;

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
            children = new ArrayList<>();
            for (Move move : getMoves()) {
                Board clone = board.clone();
                if (clone.doMove(move)) {
                    //todo add filtering alpha/beta and null moving pruning
                    children.add(new Node(clone, this, move));
                }
            }
            children.sort(Comparator.comparing(Node::getEvaluation));
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


//    int alphaBetaMax( int alpha, int beta, int depthleft ) {
//        if ( depthleft == 0 ) return evaluate();
//        for ( all moves) {
//            score = alphaBetaMin( alpha, beta, depthleft - 1 );
//            if( score >= beta )
//                return beta;   // fail hard beta-cutoff
//            if( score > alpha )
//                alpha = score; // alpha acts like max in MiniMax
//        }
//        return alpha;
//    }
//
//    int alphaBetaMin( int alpha, int beta, int depthleft ) {
//        if ( depthleft == 0 ) return -evaluate();
//        for ( all moves) {
//            score = alphaBetaMax( alpha, beta, depthleft - 1 );
//            if( score <= alpha )
//                return alpha; // fail hard alpha-cutoff
//            if( score < beta )
//                beta = score; // beta acts like min in MiniMax
//        }
//        return beta;
//    }

    public float getEvaluation() {
        if (evaluation == null) {
            evaluation = 0f;

            Side sideToMove = board.getSideToMove();

            Map<PieceType, Float> pieceWeightMap = new HashMap<>();
            pieceWeightMap.put(PieceType.KING, 200f);
            pieceWeightMap.put(PieceType.QUEEN, 9f);
            pieceWeightMap.put(PieceType.ROOK, 5f);
            pieceWeightMap.put(PieceType.BISHOP, 3f);
            pieceWeightMap.put(PieceType.KNIGHT, 3f);
            pieceWeightMap.put(PieceType.PAWN, 1f);

            for (PieceType pieceType : Arrays.stream(PieceType.values()).filter(pieceType -> pieceType != PieceType.NONE).toList()) {
                int sideCount = board.getPieceLocation(Piece.make(sideToMove, pieceType)).size();
                int flipSideCount = board.getPieceLocation(Piece.make(sideToMove.flip(), pieceType)).size();
                evaluation += pieceWeightMap.get(pieceType) * (sideCount - flipSideCount);
            }

            int sideLegalMoves = board.legalMoves().size();
            Board clone = board.clone();
            clone.setSideToMove(sideToMove.flip());
            int flipSideLegalMoves = clone.legalMoves().size();


            evaluation += 0.1f * (sideLegalMoves - flipSideLegalMoves);
        }
        return evaluation;
    }

    public Move getMove() {
        return move;
    }

}
