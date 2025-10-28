
package net.minecraft;

import org.lwjgl.opengl.GLContext;

public class OpenGlCapsChecker {

    public boolean checkARBOcclusion() {
        GLContext.getCapabilities();
        return false;
    }
}
