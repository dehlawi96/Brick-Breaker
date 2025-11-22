package com.brickbreaker;

import java.util.ArrayList;

public class Level {
    public ArrayList<Brick> bricks = new ArrayList<>();

    public void loadLevel(int levelNumber) {
        bricks.clear();

        switch (levelNumber) {
            case 1: loadLevel1(); break;
            case 2: loadLevel2(); break;
            case 3: loadLevel3(); break;
            default: loadLevel1(); break;
        }
    }

    private void loadLevel1() {
        createGrid(5, 10);
    }

    private void loadLevel2() {
        createGrid(7, 12);
    }

    private void loadLevel3() {
        createGrid(8, 14);
    }

    private void createGrid(int rows, int cols) {
        int brickWidth = 70;
        int brickHeight = 25;
        int startX = 20;
        int startY = 40;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int x = startX + c * (brickWidth + 5);
                int y = startY + r * (brickHeight + 5);

                bricks.add(new Brick(x, y, brickWidth, brickHeight));
            }
        }
    }
}
