package net.csoft.joe;

public class Palette
{
  public int color[];

  Palette()
  {
    color = new int[256];
    randomize();
  }

  public void randomize()
  {

    int h1 = Rnd.get() % 1536;
    int h2 = Rnd.get() % 1536;
    float hstep = Math.abs((float)(h1 - h2) / 128);

    int s = Rnd.get() & 255;
    int v = Rnd.get() & 1;

    RgbColor rgb = new RgbColor();

    int i;

    for(i = 0; i < 128; i++)
    {
      rgb.hsvToRgb((int)(h1 + i * hstep) % 1536, s, v > 0 ? i * 2 : 255 - i * 2);

      color[i] = Col.makeRGB(rgb.r, rgb.g, rgb.b);
      color[255 - i] = Col.makeRGB(rgb.r, rgb.g, rgb.b);
    }

    //color[0] = Col.makeRGB(0, 0, 0);
  } 
}

