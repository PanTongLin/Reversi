package pack.comp;

import java.awt.*;
import javax.swing.*;

public class ReversiDisk
{
    private ImageIcon blackDiskImageIcon,whiteDiskImageIcon,diskFrameImageIcon,hintFrameImageIcon;
    public ReversiDisk()
    {
        blackDiskImageIcon = new ImageIcon(ReversiDisk.class.getResource("blackDisk.png"));
        whiteDiskImageIcon = new ImageIcon(ReversiDisk.class.getResource("whiteDisk.png"));
        diskFrameImageIcon = new ImageIcon(ReversiDisk.class.getResource("disk_frame.png"));
        hintFrameImageIcon = new ImageIcon(ReversiDisk.class.getResource("hint_frame.gif"));
    }

    public ImageIcon getDiskImageIcon(Color color)
    {
        if(color == Color.BLACK)
            return blackDiskImageIcon;
        else if(color == Color.WHITE)
            return whiteDiskImageIcon;

        return null;
    }

    public ImageIcon getDiskFrameImageIcon()
    {
        return diskFrameImageIcon;
    }

    public ImageIcon getHintFrameImageIcon()
    {
        return hintFrameImageIcon;
    }
}
