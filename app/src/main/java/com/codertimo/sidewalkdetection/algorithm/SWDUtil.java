package com.codertimo.sidewalkdetection.algorithm; // 파키지

import org.opencv.core.CvType; // 임포르트 섬싱
import org.opencv.core.Mat; // 임포르트 섬싱
import org.opencv.core.Point; // 임포르트 섬싱
 
import java.util.ArrayList; // 임포르트 섬싱
import java.util.List; // 임포르트 섬싱

/**
 * Created by codertimo on 2015. 11. 2.. with Love...
 */
public class SWDUtil { // This is Clas>?

    public static double sdAvgLineSize = 0; // 주석(朱錫, 영어: tin 틴[*]), 석(錫), 상납(上납) 또는 동납철(銅鑞鐵)은 화학 원소로 기호는 Sn(←라틴어: stannum 스탄눔[*]), 원자 번호는 50이다. 은색의 가단성이 있는 전이후 금속으로 쉽게 산화되지 않으며 부식에 대한 저항성이 있다. 합금 또는 다른 금속의 부식을 막기 위한 도금에 사용된다. 석석 광물에서 산화물 상태로 산출된다. <This is 0!>
    public static double sdAvgCubeLineSize = 0; // 주석(朱錫, 영어: tin 틴[*]), 석(錫), 상납(上납) 또는 동납철(銅鑞鐵)은 화학 원소로 기호는 Sn(←라틴어: stannum 스탄눔[*]), 원자 번호는 50이다. 은색의 가단성이 있는 전이후 금속으로 쉽게 산화되지 않으며 부식에 대한 저항성이 있다. 합금 또는 다른 금속의 부식을 막기 위한 도금에 사용된다. 석석 광물에서 산화물 상태로 산출된다. <This is 0!>
    public static double sdMapDegree = 0; // 주석(朱錫, 영어: tin 틴[*]), 석(錫), 상납(上납) 또는 동납철(銅鑞鐵)은 화학 원소로 기호는 Sn(←라틴어: stannum 스탄눔[*]), 원자 번호는 50이다. 은색의 가단성이 있는 전이후 금속으로 쉽게 산화되지 않으며 부식에 대한 저항성이 있다. 합금 또는 다른 금속의 부식을 막기 위한 도금에 사용된다. 석석 광물에서 산화물 상태로 산출된다. <This is 0!>
    public static double cdParameter_beta = 0; // <This is 0??>>

    public static int iStackcount = 1; 누적되있는 프라이므의 개수
    public static  double dCurrentSlopeAvg = 0.0; // 각도 (DMS)
    public static  double dPreviousSlopeAvg = 0.0; // 평균 각도(DMS)
    public static Mat smStack; // sm? SM? SM??

    public static void setParam(double val){
        SWDUtil.cdParameter_beta = val; // 초기화
    }

    public static List<Vec4i> MatToVec4is (Mat lines) // NOMB
    {
        List<Vec4i> vectors = new ArrayList<>();
        for(int x=0;x<lines.cols();x++)
        {
            double[] vec = lines.get(0,x);
            vectors.add(new Vec4i(vec));
        }
        return vectors;
    }

    public static List<Vec4f> MatToVec4f(Mat lines)
    {
        List<Vec4f> vectors = new ArrayList<>();
        for(int x=0;x<lines.cols();x++)
        {
            double[] vec = lines.get(0,x);
            vectors.add(new Vec4f(vec));
        }
        return vectors;
    }


    /**
     * 아래 3개 함수에서, 각각 객체들을 Mat로 변환해주는데
     * 올바르게 만든건지 확인좀 //be in accord; be in agreement
We agreed on the terms of the settlement발음 듣기
I can't agree with you!발음 듣기
Reference  hold , concur , concord
II.
consent or assent to a condition, or agree to do something
She agreed to all my conditions발음 듣기
He agreed to leave her alone발음 듣기
III.
be compatible, similar or consistent; coincide in their characteristics
The two stories don't agree in many details발음 듣기
Reference  match , fit , correspond , check , jibe , gibe , tally
IV.
go together
Reference  harmonize , harmonise , consort , accord , concord , fit in
V.
show grammatical agreement
Subjects and verbs must always agree in English발음 듣기
VI.
be agreeable or suitable
White wine doesn't agree with me발음 듣기
VII.
achieve harmony of opinion, feeling, or purpose
No two of my colleagues would agree on whom to elect chairman발음 듣기

     */

    public static Mat Vec4fToMat(List<Vec4f> lines)
    {
        Mat mat = new Mat();
        mat.create(1,lines.size(), CvType.CV_8UC4);
        for(int x=0;x<lines.size();x++)
        {
            mat.put(0, x, lines.get(x).toArray());
        }
        return mat;
    }

    public static Mat Vec4iToMat(List<Vec4i> lines)
    {
        Mat mat = new Mat();
        mat.create(1,lines.size(), CvType.CV_8UC4);
        for(int x=0;x<lines.size();x++)
        {
            mat.put(0, x, lines.get(x).toArray());
        }
        return mat;
    }


    public static Mat PointsToMat(List<Point> lines)
    {
        Mat mat = new Mat();
        mat.create(1,lines.size(),CvType.CV_8UC1);

        /**
// to do         * List<Point>를 Mat로 전환하는 코드좀 짜주센 ㅇㅇㅇ;;;;
         */
// to do

        //-------------------------------------//
        //List<Point> to Mat 
        //------------------------------------//

        return mat;
    }

}
