package com.bignerdranch.android.thirty;

import java.util.ArrayList;

public class Calculator {

    public Calculator() {

    }

    public boolean calculate(ArrayList<Integer> array, int category) {
        int sum = 0;

        for (Integer i : array) {
            sum += i;
        }

        return false;
    }

//    public int filterSingles(ArrayList<Integer> array, int value) {
//        int points = 0;
//
//        ArrayList<Integer> rest = new ArrayList<>();
//
//
//        for (int i = 0; i < array.size(); i++) {
//            if (array.get(i) == value) {
//                points++;
//            } else {
//                rest.add(array.get(i));
//            }
//        }
//
//        if (rest.isEmpty() || rest.size() == 1) {
//            return value * points;
//
//        } else {
//            int current = rest.get(0);
//            int index = 0;
//            rest.remove(index);
//
//            return value * calculate(rest, current, points, value, 1);
//        }
//    }

//    public int calculate(ArrayList<Integer> array, int current, int points, int value, int level) {
//        ArrayList<Integer> rest = new ArrayList<>();
//        ArrayList<Integer> finalRest;
//
//        int next = 0;
//
//        while (next < array.size()) {
//            if (current + array.get(next) == value) {
//
//                if (next + 1 < array.size()) { // if there is more to add, add it
//
//                    for (int i = next + 1; i < array.size(); i++) {
//                        rest.add(array.get(i));
//                    }
//                }
//
//                if (rest.size() > 1) {
//
//                    int toIndex = rest.size();
//                    finalRest = new ArrayList(rest.subList(1, toIndex));
//
//                    return calculate(finalRest, rest.get(0), points + 1, value, level + 1);
//
//                } else {
//                    return points + 1;
//                }
//
//            } else {
//                rest.add(array.get(next));
//            }
//
//            next++;
//        }
//
//        if (rest.size() > 1) {
//
//            int newCurrent = rest.get(0);
//            int toIndex = rest.size();
//            finalRest = new ArrayList(rest.subList(1, toIndex));
//
//            return calculate(finalRest, newCurrent, points, value, level + 1);
//        } else {
//            return points;
//        }
//
//    }

}
