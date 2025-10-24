package net.minecraft.client.renderer;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.LinkedList;

@Getter
public class VboRange
{
    @Setter
    private int position = -1;
    @Setter
    private int size = 0;
    private final LinkedList.Node<VboRange> node = new LinkedList.Node<>(this);

    public int getPositionNext()
    {
        return this.position + this.size;
    }

    public VboRange getPrev()
    {
        LinkedList.Node<VboRange> node = this.node.getPrev();
        return node == null ? null : (VboRange)node.getItem();
    }

    public VboRange getNext()
    {
        LinkedList.Node<VboRange> node = this.node.getNext();
        return node == null ? null : (VboRange)node.getItem();
    }

    public String toString()
    {
        return this.position + "/" + this.size + "/" + (this.position + this.size);
    }
}
