package com.codertimo.sidewalkdetection.algorithm;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt4;
import org.opencv.core.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by codertimo on 2015. 11. 2.. and me
 */
public class Vec4i {
    public int x1;
    public int y1;
    public int x2;
    public int y2;

    public Vec4i(Mat mat)
    {
        double vec[] = mat.get(0, 0);
        this.x1 = (int)vec[0];
        this.y1 = (int)vec[1];
        this.x2 = (int)vec[2];
        this.y2 = (int)vec[3];
    }
    public Vec4i(double[] array)
    {
        this.x1 = (int)array[0];
        this.y1 = (int)array[1];
        this.x2 = (int)array[2];
        this.y2 = (int)array[3];
    }
    public Vec4i(int[] array)
    {
        this.x1 = array[0];
        this.y1 = array[1];
        this.x2 = array[2];
        this.y2 = array[3];
    }

    public int[] toArray()
    {
        int[] array = {x1,y1,x2,y2};
        return array;
    }
    public List<Point> toPoints()
    {
        List<Point> points = new ArrayList<>();
        points.add(new Point(x1,y1));
        points.add(new Point(x1,y1));
        return points;
    }
}
