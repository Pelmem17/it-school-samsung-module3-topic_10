package ru.samsung.gamestudio.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import ru.samsung.gamestudio.GameSettings;
import java.util.Random;

public class CoolTrashObject extends GameObject {

    private static final int paddingHorizontal = 30;

    private int livesLeft;

    public CoolTrashObject(int width, int height, String texturePath, World world) {
        super(
                texturePath,
                width / 2 + paddingHorizontal + (new Random()).nextInt((GameSettings.SCREEN_WIDTH - 2 * paddingHorizontal - width)),
                GameSettings.SCREEN_HEIGHT + height / 2,
                width, height,
                GameSettings.COOL_TRASH_BIT,
                world
        );
        livesLeft = 2;
    }

    public boolean isAlive() {
        return livesLeft > 0;
    }

    public void move(Vector3 vector3) {
        body.applyForceToCenter(new Vector2(
                        (vector3.x - getX()) * 0.05f,
                        (vector3.y - getY()) * 0.05f),
                true
        );
    }
    public boolean isInFrame() {
        return getY() + height / 2 > 0;
    }

    @Override
    public void hit() {
        livesLeft -= 1;
    }
}
