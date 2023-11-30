package org.example;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.*;
import org.example.physics2d.Physics2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;


public class Main {
    private static double map_x = 0;

    public static void main(String[] args) throws IOException, InterruptedException, FontFormatException {
        File fontFile = new File("src/main/resources/fonts/square.ttf");
        Font font =  Font.createFont(Font.TRUETYPE_FONT, fontFile);

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(font);

        AWTTerminalFontConfiguration cfg = new SwingTerminalFontConfiguration(true, AWTTerminalFontConfiguration.BoldMode.NOTHING, font);

        DefaultTerminalFactory factory = new DefaultTerminalFactory()
                .setInitialTerminalSize(new TerminalSize(160, 140))
                .setTerminalEmulatorFontConfiguration(cfg)
                .setTerminalEmulatorTitle("SuperMario");

        Font loadedFont = font.deriveFont(Font.PLAIN, 5);
        AWTTerminalFontConfiguration fontConfig = AWTTerminalFontConfiguration.newInstance(loadedFont);
        factory.setTerminalEmulatorFontConfiguration(fontConfig);
        factory.setForceAWTOverSwing(true);

        Terminal terminal = factory.createTerminal();

        Screen screen = new TerminalScreen(terminal);
        screen.setCursorPosition(null);
        screen.startScreen();


        terminal.enterPrivateMode();

        Physics2D physics2D = new Physics2D();

        Image mario_image = new Image("src/main/resources/sprites/mario.png", screen);
        Image map = new Image("src/main/resources/map.png", screen);
        BufferedImage tile_image = ImageIO.read(new File("src/main/resources/tilesets/overworld_tileset.png"));

        Tileset tileset = new Tileset(tile_image, screen);
        tileset.addTile(0, 0, 0, 16, 16);
        Mario mario = new Mario(10, 192, mario_image, physics2D.getWorld());
        Image block = tileset.getTile(0);


        AtomicLong startTime = new AtomicLong();
        AtomicLong oldStartTime = new AtomicLong();
        AtomicLong deltaTime = new AtomicLong();


        ScheduledExecutorService scheduler1 = Executors.newScheduledThreadPool(1);
        ScheduledExecutorService scheduler2 = Executors.newScheduledThreadPool(1);

        while (true) {
            try {
                if (Keyboard.isKeyPressed(KeyEvent.VK_UP)) mario.jump();

                if (Keyboard.isKeyPressed(KeyEvent.VK_RIGHT)) map_x += 7;
                else if (Keyboard.isKeyPressed(KeyEvent.VK_LEFT)) map_x -= 7;
                    // else if (Keyboard.isKeyPressed(KeyEvent.VK_UP)) mario.incrementY(-4);
                else if (Keyboard.isKeyPressed(KeyEvent.VK_DOWN)) mario.incrementY(4);

                oldStartTime.set(startTime.get());
                startTime.set(System.nanoTime());

                long startTimDraw = System.currentTimeMillis();
                map.displacedDraw((int) map_x, 100);
                mario.draw();
                long endTimeDraw = System.currentTimeMillis();
                System.out.println("Time to draw: " + (endTimeDraw - startTimDraw) + " ms");

                long startTimeRefresh = System.currentTimeMillis();
                screen.refresh(Screen.RefreshType.COMPLETE);
                long endTimeRefresh = System.currentTimeMillis();
                System.out.println("Time to refresh: " + (endTimeRefresh - startTimeRefresh) + " ms");


                deltaTime.set((oldStartTime.get() - startTime.get()) / 1_000_000); // Convert nanoseconds to milliseconds
                System.out.println("Time between frames: " + deltaTime + " ms\n");
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        /*
        Runnable physics = () -> {

            physics2D.update(1.0f / 60.0f);
            mario.update();
        };

         */

        // scheduler1.scheduleWithFixedDelay(run, 0, 16, TimeUnit.MILLISECONDS);
        // scheduler2.scheduleWithFixedDelay(physics, 0, 5, TimeUnit.MILLISECONDS);

    }
}
