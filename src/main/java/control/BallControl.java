package control;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Control;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.geometry.Point2D;

import static com.almasb.fxgl.app.DSLKt.*;

public class BallControl extends Control {

    private PhysicsComponent physics;
    private static boolean release;

    @Override
    public void onUpdate(Entity entity, double v) {
        limitVelocity();
    }

    /**
     * Kontroluje predkosc ball
     */
    private void limitVelocity() {
        if (Math.abs(physics.getLinearVelocity().getX()) < 4.1 * 70) {
            physics.setLinearVelocity(Math.signum(physics.getLinearVelocity().getX()) * 4.1 * 70,
                    physics.getLinearVelocity().getY());
        }

        if (Math.abs(physics.getLinearVelocity().getY()) < 4.1 * 70) {
            physics.setLinearVelocity(physics.getLinearVelocity().getX(),
                    Math.signum(physics.getLinearVelocity().getY()) * 4.1 * 70);
        }
    }

    /**
     * Metoda nadajaca poczatkowa wartosc predkosci ball
     */
    public void release()
    {
        float x =0,y=0;
        while(x==0 || y==0) {
                x = FXGLMath.random(-4,4);
                y = FXGLMath.random(1,4);
        }
        physics.setLinearVelocity(x,y);
    }
    public boolean isLost()
    {
        if(getEntity().getY()>FXGL.getAppHeight()-50) {
            FXGL.getApp().getGameScene().getViewport().shake(10,0.5);
            inc("lives",-1);
            getEntity().removeFromWorld();
            spawn("ball",FXGL.getAppWidth()/2-15,590);
            setRelease(false);
            return true;
        }
        else
            return false;
    }

    public static boolean isRelease() {
        return release;
    }

    public static void setRelease(boolean release) {
        BallControl.release = release;
    }
}
