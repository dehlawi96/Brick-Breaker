package com.brickbreaker;

import java.awt.*;

public class PowerUp {

    public int x, y;
    public int width = 20;
    public int height = 20;
    public int speed = 3;
    public boolean active = true;

    public PowerUpType type;

    public PowerUp(int x, int y, PowerUpType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public void update() {
        y += speed;
    }

    public void draw(Graphics g) {
        switch (type) {
            case EXPAND_PADDLE -> g.setColor(Color.CYAN);
            case SHRINK_PADDLE -> g.setColor(Color.RED);
            case MULTI_BALL -> g.setColor(Color.YELLOW);
        }
        g.fillOval(x, y, width, height);
    }
}
