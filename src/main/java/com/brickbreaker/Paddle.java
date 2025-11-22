package com.brickbreaker;

import java.awt.*;

public class Paddle {
    public int x, y, width, height;
    private int speed = 7;

    public Paddle(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 100;
        this.height = 20;
    }

    public void update(InputHandler input) {
       if (input.leftPressed) x -= speed;
       if (input.rightPressed) x += speed;
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);
    }
}
