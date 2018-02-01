package control;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.effect.ParticleControl;
import com.almasb.fxgl.effect.ParticleEmitters;
import com.almasb.fxgl.entity.Control;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.HealthComponent;
import com.almasb.fxgl.entity.control.ExpireCleanControl;
import javafx.geometry.Point2D;
import javafx.util.Duration;

public class BrickControl extends Control {
    /**
     * Liczba zyc bricka
     */
    private int live;

    @Override
    public void onAdded(Entity entity) {
        live = entity.getComponent(HealthComponent.class).getValue();
    }

    @Override
    public void onRemoved(Entity entity) {
        FXGL.getApp().getGameState().increment("bricks",-1);
    }

    @Override
    public void onUpdate(Entity entity, double v) {}

    /**
     * decrementuje liczbe pkt. zycia o jeden
     * Sprawdzam liczbe pkt. zycia jesli <=0 to wywoluję metode effectAndDie()
     * jeśli >0 to wywoluje metode effect1()
     */
    public void onHit()
    {
        live--;
        if(live>0)
            effect1();
        else
            effectAndDie();
    }

    /**
     * przypisuje animacje do bricka
     */
    private void effect1()
    {

        Entities.animationBuilder()
                .autoReverse(true)
                .repeat(2)
                .interpolator(Interpolators.CIRCULAR.EASE_IN())
                .duration(Duration.seconds(0.33))
                .scale(getEntity())
                .to(new Point2D(1.3, 1.3))
                .buildAndPlay();
    }
    /**
     * przypisuje animacje do bricka po zakonczonej animacji usuwa bricka ze swiata gry
     */
    private void effectAndDie()
    {
        Entity entity2 = Entities.builder()
                .viewFromNode(getEntity().getView())
                .at(getEntity().getPosition())
                .rotate(getEntity().getRotation())
                .buildAndAttach(FXGL.getApp().getGameWorld());
        getEntity().removeFromWorld();

        Entities.animationBuilder()
                .interpolator(Interpolators.SINE.EASE_IN())
                .onFinished(entity2::removeFromWorld)
                .translate(entity2)
                .from(entity2.getPosition())
                .to(new Point2D(entity2.getX(), FXGL.getAppHeight() + 10))
                .buildAndPlay();
        Entities.animationBuilder()
                .rotate(entity2)
                .rotateFrom(0)
                .rotateTo(360)
                .buildAndPlay();
    }

}
