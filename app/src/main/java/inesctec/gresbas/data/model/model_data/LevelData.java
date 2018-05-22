package inesctec.gresbas.data.model.model_data;

import java.util.Vector;

import inesctec.gresbas.data.model.model_files.Level;

public class LevelData {
    Vector<Level> userlevel;

    public Vector<Level> getLevel() {
        return userlevel;
    }

    public void setUser(Vector<Level> level) {
        this.userlevel = level;
    }
}
