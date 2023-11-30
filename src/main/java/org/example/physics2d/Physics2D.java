package org.example.physics2d;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class Physics2D {
    private Vec2 gravity = new Vec2(0, 501f);
    private World world = new World(gravity);

    private float physicsTime = 0.0f;
    private float physicsTimeStep = 1.0f / 60.0f;
    private int velocityIterations = 8;
    private int positionIterations = 3;

    public Physics2D() {
        // Create a static ground
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(0, 131); // Adjust the position as needed
        Body groundBody = world.createBody(groundBodyDef);

        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(140.0f, 1.0f); // Set the shape as a wide and flat box

        FixtureDef groundFixtureDef = new FixtureDef();
        groundFixtureDef.shape = groundShape;

        groundBody.createFixture(groundFixtureDef);
    }

    public void update(float dt) {
        physicsTime += dt;

        if (physicsTime >= 0.0f) {
            physicsTime -= physicsTimeStep;
            world.step(physicsTimeStep, velocityIterations, positionIterations);
        }
    }

    public World getWorld() {
        return world;
    }
}
