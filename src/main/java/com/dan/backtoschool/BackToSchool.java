package com.dan.backtoschool;

import com.dan.backtoschool.engine.Engine;
import com.dan.backtoschool.engine.IAppLogic;
import com.dan.backtoschool.engine.MouseInput;
import com.dan.backtoschool.engine.Window;
import com.dan.backtoschool.engine.graph.Material;
import com.dan.backtoschool.engine.graph.Mesh;
import com.dan.backtoschool.engine.graph.Model;
import com.dan.backtoschool.engine.graph.Render;
import com.dan.backtoschool.engine.scene.Camera;
import com.dan.backtoschool.engine.scene.Entity;
import com.dan.backtoschool.engine.scene.ModelLoader;
import com.dan.backtoschool.engine.scene.Scene;
import com.dan.backtoschool.engine.scene.lights.DirLight;
import com.dan.backtoschool.engine.scene.lights.SceneLights;
import com.dan.backtoschool.game.entities.Player;
import com.dan.backtoschool.game.entities.Room;
import com.dan.backtoschool.game.entities.Table;
import org.joml.*;
import org.lwjgl.system.Configuration;

import java.lang.Math;
import java.util.Collection;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class BackToSchool implements IAppLogic {

    private static final float MOUSE_SENSITIVITY = 0.1f;
    private static final float MOVEMENT_SPEED = 0.0035f;
    private Room room;
    private Player player;


    private float lightAngle;

    public static void main(String[] args) {
        BackToSchool main = new BackToSchool();
        Engine gameEng = new Engine("test", new Window.WindowOptions(), main);
        gameEng.start();
    }

    @Override
    public void cleanup() {
        // Nothing to be done yet
    }

    private void addWithPosition(String name, String modelId, int x, float y, int z, Scene scene) {
        Table table = new Table(name, modelId);
        table.setPosition(x, y, z);
        room.addTable(table);
        scene.addEntity(table);
    }

    @Override
    public void init(Window window, Scene scene, Render render) {
        Configuration.STACK_SIZE.set(120);
        Model roomModel = ModelLoader.loadModel("room-model", "resources/models/room/room.obj",
                scene.getTextureCache(), false);
        scene.addModel(roomModel);
        room = new Room("room", roomModel.getId());
        room.setPosition(0, 0, 0);
        scene.addEntity(room);

        Model table = ModelLoader.loadModel("table-model", "resources/models/room/table.obj",
                scene.getTextureCache(), false);
        scene.addModel(table);
        addWithPosition("table1", table.getId(), -2, 1, 1, scene);


        SceneLights sceneLights = new SceneLights();
        sceneLights.getAmbientLight().setIntensity(0.2f);
        DirLight dirLight = sceneLights.getDirLight();
        dirLight.setPosition(1, 1, 0);
        dirLight.setIntensity(1.0f);
        scene.setSceneLights(sceneLights);

        Camera camera = scene.getCamera();
        camera.addRotation(0, (float) Math.toRadians(90));

        player = new Player("player", null);
        player.setCamera(camera);
        player.setPosition(0, 0, 0);
        camera.setPosition(0, 0.8f, 0);

        lightAngle = -35;

    }

    @Override
    public void input(Window window, Scene scene, double diffTimeMillis, boolean inputConsumed) {
        if (inputConsumed) {
            return;
        }
        double move = diffTimeMillis * MOVEMENT_SPEED;
        Camera camera = scene.getCamera();
        if (window.isKeyPressed(GLFW_KEY_W)) {
            player.moveForward((float) move, room);
        }
        if (window.isKeyPressed(GLFW_KEY_S)) {
            player.moveBackwards((float) move, room);
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            player.moveLeft((float) move, room);
        }
        if (window.isKeyPressed(GLFW_KEY_D)) {
            player.moveRight((float) move, room);
        }
        if (window.isKeyPressed(GLFW_KEY_R)) {
            System.out.println(this.player.getPosition());
        }
        if (window.isKeyPressed(GLFW_KEY_SPACE)) {
            player.jump(room);
        }
        if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            lightAngle -= 2.5f;
            if (lightAngle < -90) {
                lightAngle = -90;
            }
        }
        if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            lightAngle += 2.5f;
            if (lightAngle > 90) {
                lightAngle = 90;
            }
        }

        MouseInput mouseInput = window.getMouseInput();
        Vector2f displVec = mouseInput.getDisplVec();
        camera.addRotation((float) Math.toRadians(displVec.x * MOUSE_SENSITIVITY), (float) Math.toRadians(displVec.y * MOUSE_SENSITIVITY));
        if (mouseInput.isLeftButtonPressed()) {

            selectEntity(window, scene, mouseInput.getCurrentPos());
        }

        SceneLights sceneLights = scene.getSceneLights();
        DirLight dirLight = sceneLights.getDirLight();
        double angRad = Math.toRadians(lightAngle);
        dirLight.getDirection().x = (float) Math.sin(angRad);
        dirLight.getDirection().y = (float) Math.cos(angRad);
        player.setPosition(camera.getPosition());
    }

    @Override
    public void update(Window window, Scene scene, double diffTimeMillis) {
        player.update(room, diffTimeMillis);
        for (Table table : room.getTables()) {
            table.update(room);
            if(table.collidesWithEntity(player.getPosition())) {
                table.onCollision();
            }
        }
    }

    private void selectEntity(Window window, Scene scene, Vector2f mousePos) {
        int wdwWidth = window.getWidth();
        int wdwHeight = window.getHeight();

        float x = (2 * mousePos.x) / wdwWidth - 1.0f;
        float y = 1.0f - (2 * mousePos.y) / wdwHeight;
        float z = -1.0f;

        Matrix4f invProjMatrix = scene.getProjection().getInvProjMatrix();
        Vector4f mouseDir = new Vector4f(x, y, z, 1.0f);
        mouseDir.mul(invProjMatrix);
        mouseDir.z = -1.0f;
        mouseDir.w = 0.0f;

        Matrix4f invViewMatrix = scene.getCamera().getInvViewMatrix();
        mouseDir.mul(invViewMatrix);
        Vector4f min = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
        Vector4f max = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
        Vector2f nearFar = new Vector2f();

        Entity selectedEntity = null;
        float closestDistance = Float.POSITIVE_INFINITY;
        Vector3f center = scene.getCamera().getPosition();

        Collection<Model> models = scene.getModelMap().values();
        Matrix4f modelMatrix = new Matrix4f();
        for (Model model : models) {
            List<Entity> entities = model.getEntitiesList();
            for (Entity entity : entities) {
                modelMatrix.translate(entity.getPosition()).scale(entity.getScale());
                for (Material material : model.getMaterialList()) {
                    for (Mesh mesh : material.getMeshList()) {
                        Vector3f aabbMin = mesh.getAabbMin();
                        min.set(aabbMin.x, aabbMin.y, aabbMin.z, 1.0f);
                        min.mul(modelMatrix);
                        Vector3f aabMax = mesh.getAabbMax();
                        max.set(aabMax.x, aabMax.y, aabMax.z, 1.0f);
                        max.mul(modelMatrix);
                        if (Intersectionf.intersectRayAab(center.x, center.y, center.z, mouseDir.x, mouseDir.y, mouseDir.z,
                                min.x, min.y, min.z, max.x, max.y, max.z, nearFar) && nearFar.x < closestDistance) {
                            closestDistance = nearFar.x;
                            selectedEntity = entity;
                        }
                    }
                }
                modelMatrix.identity();
            }
        }
    }

}
