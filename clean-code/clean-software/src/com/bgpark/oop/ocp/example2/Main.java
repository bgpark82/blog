package com.bgpark.oop.ocp.example2;

public class Main {

    void drawAllShapes(Shape[] list) {
        for(Shape s: list) {
            s.draw();
        }
    }
}
