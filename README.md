# **Angry Birds Game**

A custom implementation of the classic **Angry Birds** game developed using the **libGDX** framework. This game combines creative gameplay mechanics with physics-based destruction, special powers, and dynamic interactions, delivering a fun and engaging experience.

---

## **Features**

### **Core Gameplay**
- **Catapult Mechanism**: To launch birds toward the target structures.
- **Dynamic Physics**: Structures collapse realistically based on the bird's impact.
- **Targets and Obstacles**:
    - **Pigs**: Primary targets to destroy.
    - **TNT Boxes**: Explosive objects that detonate upon impact, adding strategic depth.
    - **Building Blocks**: Wood and steel blocks make up destructible structures.

### **Bird Special Powers**
- **Yellow Bird (Speedy)**:
    - Press **1** to **increase its speed** after launch, perfect for hitting long-distance targets.
- **Black Bird (Bomber)**:
    - Press **2** to trigger a **forceful impact**, causing massive destruction to structures.

### **Gameplay Controls**
- **Arrow Keys (Right, Left, Up, Down)**: Adjust the angle of the bird's launch.
- **0 Key**: Pause the game at any moment.

### **Other Features**
- **Serialization Support**: Game states can be saved and restored, enabling users to continue gameplay seamlessly.
- **Explosive TNT Boxes**: Mimics the classic TNT element from the original game for added challenge.

---

## **JUnit Testing**

The game includes a suite of **JUnit tests** to ensure its reliability and correctness. Below are the key test cases included in the project:

### **Test Cases**
1. **King Pig Position Test**
    - **Method**: `testKingPig()`
    - **Description**: Validates that the **King Pig** object is initialized with the correct x and y positions.
    - **Assertions**:
      ```java
      assertEquals(pg.getPosX(), 2, 0.01f);
      assertEquals(pg.getPosY(), 3, 0.1f);
      ```

2. **Medium Pig Position Test**
    - **Method**: `testMedPig()`
    - **Description**: Ensures that the **Medium Pig** object has the correct position after initialization.
    - **Assertions**:
      ```java
      assertEquals(pg.getPosX(), 4, 0.01f);
      assertEquals(pg.getPosY(), 5, 0.1f);
      ```

3. **Small Pig Position Test**
    - **Method**: `testSmallPig()`
    - **Description**: Confirms that the **Small Pig** object is properly initialized with the given coordinates.
    - **Assertions**:
      ```java
      assertEquals(pg.getPosX(), 2, 0.01f);
      assertEquals(pg.getPosY(), 3, 0.1f);
      ```

4. **Level Initialization Test**
    - **Method**: `testInitializeLevelOnePigs()`
    - **Description**: Validates that the pigs are correctly initialized within a level.
    - **Assertions**:
      ```java
      assertEquals(pigs.size(), lv.pigs.size());
      ```

5. **Yellow Bird Special Power Test**
    - **Method**: `testYellowBirdSpecial()`
    - **Description**: Ensures that the **Yellow Bird's speed boost** works correctly.
    - **Assertions**:
      ```java
      assertEquals(bird.getBody().getLinearVelocity().x, 2 * initialx, 0.01);
      assertEquals(bird.getBody().getLinearVelocity().y, 2 * initialy, 0.01);
      ```

6. **Pig Damage Test**
    - **Method**: `testPigDamage()`
    - **Description**: Verifies that the pig's health is reduced correctly and the pig is marked as destroyed when health is below zero.
    - **Assertions**:
      ```java
      assertFalse(pg.isStatus());
      ```

---

## **How to Run Tests**

1. Ensure **JUnit** is included in your development environment.
2. Open the project in **IntelliJ IDEA**.
3. Navigate to the `test` folder in the project directory.
4. Right-click the `src/test` folder and select **Run All Tests**.
5. View results in the **JUnit test panel**.

---

## **Gameplay Instructions**

1. **Launch Birds**:
    - Adjust the launch angle using the **arrow keys** (Right, Left, Up, Down).
    - Drag and release the bird to launch it toward the target.

2. **Use Special Powers**:
    - Press **1** for the yellow bird's **speed boost** after launch.
    - Press **2** for the black bird's **forceful explosion** after launch.

3. **Destroy All Pigs**:
    - The goal is to eliminate all pigs using the birds and the environment.
    - Strategically target TNT and weak structural points for maximum destruction.

4. **Pause Gameplay**:
    - Press `0` to pause the game at any point.

---

## **How to Run**

1. Install **Java JDK 17** or higher.
2. Clone or download the repository.
3. Open the terminal, navigate to the project directory, and run:
   ```bash
   ./gradlew desktop:run
