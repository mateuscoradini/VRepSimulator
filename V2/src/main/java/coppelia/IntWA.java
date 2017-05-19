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

public class IntWA
{
    int[] w;

    public IntWA(int i)
    {
        w = new int[i];
    }

    public void initArray(int i)
    {
        w = new int[i];
    }

    public int getLength()
    {
        return w.length;
    }

    public int[] getNewArray(int i)
    {
        w = new int[i];
        return w;
    }

    public int[] getArray()
    {
        return w;
    }
    
    public char[] getCharArrayFromArray()
    {
        char[] a=new char[4*w.length];
        for (int i=0;i<w.length;i++)
        {
            a[4*i+0]=(char)(w[i]&0xff); 
            a[4*i+1]=(char)((w[i] >>> 8)&0xff);
            a[4*i+2]=(char)((w[i] >>> 16)&0xff);
            a[4*i+3]=(char)((w[i] >>> 24)&0xff);
        }
        return a;
    }
    
    public void initArrayFromCharArray(char[] a)
    {
        w = new int[a.length/4];
        for (int i=0;i<a.length/4;i++)
            w[i]=(int)(((a[4*i+3]&0xff) << 24) + ((a[4*i+2]&0xff) << 16) + ((a[4*i+1]&0xff) << 8) + (a[4*i+0]&0xff));
    }
}
