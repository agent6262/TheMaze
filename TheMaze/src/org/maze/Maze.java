/**
 * 
 */
package org.maze;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glEnable;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.ajgl.AJGLData;
import org.ajgl.Logger;
import org.ajgl.PluginLoader;
import org.ajgl.collision.QuadTree;
import org.ajgl.event.EventDispatcher;
import org.ajgl.event.keyboard.KeyboardPressEvent;
import org.ajgl.event.keyboard.KeyboardReleaseEvent;
import org.ajgl.event.mouse.MouseMoveEvent;
import org.ajgl.event.mouse.MousePressEvent;
import org.ajgl.event.mouse.MouseReleaseEvent;
import org.ajgl.graphics.Graphics;
import org.ajgl.primary.GameObject;
import org.ajgl.util.Configuration;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.maze.event.CollisionEvent;
import org.maze.mouse.MouseBox;
import org.maze.mouse.MouseListener;


/**
 * @author Tyler Bucher
 */
public class Maze {
    
    // Game data
    private static int xSize, ySize;
    private static String title;
    private static boolean fullscreen;
    private static boolean resizable;
    private static boolean vSync;
    private static boolean debug;
    
    // Directory
    private static File pluginDir;
    
    // Configuration
    private static Configuration globalConfig;
    
    // Vertex Buffered Object (VBO)
    private static MouseBox mouseBox;
    private static ArrayList<GameObject> movingObjectList;
    private static ArrayList<GameObject> staticObjectList;
    
    // Event Dispatcher
    private static EventDispatcher eventDispatcher;
    
    // QuadTree collision detection
    private static QuadTree quadTree;

    private static void preInitGl() {
        configurationPreInitGl();
        gameDataPreInitGL();
    }
    
    private static void configurationPreInitGl() {
        // Global configuration loading/creating
        Logger.log("Loading Global Configuration");
        File globalConfigFile = new File(System.getProperty("user.dir")+"\\Global.config");
        if(!globalConfigFile.exists() || globalConfigFile.length() == 0) {
            try {
                Logger.log("    Creating Global Configuration");
                Files.copy(Maze.class.getResourceAsStream("/Global.config"), globalConfigFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                globalConfig = Configuration.loadConfiguration(globalConfigFile);
            } catch (IOException e) {
                Logger.warning("Configuration Error: "+e.getMessage());
            }
        } else {
            globalConfig = Configuration.loadConfiguration(globalConfigFile);
        }
        
        // Plugin directory creating
        Logger.log("Loading Plugin Directory");
        pluginDir = new File(System.getProperty("user.dir")+"\\plugins");
        if(!pluginDir.exists()) {
            Logger.log("    Creating Plugin Directory");
            pluginDir.mkdir();
        }
    }
    
    private static void gameDataPreInitGL() {
        Logger.log("Grabbing Values from [Global.config]");
        xSize = globalConfig.getInt("xSize", 1200);
        ySize = globalConfig.getInt("ySize", 800);
        title = globalConfig.getString("Title", "Abstract Java Game Library");
        fullscreen = globalConfig.getBoolean("FullScreen", false);
        resizable = globalConfig.getBoolean("Resizable", false);
        vSync = globalConfig.getBoolean("VSync", true);
        debug = globalConfig.getBoolean("debug", false);
    }
    
    private static void initGL() {
        Logger.log("Initializing OpenGL Context");
        try {
            // Initialize display information
            Display.setDisplayMode(new DisplayMode(xSize, ySize));
            Display.setTitle(title);
            Display.setFullscreen(fullscreen);
            Display.setResizable(resizable);
            Display.setVSyncEnabled(vSync);
            // Set display icons
            Display.setIcon(new ByteBuffer[]{AJGLData.Icon_16, AJGLData.Icon_32});
            // Create display
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        // Initialize openGl
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, xSize, 0, ySize, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        // Enable extra OpenGL stuff
        glEnable(GL_TEXTURE_2D);
    }

    private static void init() {
        vertexBufferedObjectInit();
        quadTreeInit();
        eventDispatcherInit();
        loadPlugins();
    }
    
    private static void vertexBufferedObjectInit() {
        // Create a GameObject list
        staticObjectList = new ArrayList<>();
        movingObjectList = new ArrayList<>();
        // Creates a GameObject box for the mouse
        mouseBox = new MouseBox();
    }
    
    private static void quadTreeInit() {
        quadTree = new QuadTree(xSize, ySize, 10);
        quadTree.add(mouseBox);
    }
    
    private static void eventDispatcherInit() {
        eventDispatcher = new EventDispatcher();
        
        // Load mouseListener
        eventDispatcher.registerEvents(MouseListener.class);
    }
    
    private static void loadPlugins() {
        try {
            Logger.log("Loading Plugins");
            PluginLoader.loadPlugins();
        } catch (IOException e) {
            Logger.severe("PluginLoader Error: "+e.getMessage());
        }
    }
    
    /**
     * Is intended to initializes main game thread. | methods below ran in order
     * | while no close request{ input(), update(), render()} exit.
     */
    private static void gameStart() {
        while (!Display.isCloseRequested()) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            // Run cycles
            input();
            update();
            render();
            // Display synchronization
            Display.update();
            Display.sync(60);
        }
        Graphics.clearVboHandlers();
        exit();
    }
    
    private static void input() {
        mouseInput();
        keyboardInput();
    }
    
    private static void mouseInput() {
        while(Mouse.next()) {
            if(Mouse.getEventButtonState()) {
                eventDispatcher.dispatchEvent(new MousePressEvent(Mouse.getEventButton()));
            } else {
                if(Mouse.getEventButton() != -1)
                    eventDispatcher.dispatchEvent(new MouseReleaseEvent(Mouse.getEventButton()));
            }
            // Mouse move
            if(Mouse.getEventDX() != 0 || Mouse.getEventDY() != 0) {
                eventDispatcher.dispatchEvent(new MouseMoveEvent(Mouse.getEventDX(), Mouse.getEventDY()));
            }
        }
    }
    
    private static void keyboardInput() {
        while(Keyboard.next()) {
            if(Keyboard.getEventKeyState()) {
                eventDispatcher.dispatchEvent(new KeyboardPressEvent(Keyboard.getEventKey()));
            } else {
                eventDispatcher.dispatchEvent(new KeyboardReleaseEvent(Keyboard.getEventKey()));
            }
        }
    }
    
    private static void update() {
        vertexBufferedObjectUpdate();
        quadTreeUpdate();
        handleCollision();
        finalization();
    }
    
    private static void vertexBufferedObjectUpdate() {
        mouseBox.updatePosition();
        // Update list of objects
        if(movingObjectList.size() != 0)
            for(GameObject o : movingObjectList)
                o.updatePosition();
    }
    
    private static void quadTreeUpdate() {
        if(movingObjectList.size() != 0)
            for(GameObject o : movingObjectList)
                quadTree.updateObject(o);
            quadTree.updateObject(mouseBox);
    }
    
    private static void handleCollision() {
        for(int i=0;i<movingObjectList.size();i++) {
            List<GameObject> tempList = quadTree.getPossibleCollision(movingObjectList.get(i));
            for(int j=0;j<tempList.size();j++) {
                if(!movingObjectList.get(i).equals(tempList.get(j))) {
                    eventDispatcher.dispatchEvent(new CollisionEvent(movingObjectList.get(i), tempList.get(j)));
                }
            }
        }
    }

    private static void finalization() {
        mouseBox.finalizeUpdate();
        
        if(movingObjectList.size() != 0)
            for(GameObject o : movingObjectList)
                o.finalizeUpdate();
    }
    
    private static void render() {
        vertexBufferedObjectRender();
        if(debug) {
            quadTreeRender();
        }
    }
    
    private static void vertexBufferedObjectRender() {
        if(debug) {
            mouseBox.draw();
            mouseBox.getAabb().draw();
        }
        // Draw list of objects
        if(staticObjectList.size() != 0)
            for(GameObject o : staticObjectList)
                o.draw();
        
        if(movingObjectList.size() != 0)
            for(GameObject o : movingObjectList)
                o.draw();
    }
    
    private static void quadTreeRender() {
        quadTree.draw();
    }
    
    /**
     * Is intended to exit and close the program.
     */
    public static void exit() {
        Display.destroy();
        System.exit(0);
    }
    
    public static void main(String[] args) {
        
        System.out.println();
        System.out.println("================================================================================");
        System.out.println("========================     Starting The Maze Game     ========================");
        System.out.println("================================================================================");
        
        Maze.preInitGl();
        Maze.initGL();
        Maze.init();
        Maze.gameStart();
    }
    
    public static EventDispatcher getDispatcher() {
        return eventDispatcher;
    }
    
    /**
     * @return the xSize
     */
    public static int getxSize() {
        return xSize;
    }
    
    /**
     * @return the ySize
     */
    public static int getySize() {
        return ySize;
    }
    
    /**
     * @return the title
     */
    public static String getTitle() {
        return title;
    }
    
    /**
     * @return the fullscreen
     */
    public static boolean isFullscreen() {
        return fullscreen;
    }
    
    /**
     * @return the resizable
     */
    public static boolean isResizable() {
        return resizable;
    }
    
    /**
     * @return the vSync
     */
    public static boolean isvSync() {
        return vSync;
    }
    
    /**
     * @return the debug
     */
    public static boolean isDebug() {
        return debug;
    }
    
    /**
     * @return the globalConfig
     */
    public static Configuration getGlobalConfig() {
        return globalConfig;
    }
    
    /**
     * @return the movingObjectList
     */
    public static ArrayList<GameObject> getMovingObjectList() {
        return movingObjectList;
    }
    
    /**
     * @return the staticObjectList
     */
    public static ArrayList<GameObject> getStaticObjectList() {
        return staticObjectList;
    }
    
    /**
     * @return the quadTree
     */
    public static QuadTree getQuadTree() {
        return quadTree;
    }
    
    public static File getPLuginDir() {
        return pluginDir;
    }
    
    public static GameObject getMouseBox() {
        return mouseBox;
    }
    
}
