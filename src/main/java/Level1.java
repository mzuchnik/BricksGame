
import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.HealthComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import javafx.geometry.Point2D;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Level1 {

    // zmienna uzywana do oddzielania liter w logu
    private static double val=10;

    /**
     * Inicializuje i dodaje cale logo do swiata gry
     */
    public void initLogoLevel()
    {
        int alfa=49;
        int xs = FXGL.getAppWidth()/5 + 55;
        int ys = FXGL.getAppHeight()/5 +10;
        double[] cirX = new double[37];
        double[] cirY = new double[37];
        double[] alfas = new double[37];
        int i=0;
        while(alfa<(296+49)) {
            cirX[i] = xs + 150 * Math.cos(Math.toRadians(alfa));
            cirY[i] = ys + 150 * Math.sin(Math.toRadians(alfa));
            alfas[i] = alfa;
            alfa+=8;
            i++;
        }
        BricksFactory.setLives(1);
        initBricksLogo(cirX,cirY,alfas);
        BricksFactory.setLives(2);
        initLetters(xs,ys);

    }

    /**
     * Inicializuje i dodaje okrag cyfr z loga do swiata gry
     * @param cirX przetrzymuje pozycje X danej cyfry
     * @param cirY przetrzymuje pozycje Y danej cyfry
     * @param alfas przetrzymuje kat o jaki ma sie obrocic dana cyfa
     */
    private void initBricksLogo(double[] cirX,double[] cirY,double[] alfas)
    {
        int[] v = new int[37];

        //---------------------------------------------------------------
        //Zbiera informacje na temat tego czy w okregu loga znaduje sie 1 czy 0
        //---------------------------------------------------------------
        v[0]=0;v[1]=0;v[2]=0;v[3]=1;v[4]=0;v[5]=1;v[6]=0;v[7]=1;
        v[8]=0;v[9]=1;v[10]=0;v[11]=1;v[12]=0;v[13]=1;v[14]=0;v[15]=1;
        v[16]=1;v[17]=1;v[18]=0;v[19]=1;v[20]=1;v[21]=0;v[22]=1;v[23]=0;
        v[24]=1;v[25]=0;v[26]=0;v[27]=0;v[28]=0;v[29]=1;v[30]=1;v[31]=0;
        v[32]=0;v[33]=0;v[34]=1;v[35]=0;v[36]=1;


        //---------------------------------------------------------------
        //Inicializuje i caly okrag cyfr do swiata gry
        //---------------------------------------------------------------
        for(int i=0;i<37;i++)
        {
            Entity e;
            Entity h = new Entity();
            Text text = new Text();
            Font font = new Font("Verdana",20);
            text.setFont(font);
            text.setFill(Paint.valueOf("WHITE"));
            if(v[i]==1)
            {
                text.setText("1");
                e =  new BricksFactory().newBrick(new SpawnData(cirX[i],cirY[i]));
                e.rotateBy(alfas[i]+90);
                HitBox hitBox = new HitBox(new Point2D(text.getX(),text.getY()-15),BoundingShape.box(text.getLayoutBounds().getWidth(),(text.getLayoutBounds().getHeight()*2/3)));
                e.getBoundingBoxComponent().addHitBox(hitBox);
                e.setView(text);

            }else
            {
                text.setText("0");
                e =  new BricksFactory().newBrick(new SpawnData(cirX[i],cirY[i]));
                e.rotateBy(alfas[i]+90);
                e.getBoundingBoxComponent().addHitBox(new HitBox(new Point2D(text.getX(),text.getY()-15),BoundingShape.box(text.getLayoutBounds().getWidth(),text.getLayoutBounds().getHeight()*2/3)));
                e.setView(text);
            }
            h.setPosition(e.getBoundingBoxComponent().getMinXWorld(),e.getBoundingBoxComponent().getMinYWorld());
            h.setRotation(e.getRotation());
            Rectangle rec = new Rectangle(e.getBoundingBoxComponent().getWidth(),e.getBoundingBoxComponent().getHeight());
            rec.setOpacity(0.5);
            h.setView(rec);
            FXGL.getApp().getGameWorld().addEntity(e);
            //FXGL.getApp().getGameWorld().addEntity(h); // <-- Pokazuje hitboxy cyfr
        }

    }

    /**
     * Inicializuje i dodaje litery wystepujace w logo do swiata gry
     * @param x wspolrzedna X
     * @param y wspolrzedna Y
     */
    private void initLetters(int x, int y)
    {
        y-=20;

        BricksFactory bricksFactory = new BricksFactory();

        //---------------------------------------------------------------
        //Inicializuje i dodaje caly duze "I" wystepujace w logu
        //---------------------------------------------------------------
        Entity e = bricksFactory.newBrick(new SpawnData(x-45,y-70));
        e.setViewWithBBox(FXGL.getAssetLoader().loadTexture("i.png",65,100));
        addEntity(e);

        //---------------------------------------------------------------
        //Inicializuje i dodaje caly kreske pomiedzy "instytu" a, "informatyka" do swiata gry
        //---------------------------------------------------------------
        Rectangle rectangle = new Rectangle(450,3);
        rectangle.setFill(Paint.valueOf("White"));
        e = bricksFactory.newBrick(new SpawnData(x-50,y+37));
        e.setViewWithBBox(rectangle);
        addEntity(e);



        //---------------------------------------------------------------
        //Inicializuje i dodaje caly napis "nstytut" do swiata gry
        //---------------------------------------------------------------
            initInstytut(x,y);
        //---------------------------------------------------------------
        //Inicializuje i dodaje caly napis "informatyki" do swiata gry
        //---------------------------------------------------------------
            initInformatyka(x,y);
        //---------------------------------------------------------------
        //Dodaje "monitor" do swiata gry
        //---------------------------------------------------------------
        e = new BricksFactory().newBrick(new SpawnData(x+315+4,y-77));
        e.setViewWithBBox(FXGL.getAssetLoader().loadTexture("monitor2.png",80,80));
        addEntity(e);

        //---------------------------------------------------------------
        //Dodaje "klawiature" do swiata gry
        //---------------------------------------------------------------
        e = new BricksFactory().newBrick(new SpawnData(x+305+3,y-5));
        e.setViewWithBBox(FXGL.getAssetLoader().loadTexture("keyboard2.png",85,35));
        addEntity(e);

        BricksFactory.setLives(100);
        e = new BricksFactory().newBrick(new SpawnData(0,FXGL.getAppHeight()-10));
        Rectangle rec = new Rectangle(FXGL.getAppWidth(),4);
        rec.setFill(Paint.valueOf("RED"));
        rec.setOpacity(0);
        e.setViewWithBBox(rec);
        addEntity(e);
        val=5;
    }

    private void initInformatyka(int x,int y)
    {
        Entity e;
        BricksFactory bricksFactory = new BricksFactory();
        String[] informatyka = {"i","n","f","o","r","m","a","t","y","k","i"};
        val =0;
        for(int i=0;i<informatyka.length;i++)
        {
            Text text = new Text("i");
            Entity h = new Entity();
            Font font = new Font("Verdana",50);
            text.setFont(font);
            text.setFill(Paint.valueOf("white"));
            text.setText(informatyka[i]);
            e = bricksFactory.newBrick(new SpawnData(x-30+val,y+81));
            val+=text.getLayoutBounds().getWidth()+15;
            e.setView(text);
            HitBox hitBox = new HitBox(new Point2D(text.getX(),text.getY()-34),BoundingShape.box(text.getLayoutBounds().getWidth(),(text.getLayoutBounds().getHeight()*2/3)));
            e.getBoundingBoxComponent().addHitBox(hitBox);
            e.setView(text);
            h.setPosition(e.getBoundingBoxComponent().getMinXWorld(),e.getBoundingBoxComponent().getMinYWorld());
            h.setRotation(e.getRotation());
            Rectangle rec = new Rectangle(e.getBoundingBoxComponent().getWidth(),e.getBoundingBoxComponent().getHeight());
            rec.setOpacity(0.5);
            h.setView(rec);
            addEntity(e);
            //addEntity(h); // <-- Pokazuje hitboxy liter
        }
    }
    private void initInstytut(int x,int y)
    {
        BricksFactory bricksFactory = new BricksFactory();
        String[] nstytut = {"n","s","t","y","t","u","t"};

        for(int i=0;i<nstytut.length;i++) {
            Text text = new Text("i");
            Entity a = new Entity();
            Entity h = new Entity();
            Font font = new Font("Verdana", 70);
            text.setFont(font);
            text.setFill(Paint.valueOf("white"));
            text.setText(nstytut[i]);
            a = bricksFactory.newBrick(new SpawnData(x + 15 + val, y + 28));
            val += text.getLayoutBounds().getWidth() + 5;
            a.setView(text);
            HitBox hitBox = new HitBox(new Point2D(text.getX(), text.getY() - 49), BoundingShape.box(text.getLayoutBounds().getWidth(), (text.getLayoutBounds().getHeight() * 2 / 3)));
            a.getBoundingBoxComponent().addHitBox(hitBox);
            a.setView(text);
            h.setPosition(a.getBoundingBoxComponent().getMinXWorld(), a.getBoundingBoxComponent().getMinYWorld());
            h.setRotation(a.getRotation());
            Rectangle rec = new Rectangle(a.getBoundingBoxComponent().getWidth(), a.getBoundingBoxComponent().getHeight());
            rec.setOpacity(0.5);
            h.setView(rec);
            a.setView(text);
            addEntity(a);
            //addEntity(h); // <-- Pokazuje hitboxy liter
        }
    }
    private void addEntity(Entity entity)
    {
        FXGL.getApp().getGameWorld().addEntity(entity);
    }


}

