package inesctec.gresbas.data.model.model_data;

import java.util.Vector;

import inesctec.gresbas.data.model.model_files.Users;

public class UsersData {
    Vector<Users> user;

    public Vector<Users> getUsers() {
        return user;
    }

    public void setUser(Vector<Users> user) {
        this.user = user;
    }
}
