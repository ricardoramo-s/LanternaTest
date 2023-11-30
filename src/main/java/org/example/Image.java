package org.example;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import org.w3c.dom.Text;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Image {
    private final TextGraphics graphics;
    private final Screen screen;
    private TextCharacter[][] pixels;

    public Image(String imgPath, Screen screen) throws IOException, FontFormatException {
        // auto-loads image
        this.load(imgPath);
        this.graphics = screen.newTextGraphics();
        this.screen = screen;
    }

    public Image(BufferedImage image, Screen screen) throws IOException {
        this.load(image);
        this.graphics = screen.newTextGraphics();
        this.screen = screen;
    }

    private void load(String imgPath) throws IOException {

        BufferedImage image = ImageIO.read(new File(imgPath));
        // load selected img ^

        int width = image.getWidth();
        int height = image.getHeight();
        this.pixels = new TextCharacter[width][height];

        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                int rgb = image.getRGB(x, y);

                int alpha = (rgb >> 24) & 0xFF;
                int r, b, g;
                r = (rgb >> 16) & 0xFF;
                g = (rgb >> 8) & 0xFF;
                b = rgb & 0xFF;

                if (alpha != 0) {
                    TextCharacter character = new TextCharacter(' ').withBackgroundColor(new TextColor.RGB(r, g, b));
                    this.pixels[x][y] = character;
                }
            }
        }
    }

    private void load(BufferedImage image) throws IOException {
        int width = image.getWidth();
        int height = image.getHeight();
        this.pixels = new TextCharacter[width][height];

        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                int rgb = image.getRGB(x, y);

                int r, b, g, alpha;
                alpha = (rgb >> 24) & 0xFF;
                r = (rgb >> 16) & 0xFF;
                g = (rgb >> 8) & 0xFF;
                b = rgb & 0xFF;

                if (alpha != 0) {
                    TextCharacter character = TextCharacter.fromCharacter(' ')[0];
                    this.pixels[x][y] = character.withBackgroundColor(new TextColor.RGB(r, g, b));
                }
            }
        }
    }

    public void draw() throws IOException, InterruptedException {
        for (int x=0; x<Math.min(this.pixels.length, 256); x++) {
            for (int y=0; y<Math.min(this.pixels[0].length, 240); y++) {
                TextCharacter character = this.pixels[x][y];
                if (character == null) continue;

                this.graphics.setCharacter(x, y, character);
            }
        }
    }

    public void draw(int x, int y) throws IOException, InterruptedException {
        for (int ix=0; ix<this.pixels.length; ix++) {
            for (int iy=0; iy<this.pixels[0].length; iy++) {
                TextCharacter character = this.pixels[ix][iy];
                if (character == null) continue;

                this.graphics.setCharacter(ix + x, iy + y, character);
            }
        }
    }

    public void displacedDraw(int x, int y) throws IOException, InterruptedException {
        for (int ix=x; ix<Math.min(this.pixels.length, x + 256); ix++) {
            for (int iy=y; iy<Math.min(this.pixels[0].length, y + 240); iy++) {
                TextCharacter character = this.pixels[ix][iy];
                if (character == null) continue;

                this.graphics.setCharacter(ix - x, iy - y, character);
            }
        }
    }
}