package de.phynnnix.tictactoe;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.BeforeEach;


public class BoardTest{

    int size;
    Board board;
    
    @BeforeEach
    public void setupNewBoard(){
        size = 3;
        board = new Board(size, size);
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,2,3,4,5,6,7,8})
    public void emptyBoard(int index){
        int x = index % size;
        int y = index / size;
        assertTrue(board.getPlayerAt(x, y).isNone(), "Spieler auf dem Feld sollte der 'None' Spieler sein");
        assertTrue(board.isEmpty(index), "Feld sollte als leer erkannt werden");
    }

    @Test
    public void setSomeFields(){
        Player p1 = Player.get(1, "Test1");
        Player p2 = Player.get(2, "Test2");
        Player p3 = Player.get(3, "Test3");

        assertTrue(board.setPlayerAt(0, 0, p1));
        assertTrue(board.setPlayerAt(0, 1, p1));
        assertTrue(board.setPlayerAt(1, 2, p2));
        assertTrue(board.setPlayerAt(2, 1, p2));
        assertTrue(board.setPlayerAt(2, 2, p3));
        assertTrue(board.setPlayerAt(0, 2, p3));

        assertTrue(p1.equals(board.getPlayerAt(0, 0)));
        assertTrue(p1.equals(board.getPlayerAt(0, 1)));
        assertTrue(p2.equals(board.getPlayerAt(1, 2)));
        assertTrue(p2.equals(board.getPlayerAt(2, 1)));
        assertTrue(p3.equals(board.getPlayerAt(2, 2)));
        assertTrue(p3.equals(board.getPlayerAt(0, 2)));
    }

    @Test
    public void overrideSomeFields(){
        Player p1 = Player.get(1, "Test1");
        Player p2 = Player.get(2, "Test2");
        Player p3 = Player.get(3, "Test3");

        assertTrue(board.setPlayerAt(0, 0, p1));
        assertTrue(board.setPlayerAt(0, 1, p1));
        assertTrue(board.setPlayerAt(1, 2, p2));
        assertTrue(board.setPlayerAt(2, 1, p2));
        assertTrue(board.setPlayerAt(2, 2, p3));
        assertTrue(board.setPlayerAt(0, 2, p3));

        assertFalse(board.setPlayerAt(0, 0, p1));
        assertFalse(board.setPlayerAt(0, 1, p2));
        assertFalse(board.setPlayerAt(1, 2, p2));
        assertFalse(board.setPlayerAt(2, 1, p3));
        assertFalse(board.setPlayerAt(2, 2, p3));
        assertFalse(board.setPlayerAt(0, 2, p1));
    }

    @Test
    public void checkWinningLines(){
        Player p1 = Player.get(1, "Test1");

        assertTrue(board.checkWinner().isNone());

        assertTrue(board.setPlayerAt(0, 0, p1));
        assertTrue(board.setPlayerAt(0, 1, p1));
        assertTrue(board.setPlayerAt(0, 2, p1));

        assertTrue(board.checkWinner().equals(p1));
    }

}