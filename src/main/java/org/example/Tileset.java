package org.example;

import com.googlecode.lanterna.screen.Screen;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

public class Tileset {
    HashMap<Integer, Area> tiles = new HashMap<>();
    BufferedImage tileset;
    Screen screen;

    public Tileset(BufferedImage tileset, Screen screen) {
        this.screen = screen;
        this.tileset = tileset;
    }

    public Image getTile(int id) throws IOException {
        // check if tile is present
        if (!tiles.containsKey(id)) return null;

        Area area = tiles.get(id);
        return new Image(tileset.getSubimage(area.getX(), area.getY(), area.getWidth(), area.getHeight()), screen);
    }

    public void addTile(int id, int x, int y, int width, int height) {
        tiles.put(id, new Area(x, y, width, height));
    }
}
