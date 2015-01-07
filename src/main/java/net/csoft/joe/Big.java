// Copyright (c) 2013 Joe Davisson. All Rights Reserved.

package net.csoft.joe;

public class Big
{
  // word width in ints
  public static int size = 4;

  // radix point position
  private static int radix = size - 1;

  // bits within each digit
  private static int bits = 32;

  // max value of each digit
  private static long max = ((long)1 << bits) - 1;

  // accumulator (for multiplication etc)
  private static long accum[] = new long[size * 2 + 1];

  // set precision
  public static void setSize(int x)
  {
    size = x;
    accum = new long[size * 2 + 1];
    radix = size - 1;
  }

  public static long[] create()
  {
    return new long[size + radix + 1];
  }

  // convert double to big
  public static void fromDouble(double num, long a[])
  {
    boolean neg = false;

    int i;

    // clear
    clear(a);

    // negative?
    if(num < 0)
    {
      neg = true;
      num = -num;
    }

    num *= (long)1 << bits;
    long temp = (long)num;

    String hexbuf = String.format("%x", temp);

    int len = hexbuf.length();

    // convert string to big number format
    int loc = (radix - 1);
    int shift = 0;
    long val = 0;

    for(i = len - 1; i >= 0; i--)
    {
      char c = hexbuf.charAt(i);

      if(c >= 'a' && c <= 'f')
        val = (c - 'a') + 10;
      else if(c >= 'A' && c <= 'F')
        val = (c - 'A') + 10;
      else if(c >= '0' && c <= '9')
        val = c - '0';
      else
        continue;

      a[loc] |= val << shift;
      shift += 4;

      if(shift >= bits)
      {
        shift = 0;
        loc++;

        if(loc >= size)
          break;
      }
    }

    if(neg)
      flip(a);
  }

  // duplicate
  public static void copy(long a[], long b[])
  {
    int i;

    for(i = 0; i < size; i++)
      b[i] = a[i];
  }

  // clear
  public static void clear(long a[])
  {
    int i;

    for(i = 0; i < size; i++)
      a[i] = 0;
  }

  // two's complement
  public static void flip(long a[])
  {
    long carry = 1;
    int i;

    for(i = 0; i < size; i++)
    {
      a[i] =  ~a[i] & max;

      long temp = a[i] + carry;

      a[i] = temp & max;
      carry = temp >>> bits;
    }
  }

  // increment
  public static void inc(long a[])
  {
    long carry = 1;
    int i;

    for(i = 0; i < size; i--)
    {
      long temp = a[i] + carry;

      a[i] = temp & max;
      carry = temp >>> bits;
    }
  }

  // add
  public static void add(long a[], long b[], long c[])
  {
    long carry = 0;
    int i;

    for(i = 0; i < size; i++)
    {
      long temp = a[i] + b[i] + carry;

      c[i] = temp & max;
      carry = temp >>> bits;
    }
  }

  // subtract
  public static void sub(long a[], long b[], long c[])
  {
    long carry = 0;
    int i;

    for(i = 0; i < size; i++)
    {
      long temp = a[i] - b[i] - carry;

      c[i] = temp & max;
      carry = (temp >>> bits) & 1;
    }
  }

  // square
  public static void sqr(long a[], long c[])
  {
    // save signs
    boolean as = (a[size - 1] >>> (bits - 1)) > 0 ? true : false;

    if(as)
      flip(a);

    int i;

    for(i = 0; i < size * 2; i++)
      accum[i] = 0;

    for(i = 0; i < size; i++)
    {
      long carry = 0;
      long ai = a[i];
      int j = 0;

      if(ai > 0)
      {
        int start = (size - 2) - i;
        if(start < 0)
          start = 0;

        for(j = start; j < size; j++)
        {
          long temp = ai * a[j] + accum[i + j] + carry;

          accum[i + j] = temp & max;
          carry = temp >>> bits;
        }

        accum[i + j] = carry;
      }
    }

    int acc = radix;

    for(i = 0; i < size; i++)
      c[i] = accum[acc++];

    if(as)
      flip(a);
  }

  // multiply
  public static void mul(long a[], long b[], long c[])
  {
    // save signs
    boolean as = (a[size - 1] >>> (bits - 1)) > 0 ? true : false;
    boolean bs = (b[size - 1] >>> (bits - 1)) > 0 ? true : false;

    if(as)
      flip(a);

    if(bs)
      flip(b);

    int i;

    for(i = 0; i < size * 2; i++)
      accum[i] = 0;

    for(i = 0; i < size; i++)
    {
      long carry = 0;
      long ai = a[i];
      int j = 0;

      if(ai > 0)
      {
        int start = (size - 2) - i;
        if(start < 0)
          start = 0;

        for(j = start; j < size; j++)
        {
          long temp = ai * b[j] + accum[i + j] + carry;

          accum[i + j] = temp & max;
          carry = temp >>> bits;
        }

        accum[i + j] = carry;
      }
    }

    int acc = radix;

    for(i = 0; i < size; i++)
      c[i] = accum[acc++];

    if((as && !bs) || (!as && bs))
      flip(c);

    if(as)
      flip(a);

    if(bs)
      flip(b);
  }

  // compare
  // 1 = a > b
  // 0 = a = b
  // -1 = a < b
  public static int comp(long a[], long b[])
  {
    int i;

    for(i = size - 1; i >= 0; i--)
    {
      if(a[i] == b[i])
        continue;

      if((a[i] < b[i]) ^ ((a[i] < 0) != (b[i] < 0)))
        return -1;
      else
        return 1;
    }

    return 0;
  }

  // print big number (hexadecimal)
  public static void print(long a[])
  {
    String hex = "0123456789ABCDEF";
    //int len = a.length;
    int len = size;

    long temp[] = new long[len];
    copy(a, temp);

    boolean neg = false;

    if((a[len - 1] >>> (bits - 1)) > 0)
    {
      neg = true;
      flip(temp);
    }

    if(neg)
      System.out.print('-');
    else
      System.out.print(' ');

    int i, j;

    for(j = len - 1; j >= 0; j--)
    {
      if(j == (radix - 1))
        System.out.print('.');

      for(i = bits - 8; i >= 0; i -= 8)
      {
        long b = (temp[j] >>> i) & 0xFF;

        System.out.print(hex.charAt((int)(b >>> 4) & 0xF));
        System.out.print(hex.charAt((int)b & 0xF));
      }
      
    }
 
    System.out.print('\n');
  }
}

