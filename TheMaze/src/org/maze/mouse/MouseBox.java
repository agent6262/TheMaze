package org.maze.mouse;

import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glPolygonMode;

import org.ajgl.graphics.Graphics;
import org.ajgl.primary.GameObject;

public class MouseBox extends GameObject {
    
    public MouseBox() {
        super(new float[]{0, 0, 0, 1, 1, 1, 1, 0}, GL_QUADS);
    }
    
    @Override
    public void draw() {
        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        glLineWidth(1.0f);
        Graphics.drawShape(vertexData, colorData, 2, 3, vertCount, GL_SHAPE_TYPE);
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
    }
}
