package com.dan.backtoschool.engine.graph;

import com.dan.backtoschool.engine.Window;
import com.dan.backtoschool.engine.scene.Scene;
import org.lwjgl.opengl.GL;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

public class Render {

    private GuiRender guiRender;
    private SceneRender sceneRender;
    private SkyBoxRender skyBoxRender;
    private Map<Character, Glyph> glyphs = new HashMap<>();

    public Render(Window window) {
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        sceneRender = new SceneRender();
        guiRender = new GuiRender(window);
        skyBoxRender = new SkyBoxRender();
    }

    public void cleanup() {
        sceneRender.cleanup();
        guiRender.cleanup();
        skyBoxRender.cleanup();
    }


    public void render(Window window, Scene scene) {

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glViewport(0, 0, window.getWidth(), window.getHeight());



        skyBoxRender.render(scene);
        sceneRender.render(scene);
        guiRender.render(scene);
        render3DSquare();
    }

    private void render3DSquare() {
        float squareSize = 10.0f;
        float squareX = 1.0f;
        float squareY = 1.0f;
        float squareZ = 1.0f;

        glColor3f(1.0f, 1.0f, 1.0f);

        glBegin(GL_LINES);
        glVertex3f(squareX - squareSize / 2, squareY, squareZ - squareSize / 2);
        glVertex3f(squareX + squareSize / 2, squareY, squareZ - squareSize / 2);

        glVertex3f(squareX + squareSize / 2, squareY, squareZ - squareSize / 2);
        glVertex3f(squareX + squareSize / 2, squareY, squareZ + squareSize / 2);

        glVertex3f(squareX + squareSize / 2, squareY, squareZ + squareSize / 2);
        glVertex3f(squareX - squareSize / 2, squareY, squareZ + squareSize / 2);

        glVertex3f(squareX - squareSize / 2, squareY, squareZ + squareSize / 2);
        glVertex3f(squareX - squareSize / 2, squareY, squareZ - squareSize / 2);
        glEnd();
    }

    public void resize(int width, int height) {
        guiRender.resize(width, height);
    }
}
