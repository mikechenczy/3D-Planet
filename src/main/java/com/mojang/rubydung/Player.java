//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.mojang.rubydung;

import com.mojang.rubydung.level.Level;
import com.mojang.rubydung.phys.AABB;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class Player {
    private Level level;
    public float xo;
    public float yo;
    public float zo;
    public float x;
    public float y;
    public float z;
    public float xd;
    public float yd;
    public float zd;
    public float yRot;
    public float xRot;
    public AABB bb;
    public boolean onGround = false;

    public Player(Level level) {
        this.level = level;
        this.resetPos();
    }

    private void resetPos() {
        float x = (float)Math.random() * (float)this.level.width;
        float y = (float)(this.level.depth + 10);
        float z = (float)Math.random() * (float)this.level.height;
        this.setPos(x, y, z);
    }

    private void setPos(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        float w = 0.3F;
        float h = 0.9F;
        this.bb = new AABB(x - w, y - h, z - w, x + w, y + h, z + w);
    }

    public void turn(float xo, float yo) {
        this.yRot = (float)((double)this.yRot + (double)xo * 0.15D);
        this.xRot = (float)((double)this.xRot - (double)yo * 0.15D);
        if (this.xRot < -90.0F) {
            this.xRot = -90.0F;
        }

        if (this.xRot > 90.0F) {
            this.xRot = 90.0F;
        }

    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        float xa = 0.0F;
        float ya = 0.0F;
        boolean front = false;
        if (Keyboard.isKeyDown(19)) {
            this.resetPos();
        }

        if (Keyboard.isKeyDown(17)) {
            --ya;
            front = true;
        }

        if (Keyboard.isKeyDown(31)) {
            ++ya;
        }

        if (Keyboard.isKeyDown(30)) {
            --xa;
        }

        if (Keyboard.isKeyDown(32)) {
            ++xa;
        }

        if ((Keyboard.isKeyDown(57)) && this.onGround) {
            this.yd = 0.12F;
        }

        this.moveRelative(xa, ya, this.onGround ? (front?0.03F:0.02F) : 0.01F);
        this.yd = (float)((double)this.yd - 0.005D);
        this.move(this.xd, this.yd, this.zd);
        this.xd *= 0.91F;
        this.yd *= 0.98F;
        this.zd *= 0.91F;
        if (this.onGround) {
            this.xd *= 0.8F;
            this.zd *= 0.8F;
        }

    }

    public void move(float xa, float ya, float za) {
        float xaOrg = xa;
        float yaOrg = ya;
        float zaOrg = za;
        List<AABB> aABBs = this.level.getCubes(this.bb.expand(xa, ya, za));

        int i;
        for(i = 0; i < aABBs.size(); ++i) {
            ya = aABBs.get(i).clipYCollide(this.bb, ya);
        }

        this.bb.move(0.0F, ya, 0.0F);

        for(i = 0; i < aABBs.size(); ++i) {
            xa = aABBs.get(i).clipXCollide(this.bb, xa);
        }

        this.bb.move(xa, 0.0F, 0.0F);

        for(i = 0; i < aABBs.size(); ++i) {
            za = aABBs.get(i).clipZCollide(this.bb, za);
        }

        this.bb.move(0.0F, 0.0F, za);
        this.onGround = yaOrg != ya && yaOrg < 0.0F;
        if (xaOrg != xa) {
            this.xd = 0.0F;
        }

        if (yaOrg != ya) {
            this.yd = 0.0F;
        }

        if (zaOrg != za) {
            this.zd = 0.0F;
        }

        this.x = (this.bb.x0 + this.bb.x1) / 2.0F;
        this.y = this.bb.y0 + 1.62F;
        this.z = (this.bb.z0 + this.bb.z1) / 2.0F;
    }

    public void moveRelative(float xa, float za, float speed) {
        float dist = xa * xa + za * za;
        if (!(dist < 0.01F)) {
            dist = speed / (float)Math.sqrt(dist);
            xa *= dist;
            za *= dist;
            float sin = (float)Math.sin((double)this.yRot * 3.141592653589793D / 180.0D);
            float cos = (float)Math.cos((double)this.yRot * 3.141592653589793D / 180.0D);
            this.xd += xa * cos - za * sin;
            this.zd += za * cos + xa * sin;
        }
    }
}
