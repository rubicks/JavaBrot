import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

public class MandelBig implements Mandel
{
  Bitmap bmp;

  // pointer to result register
  long w1[];
  long h1[];
  long w2[];
  long h2[];
  long r[];
  long s[];
  long two[];
  long four[];
  long fourth[];
  long x[];
  long y[];
  long rw[];
  long re[];
  long im[];
  long recen[];
  long imcen[];
  long re2[];
  long im2[];
  long rec[];
  long imc[];
  long temp[];
  long temp2[];
  long step[];

  int w, h, w8, h8;

  public MandelBig()
  {
    bmp = JavaBrot.backbuf;

    w1 = Big.create();
    h1 = Big.create();
    w2 = Big.create();
    h2 = Big.create();
    r = Big.create();
    s = Big.create();
    two = Big.create();
    four = Big.create();
    fourth = Big.create();
    x = Big.create();
    y = Big.create();
    rw = Big.create();
    re = Big.create();
    im = Big.create();
    recen = Big.create();
    imcen = Big.create();
    re2 = Big.create();
    im2 = Big.create();
    rec = Big.create();
    imc = Big.create();
    temp = Big.create();
    temp2 = Big.create();
    step = Big.create();

    init();
  }

  public void init()
  {
    w = bmp.w;
    h = bmp.h;
    w8 = w / 8;
    h8 = h / 8;

    Big.fromDouble(w / 2, w2);
    Big.fromDouble(h / 2, h2);
    Big.fromDouble(-.45, recen);
    Big.fromDouble(0, imcen);
    Big.fromDouble(2.0, r);
    Big.fromDouble(4.0 / w, s);
    Big.fromDouble(2.0, two);
    Big.fromDouble(4.0, four);
    Big.fromDouble(.25, fourth);
    Big.fromDouble(1.0 / w, rw);
    Big.fromDouble(bmp.w, w1);
    Big.fromDouble(bmp.h, h1);
    Big.fromDouble(bmp.w / 2, w2);
    Big.fromDouble(bmp.h / 2, h2);
  }

  private boolean limit_reached(long a[])
  {
    int i;

    boolean all_zeros = true;

    for(i = 1; i < Big.size; i++)
    {
      if(a[i] != 0)
      {
        all_zeros = false;
        break;
      }
    }

    if(all_zeros == true)
    {
      if(a[0] > 0x200)
        return false;
      else
        return true;
    }

    return false;
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
        if(limit_reached(r) == true)
          break;

        Big.print(recen);
        Big.print(imcen);
        Big.print(r);

        mx = JavaBrot.input.mousex; 
        my = JavaBrot.input.mousey; 

        bmp.rect(mx - w8, my - h8, mx + w8, my + h8, 0xFF000000, 0);
        bmp.rect(mx - w8 + 1, my - h8 + 1, mx + w8 - 1, my + h8, 0xFFFFFFFF, 0);

        mx = JavaBrot.input.mousex; 
        my = JavaBrot.input.mousey; 

        // recen += s * (x - w / 2)
        Big.fromDouble((double)mx, x);
        Big.sub(x, w2, temp);
        Big.mul(temp, s, temp2);
        Big.add(temp2, recen, recen);
        
        // imcen += s * (y - h / 2)
        Big.fromDouble((double)my, y);
        Big.sub(y, h2, temp);
        Big.mul(temp, s, temp2);
        Big.add(temp2, imcen, imcen);

        // r /= 4
        Big.mul(r, fourth, temp);
        Big.copy(temp, r);

        // s = 2 * r * (1.0 / w)
        Big.mul(r, two, s);
        Big.mul(s, rw, temp);
        Big.copy(temp, s);

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

  public void calc(long recen[], long imcen[], long s[])
  {
    render(recen, imcen, s, 16, false);
    render(recen, imcen, s, 8, true);
    render(recen, imcen, s, 4, true);
    render(recen, imcen, s, 2, true);
    render(recen, imcen, s, 1, true);
  }

  public void render(long recen[], long imcen[], long s[], int step_size, boolean guess)
  {
    int iter = JavaBrot.iter;

    int color;

    int xx, yy;

    Big.fromDouble(step_size, step);
    Big.fromDouble(0, y);

    // y loop
    for(yy = 0; yy <= bmp.h; yy += step_size)
    {
      // imc = s * (y - h2) + imcen
      Big.sub(y, h2, temp);
      Big.mul(temp, s, temp2);
      Big.add(temp2, imcen, imc);

      Big.fromDouble(0, x);

      // x loop
      for(xx = 0; xx < bmp.w; xx += step_size)
      {
        // stop
        if(JavaBrot.input.keydown || JavaBrot.input.button1 || JavaBrot.reset || JavaBrot.restart)
        {
          return;
        }

        if(guess)
        {
          int c = bmp.getpixel(xx, yy);
          int c0 = bmp.getpixel(xx - step_size, yy);
          int c1 = bmp.getpixel(xx + step_size, yy);
          int c2 = bmp.getpixel(xx, yy - step_size);
          int c3 = bmp.getpixel(xx, yy + step_size);

          if(c == c0 && c == c1 && c == c2 && c == c3)
          {
            Big.add(x, step, x);
            continue;
          }
        }

        // rec = s * (x - w2) + recen
        Big.sub(x, w2, temp);
        Big.mul(temp, s, temp2);
        Big.add(temp2, recen, rec);

        Big.copy(rec, re);
        Big.copy(imc, im);
        Big.sqr(re, re2);
        Big.sqr(im, im2);

        for(color = 0; color < iter; color++)
        {
          // bailout
          Big.add(re2, im2, temp);
          if(Big.comp(temp, four) > 0)
            break;

          // im = re * im * 2 + imc
          Big.mul(re, im, temp);
          Big.add(temp, temp, temp);
          Big.add(temp, imc, im);

          // re = re2 - im2 + rec
          Big.sub(re2, im2, re);
          Big.add(re, rec, re);

          // re2 = re * re
          Big.sqr(re, re2);

          // im2 = im * im
          Big.sqr(im, im2);
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

        bmp.rectfill(xx, yy, xx + step_size - 1, yy + step_size - 1, color, 0);

        Big.add(x, step, x);
      }

      Big.add(y, step, y);

      JavaBrot.panel.repaint();
    }
  }
}

