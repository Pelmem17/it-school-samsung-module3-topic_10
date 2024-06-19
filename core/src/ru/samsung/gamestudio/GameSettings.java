package ru.samsung.gamestudio;

public class GameSettings {

    // Device settings

    public static final int SCREEN_WIDTH = 720;
    public static final int SCREEN_HEIGHT = 1280;

    // Physics settings

    public static final float STEP_TIME = 1f / 60f;
    public static final int VELOCITY_ITERATIONS = 6;
    public static final int POSITION_ITERATIONS = 6;
    public static final float SCALE = 0.05f;

    public static float SHIP_FORCE_RATIO = 10;
    public static float TRASH_VELOCITY = 20;
    public static float ENEMY_VELOCITY = 13;
    public static long STARTING_TRASH_APPEARANCE_COOL_DOWN = 2000; // in [ms] - milliseconds
    public static long STARTING_ENEMY_APPEARANCE_COOL_DOWN = 8021;
    public static long STARTING_BOX_APPEARANCE_COOL_DOWN = 40263;
    public static long STARTING_COOL_TRASH_APPEARANCE_COOL_DOWN = 5451;
    public static int BULLET_VELOCITY = 200; // in [m/s] - meter per second
    public static int BOX_VELOCITY = 25;
    public static int SHOOTING_COOL_DOWN = 1000; // in [ms] - milliseconds

    public static final short TRASH_BIT = 2;
    public static final short SHIP_BIT = 4;
    public static final short BULLET_BIT = 8;
    public static final short ENEMY_BIT = 16;
    public static final short BOX_BIT = 32;
    public static final short COOL_TRASH_BIT = 64;
    public static final short BONUS_BIT = 128;

    // Object sizes

    public static final int SHIP_WIDTH = 150;
    public static final int SHIP_HEIGHT = 150;
    public static final int TRASH_WIDTH = 140;
    public static final int TRASH_HEIGHT = 100;
    public static final int ENEMY_WIDTH = 140;
    public static final int ENEMY_HEIGHT = 90;
    public static final int BULLET_WIDTH = 15;
    public static final int BULLET_HEIGHT = 45;
    public static final int BOX_WIDTH = 91;
    public static final int BOX_HEIGHT = 91;


}