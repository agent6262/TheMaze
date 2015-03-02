package org.maze.mouse;

import org.ajgl.event.EventHandler;
import org.ajgl.event.mouse.MouseMoveEvent;
import org.lwjgl.input.Mouse;
import org.maze.Maze;

public class MouseListener {

    @EventHandler(MouseMoveEvent.class)
    public void onMouseMoveEbent(MouseMoveEvent event) {
        // Update mouse box
        Maze.getMouseBox().dx += Mouse.getEventDX();
        Maze.getMouseBox().dy += Mouse.getEventDY();
    }
}
