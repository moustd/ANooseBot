package com.anoose.bot;

import com.github.bhlangonijr.chesslib.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class NodeUtil {
    public static Node createNode(Board board) {

        return new Node(board);

    }

    public static void createTree(Node node, int depth) {
        if (depth > 0) {
            for (Node child : node.getChildren(true)) {
                createTree(child, depth - 1);
            }
        }
    }

    public static float minimax(Node node, int depth, Side maximizingPlayer, Side currentPlayer) {
        if (depth == 0 || node.getChildren() == null) {
            return node.getEvaluation();
        }
        if (currentPlayer.equals(maximizingPlayer)) {
            Optional<Float> max = node.getChildren(false).parallelStream()
                    .map(node1 -> minimax(node1, depth - 1, maximizingPlayer, currentPlayer.flip()))
                    .max(Comparator.naturalOrder());
            return max.orElse(Float.MIN_VALUE);
        } else {
            Optional<Float> min = node.getChildren(false).parallelStream()
                    .map(node1 -> minimax(node1, depth - 1, maximizingPlayer, currentPlayer))
                    .min(Comparator.naturalOrder());
            return min.orElse(Float.MAX_VALUE);

        }
    }


    public static float alphaBetaPruning(Node node, float alpha, float beta, Side maximizingPlayer) {
        if (node.getChildren() == null) {
            return node.getEvaluation();
        }
        Side sideToMove = node.getBoard().getSideToMove();
        if (sideToMove.equals(maximizingPlayer)) {
            float value = Float.MIN_VALUE;
            for (Node child : node.getChildren()) {
                value = Math.max(value, alphaBetaPruning(child, alpha, beta, maximizingPlayer.flip()));
                if (value >= beta) {
                    break;
                } else {
                    alpha = Math.max(alpha, value);
                }
            }
            return value;
        } else {
            float value = Float.MAX_VALUE;
            for (Node child : node.getChildren()) {
                value = Math.min(value, alphaBetaPruning(child, alpha, beta, maximizingPlayer));
                if (value <= alpha) {
                    break;
                } else {
                    alpha = Math.min(alpha, value);
                }
            }
            return value;
        }
    }

    public static float evaluateBoard(Board board) {

        float evaluation = 0f;
        Side sideToMove = board.getSideToMove();
        Side flipSideToMove = board.getSideToMove().flip();


        List<Square> sideLocations = board.getPieceLocation(Piece.make(sideToMove, PieceType.KING));
        List<Square> flipSideLocations = board.getPieceLocation(Piece.make(flipSideToMove, PieceType.KING));
        long sideCount = sideLocations.size();
        long flipSideCount = flipSideLocations.size();
        evaluation += 200 * (sideCount - flipSideCount);
        evaluation -= 200 * (sideLocations.stream().filter(square -> board.squareAttackedBy(square, flipSideToMove) != 0).count());
        evaluation += 200 * (flipSideLocations.stream().filter(square -> board.squareAttackedBy(square, sideToMove) != 0).count());

        sideLocations = board.getPieceLocation(Piece.make(sideToMove, PieceType.QUEEN));
        flipSideLocations = board.getPieceLocation(Piece.make(flipSideToMove, PieceType.QUEEN));
        sideCount = sideLocations.size();
        flipSideCount = flipSideLocations.size();
        evaluation += 9 * (sideCount - flipSideCount);
        evaluation -= 9 * (sideLocations.stream().filter(square -> board.squareAttackedBy(square, flipSideToMove) != 0).count());
        evaluation += 9 * (flipSideLocations.stream().filter(square -> board.squareAttackedBy(square, sideToMove) != 0).count());

        sideLocations = board.getPieceLocation(Piece.make(sideToMove, PieceType.ROOK));
        flipSideLocations = board.getPieceLocation(Piece.make(flipSideToMove, PieceType.ROOK));
        sideCount = sideLocations.size();
        flipSideCount = flipSideLocations.size();
        evaluation += 5 * (sideCount - flipSideCount);
        evaluation -= 5 * (sideLocations.stream().filter(square -> board.squareAttackedBy(square, flipSideToMove) != 0).count());
        evaluation += 5 * (flipSideLocations.stream().filter(square -> board.squareAttackedBy(square, sideToMove) != 0).count());

        sideLocations = board.getPieceLocation(Piece.make(sideToMove, PieceType.BISHOP));
        flipSideLocations = board.getPieceLocation(Piece.make(flipSideToMove, PieceType.BISHOP));
        sideCount = sideLocations.size();
        flipSideCount = flipSideLocations.size();
        evaluation += 3 * (sideCount - flipSideCount);
        evaluation -= 3 * (sideLocations.stream().filter(square -> board.squareAttackedBy(square, flipSideToMove) != 0).count());
        evaluation += 3 * (flipSideLocations.stream().filter(square -> board.squareAttackedBy(square, sideToMove) != 0).count());

        sideLocations = board.getPieceLocation(Piece.make(sideToMove, PieceType.KNIGHT));
        flipSideLocations = board.getPieceLocation(Piece.make(flipSideToMove, PieceType.KNIGHT));
        sideCount = sideLocations.size();
        flipSideCount = flipSideLocations.size();
        evaluation += 3 * (sideCount - flipSideCount);
        evaluation -= 3 * (sideLocations.stream().filter(square -> board.squareAttackedBy(square, flipSideToMove) != 0).count());
        evaluation += 3 * (flipSideLocations.stream().filter(square -> board.squareAttackedBy(square, sideToMove) != 0).count());

        sideLocations = board.getPieceLocation(Piece.make(sideToMove, PieceType.PAWN));
        flipSideLocations = board.getPieceLocation(Piece.make(flipSideToMove, PieceType.PAWN));
        sideCount = sideLocations.size();
        flipSideCount = flipSideLocations.size();
        evaluation += (sideCount - flipSideCount);
        evaluation -= (sideLocations.stream().filter(square -> board.squareAttackedBy(square, flipSideToMove) != 0).count());
        evaluation += (flipSideLocations.stream().filter(square -> board.squareAttackedBy(square, sideToMove) != 0).count());


        int sideLegalMoves = board.legalMoves().size();
        Board clone = board.clone();
        clone.setSideToMove(flipSideToMove);
        int flipSideLegalMoves = 0;
        try {
            flipSideLegalMoves = clone.legalMoves().size();
        } catch (Exception e) {
            e.printStackTrace();
        }


        evaluation += 0.1f * (sideLegalMoves - flipSideLegalMoves);
        return evaluation;
    }


}
