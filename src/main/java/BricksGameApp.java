import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.view.EntityView;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.InputMapping;
import com.almasb.fxgl.input.OnUserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.settings.GameSettings;
import control.BallControl;
import control.BrickControl;
import control.PlatformControl;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import static com.almasb.fxgl.app.DSLKt.spawn;
import static com.almasb.fxgl.app.DSLKt.*;
import java.util.Map;

/**
 * Prosta gra na zasadzie brick, zamiast zbijania standartowaych "brick" robimy to z logiem Instytutu Informatyki Politechniki Lubleskiej
 * @author Mateusz Żuchnik (mateusz.zuchnik97@gmail.com)
 */
public class BricksGameApp extends GameApplication {
    /**
     * @return Zwraca PlatformControl
     */
    private PlatformControl getPlatformControl() {
        return getGameWorld().getSingleton(BricksType.PLATFORM).get().getControl(PlatformControl.class);
    }

    /**
     * @return Zwraca BallControl
     */
    private BallControl getBallControl() {
        return getGameWorld().getSingleton(BricksType.BALL).get().getControl(BallControl.class);
    }

    @Override
    protected void initSettings(GameSettings gameSettings) {
            gameSettings.setHeight(800);
            gameSettings.setWidth(600);
            gameSettings.setProfilingEnabled(true);
            gameSettings.setMenuEnabled(false);
            gameSettings.setIntroEnabled(false);
            gameSettings.setTitle("Bricks");
            gameSettings.setFullScreenAllowed(false);
    }

    @Override
    protected void preInit() {
        getAudioPlayer().setGlobalSoundVolume(0.2);
        getAudioPlayer().setGlobalMusicVolume(0.2);
    }

    @Override
    protected void initGame() {
        getGameWorld().setEntityFactory(new BricksFactory());
        initBackground();
        initNewGame();
    }

    @Override
    protected void initInput() {
        Input input = getInput();

        input.addInputMapping(new InputMapping("Move Left",KeyCode.LEFT));
        input.addInputMapping(new InputMapping("Move Right",KeyCode.RIGHT));
        input.addInputMapping(new InputMapping("Release Ball",KeyCode.SPACE));
        input.addInputMapping(new InputMapping("Mouse", MouseButton.PRIMARY)); // Developer cheat, nacisnac myszka na brick, by go usunac ze swiata gry

    }
    @OnUserAction(name = "Move Left")
    public void moveLeft()
    {
        getPlatformControl().left();
    }
    @OnUserAction(name = "Move Right")
    public void moveRight()
    {
        getPlatformControl().right();
    }
    @OnUserAction(name = "Release Ball")
    public void releaseBall() {if(!BallControl.isRelease()) { getBallControl().release();BallControl.setRelease(true); } }

    //-----------------------------------------------------------------------------
    // Developer cheat, nacisnac myszka na brick, by go usunac ze swiata gry
    //-----------------------------------------------------------------------------
    @OnUserAction(name = "Mouse")
    public void changeBallPosition() {
    double x =getInput().getMouseXWorld();
    double y =getInput().getMouseYWorld();
      getGameWorld().getEntitiesInRange(new Rectangle2D(x,y,2,2)).forEach(entity -> {
          if(entity.getType().equals(BricksType.BRICK))
          {
              entity.removeFromWorld();
          }
      });
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().setGravity(0,0);
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(BricksType.BALL, BricksType.BRICK) {
            @Override
            protected void onCollisionBegin(Entity ball, Entity brick) {
                brick.getControl(BrickControl.class).onHit();
                ball.getControl(BallControl.class).isLost();
            }
        });
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) { vars.put("lives",3);vars.put("bricks",59);
    }

    public void initBackground()
    {
        Rectangle bg0 = new Rectangle(getWidth(), getHeight(),
                new LinearGradient(getWidth() / 2, 0, getWidth() / 2, getHeight(),
                        false, CycleMethod.NO_CYCLE,
                        new Stop(0.2, Color.color(0.3216, 0.5373, 0.902)), new Stop(0.8, Color.BLACK)));

        Rectangle bg1 = new Rectangle(getWidth(), getHeight(),  Color.valueOf("#00536d"));
        bg1.setBlendMode(BlendMode.DARKEN);

        EntityView bg = new EntityView();
        bg.addNode(bg0);
        bg.addNode(bg1);

        Entities.builder()
                .viewFromNode(bg)
                .buildAndAttach(getGameWorld());

        Entity screenBounds = Entities.makeScreenBounds(100);

        getGameWorld().addEntity(screenBounds);
    }

    /**
     * dodaj "ball" do swiat gry
     */
    public void initBall() {spawn("ball", FXGL.getAppWidth()/2-15,590);}

    /**
     * dodaje "platforme" do swiata gry
     */
    public void initPlatform() {spawn("platform",FXGL.getAppWidth()/2-57,650);}

    public void initNewGame()
    {
        initBall();
        initPlatform();
        new Level1().initLogoLevel();
    }

    @Override
    protected void onUpdate(double tpf) {
        if(geti("bricks")==0 || geti("lives")==0)
        {
            showGameOver();
        }
    }

    private void showGameOver() {
        getDisplay().showConfirmationBox("Koniec gry. Chcesz zagrać ponownie ?", yes -> {
            if (yes) {
                startNewGame();
            } else {
                exit();
            }
        }
        );
}



    public static void main(String[] args) {
        launch(args);
    }
}
