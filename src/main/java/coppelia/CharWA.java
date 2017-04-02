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

public class CharWA
{
    char[] w;

    public CharWA(int i)
    {
        w = new char[i];
    }
    
    public CharWA(String s)
    {
        w=s.toCharArray();
    }
    
    public String getString()
    {
        String a;
        a = new String(w);
        return a;
    }

    public void initArray(int i)
    {
        w = new char[i];
    }

    public char[] getArray()
    {
        return w;
    }

    public int getLength()
    {
        return w.length;
    }

    public char[] getNewArray(int i)
    {
        w = new char[i];
        return w;
    }
}