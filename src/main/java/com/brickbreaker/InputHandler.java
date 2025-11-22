package com.brickbreaker;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InputHandler extends KeyAdapter {
    public boolean leftPressed = false;
    public boolean rightPressed = false;
    public boolean enterPressed = false;


    @Override
    public void keyPressed(KeyEvent e) {
        int code  = e.getKeyCode();
        if (code == KeyEvent.VK_LEFT) leftPressed = true;
        if (code == KeyEvent.VK_RIGHT) rightPressed = true;
        if (code == KeyEvent.VK_ENTER) enterPressed = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_LEFT) leftPressed = false;
        if (code == KeyEvent.VK_RIGHT) rightPressed = false;
    }
}
