
package net.minecraft.client.input;

import net.minecraft.entity.EntityPlayer;

public class MovementInput {

    public float moveStrafe;
    public float moveForward;
    public boolean jump;
    public boolean sprint;

    public MovementInput() {
        this.moveStrafe = 0.0f;
        this.moveForward = 0.0f;
        this.jump = false;
        this.sprint = false;
    }

    public void updatePlayerMoveState(final EntityPlayer gi) {
    }

    public void resetKeyState() {
    }

    public void checkKeyForMovementInput(final int integer, final boolean boolean2) {
    }
}
