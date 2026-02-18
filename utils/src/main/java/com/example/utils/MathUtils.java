package com.example.utils;

public class MathUtils {

    // Сложение двух чисел
    public static int add(int a, int b) {
        return a + b;
    }

    // Вычитание
    public static int subtract(int a, int b) {
        return a - b;
    }

    // Умножение
    public static int multiply(int a, int b) {
        return a * b;
    }

    // Деление
    public static double divide(int a, int b) {
        if (b == 0) {
            return 0;
        }
        return (double) a / b;
    }
}