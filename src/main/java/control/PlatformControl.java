package control;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.Control;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.PhysicsComponent;

public class PlatformControl extends Control {

    private static final float BOUNCE_FACTOR = 1.5f;
    private static final float SPEED_DECAY = 0.66f;
    private Entity platform;
    private float speed = 0;
    private PhysicsComponent physics;

    private Vec2 velocity = new Vec2();

    @Override
    public void onAdded(Entity entity) {
        platform = entity;
    }

    @Override
    public void onUpdate(Entity entity, double v) {

        speed = 760 * (float)v;

        velocity.mulLocal(SPEED_DECAY);

        if (platform.getX() < 0) {
            velocity.set(BOUNCE_FACTOR * (float) -platform.getX(), 0);
        } else if (platform.getRightX() > FXGL.getApp().getWidth()) {
            velocity.set(BOUNCE_FACTOR * (float) -(platform.getRightX() - FXGL.getApp().getWidth()), 0);
        }

        physics.setBodyLinearVelocity(velocity);
    }

    public void left()
    {
        velocity.set(-speed,0);
    }
    public void right()
    {
        velocity.set(speed,0);
    }

}
