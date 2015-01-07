// Copyright (c) 2013 Joe Davisson.

package net.csoft.joe;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

public class MainMenu extends JMenuBar
{
  public MainMenu()
  {
    // file menu
    JMenu fileMenu = new JMenu("File");

    // quit
    JMenuItem quitItem = new JMenuItem("Quit");
    quitItem.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JavaBrot.quit();
        }
      } );

    fileMenu.add(quitItem);

    add(fileMenu);

    // fractal menu
    JMenu fractalMenu = new JMenu("Fractal");

    // random palette
    JMenuItem randomPaletteItem = new JMenuItem("Random Palette");
    randomPaletteItem.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JavaBrot.nextPal();
          JavaBrot.restart = true;
        }
      } );
    fractalMenu.add(randomPaletteItem);

    fractalMenu.addSeparator();

    // reset
    JMenuItem resetItem = new JMenuItem("Reset");
    resetItem.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JavaBrot.reset = true;
        }
      } );
    fractalMenu.add(resetItem);

    fractalMenu.addSeparator();

    JRadioButtonMenuItem mode0 = new JRadioButtonMenuItem("Floating Point", true);
    JRadioButtonMenuItem mode1 = new JRadioButtonMenuItem("Big Integer (128-bit)", false);
    JRadioButtonMenuItem mode2 = new JRadioButtonMenuItem("Big Integer (256-bit)", false);
    JRadioButtonMenuItem mode3 = new JRadioButtonMenuItem("Big Integer (512-bit)", false);
    JRadioButtonMenuItem mode4 = new JRadioButtonMenuItem("Big Integer (1024-bit)", false);

    ButtonGroup modeGroup = new ButtonGroup();
    modeGroup.add(mode0);
    modeGroup.add(mode1);
    modeGroup.add(mode2);
    modeGroup.add(mode3);
    modeGroup.add(mode4);

    mode0.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JavaBrot.mode = 0;
          JavaBrot.reset = true;
        }
      } );

    mode1.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JavaBrot.mode = 1;
          JavaBrot.reset = true;
        }
      } );

    mode2.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JavaBrot.mode = 2;
          JavaBrot.reset = true;
        }
      } );

    mode3.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JavaBrot.mode = 3;
          JavaBrot.reset = true;
        }
      } );

    mode4.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JavaBrot.mode = 4;
          JavaBrot.reset = true;
        }
      } );

    fractalMenu.add(mode0);
    fractalMenu.add(mode1);
    fractalMenu.add(mode2);
    fractalMenu.add(mode3);
    fractalMenu.add(mode4);

    add(fractalMenu);

    // iter menu
    JMenu iterMenu = new JMenu("Iterations");

    JRadioButtonMenuItem iter1 = new JRadioButtonMenuItem("1000", true);
    JRadioButtonMenuItem iter2 = new JRadioButtonMenuItem("2000", false);
    JRadioButtonMenuItem iter3 = new JRadioButtonMenuItem("3000", false);
    JRadioButtonMenuItem iter4 = new JRadioButtonMenuItem("4000", false);
    JRadioButtonMenuItem iter5 = new JRadioButtonMenuItem("5000", false);
    JRadioButtonMenuItem iter6 = new JRadioButtonMenuItem("6000", false);
    JRadioButtonMenuItem iter7 = new JRadioButtonMenuItem("7000", false);
    JRadioButtonMenuItem iter8 = new JRadioButtonMenuItem("8000", false);
    JRadioButtonMenuItem iter9 = new JRadioButtonMenuItem("9000", false);

    ButtonGroup iterGroup = new ButtonGroup();
    iterGroup.add(iter1);
    iterGroup.add(iter2);
    iterGroup.add(iter3);
    iterGroup.add(iter4);
    iterGroup.add(iter5);
    iterGroup.add(iter6);
    iterGroup.add(iter7);
    iterGroup.add(iter8);
    iterGroup.add(iter9);

    iter1.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JavaBrot.iter = 1000;
          JavaBrot.restart = true;
        }
      } );

    iter2.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JavaBrot.iter = 2000;
          JavaBrot.restart = true;
        }
      } );

    iter3.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JavaBrot.iter = 3000;
          JavaBrot.restart = true;
        }
      } );

    iter4.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JavaBrot.iter = 4000;
          JavaBrot.restart = true;
        }
      } );

    iter5.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JavaBrot.iter = 5000;
          JavaBrot.restart = true;
        }
      } );

    iter6.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JavaBrot.iter = 6000;
          JavaBrot.restart = true;
        }
      } );

    iter7.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JavaBrot.iter = 7000;
          JavaBrot.restart = true;
        }
      } );

    iter8.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JavaBrot.iter = 8000;
          JavaBrot.restart = true;
        }
      } );

    iter9.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JavaBrot.iter = 9000;
          JavaBrot.restart = true;
        }
      } );

    iterMenu.add(iter1);
    iterMenu.add(iter2);
    iterMenu.add(iter3);
    iterMenu.add(iter4);
    iterMenu.add(iter5);
    iterMenu.add(iter6);
    iterMenu.add(iter7);
    iterMenu.add(iter8);
    iterMenu.add(iter9);

    add(iterMenu);

    // help menu
    JMenu helpMenu = new JMenu("Help");

    JMenuItem aboutItem = new JMenuItem("About...");
    aboutItem.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JavaBrot.about();
        }
      } );

    helpMenu.add(aboutItem);

    add(helpMenu);
  }
}

