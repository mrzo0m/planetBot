package com.github.br.starmarines.map.service.api;

import java.util.List;

import com.github.br.starmarines.gamecore.api.Galaxy;

public interface MapService {

	//todo помимо названий хранить максимальное число игроков
	List<String> getAllTitles();
	
	List<String> getTitles(int startIndex, int count); // для пейджинга
	
	Galaxy getMap(String title) throws MapValidationException;

	void saveMap(Galaxy galaxy);

}
