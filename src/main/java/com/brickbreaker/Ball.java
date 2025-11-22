package com.brickbreaker;

import java.awt.*;

public class Ball {
    public int x, y, diameter;
    public int dx = 3;
    public int dy = 3;

    public Ball(int x, int y) {
        this.x = x;
        this.y = y;
        this.diameter = 15;
    }

    public void update() {
        x  += dx;
        y += dy;
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(x, y, diameter, diameter);
    }
}
