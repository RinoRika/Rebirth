package net.minecraft.util;


import lombok.Getter;

import java.io.FileNotFoundException;
import java.io.InputStream;

@Getter
public class ResourceLocation
{
    protected final String resourceDomain;
    protected final String resourcePath;

    public ResourceLocation(String resourceName)
    {
        this.resourceDomain = splitObjectName(resourceName)[0] == null || splitObjectName(resourceName)[0].isEmpty() ? "minecraft" : splitObjectName(resourceName)[0].toLowerCase();
        this.resourcePath = splitObjectName(resourceName)[1];
    }

    public ResourceLocation(String resourceDomainIn, String resourcePathIn)
    {
        this.resourceDomain = resourceDomainIn;
        this.resourcePath = resourcePathIn;
    }

    protected static String[] splitObjectName(String toSplit)
    {
        String[] astring = new String[] {null, toSplit};
        int i = toSplit.indexOf(58);

        if (i >= 0)
        {
            astring[1] = toSplit.substring(i + 1);

            if (i > 1)
            {
                astring[0] = toSplit.substring(0, i);
            }
        }

        return astring;
    }

    public String getAbsolutePath() {
        return "/" + resourcePath;
    }

    public String toString()
    {
        return this.resourceDomain + ':' + this.resourcePath;
    }

    public InputStream getFileInputStream() {
        return this.getClass().getResourceAsStream("/" + resourcePath);
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (!(p_equals_1_ instanceof ResourceLocation))
        {
            return false;
        }
        else
        {
            ResourceLocation resourcelocation = (ResourceLocation)p_equals_1_;
            return this.resourceDomain.equals(resourcelocation.resourceDomain) && this.resourcePath.equals(resourcelocation.resourcePath);
        }
    }

    public int hashCode()
    {
        return 31 * this.resourceDomain.hashCode() + this.resourcePath.hashCode();
    }
}
