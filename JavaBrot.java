/*
Copyright (c) 2012 Joe Davisson.

This file is part of JavaBrot.

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301 USA
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.text.*;
import java.io.*;
import java.net.*;

public class JavaBrot
{
  public static Frame win = null;
  public static JPanel panel = null;
  public static Palette pal;

  public static Input input;
  public static int iter;
  public static boolean restart = false;
  public static boolean reset = false;
  public static int mode = 0;

  public static Bitmap backbuf;
  public static BufferedImage bi;

  public static void about()
  {
    JOptionPane.showMessageDialog(win, "JavaBrot",
      "About", JOptionPane.INFORMATION_MESSAGE);
  }

  public static void quit() 
  {
    System.exit(0);
  }

  public static void nextPal()
  {
    pal.randomize();
  }

  public static void main(String[] args)
  {
    final int w = 640;
    final int h = 480;

    Blend.setMode(Blend.NORMAL);

    backbuf = new Bitmap(w, h);
    backbuf.clear(Col.makeRGB(0, 0, 0));
    DataBuffer data_buffer = new DataBufferInt(backbuf.data, w * h);
    int band_masks[] = { 0xFF0000, 0xFF00, 0xFF, 0xFF000000 };
    WritableRaster write_raster =
      Raster.createPackedRaster(data_buffer, w, h, w, band_masks, null);
    ColorModel color_model = ColorModel.getRGBdefault();
    bi = new BufferedImage(color_model, write_raster, false, null);

    // input class needs these set
    Screen.w = w;
    Screen.h = h;

    JFrame frame = new JFrame("JavaBrot");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setResizable(false);
    MainMenu menu = new MainMenu();
    frame.setJMenuBar(menu);

    panel = new JPanel()
    {
      public void paintComponent(Graphics g)
      {
        g.drawImage(bi, 0, 0, w, h, null);
      }
    };

    panel.setSize(w, h);
    panel.setPreferredSize(new Dimension(w, h));
    panel.setLayout(new BorderLayout());
    frame.add(panel);

    input = new Input();
    panel.addKeyListener(input);
    panel.addMouseListener(input);
    panel.addMouseMotionListener(input);
    panel.setFocusable(true);

    frame.pack();
    frame.setVisible(true);
    win = (Frame)frame;

    Mandel mandel = null;

    Rnd.set(1234567);

    iter = 1000;
    restart = false;
    reset = false;
    mode = 0;
    pal = new Palette();

    while(true)
    {
      mandel = null;
      
      switch(mode)
      {
        case 0:
          mandel = new MandelFloat();
          break;
        case 1:
          Big.setSize(4);
          mandel = new MandelBig();
          break;
        case 2:
          Big.setSize(8);
          mandel = new MandelBig();
          break;
        case 3:
          Big.setSize(16);
          mandel = new MandelBig();
          break;
        case 4:
          Big.setSize(32);
          mandel = new MandelBig();
          break;
        default:
          mandel = new MandelFloat();
          break;
      }

      mandel.loop();
    }
  }
}
