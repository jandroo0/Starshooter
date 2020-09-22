package com.garcia.starshooter.game.objects.ships.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;

public class EnemyShipFactory {

    private ObjectMap<String, EnemyShipStats> enemyShipStats;

    public EnemyShipFactory() {
        // load the enemy ship stats from the json file
//        loadStats();
    }

    @SuppressWarnings("unchecked")
    private void loadStats() {
        Json json = new Json();// create a json object to load the json configuration file
        FileHandle configFileHandle = Gdx.files.internal("stats/enemy_ship_stats.json");//references the json config file in the assets folder
        enemyShipStats = json.fromJson(ObjectMap.class, EnemyShipStats.class, configFileHandle);//load the config into objects
    }

//    public EnemyShip createEnemyShip(int waveCount) {
//        //here you still need to convert the waveCount to the level of enemy ships
//        //I'll use an enum here, but you could also do this by using another configuration json file
//        String level = ShipLevel.of(waveCount).name();
//        EnemyShipStats stats = enemyShipStats.get(level);
//
//        return new EnemyShip(stats);
//    }

    private enum ShipLevel {

        LEVEL_1(1, 2),//
        LEVEL_2(3, 6),//
        LEVEL_3(7, 10);//
        //more levels...

        public final int minWaveCount;
        public final int maxWaveCount;

        private ShipLevel(int minWaveCount, int maxWaveCount) {
            this.minWaveCount = minWaveCount;
            this.maxWaveCount = maxWaveCount;
        }

        public static ShipLevel of(int waveCount) {
            for (ShipLevel level : values()) {
                if (level.minWaveCount >= waveCount && level.maxWaveCount <= waveCount) {
                    return level;
                }
            }
            return LEVEL_3;//return max level by default
        }
    }
}
