//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.mojang.rubydung;

import com.mojang.rubydung.level.Chunk;
import com.mojang.rubydung.level.Level;
import com.mojang.rubydung.level.LevelRenderer;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import javax.swing.*;
import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class RubyDung implements Runnable {
    private static final boolean FULLSCREEN_MODE = false;
    private int width;
    private int height;
    private FloatBuffer fogColor = BufferUtils.createFloatBuffer(4);
    private Timer timer = new Timer(60.0F);
    private Level level;
    private LevelRenderer levelRenderer;
    private Player player;
    private IntBuffer viewportBuffer = BufferUtils.createIntBuffer(16);
    private IntBuffer selectBuffer = BufferUtils.createIntBuffer(2000);
    private HitResult hitResult = null;

    public RubyDung() {
    }

    public void init() throws LWJGLException {
        int col = 920330;
        float fr = 0F;
        float fg = 0F;
        float fb = 0F;
        this.fogColor.put(new float[]{(float)(col >> 16 & 255) / 255.0F, (float)(col >> 8 & 255) / 255.0F, (float)(col & 255) / 255.0F, 1.0F});
        this.fogColor.flip();
        Display.setDisplayMode(new DisplayMode(1024, 768));
        Display.create();
        Keyboard.create();
        Mouse.create();
        this.width = Display.getDisplayMode().getWidth();
        this.height = Display.getDisplayMode().getHeight();
        GL11.glEnable(3553);
        GL11.glShadeModel(7425);
        GL11.glClearColor(fr, fg, fb, 0.0F);
        GL11.glClearDepth(1.0D);
        GL11.glEnable(2929);
        GL11.glDepthFunc(515);
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(5888);
        this.level = new Level(256, 256, 64);
        this.levelRenderer = new LevelRenderer(this.level);
        this.player = new Player(this.level);
        Mouse.setGrabbed(true);
    }

    public void destroy() {
        this.level.save();
        Mouse.destroy();
        Keyboard.destroy();
        Display.destroy();
    }

    public void run() {
        try {
            this.init();
        } catch (Exception var9) {
            JOptionPane.showMessageDialog((Component)null, var9.toString(), "Failed to start RubyDung", 0);
            System.exit(0);
        }

        long lastTime = System.currentTimeMillis();
        int frames = 0;

        try {
            while(!Keyboard.isKeyDown(1) && !Display.isCloseRequested()) {
                this.timer.advanceTime();

                for(int i = 0; i < this.timer.ticks; ++i) {
                    this.tick();
                }

                this.render(this.timer.a);
                ++frames;

                while(System.currentTimeMillis() >= lastTime + 1000L) {
                    //System.out.println(frames + " fps, " + Chunk.updates);
                    Chunk.updates = 0;
                    lastTime += 1000L;
                    frames = 0;
                }
            }
        } catch (Exception var10) {
            var10.printStackTrace();
        } finally {
            this.destroy();
        }

    }

    public void tick() {
        this.player.tick();
    }

    private void moveCameraToPlayer(float a) {
        GL11.glTranslatef(0.0F, 0.0F, -0.3F);
        GL11.glRotatef(this.player.xRot, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(this.player.yRot, 0.0F, 1.0F, 0.0F);
        float x = this.player.xo + (this.player.x - this.player.xo) * a;
        float y = this.player.yo + (this.player.y - this.player.yo) * a;
        float z = this.player.zo + (this.player.z - this.player.zo) * a;
        GL11.glTranslatef(-x, -y, -z);
    }

    private void setupCamera(float a) {
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GLU.gluPerspective(70.0F, (float)this.width / (float)this.height, 0.05F, 1000.0F);
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        this.moveCameraToPlayer(a);
    }

    private void setupPickCamera(float a, int x, int y) {
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        this.viewportBuffer.clear();
        GL11.glGetInteger(2978, this.viewportBuffer);
        this.viewportBuffer.flip();
        this.viewportBuffer.limit(16);
        GLU.gluPickMatrix((float)x, (float)y, 5.0F, 5.0F, this.viewportBuffer);
        GLU.gluPerspective(70.0F, (float)this.width / (float)this.height, 0.05F, 1000.0F);
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        this.moveCameraToPlayer(a);
    }

    private void pick(float a) {
        this.selectBuffer.clear();
        GL11.glSelectBuffer(this.selectBuffer);
        GL11.glRenderMode(7170);
        this.setupPickCamera(a, this.width / 2, this.height / 2);
        this.levelRenderer.pick(this.player);
        int hits = GL11.glRenderMode(7168);
        this.selectBuffer.flip();
        this.selectBuffer.limit(this.selectBuffer.capacity());
        long closest = 0L;
        int[] names = new int[10];
        int hitNameCount = 0;

        for(int i = 0; i < hits; ++i) {
            int nameCount = this.selectBuffer.get();
            long minZ = this.selectBuffer.get();
            this.selectBuffer.get();
            int j;
            if (minZ >= closest && i != 0) {
                for(j = 0; j < nameCount; ++j) {
                    this.selectBuffer.get();
                }
            } else {
                closest = minZ;
                hitNameCount = nameCount;

                for(j = 0; j < nameCount; ++j) {
                    names[j] = this.selectBuffer.get();
                }
            }
        }

        if (hitNameCount > 0) {
            this.hitResult = new HitResult(names[0], names[1], names[2], names[3], names[4]);
        } else {
            this.hitResult = null;
        }

    }

    public void render(float a) {
        float xo = (float)Mouse.getDX();
        float yo = (float)Mouse.getDY();
        this.player.turn(xo, yo);
        this.pick(a);

        while(Mouse.next()) {
            if (Mouse.getEventButton() == 0 && Mouse.getEventButtonState() && this.hitResult != null) {
                this.level.setTile(this.hitResult.x, this.hitResult.y, this.hitResult.z, 0);
            }

            if (Mouse.getEventButton() == 1 && Mouse.getEventButtonState() && this.hitResult != null && !hitPlayer(this.hitResult)) {
                int x = this.hitResult.x;
                int y = this.hitResult.y;
                int z = this.hitResult.z;
                if (this.hitResult.f == 0) {
                    --y;
                }

                if (this.hitResult.f == 1) {
                    ++y;
                }

                if (this.hitResult.f == 2) {
                    --z;
                }

                if (this.hitResult.f == 3) {
                    ++z;
                }

                if (this.hitResult.f == 4) {
                    --x;
                }

                if (this.hitResult.f == 5) {
                    ++x;
                }

                this.level.setTile(x, y, z, 1);
            }
        }

        while(Keyboard.next()) {
            if (Keyboard.getEventKey() == 28 && Keyboard.getEventKeyState()) {
                this.level.save();
            }
        }

        GL11.glClear(16640);
        this.setupCamera(a);
        GL11.glEnable(2884);
        GL11.glEnable(2912);
        GL11.glFogi(2917, 2048);
        GL11.glFogf(2914, 0.2F);
        GL11.glFog(2918, this.fogColor);
        GL11.glDisable(2912);
        this.levelRenderer.render(this.player, 0);
        GL11.glEnable(2912);
        this.levelRenderer.render(this.player, 1);
        GL11.glDisable(3553);
        if (this.hitResult != null) {
            this.levelRenderer.renderHit(this.hitResult);
        }

        GL11.glDisable(2912);
        Display.update();
    }

    private boolean hitPlayer(HitResult hitResult) {
        System.out.println(player.x);
        System.out.println(player.y);
        System.out.println(player.z);
        System.err.println(hitResult.x);
        System.err.println(hitResult.y);
        System.err.println(hitResult.z);
        if (hitResult.x == (int)player.x && hitResult.z == (int)player.z) {
            return hitPlayerY(hitResult.y);
        }
        boolean zeroX = (float)(int)player.x == player.x;
        boolean zeroY = (float)(int)player.y == player.y;
        if (!zeroX && hitResult.x == (int)player.x+1 && !zeroY && hitResult.z == (int)player.z) {
            return hitPlayerY(hitResult.y);
        }
        if (!zeroX && hitResult.x == (int)player.x && !zeroY && hitResult.z == (int)player.z+1) {
            return hitPlayerY(hitResult.y);
        }
        if (!zeroX && hitResult.x == (int)player.x+1 && !zeroY && hitResult.z == (int)player.z+1) {
            return hitPlayerY(hitResult.y);
        }
        return false;
    }

    private boolean hitPlayerY(int y) {
        return (int)player.y-1==y || (int)player.y-2==y;
    }

    public static void checkError() {
        int e = GL11.glGetError();
        if (e != 0) {
            throw new IllegalStateException(GLU.gluErrorString(e));
        }
    }

    public static void main(String[] args) throws LWJGLException {
        (new Thread(new RubyDung())).start();
    }
}
