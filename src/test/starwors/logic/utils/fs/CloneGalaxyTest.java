package starwors.logic.utils.fs;


import junit.framework.Assert;
import starwors.model.lx.bot.Logic;
import starwors.model.lx.galaxy.Planet;
import starwors.model.lx.galaxy.PlanetType;
import starwors.model.lx.logic.utils.PlanetCloner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

public class CloneGalaxyTest {

    private Collection<Planet> galaxy;

    private static final String ME = "me";
    private static final String ENEMY = "enemy";
    private static final String FREE = "";


    @Before
    public void setUp() {
        Logic.botName = ME;
        galaxy = new ArrayList<Planet>();

        Planet start = UtilsForTest.getPlanet("0", ME, PlanetType.TYPE_D.getLimit(), PlanetType.TYPE_D);
        Planet child1 = UtilsForTest.getPlanet("1", ME, PlanetType.TYPE_C.getLimit(), PlanetType.TYPE_C);
        Planet child2 = UtilsForTest.getPlanet("2", ME, PlanetType.TYPE_C.getLimit(), PlanetType.TYPE_C);
        start.addNeighbours(child1);
        start.addNeighbours(child2);
        child1.addNeighbours(start);
        child2.addNeighbours(start);

        Planet child11 = UtilsForTest.getPlanet("11", FREE, PlanetType.TYPE_C.getLimit(), PlanetType.TYPE_C);
        Planet child12 = UtilsForTest.getPlanet("12", FREE, PlanetType.TYPE_B.getLimit(), PlanetType.TYPE_B);
        child1.addNeighbours(child11);
        child1.addNeighbours(child12);
        child11.addNeighbours(child1);
        child12.addNeighbours(child1);

        Planet child21 = UtilsForTest.getPlanet("21", FREE, PlanetType.TYPE_C.getLimit(), PlanetType.TYPE_C);
        Planet child22 = UtilsForTest.getPlanet("22", FREE, PlanetType.TYPE_B.getLimit(), PlanetType.TYPE_B);
        child2.addNeighbours(child21);
        child2.addNeighbours(child22);
        child21.addNeighbours(child2);
        child21.addNeighbours(child11);
        child22.addNeighbours(child2);
        child22.addNeighbours(child12);

        Planet child121 = UtilsForTest.getPlanet("121", FREE, PlanetType.TYPE_C.getLimit(), PlanetType.TYPE_A);
        Planet child122 = UtilsForTest.getPlanet("122", ENEMY, PlanetType.TYPE_B.getLimit(), PlanetType.TYPE_D);
        child12.addNeighbours(child121);
        child12.addNeighbours(child122);
        child121.addNeighbours(child12);
        child121.addNeighbours(child122);
        child122.addNeighbours(child12);

        galaxy.add(start);
        galaxy.add(child1);
        galaxy.add(child2);
        galaxy.add(child11);
        galaxy.add(child12);
        galaxy.add(child21);
        galaxy.add(child22);
        galaxy.add(child121);
        galaxy.add(child122);

    }


    @Test
    public void cloneGalaxy(){
        Collection<Planet> galaxy2 = PlanetCloner.clonePlanets(galaxy);
        Assert.assertEquals(galaxy.toString(), galaxy2.toString());

    }

    @After
    public void tearDown() {
        galaxy.clear();
    }


}