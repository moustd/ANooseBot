package com.anoose.bot;

import com.github.bhlangonijr.chesslib.Board;

import java.util.HashMap;
import java.util.Map;

public interface NodeCache {
    Map<Board, Node> boardNodeCache = new HashMap<>();
}
