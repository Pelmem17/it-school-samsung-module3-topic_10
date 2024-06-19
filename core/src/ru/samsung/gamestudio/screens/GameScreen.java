package ru.samsung.gamestudio.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.samsung.gamestudio.*;
import ru.samsung.gamestudio.components.*;
import ru.samsung.gamestudio.managers.ContactManager;
import ru.samsung.gamestudio.managers.MemoryManager;
import ru.samsung.gamestudio.objects.BonusObject;
import ru.samsung.gamestudio.objects.BoxObject;
import ru.samsung.gamestudio.objects.BulletObject;
import ru.samsung.gamestudio.objects.CoolTrashObject;
import ru.samsung.gamestudio.objects.EnemyObject;
import ru.samsung.gamestudio.objects.ShipObject;
import ru.samsung.gamestudio.objects.TrashObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class GameScreen extends ScreenAdapter {

    MyGdxGame myGdxGame;
    GameSession gameSession;
    ShipObject shipObject;

    ArrayList<TrashObject> trashArray;
    ArrayList<BulletObject> bulletArray;
    ArrayList<EnemyObject> enemyArray;
    ArrayList<BoxObject> boxArray;
    ArrayList<CoolTrashObject> coolTrashArray;
    ArrayList<BonusObject> bonusArray; 

    ContactManager contactManager;

    // PLAY state UI
    MovingBackgroundView backgroundView;
    ImageView topBlackoutView;
    LiveView liveView;
    TextView scoreTextView;
    ButtonView pauseButton;

    // PAUSED state UI
    ImageView fullBlackoutView;
    TextView pauseTextView;
    ButtonView homeButton;
    ButtonView continueButton;

    // ENDED state UI
    TextView recordsTextView;
    RecordsListView recordsListView;
    ButtonView homeButton2;

    public GameScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        gameSession = new GameSession();

        contactManager = new ContactManager(myGdxGame.world);

        trashArray = new ArrayList<>();
        bulletArray = new ArrayList<>();
        enemyArray = new ArrayList<>();
        boxArray = new ArrayList<>();
        coolTrashArray = new ArrayList<>();
        bonusArray = new ArrayList<>();

        shipObject = new ShipObject(
                GameSettings.SCREEN_WIDTH / 2, 150,
                GameSettings.SHIP_WIDTH, GameSettings.SHIP_HEIGHT,
                GameResources.SHIP_IMG_PATH,
                myGdxGame.world
        );

        backgroundView = new MovingBackgroundView(GameResources.BACKGROUND_IMG_PATH);
        topBlackoutView = new ImageView(0, 1180, GameResources.BLACKOUT_TOP_IMG_PATH);
        liveView = new LiveView(305, 1215);
        scoreTextView = new TextView(myGdxGame.commonWhiteFont, 50, 1215);
        pauseButton = new ButtonView(
                605, 1200,
                46, 54,
                GameResources.PAUSE_IMG_PATH
        );

        fullBlackoutView = new ImageView(0, 0, GameResources.BLACKOUT_FULL_IMG_PATH);
        pauseTextView = new TextView(myGdxGame.largeWhiteFont, 282, 842, "Pause");
        homeButton = new ButtonView(
                138, 695,
                200, 70,
                myGdxGame.commonBlackFont,
                GameResources.BUTTON_SHORT_BG_IMG_PATH,
                "Home"
        );
        continueButton = new ButtonView(
                393, 695,
                200, 70,
                myGdxGame.commonBlackFont,
                GameResources.BUTTON_SHORT_BG_IMG_PATH,
                "Continue"
        );

        recordsListView = new RecordsListView(myGdxGame.commonWhiteFont, 690);
        recordsTextView = new TextView(myGdxGame.largeWhiteFont, 206, 842, "Last records");
        homeButton2 = new ButtonView(
                280, 365,
                160, 70,
                myGdxGame.commonBlackFont,
                GameResources.BUTTON_SHORT_BG_IMG_PATH,
                "Home"
        );

    }

    @Override
    public void show() {
        restartGame();
    }

    @Override
    public void render(float delta) {

        handleInput();

        if (gameSession.state == GameState.PLAYING) {
            if (gameSession.shouldSpawnTrash()) {
                TrashObject trashObject = new TrashObject(
                        GameSettings.TRASH_WIDTH, GameSettings.TRASH_HEIGHT,
                        GameResources.TRASH_IMG_PATH,
                        myGdxGame.world
                );
                trashArray.add(trashObject);
            }
            if (gameSession.shouldSpawnEnemy()) {
                EnemyObject enemyObject = new EnemyObject(
                        GameSettings.ENEMY_WIDTH, GameSettings.ENEMY_HEIGHT,
                        GameResources.ENEMY_IMG_PATH,
                        myGdxGame.world
                );
                enemyArray.add(enemyObject);
            }
            if (gameSession.shouldSpawnBox()) {
                BoxObject boxObject = new BoxObject(
                        GameSettings.BOX_WIDTH, GameSettings.BOX_HEIGHT,
                        GameResources.BOX_IMG_PATH,
                        myGdxGame.world
                );
                boxArray.add(boxObject);
            }
            if (gameSession.shouldSpawnCoolTrash()){
                CoolTrashObject coolTrashObject = new CoolTrashObject(
                        GameSettings.TRASH_WIDTH, GameSettings.TRASH_HEIGHT,
                        GameResources.COOL_TRASH_IMG_PATH,
                        myGdxGame.world
                );
                coolTrashArray.add(coolTrashObject);
            }

            if (shipObject.needToShoot()) {
                BulletObject laserBullet = new BulletObject(
                        shipObject.getX(), shipObject.getY() + shipObject.height+1,
                        GameSettings.BULLET_WIDTH, GameSettings.BULLET_HEIGHT,
                        GameResources.BULLET_IMG_PATH,
                        myGdxGame.world, 1
                );
                bulletArray.add(laserBullet);
                if (myGdxGame.audioManager.isSoundOn) myGdxGame.audioManager.shootSound.play();
            }

            if (!shipObject.isAlive()) {
                gameSession.endGame();
                recordsListView.setRecords(MemoryManager.loadRecordsTable());
            }

            updateTrash();
            updateBullets();
            updateEnemy();
            updateBox();
            updateCoolTrash();
            updateBonus();
            backgroundView.move();
            gameSession.updateScore();
            scoreTextView.setText("Score: " + gameSession.getScore());
            liveView.setLeftLives(shipObject.getLiveLeft());

            myGdxGame.stepWorld();
        }

        draw();
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            myGdxGame.touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            switch (gameSession.state) {
                case PLAYING:
                    if (pauseButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        gameSession.pauseGame();
                    }
                    shipObject.move(myGdxGame.touch);
                    break;

                case PAUSED:
                    if (continueButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        gameSession.resumeGame();
                    }
                    if (homeButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        myGdxGame.setScreen(myGdxGame.menuScreen);
                    }
                    break;

                case ENDED:

                    if (homeButton2.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        myGdxGame.setScreen(myGdxGame.menuScreen);
                    }
                    break;
            }

        }
    }

    private void draw() {

        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        myGdxGame.batch.begin();
        backgroundView.draw(myGdxGame.batch);
        for (TrashObject trash : trashArray) trash.draw(myGdxGame.batch);
        shipObject.draw(myGdxGame.batch);
        for (BulletObject bullet : bulletArray) bullet.draw(myGdxGame.batch);
        for (EnemyObject enemy : enemyArray) enemy.draw(myGdxGame.batch);
        for (BoxObject box: boxArray) box.draw(myGdxGame.batch);
        for (CoolTrashObject coolTrash: coolTrashArray) coolTrash.draw(myGdxGame.batch);
        for (BonusObject bonus: bonusArray) bonus.draw(myGdxGame.batch);
        topBlackoutView.draw(myGdxGame.batch);
        scoreTextView.draw(myGdxGame.batch);
        liveView.draw(myGdxGame.batch);
        pauseButton.draw(myGdxGame.batch);

        if (gameSession.state == GameState.PAUSED) {
            fullBlackoutView.draw(myGdxGame.batch);
            pauseTextView.draw(myGdxGame.batch);
            homeButton.draw(myGdxGame.batch);
            continueButton.draw(myGdxGame.batch);
        } else if (gameSession.state == GameState.ENDED) {
            fullBlackoutView.draw(myGdxGame.batch);
            recordsTextView.draw(myGdxGame.batch);
            recordsListView.draw(myGdxGame.batch);
            homeButton2.draw(myGdxGame.batch);
        }

        myGdxGame.batch.end();

    }

    private void updateTrash() {
        Iterator<TrashObject> iteratorTrash = trashArray.iterator();
        while (iteratorTrash.hasNext()) {
            TrashObject nextTrash = iteratorTrash.next();
            boolean hasToBeDestroyed = !nextTrash.isAlive() || !nextTrash.isInFrame();
            if (!nextTrash.isAlive()){
                gameSession.destructionRegistration();
                if (myGdxGame.audioManager.isSoundOn) myGdxGame.audioManager.explosionSound.play(0.2f);
            }
            if (hasToBeDestroyed) {
                myGdxGame.world.destroyBody(nextTrash.body);
                iteratorTrash.remove();
            }
        }
    }
    private void updateBox() {
        Iterator<BoxObject> iteratorBox = boxArray.iterator();
        while (iteratorBox.hasNext()) {
            BoxObject nextBox = iteratorBox.next();
            boolean hasToBeDestroyed = !nextBox.isAlive() || !nextBox.isInFrame();
            if (!nextBox.isAlive()){
                bonusArray.add(new BonusObject(
                        nextBox.getX(), nextBox.getY(),
                        GameSettings.BOX_WIDTH, GameSettings.BOX_HEIGHT,
                        GameResources.BONUS_IMG_PATH,
                        myGdxGame.world));
                gameSession.destructionRegistration();
                if (myGdxGame.audioManager.isSoundOn) myGdxGame.audioManager.explosionSound.play(0.2f);
            }
            if (hasToBeDestroyed) {
                myGdxGame.world.destroyBody(nextBox.body);
                iteratorBox.remove();
            }
        }
    }

    private void updateBonus() {
        Iterator<BonusObject> iteratorBonus = bonusArray.iterator();
        while (iteratorBonus.hasNext()) {
            BonusObject nextBonus = iteratorBonus.next();
            boolean hasToBeDestroyed = !nextBonus.isAlive() || !nextBonus.isInFrame();
            if (!nextBonus.isAlive()){
                shipObject.touchedBonus();
            }
            if (hasToBeDestroyed) {
                myGdxGame.world.destroyBody(nextBonus.body);
                iteratorBonus.remove();
            }
        }
    }
    
    private void updateEnemy() {
        Iterator<EnemyObject> iteratorEnemy = enemyArray.iterator();
        while (iteratorEnemy.hasNext()) {
            EnemyObject nextEnemy = iteratorEnemy.next();
            boolean hasToBeDestroyed = !nextEnemy.isAlive() || !nextEnemy.isInFrame();
            if (nextEnemy.needToShoot()){
                BulletObject laserBullet = new BulletObject(
                        nextEnemy.getX(), nextEnemy.getY() - nextEnemy.height*2,
                        GameSettings.BULLET_WIDTH, GameSettings.BULLET_HEIGHT,
                        GameResources.BULLET_IMG_PATH,
                        myGdxGame.world, 0
                );
                bulletArray.add(laserBullet);
                if (myGdxGame.audioManager.isSoundOn) myGdxGame.audioManager.shootSound.play(0.1f);
            }
            if (!nextEnemy.isAlive()){
                gameSession.destructionRegistration();
                if (myGdxGame.audioManager.isSoundOn) myGdxGame.audioManager.explosionSound.play(0.2f);
            }
            if (hasToBeDestroyed) {
                myGdxGame.world.destroyBody(nextEnemy.body);
                iteratorEnemy.remove();
            }
        }
    }

    private void updateCoolTrash() {
        Iterator<CoolTrashObject> iteratorCoolTrash = coolTrashArray.iterator();
        while (iteratorCoolTrash.hasNext()) {
            CoolTrashObject nextCoolTrash = iteratorCoolTrash.next();
            boolean hasToBeDestroyed = !nextCoolTrash.isAlive() || !nextCoolTrash.isInFrame();
            nextCoolTrash.move(myGdxGame.touch);
            if (!nextCoolTrash.isAlive()){
                gameSession.destructionRegistration();
                if (myGdxGame.audioManager.isSoundOn) myGdxGame.audioManager.explosionSound.play(0.2f);
            }
            if (hasToBeDestroyed) {
                myGdxGame.world.destroyBody(nextCoolTrash.body);
                iteratorCoolTrash.remove();
            }
        }
    }

    private void updateBullets() {
        Iterator<BulletObject> iteratorBullet = bulletArray.iterator();
        while (iteratorBullet.hasNext()) {
            BulletObject nextBullet = iteratorBullet.next();
            if (nextBullet.hasToBeDestroyed()) {
                myGdxGame.world.destroyBody(nextBullet.body);
                iteratorBullet.remove();
            }
        }
    }

    private void restartGame() {
        Iterator<TrashObject> iteratorTrash = trashArray.iterator();
        while (iteratorTrash.hasNext()) {
            TrashObject nextTrash = iteratorTrash.next();
            myGdxGame.world.destroyBody(nextTrash.body);
            iteratorTrash.remove();
        }

        for (int i = 0; i < trashArray.size(); i++) {
            myGdxGame.world.destroyBody(trashArray.get(i).body);
            trashArray.remove(i--);
        }
        for (int i = 0; i < enemyArray.size(); i++) {
            myGdxGame.world.destroyBody(enemyArray.get(i).body);
            enemyArray.remove(i--);
        }

        if (shipObject != null) {
            myGdxGame.world.destroyBody(shipObject.body);
        }

        shipObject = new ShipObject(
                GameSettings.SCREEN_WIDTH / 2, 150,
                GameSettings.SHIP_WIDTH, GameSettings.SHIP_HEIGHT,
                GameResources.SHIP_IMG_PATH,
                myGdxGame.world
        );

        bulletArray.clear();
        gameSession.startGame();
    }

}
