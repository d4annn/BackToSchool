package com.dan.backtoschool.engine.graph;

import com.dan.backtoschool.engine.scene.Entity;

import java.util.ArrayList;
import java.util.List;

public class Model {

    private final String id;
    private List<Entity> entitiesList;
    private List<Mesh> meshList;
    private List<Material> materialList;

    public Model(String id, List<Material> materialList) {
        this.id = id;
        entitiesList = new ArrayList<>();
        this.materialList = materialList;
    }

    public Model(String id, List<Mesh> meshList, String empty) {
        this.id = id;
        this.meshList = meshList;
        entitiesList = new ArrayList<>();
    }

    public void cleanup() {
        materialList.forEach(Material::cleanup);
        meshList.forEach(Mesh::cleanup);
    }

    public List<Entity> getEntitiesList() {
        return entitiesList;
    }

    public String getId() {
        return id;
    }

    public List<Material> getMaterialList() {
        return materialList;
    }

    public List<Mesh> getMeshList() {
        return meshList;
    }
}
