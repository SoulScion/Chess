package model;

import java.util.concurrent.ConcurrentHashMap;

public record GamesObjects(ConcurrentHashMap<Integer, GameData> gameObjects) {


}
