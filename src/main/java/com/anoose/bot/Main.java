package com.anoose.bot;

import chariot.Client;
import chariot.ClientAuth;
import chariot.model.*;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {
    public static void main(String[] args) {
        ClientAuth client = Client.auth("lip_F6vYdszbwCQ6CoqjujAA");
        User user = client.account().profile().get();
//        Get Challenges
        Result<PendingChallenges> challenges = client.challenges().challenges();
        if (challenges.isPresent()) {
            for (ChallengeResult.Challenge challenge : challenges.get().in()) {
                Result<Ack> acceptChallenge = client.challenges().acceptChallenge(challenge.id());
                if (acceptChallenge.isPresent()) {
                    System.out.println(acceptChallenge);
                }
            }

        }

        boolean running = true;
        while (running) {
            try {
                // Sending get request
                URL url = new URL("https://lichess.org/api/account/playing");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestProperty("Authorization", "Bearer lip_F6vYdszbwCQ6CoqjujAA");

                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestMethod("GET");


                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String output;

                StringBuffer response = new StringBuffer();
                while ((output = in.readLine()) != null) {
                    response.append(output);
                }

                in.close();
                // printing result from response
                JSONObject jsonObject = new JSONObject(response.toString());
                System.out.println("Response:-" + jsonObject);
                JSONArray nowPlayingArray = jsonObject.getJSONArray("nowPlaying");
                for (Object o : nowPlayingArray) {
                    JSONObject nowPlaying = (JSONObject) o;
                    if (nowPlaying.getBoolean("isMyTurn")) {
                        String nextMove = getNextMove(nowPlaying.getString("fen"));
                        Result<Ack> move1 = client.board().move(nowPlaying.getString("gameId"), nextMove);
                    }
                }

                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Creates a new chessboard in the standard initial position
//        Board board = new Board();
//        long start = System.nanoTime();
//        Node node = NodeUtil.createNode(board);
//        NodeUtil.createTree(node, 3);
//        long stop = System.nanoTime();
//        System.out.printf("%.3f%n", (stop - start) / 1_000_000_000f);
//        System.out.println(node.getCount());
    }

    public static String getNextMove(String fen) {
        Board board = new Board();
        board.loadFromFen(fen);
        System.out.println(board);
        Move move = board.legalMoves().get(1);
        return (move.getFrom().value() + move.getTo().value()).toLowerCase();
    }


}
