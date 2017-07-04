package com.bignerdranch.android.thirty;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private Game game;
    private Calculator calculator;

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Before
    public void createGame() {
        game = new Game();
        calculator = new Calculator();



    }

    @Test
    public void addPlayer() {
        game.addPlayer("Leif");
        Game.Player leif = game.getActivePlayer();
        assertEquals(1, game.getNumberOfPlayers());

        assertFalse(null == leif);
    }

//    @Test
//    public void filterList_duplicates() {
//        ArrayList<Integer> array1 = new ArrayList<>();
//        ArrayList<Integer> array2 = new ArrayList<>();
//        ArrayList<Integer> expected = new ArrayList<>();
//
//        Integer i1 = 1;
//        Integer i2 = 1;
//        Integer i3 = 2;
//        Integer i4 = 2;
//        array1.add(i1);
//        array1.add(i2);
//        array1.add(i3);
//        array1.add(i4);
//
//        Integer j1 = 1;
//        Integer j2 = 2;
//        array2.add(j1);
//        array2.add(j2);
//
//        expected.add(j1);
//        expected.add(j2);
//
//        assertEquals(expected, game.filterList(array1, array2));
//    }
//
//    @Test
//    public void calculate_baseCaseOnly() {
//
//        ArrayList<Integer> array = new ArrayList<>();
//        array.add(6);
//
//        assertEquals(1, array.size());
//        assertEquals(6, game.calculatePoints(array, 6));
//
//        Integer i2 = 6;
//        array.add(i2);
//        assertEquals(2, array.size());
//
//        assertEquals(12, game.calculatePoints(array, 6));
//
//        Integer i4 = 6;
//        array.add(i4);
//
//        assertEquals(3, array.size());
//        assertEquals(18, game.calculatePoints(array, 6));
//    }
//
//    @Test
//    public void calculate_baseCaseMixed2() {
//
//        ArrayList<Integer> array = new ArrayList<>();
//        Integer i1 = 1;
//        Integer i2 = 1;
//
//        array.add(i1);
//        array.add(i2);
//        assertEquals(2, array.size());
//        assertEquals(2, game.calculatePoints(array, 2));
//    }
//
//    @Test
//    public void calculate_baseCaseMixed() {
//
//        ArrayList<Integer> array = new ArrayList<>();
//        Integer i1 = 1;
//        Integer i2 = 1;
//        Integer i3 = 2;
//
//        array.add(i1);
//        array.add(i2);
//        assertEquals(2, array.size());
//        assertEquals(2, game.calculatePoints(array, 2));
//
//        array.add(i3);
//        assertEquals(3, array.size());
//        assertEquals(4, game.calculatePoints(array, 2));
//    }
//
//    @Test
//    public void calculate_low() {
//        ArrayList<Integer> array = new ArrayList<>();
//        Integer i1 = 2;
//        Integer i2 = 2;
//        Integer i3 = 2;
//        Integer i4 = 2;
//        Integer i5 = 2;
//        Integer i6 = 2;
//
//        array.add(i1);
//        array.add(i2);
//        array.add(i3);
//        assertEquals(6, game.calculatePoints(array, 6));
//
//        array.add(i4);
//        array.add(i5);
//        array.add(i6);
//
//        assertEquals(12, game.calculatePoints(array, 6));
//
//    }


    @Test
    public void calculator() {
//        ArrayList<Integer> input = new ArrayList<>();
//        for (int i = 0; i < 6; i++) {
//            Integer integer = 1;
//            input.add(integer);
//        }
//
//        assertEquals(0, calculator.calculate(input, 0));

    }
//
//    @Test
//    public void calculator_singles() {
//        ArrayList<Integer> input = new ArrayList<>();
//        for (int i = 0; i < 6; i++) {
//            Integer integer = 1;
//            input.add(integer);
//        }
//
//        assertEquals(6, calculator.filterSingles(input, 1));
//
//    }
//
//    @Test
//    public void calculator_singles2() {
//        ArrayList<Integer> input = new ArrayList<>();
//        for (int i = 0; i < 6; i++) {
//            Integer integer = 6;
//            input.add(integer);
//        }
//
//        assertEquals(36, calculator.filterSingles(input, 6));
//    }
//
//    @Test
//    public void calculator_pairs() {
//        ArrayList<Integer> input = new ArrayList<>();
//        for (int i = 0; i < 6; i++) {
//            Integer integer = 1;
//            input.add(integer);
//        }
//
//        assertEquals(6, calculator.filterSingles(input, 2));
//    }
//
//    @Test
//    public void calculator_singlesAndPairs() {
//        ArrayList<Integer> input = new ArrayList<>();
//        for (int i = 0; i < 4; i++) {
//            Integer integer = 2;
//            input.add(integer);
//        }
//
//        Integer i1 = 1;
//        Integer i2 = 1;
//        input.add(i1);
//        input.add(i2);
//
//        assertEquals(10, calculator.filterSingles(input, 2));
//
//        Integer i3 = 1;
//        Integer i4 = 1;
//        input.add(i3);
//        input.add(i4);
//        assertEquals(12, calculator.filterSingles(input, 2));
//    }
//
//
//
//    @Test
//    public void calculator_triplets() {
//        ArrayList<Integer> input = new ArrayList<>();
//        for (int i = 0; i < 6; i++) {
//            Integer integer = 1;
//            input.add(integer);
//        }
//
//        assertEquals(6, calculator.filterSingles(input, 3));
//
//    }



}