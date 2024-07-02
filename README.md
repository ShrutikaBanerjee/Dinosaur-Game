# DINOSAUR GAME

![Dinosaur Game](https://images.crazygames.com/games/chrome-dino/cover-1669113832091.png?auto=format,compress&q=75&cs=strip)

## SuperpaintComponent Call

To draw (draw once call), we created a timer game loop that will call draw repeatedly. To implement time, we will call `ActionListener` and pass `repaint` to it so that multiple frames run together.

## Implement Key Listener

1. Implement the key listener:
    ```java
    addKeyListener(this);
    setFocusable(true);
    ```
2. Add the code to the `dinosaur` method.
