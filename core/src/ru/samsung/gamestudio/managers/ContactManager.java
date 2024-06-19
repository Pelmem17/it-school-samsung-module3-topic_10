package ru.samsung.gamestudio.managers;

import com.badlogic.gdx.physics.box2d.*;
import ru.samsung.gamestudio.GameSettings;
import ru.samsung.gamestudio.objects.GameObject;

public class ContactManager {

    World world;

    public ContactManager(World world) {
        this.world = world;

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {

                Fixture fixA = contact.getFixtureA();
                Fixture fixB = contact.getFixtureB();

                int cDef = fixA.getFilterData().categoryBits;
                int cDef2 = fixB.getFilterData().categoryBits;

                if (cDef == GameSettings.TRASH_BIT && cDef2 == GameSettings.BULLET_BIT
                        || cDef2 == GameSettings.TRASH_BIT && cDef == GameSettings.BULLET_BIT
                        || cDef == GameSettings.TRASH_BIT && cDef2 == GameSettings.SHIP_BIT
                        || cDef2 == GameSettings.TRASH_BIT && cDef == GameSettings.SHIP_BIT) {

                    ((GameObject) fixA.getUserData()).hit();
                    ((GameObject) fixB.getUserData()).hit();

                }
                Fixture fixC = contact.getFixtureA();
                Fixture fixD = contact.getFixtureB();

                int cDef3 = fixC.getFilterData().categoryBits;
                int cDef4 = fixD.getFilterData().categoryBits;

                if (cDef3 == GameSettings.ENEMY_BIT && cDef4 == GameSettings.BULLET_BIT
                        || cDef4 == GameSettings.ENEMY_BIT && cDef3 == GameSettings.BULLET_BIT
                        || cDef3 == GameSettings.ENEMY_BIT && cDef4 == GameSettings.SHIP_BIT
                        || cDef4 == GameSettings.ENEMY_BIT && cDef3 == GameSettings.SHIP_BIT) {

                    ((GameObject) fixC.getUserData()).hit();
                    ((GameObject) fixD.getUserData()).hit();

                }
                
                Fixture fixE = contact.getFixtureA();
                Fixture fixF = contact.getFixtureB();

                int cDef5 = fixE.getFilterData().categoryBits;
                int cDef6 = fixF.getFilterData().categoryBits;

                if (cDef5 == GameSettings.SHIP_BIT && cDef6 == GameSettings.BULLET_BIT
                        || cDef6 == GameSettings.SHIP_BIT && cDef5 == GameSettings.BULLET_BIT
                        || cDef6 == GameSettings.SHIP_BIT && cDef5 == GameSettings.SHIP_BIT) {

                    ((GameObject) fixE.getUserData()).hit();
                    ((GameObject) fixF.getUserData()).hit();

                }
                Fixture fixG = contact.getFixtureA();
                Fixture fixH = contact.getFixtureB();

                int cDef7 = fixG.getFilterData().categoryBits;
                int cDef8 = fixH.getFilterData().categoryBits;

                if (cDef7 == GameSettings.BULLET_BIT && cDef8 == GameSettings.BULLET_BIT
                        || cDef7 == GameSettings.BULLET_BIT && cDef8 == GameSettings.SHIP_BIT) {

                    ((GameObject) fixG.getUserData()).hit();
                    ((GameObject) fixH.getUserData()).hit();

                }
                
                Fixture fixI = contact.getFixtureA();
                Fixture fixJ = contact.getFixtureB();

                int cDef9 = fixI.getFilterData().categoryBits;
                int cDef10 = fixJ.getFilterData().categoryBits;

                if (cDef9 == GameSettings.BOX_BIT && cDef10 == GameSettings.BULLET_BIT
                        || cDef10 == GameSettings.BOX_BIT && cDef9 == GameSettings.BULLET_BIT
                        || cDef9 == GameSettings.BOX_BIT && cDef10 == GameSettings.SHIP_BIT
                        || cDef10 == GameSettings.BOX_BIT && cDef9 == GameSettings.SHIP_BIT) {

                    ((GameObject) fixI.getUserData()).hit();
                    ((GameObject) fixJ.getUserData()).hit();

                }
                
                Fixture fixK = contact.getFixtureA();
                Fixture fixL = contact.getFixtureB();

                int cDef11 = fixK.getFilterData().categoryBits;
                int cDef12 = fixL.getFilterData().categoryBits;

                if (cDef11 == GameSettings.COOL_TRASH_BIT && cDef12 == GameSettings.BULLET_BIT
                        || cDef12 == GameSettings.COOL_TRASH_BIT && cDef11 == GameSettings.BULLET_BIT
                        || cDef11 == GameSettings.COOL_TRASH_BIT && cDef12 == GameSettings.SHIP_BIT
                        || cDef12 == GameSettings.COOL_TRASH_BIT && cDef11 == GameSettings.SHIP_BIT) {

                    ((GameObject) fixK.getUserData()).hit();
                    ((GameObject) fixL.getUserData()).hit();

                }

                Fixture fixM = contact.getFixtureA();
                Fixture fixN = contact.getFixtureB();

                int cDef13 = fixM.getFilterData().categoryBits;
                int cDef14 = fixN.getFilterData().categoryBits;

                if (cDef13 == GameSettings.BONUS_BIT && cDef14 == GameSettings.SHIP_BIT
                        || cDef14 == GameSettings.BONUS_BIT && cDef13 == GameSettings.SHIP_BIT) {

                    ((GameObject) fixM.getUserData()).touchedShip();
                    ((GameObject) fixN.getUserData()).touchedShip();

                }

                Fixture fixO = contact.getFixtureA();
                Fixture fixP = contact.getFixtureB();

                int cDef15 = fixO.getFilterData().categoryBits;
                int cDef16 = fixP.getFilterData().categoryBits;

                if (cDef15 == GameSettings.BONUS_BIT && cDef16 == GameSettings.BULLET_BIT
                        || cDef16 == GameSettings.BONUS_BIT && cDef15 == GameSettings.BULLET_BIT) {

                    ((GameObject) fixO.getUserData()).hit();
                    ((GameObject) fixP.getUserData()).hit();

                }
                
            }


            @Override
            public void endContact(Contact contact) {
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }
        });

    }

}
