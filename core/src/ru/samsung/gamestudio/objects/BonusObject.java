package ru.samsung.gamestudio.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import ru.samsung.gamestudio.GameSettings;

import java.util.Random;

public class BonusObject extends GameObject {

    private int livesLeft;

    public BonusObject(int x, int y, int width, int height, String texturePath, World world) {
        super(
                texturePath,
                x, y,
                width, height,
                GameSettings.BONUS_BIT,
                world
        );

        livesLeft = 2147483647;
        body.setLinearVelocity(new Vector2(0, -11));
    }

    public boolean isInFrame() {
        return getY() + height / 2 > 0 & (getX()+width>0 & getX()-width<GameSettings.SCREEN_WIDTH);
    }

    public boolean isAlive(){
        return livesLeft > 0;
    }

    @Override
    public void hit(){
        livesLeft -= 1;
    }

    @Override
    public void touchedShip() {
        livesLeft = 0;
    }
}
