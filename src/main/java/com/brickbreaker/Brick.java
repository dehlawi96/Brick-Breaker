package com.brickbreaker;

import java.awt.*;

public class Brick {
    public int x, y, width, height;
    public boolean destroyed = false;

    public boolean hasPowerUp = false;
    public PowerUpType powerUpType;

    public Brick(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        if (Math.random() < 0.15) { // 15% chance
            hasPowerUp = true;

            double r = Math.random();
            if (r < 0.33) powerUpType = PowerUpType.EXPAND_PADDLE;
            else if (r < 0.66) powerUpType = PowerUpType.SHRINK_PADDLE;
            else powerUpType = PowerUpType.MULTI_BALL;
        }

    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics g) {
        if (!destroyed) {
            g.setColor(Color.RED);
            g.fillRect(x, y, width, height);
        }
    }
}
