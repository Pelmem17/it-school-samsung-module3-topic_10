package ru.samsung.gamestudio.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Vector4;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;

import ru.samsung.gamestudio.GameSettings;

import java.util.Random;

public class EnemyObject extends GameObject {

    private static final int paddingHorizontal = 30;

    long lastShotTime;

    private int livesLeft;

    public EnemyObject(int width, int height, String texturePath, World world) {
        super(
                texturePath,
                width / 2 + paddingHorizontal + (new Random()).nextInt((GameSettings.SCREEN_WIDTH - 2 * paddingHorizontal - width)),
                GameSettings.SCREEN_HEIGHT + height / 2,
                width, height,
                GameSettings.ENEMY_BIT,
                world
        );

        body.setLinearVelocity(new Vector2(new Random().nextInt(-4, 4), -GameSettings.ENEMY_VELOCITY));
        livesLeft = 3;
    }

    public boolean isAlive() {
        return livesLeft > 0;
    }

    public boolean isInFrame() {
        return getY() + height / 2 > 0;
    }

    public boolean needToShoot() {
        if (livesLeft == 1){
            if (TimeUtils.millis() - lastShotTime >= 1100){ //GameSettings.SHOOTING_COOL_DOWN) {
                lastShotTime = TimeUtils.millis();
                return true;
            }
        }
        if (TimeUtils.millis() - lastShotTime >= 1500){ //GameSettings.SHOOTING_COOL_DOWN) {
            lastShotTime = TimeUtils.millis();
            return true;
        }
        return false;
    }


    @Override
    public void hit() {
        livesLeft -= 1;
    }
}
