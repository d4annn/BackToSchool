package com.dan.backtoschool.engine;

import com.dan.backtoschool.engine.graph.Render;
import com.dan.backtoschool.engine.scene.Scene;

public interface IAppLogic {

    void cleanup();

    void init(Window window, Scene scene, Render render);

    void input(Window window, Scene scene, double diffTimeMillis, boolean inputConsumed);

    void update(Window window, Scene scene, double diffTimeMillis);
}
