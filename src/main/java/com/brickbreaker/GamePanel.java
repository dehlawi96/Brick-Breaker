package com.brickbreaker;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable {

    private Thread gameThread;
    private boolean running = false;

    private int score = 0;

    private long levelCompleteTime = 0;

    private InputHandler input;
    private Paddle paddle;
    private Ball ball;
    private Level level;

    private int currentLevel = 1;
    private int lives = 3;
    private GameState state = GameState.PLAYING;

    private ArrayList<PowerUp> powerUps = new ArrayList<>();
    private long powerUpTimer = 0;
    private boolean paddleExpanded = false;
    private boolean paddleShrunk = false;

    // Sounds
    private Clip paddleHitSound;
    private Clip brickHitSound;
    private Clip wallHitSound;
    private Clip loseLifeSound;
    private Clip gameOverSound;

    public GamePanel() {

        // -----------------------------
        // Load WAV files
        // -----------------------------
        paddleHitSound = SoundManager.loadClip("paddle.wav");
        brickHitSound = SoundManager.loadClip("brick.wav");
        wallHitSound = SoundManager.loadClip("wall.wav");
        loseLifeSound = SoundManager.loadClip("lose.wav");
        gameOverSound = SoundManager.loadClip("gameover.wav");

        // Panel setup
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);

        // Input
        input = new InputHandler();
        addKeyListener(input);
        setFocusable(true);

        // Game objects
        level = new Level();
        level.loadLevel(1);

        paddle = new Paddle(350, 550);
        ball = new Ball(390, 300);
    }

    public void startGame() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double fps = 60.0;
        double timePerFrame = 1000000000 / fps;
        long lastTime = System.nanoTime();
        double delta = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / timePerFrame;
            lastTime = now;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    // ----------------------------------------------------
    // UPDATE METHOD
    // ----------------------------------------------------
    public void update() {

        // GAME OVER logic first
        if (state == GameState.GAME_OVER) {
            if (input.enterPressed) {
                restartGame();
                input.enterPressed = false;
            }
            return;
        }

        // pause / other states
        if (state != GameState.PLAYING) return;

        if (state == GameState.LEVEL_COMPLETE) {
            long elapsed = System.currentTimeMillis() - levelCompleteTime;

            // Wait 2 seconds (2000 ms)
            if (elapsed >= 2000) {
                nextLevel();
                state = GameState.PLAYING;
            }

            return;
        }

        paddle.update(input);
        ball.update();

        handleWallCollision();
        handlePaddleCollision();
        handleBrickCollision();
        checkLevelComplete();
        updatePowerUps();
        updatePowerUpEffects();
    }

    // ----------------------------------------------------
    // COLLISIONS + SOUND EFFECTS
    // ----------------------------------------------------
    private void handleWallCollision() {

        if (ball.x <= 0) {
            ball.dx = -ball.dx;
            SoundManager.play(wallHitSound);
        }

        if (ball.x + ball.diameter >= getWidth()) {
            ball.dx = -ball.dx;
            SoundManager.play(wallHitSound);
        }

        if (ball.y <= 0) {
            ball.dy = -ball.dy;
            SoundManager.play(wallHitSound);
        }

        // Bottom = lose life
        if (ball.y + ball.diameter >= getHeight()) {
            loseLife();
        }
    }

    private void loseLife() {
        lives--;

        SoundManager.play(loseLifeSound);

        if (lives <= 0) {
            state = GameState.GAME_OVER;
            SoundManager.play(gameOverSound);
            return;
        }

        resetPositions();
    }

    private void updatePowerUps() {
        for (PowerUp p : powerUps) {
            p.update();

            // If caught by paddle
            Rectangle paddleRect = new Rectangle(paddle.x, paddle.y, paddle.width, paddle.height);
            Rectangle pRect = new Rectangle(p.x, p.y, p.width, p.height);

            if (pRect.intersects(paddleRect)) {
                applyPowerUp(p);
                p.active = false;
            }

            // If falls off screen
            if (p.y > getHeight()) {
                p.active = false;
            }
        }

        // Remove inactive
        powerUps.removeIf(p -> !p.active);
    }

    private void applyPowerUp(PowerUp p) {
        switch (p.type) {
            case EXPAND_PADDLE -> {
                paddle.width = 160;
                paddleExpanded = true;
                paddleShrunk = false;
                powerUpTimer = System.currentTimeMillis();
            }
            case SHRINK_PADDLE -> {
                paddle.width = 60;
                paddleShrunk = true;
                paddleExpanded = false;
                powerUpTimer = System.currentTimeMillis();
            }
            case MULTI_BALL -> {
                // Simple: speed up ball slightly
                ball.dx *= 1.2;
                ball.dy *= 1.2;
            }
        }
    }

    private void updatePowerUpEffects() {
        if (paddleExpanded || paddleShrunk) {
            if (System.currentTimeMillis() - powerUpTimer > 8000) {
                paddle.width = 100; // normal width
                paddleExpanded = false;
                paddleShrunk = false;
            }
        }
    }

    private void resetPositions() {
        paddle.x = 350;
        paddle.y = 550;

        ball.x = 390;
        ball.y = 300;

        ball.dx = 3;
        ball.dy = 3;
    }

    private void handlePaddleCollision() {
        Rectangle ballRect = new Rectangle(ball.x, ball.y, ball.diameter, ball.diameter);
        Rectangle paddleRect = new Rectangle(paddle.x, paddle.y, paddle.width, paddle.height);

        if (ballRect.intersects(paddleRect)) {

            // Bounce upward
            ball.dy = -Math.abs(ball.dy);

            // Fix ball sticking inside paddle
            ball.y = paddle.y - ball.diameter;

            SoundManager.play(paddleHitSound);
        }
    }

    // ----------------------------------------------------
    // FIXED BRICK COLLISION
    // ----------------------------------------------------
    private void handleBrickCollision() {
        Rectangle ballRect = new Rectangle(ball.x, ball.y, ball.diameter, ball.diameter);

        for (Brick b : level.bricks) {
            if (!b.destroyed) {

                Rectangle brickRect = new Rectangle(b.x, b.y, b.width, b.height);

                if (ballRect.intersects(brickRect)) {

                    b.destroyed = true;
                    score += 10;
                    ball.dy = -ball.dy;

                    SoundManager.play(brickHitSound);

                    // SPAWN POWER-UP
                    if (b.hasPowerUp) {
                        powerUps.add(new PowerUp(b.x + b.width / 2, b.y, b.powerUpType));
                    }

                    break;
                }
            }
        }
    }


    private void checkLevelComplete() {
        for (Brick b : level.bricks) {
            if (!b.destroyed) return;
        }

        // Trigger level complete state
        state = GameState.LEVEL_COMPLETE;
        levelCompleteTime = System.currentTimeMillis();
    }

    private void nextLevel() {
        currentLevel++;

        if (currentLevel > 3) currentLevel = 1;

        level.loadLevel(currentLevel);
        resetPositions();


        state = GameState.PLAYING;
    }

    private void restartGame() {
        currentLevel = 1;
        lives = 3;
        score = 0;
        state = GameState.PLAYING;

        level.loadLevel(1);
        resetPositions();
    }

    // ----------------------------------------------------
    // RENDERING
    // ----------------------------------------------------
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw normal game objects
        paddle.draw(g);
        ball.draw(g);

        for (Brick b : level.bricks) {
            b.draw(g);
        }

        for (PowerUp p : powerUps) {
            p.draw(g);
        }

        // HUD
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Lives: " + lives, 20, 20);
        g.drawString("Score: " + score, 20, 45);

        // -------------------------------
        // LEVEL COMPLETE SCREEN
        // -------------------------------
        if (state == GameState.LEVEL_COMPLETE) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("LEVEL " + currentLevel + " COMPLETE!", 200, 300);

            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Get Ready For Level " + (currentLevel + 1), 270, 340);

            return; // Stop drawing anything over it
        }

        // -------------------------------
        // GAME OVER SCREEN
        // -------------------------------
        if (state == GameState.GAME_OVER) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("GAME OVER", 280, 300);

            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Press ENTER to Restart", 290, 340);
        }
    }
}
