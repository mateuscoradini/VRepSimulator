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

public class StringWA
{
    String[] w;

    public StringWA(int i)
    {
        w = new String[i];
    }

    public void initArray(int i)
    {
        w = new String[i];
    }

    public String[] getArray()
    {
        return w;
    }

    public int getLength()
    {
        return w.length;
    }

    public String[] getNewArray(int i)
    {
        w = new String[i];
        return w;
    }
}