import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.test_game.main.Birds.BlackBird;
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

}
