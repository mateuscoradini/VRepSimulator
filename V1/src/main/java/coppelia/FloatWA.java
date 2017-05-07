/*
 * Copyright 2006-2016 Coppelia Robotics GmbH. All rights reserved.
 * marc@coppeliarobotics.com
 * www.coppeliarobotics.com
 *
 * -------------------------------------------------------------------
 * THIS FILE IS DISTRIBUTED "AS IS", WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTY. THE USER WILL USE IT AT HIS/HER OWN RISK. THE ORIGINAL
 * AUTHORS AND COPPELIA ROBOTICS GMBH WILL NOT BE LIABLE FOR DATA LOSS,
 * DAMAGES, LOSS OF PROFITS OR ANY OTHER KIND OF LOSS WHILE USING OR
 * MISUSING THIS SOFTWARE.
 *
 * You are free to use/modify/distribute this file for whatever purpose!
 * -------------------------------------------------------------------
 *
 * This file was automatically created for V-REP release V3.3.2 on August 29th 2016
 */

package coppelia;

public class FloatWA
{
    float[] w;

    public FloatWA(int i)
    {
        w = new float[i];
    }

    public void initArray(int i)
    {
        w = new float[i];
    }

    public float[] getArray()
    {
        return w;
    }

    public int getLength()
    {
        return w.length;
    }

    public float[] getNewArray(int i)
    {
        w = new float[i];
        for (int k = 0; k < i; k++)
            w[k] = 0.0f;
        return w;
    }

    public char[] getCharArrayFromArray()
    {
        char[] a=new char[4*w.length];
        for (int i=0;i<w.length;i++)
        {
            int iw=Float.floatToIntBits(w[i]);
            a[4*i+0]=(char)(iw&0xff);   
            a[4*i+1]=(char)((iw >>> 8)&0xff);
            a[4*i+2]=(char)((iw >>> 16)&0xff);
            a[4*i+3]=(char)((iw >>> 24)&0xff);
        }
        return a;
    }
    
    public void initArrayFromCharArray(char[] a)
    {
        w = new float[a.length/4];
        for (int i=0;i<a.length/4;i++)
        {
            int iw=(int)(((a[4*i+3]&0xff) << 24) + ((a[4*i+2]&0xff) << 16) + ((a[4*i+1]&0xff) << 8) + (a[4*i+0]&0xff));
            w[i]=Float.intBitsToFloat(iw);
        }               
    }
}