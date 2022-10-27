package com.github.br.starmarines.map;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.jgrapht.graph.SimpleGraph;
import org.junit.Ignore;
import org.junit.Test;

import com.github.br.starmarines.game.api.galaxy.Planet;
import com.github.br.starmarines.game.api.galaxy.PlanetType;
import com.github.br.starmarines.gamecore.api.Galaxy;
import com.github.br.starmarines.map.converter.GalaxyEdge;
import com.github.br.starmarines.map.converter.VertexPlanet;
import com.github.br.starmarines.map.converter.fromgalaxy.GalaxyGraphConverter;
import com.github.br.starmarines.map.converter.fromgalaxy.GraphStringConverter;
import com.github.br.starmarines.map.converter.togalaxy.GraphGalaxyConverter;
import com.github.br.starmarines.map.converter.togalaxy.StringGraphConverter;

public class MapServiceImplTest {

	@Ignore("конфликт grapht и saxon. Возможно, проще самому парсить, "
			+ "чем разруливать баги grapht")
	@Test
	public void testConvertGalaxyToGraphML() {
		Galaxy.Builder builder = new Galaxy.Builder("test", new byte[0]);
		
		Planet planet1 = new Planet();
		planet1.setId((short) 1);
		planet1.setOwnerId((short) 1);
		planet1.setType(PlanetType.TYPE_C);
		planet1.setUnits(100);
		
		Planet planet2 = new Planet();
		planet2.setId((short) 2);
		planet2.setOwnerId((short) 2);
		planet2.setType(PlanetType.TYPE_D);
		planet2.setUnits(200);
		
		builder.addPlanet(planet1, true);
		builder.addPlanet(planet2, true);
		
		builder.addEdge(planet1.getId(), planet2.getId());
	    Galaxy galaxy = builder.build();
	    
	    GalaxyGraphConverter galaxyGraphConverter = new GalaxyGraphConverter();
	    GraphStringConverter graphStringConverter = new GraphStringConverter();

		SimpleGraph<VertexPlanet, GalaxyEdge> graph = galaxyGraphConverter
				.convert(galaxy);
		String graphML = graphStringConverter.convert(graph);
		
		System.out.println(graphML);
		assertNotNull(graphML);
	}

	@Test
	public void testConvertGraphMLToGalaxy() throws IOException, URISyntaxException {
		URL url = this.getClass().getResource("/example/example1.graphml");
		byte[] encoded = Files.readAllBytes(Paths.get(url.toURI()));
        String mapAsString = new String(encoded);
        StringGraphConverter stringGraphConverter = new StringGraphConverter();
		SimpleGraph<VertexPlanet, GalaxyEdge> graph = stringGraphConverter
				.convert(mapAsString);
        GraphGalaxyConverter graphGalaxyConverter = new GraphGalaxyConverter();
		Galaxy galaxy = graphGalaxyConverter.convert(url.getFile(), new byte[0], graph);
		System.out.println(galaxy);
	}

}
