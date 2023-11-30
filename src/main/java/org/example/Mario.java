package org.example;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import org.example.physics2d.components.Rigidbody2D;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Mario {
    private int x;
    private int y;
    private int vertical_accel = 0;
    private int horizontal_accel = 0;
    private int vertical_vel = 0;
    private int horizontal_vel = 0;
    private Image sprite;
    private Body body;



    public Mario(int x, int y, Image sprite, World world) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(10, 10);
        bodyDef.type = BodyType.DYNAMIC;
        body = world.createBody(bodyDef);

        // Create a polygon shape (square)
        PolygonShape squareShape = new PolygonShape();
        float halfWidth = 1.0f; // Half of the square's width
        squareShape.setAsBox(halfWidth, halfWidth); // Set the shape as a box

        // Create a fixture definition
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = squareShape; // Set the shape of the fixture
        fixtureDef.density = 1.0f; // Density of the body (mass is calculated from density and shape area)
        fixtureDef.friction = 0.0f; // Friction coefficient

        // Attach the fixture to the body
        body.createFixture(fixtureDef);
    }

    public void update() {
        this.x = (int) body.getPosition().x;
        this.y = (int) body.getPosition().y;
    }

    public void jump() {
        // Apply an impulse upwards to make the player jump
        Vec2 jumpImpulse = new Vec2(0.0f, -50.0f * body.getMass()); // Adjust the impulse strength as needed
        body.applyLinearImpulse(jumpImpulse, body.getPosition());
    }

    public void draw() throws IOException, InterruptedException {
        sprite.draw(x, y);
    }


    public void incrementX(int x) {
        this.x += x;
    }

    public void incrementY(int y) {
        this.y += y;
    }
}
