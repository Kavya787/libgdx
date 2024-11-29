import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.test_game.main.Birds.Bird;
import com.test_game.main.Birds.BlackBird;
import com.test_game.main.Birds.RedBird;
import com.test_game.main.Birds.YellowBird;
import com.test_game.main.Levels.LevelOne;
import com.test_game.main.Pigs.KingPig;
import com.test_game.main.Pigs.MediumPig;
import com.test_game.main.Pigs.Pig;
import com.test_game.main.Pigs.smallPig;
import com.test_game.main.Player;
import org.junit.Test;


import java.util.ArrayList;

import static org.junit.Assert.*;

public class tester {
    @Test
    public void testKingPig() {
        Pig pg=new KingPig(2,3);
        assertEquals(pg.getPosX(),2,0.01f);
        assertEquals(pg.getPosY(),3,0.1f);
    }
    @Test
    public void testMedPig() {
        Pig pg=new MediumPig(4,5);
        assertEquals(pg.getPosX(),4,0.01f);
        assertEquals(pg.getPosY(),5,0.1f);
    }
    @Test
    public void testSmallPig() {
        Pig pg=new smallPig(2,3);
        assertEquals(pg.getPosX(),2,0.01f);
        assertEquals(pg.getPosY(),3,0.1f);
    }
    @Test
    public void testInitializeLevelOnePigs(){
        ArrayList<Pig>pigs=new ArrayList<>();
        pigs.add(new smallPig(2,3));
        pigs.add(new smallPig(2,3));
        LevelOne lv= new LevelOne(pigs);
        assertEquals(pigs.size(),lv.pigs.size());
    }
    @Test
    public void testYellowBirdSpecial(){
        World world = new World(new Vector2(0, -10f), true);
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(2, 2);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        CircleShape shape = new CircleShape();
        shape.setRadius(2);
        Body body = world.createBody(bodyDef);
        Bird bird= new YellowBird(body);
        float initialx=2;
        float initialy=4;
        Vector2 initialVelocity = new Vector2(initialx, initialy);
        bird.getBody().setLinearVelocity(initialVelocity);
        bird.specialBoost();
        assertEquals(bird.getBody().getLinearVelocity().x,2*initialx,0.01);
        assertEquals(bird.getBody().getLinearVelocity().y,2*initialy,0.01);

    }
    @Test
    public void testPigDamage(){
        Pig pg=new smallPig(2,3);
        pg.reduceHealth(400);
        assertFalse("the status means the pig is alive or not so it is true means the pig is not destroyed",pg.isStatus());

    }

}
