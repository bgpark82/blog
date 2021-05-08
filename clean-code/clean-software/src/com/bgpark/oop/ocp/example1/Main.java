package com.bgpark.oop.ocp.example1;

public class Main {

    void drawAllShape(Shape[] list, int n) {
        for (int i = 0; i < n; i++) {
            Shape s = list[i];
            switch(s.type) {
                case square:
                    ((Square) s).drawSquare();
                    break;
                case circle:
                    ((Circle) s).drawCircle();
                    break;
            }
        }
    }
}
