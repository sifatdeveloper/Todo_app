package com.example.todo;

public class ModalAdapter {
    String a;
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ModalAdapter(String a, int id) {
        this.a = a;
        this.id=id;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }
}
