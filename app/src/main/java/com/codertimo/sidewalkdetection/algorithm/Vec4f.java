package com.codertimo.sidewalkdetection.algorithm; // ÆÄÅ°Áö 

import org.opencv.core.Mat; // ÀÓÆ÷¸£ºv
import org.opencv.core.Point; // ÀÓÆ÷¸£ºv

 
import java.util.ArrayList; // ÀÓÆ÷¸£ºv

import java.util.List; // ÀÓÆ÷¸£ºv


/**
 * Created by codertimo on 2015. 11. 2.. and me
 */
public class Vec4f {
    public float x1; // 1
    public float y1; // 2
    public float x2; // 3
    public float y2; // 4

    public Vec4f() {}
    public Vec4f(Mat mat)
    {
        double vec[] = mat.get(0, 0);
        this.x1 = (float)vec[0];
        this.y1 = (float)vec[1];
        this.x2 = (float)vec[2];
        this.y2 = (float)vec[3];
    }
    public Vec4f(double[] array)
    {
        this.x1 = (float)array[0];
        this.y1 = (float)array[1];
        this.x2 = (float)array[2];
        this.y2 = (float)array[3];
    }
    public Vec4f(float[] array)
    {
        this.x1 = array[0];
        this.y1 = array[1];
        this.x2 = array[2];
        this.y2 = array[3];
    }

    public float[] toArray()
    {
        float[] array = {x1,y1,x2,y2};
        return array;
    }

    public List<Point> toPofloats()
    {
        List<Point> pofloats = new ArrayList<>();
        pofloats.add(new Point(x1,y1));
        pofloats.add(new Point(x1,y1));
        return pofloats;
    }

}

