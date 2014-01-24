import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

public class MandelFloat implements Mandel
{
  Bitmap bmp;
 
  double recen;
  double imcen;
  double re;
  double im;
  double re2;
  double im2;
  double rec;
  double imc;
  double r;
  double s;
  int step;

  int w, h, w8, h8;

  public MandelFloat()
  {
    bmp = JavaBrot.backbuf;

    init();
  }

  public void init()
  {
    w = bmp.w;
    h = bmp.h;
    w8 = w /  8;
    h8 = h /  8;

    recen = -.45;
    imcen = 0;
    r = 2.0;
    s = 4.0 / w;
  }

  public void loop()
  {
    int mx, my;

    calc(recen, imcen, s);

    while(true)
    {
      // keys
      if(JavaBrot.input.keydown == true)
      {
        JavaBrot.input.keydown = false;
        if(JavaBrot.input.lastkey == KeyEvent.VK_R)
        {
          JavaBrot.nextPal();
          calc(recen, imcen, s);
        }
      }

      // reset
      if(JavaBrot.reset == true)
      {
        JavaBrot.reset = false;
        return;
        //init();
        //calc(recen, imcen, s);
      }

      // restart
      if(JavaBrot.restart == true)
      {
        JavaBrot.restart = false;
        calc(recen, imcen, s);
      }

      // zooooom into the depths of the mandel
      while(JavaBrot.input.button1 == true)
      {
        System.out.println(recen);
        System.out.println(imcen);
        System.out.println(r);

        mx = JavaBrot.input.mousex; 
        my = JavaBrot.input.mousey; 

        bmp.rect(mx - w8, my - h8, mx + w8, my + h8, 0xFF000000, 0);
        bmp.rect(mx - w8 + 1, my - h8 + 1, mx + w8 - 1, my + h8, 0xFFFFFFFF, 0);

        recen += s * (mx - w / 2);
        imcen += s * (my - h / 2);
        r /= 4;
        s = 2 * r / w;

        JavaBrot.input.button1 = false;

        calc(recen, imcen, s);
      }

      try
      {
        Thread.sleep(100);
      }
      catch(InterruptedException e)
      {
      }
    }
  }

  public void calc(double recen, double imcen, double s)
  {
    render(recen, imcen, s, 4, false);
    render(recen, imcen, s, 2, true);
    render(recen, imcen, s, 1, true);
  }

  public void render(double recen, double imcen, double s, int step_size, boolean guess)
  {
    int iter = JavaBrot.iter;

    int color;

    int x, y;

    // y loop
    for(y = 0; y <= bmp.h; y += step_size)
    {
      imc = s * (y - h / 2) + imcen;

      // stopped
      if(JavaBrot.input.keydown || JavaBrot.input.button1 || JavaBrot.reset || JavaBrot.restart)
      {
        //JavaBrot.input.button1 = false;
        return;
      }
      
      // x loop
      for(x = 0; x < bmp.w; x += step_size)
      {
        rec = s * (x - w / 2) + recen;

        if(guess)
        {
          int c = bmp.getpixel(x, y);
          int c1 = bmp.getpixel(x - step_size, y);
          int c2 = bmp.getpixel(x + step_size, y);
          int c3 = bmp.getpixel(x, y - step_size);
          int c4 = bmp.getpixel(x, y + step_size);

          // solid guessing
          if(c == c1 && c == c2 && c == c3 && c == c4)
          {
            continue;
          }
        }

        re = rec;
        im = imc;

        re2 = re * re;
        im2 = im * im;

        for(color = 0; color < iter; color++)
        {
          if(re2 + im2 > 4.0)
            break;

          im = re * im * 2 + imc;
          re = re2 - im2 + rec;

          re2 = re * re;
          im2 = im * im;
        }

        // make set black
        if(color == iter)
        {
          color = 0xFF000000;
        }
        else
        {
          color = JavaBrot.pal.color[color & 255] | 0xFF000000;
        }

        bmp.rectfill(x, y, x + step_size - 1, y + step_size - 1, color, 0);
      }

      JavaBrot.panel.repaint();
    }
  }
}

