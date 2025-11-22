# 🎮 Brick Breaker – Java Swing Game  
A fully functional Brick Breaker arcade game built using **Java (Swing + AWT)**.  
The project includes smooth gameplay, sound effects, multiple levels, collision detection, and a clean object-oriented architecture.

---

## 🚀 Features

### 🔹 Core Gameplay  
- Move the paddle to bounce the ball  
- Break all the bricks to clear each level  
- Increasing difficulty with every level  
- Lose a life when the ball touches the bottom  
- Game Over screen + Restart system  

### 🔹 Technical Highlights  
- **Java Swing** for rendering  
- **Separate game loop** running at 60 FPS  
- **Input handling** using a custom `InputHandler` class  
- **Object-oriented design**  
  - `GamePanel`
  - `Ball`
  - `Paddle`
  - `Brick`
  - `Level`
  - `SoundManager`
  - `InputHandler`
  - `GameState`

### 🔹 Sound Effects  
- Paddle hit  
- Brick break  
- Wall bounce  
- Lose life  
- Game over  

(Uses WAV clips loaded via `SoundManager`)

---

## 🕹️ Controls

| Key | Action |
|-----|--------|
| **Left Arrow**  | Move paddle left |
| **Right Arrow** | Move paddle right |
| **Enter**       | Restart game after Game Over |
| **Space (optional)** | Start next level (if LEVEL_COMPLETE implemented) |

---

## 📦 How to Run

1. Install **Java 8+**
2. Clone the repository:
   ```bash
   git clone https://github.com/dehlawi96/Brick-Breaker

Open in IntelliJ IDEA and run `Main.java`
