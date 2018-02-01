import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.effect.ParticleControl;
import com.almasb.fxgl.effect.ParticleEmitter;
import com.almasb.fxgl.effect.ParticleEmitters;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.component.CollidableComponent;
import com.almasb.fxgl.entity.component.HealthComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.almasb.fxgl.texture.Texture;
import control.BallControl;
import control.BrickControl;
import control.PlatformControl;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;



@SetEntityFactory
public final class BricksFactory implements EntityFactory {

    private static int lives; // liczba uderzen potrzeba by zniszczyc brick


    @Spawns("platform")
    public Entity newPlatform(SpawnData data) {

        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.KINEMATIC);
        Texture texture = FXGL.getAssetLoader().loadTexture("platforma3.png",115,20);
        texture.setPreserveRatio(true);

        return Entities.builder()
                .from(data)
                .type(BricksType.PLATFORM)
                .viewFromNodeWithBBox(texture)
                .with(physics,new CollidableComponent(true))
                .with(new PlatformControl())
                .build();
    }

    @Spawns("brick")
    public Entity newBrick(SpawnData spawnData)
    {
        return Entities.builder()
                .from(spawnData)
                .type(BricksType.BRICK)
                .with(new PhysicsComponent(), new CollidableComponent(true),new HealthComponent(lives))
                .with(new BrickControl())
                .build();

    }
    @Spawns("ball")
    public Entity newBall(SpawnData spawnData)
    {

        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setFixtureDef(new FixtureDef().restitution(1f).density(0.03f));

        Texture texture = FXGL.getAssetLoader().loadTexture("ball.png",20,20);
        texture.setPreserveRatio(true);
        return Entities.builder()
                .from(spawnData)
                .type(BricksType.BALL)
                .bbox(new HitBox("Main", BoundingShape.circle(10f)))
                //.viewFromNode(new Circle(7, Color.LIGHTCORAL))
                .viewFromNode(texture)
                .with(physics, new CollidableComponent(true))
                .with(new BallControl())
                .build();
    }

    public static void setLives(int lives) {
        BricksFactory.lives = lives;
    }

    public static int getLives() {
        return lives;
    }
}
