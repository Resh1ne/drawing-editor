package by.syritsa.GIIS.algorithms.lb4;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyboardHandler extends KeyAdapter {
    private final Object3D object;
    private final JPanel panel;

    public KeyboardHandler(Object3D object, JPanel panel) {
        this.object = object;
        this.panel = panel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> object.transform(Matrix4x4.translation(0, 1, 0));
            case KeyEvent.VK_S -> object.transform(Matrix4x4.translation(0, -1, 0));
            case KeyEvent.VK_A -> object.transform(Matrix4x4.translation(-1, 0, 0));
            case KeyEvent.VK_D -> object.transform(Matrix4x4.translation(1, 0, 0));
            case KeyEvent.VK_LEFT -> object.rotateY(-10);
            case KeyEvent.VK_RIGHT -> object.rotateY(10);
            case KeyEvent.VK_UP -> object.rotateX(-10);
            case KeyEvent.VK_DOWN -> object.rotateX(10);
            case KeyEvent.VK_Z -> object.scale(1.2);
            case KeyEvent.VK_X -> object.scale(0.8);
        }
        panel.repaint();
    }
}