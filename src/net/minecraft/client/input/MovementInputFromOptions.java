
package net.minecraft.client.input;

import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.EntityPlayer;

public class MovementInputFromOptions extends MovementInput {

    private final boolean[] states;
    private final GameSettings gameSettings;

    public MovementInputFromOptions(final GameSettings gameSettings) {
        this.states = new boolean[10];
        this.gameSettings = gameSettings;
    }

    @Override
    public void checkKeyForMovementInput(final int key, final boolean state) {
        int n = -1;
        if (key == this.gameSettings.keyBindForward.keyCode) {
            n = 0;
        }
        if (key == this.gameSettings.keyBindBack.keyCode) {
            n = 1;
        }
        if (key == this.gameSettings.keyBindLeft.keyCode) {
            n = 2;
        }
        if (key == this.gameSettings.keyBindRight.keyCode) {
            n = 3;
        }
        if (key == this.gameSettings.keyBindJump.keyCode) {
            n = 4;
        }
        if (key == this.gameSettings.keyBindSprint.keyCode) {
            n = 5;
        }
        if (n >= 0) {
            this.states[n] = state;
        }
    }

    @Override
    public void resetKeyState() {
        for (int i = 0; i < 10; ++i) {
            this.states[i] = false;
        }
    }

    @Override
    public void updatePlayerMoveState(final EntityPlayer gi) {
        this.moveStrafe = 0.0f;
        this.moveForward = 0.0f;
        if (this.states[0]) {
            ++this.moveForward;
        }
        if (this.states[1]) {
            --this.moveForward;
        }
        if (this.states[2]) {
            ++this.moveStrafe;
        }
        if (this.states[3]) {
            --this.moveStrafe;
        }
        this.jump = this.states[4];
        this.sprint = this.states[5];
    }
}
