package com.dan.backtoschool.engine;

import com.dan.backtoschool.engine.scene.Scene;

public interface IGuiInstance {
    void drawGui();

    boolean handleGuiInput(Scene scene, Window window);
}
