 package com.codertimo.sidewalkdetection.algorithm; // 파키지 

import org.opencv.core.Mat; // 임포르트

/**
 * Created by codertimo on 2015. 11. 3.. // AND DMK98 ON 2015 142123
 */
public class ComparableLine { // ComparableLine : 직선간의 정보를 비교하기 위해서 정보를 저장하고, 비교하는 기능을 가진 클래스

//    /**
//     * 이변수들은 쓰지도 않으면서 왜 만듬...? // 난!독!화!
//     */// 난!독!화!
//    private final double sdAllowedMinGap = 0.9;// 난!독!화!
//    private final double sdAllowedMaxGap = 1.1;// 난!독!화!
//    private final double sdDeniedMinGap = 0.8;// 난!독!화!
//    private final double sdDeniedMaxGap = 1.2;// 난!독!화!


    private int iLine_size =0; // 기본 라인 사이즈 지정
    private double dSlope =0.0; // 기본 기울기 지이정
    Vec4i vPoint = null; // 초기화하ㅗ

    /**
     * operator 은 뭐하는거야....? // 생성자 작성, 복사시 필요한데 안 넣음
     */

    public ComparableLine(){} // 기본 생성자

    /**
     * 내부 변수들에 대한 설명 : Vec4i vec : 입력시 받은 2개의 점(x1,y1),(x2,y2)순서 
     * 설명 : 생성자이며, 기본적인 정보를 설정한다.
     */
    public ComparableLine(Vec4i vec){
        this.vPoint = vec; //생략

        iLine_size = (int)Math.pow(vPoint.x1-vPoint.x2,2) +(int) Math.pow(vPoint.y1-vPoint.y2,2); // 라인 길이

        if(vPoint.x1 == vPoint.x2) dSlope = 90; // 0으로 나눌려면 90도로 미리 처리
            dSlope = Math.atan(-1 * (vPoint.y2 - vPoint.y1)/(vPoint.x2 - vPoint.x1)) * 180.0 /Math.PI; // 아니면 arctan, 호도법으로 DMS Notation으로 전환

        if(dSlope < 0) // 각도는 0~180도 까지만 허용
            dSlope += 180;
    }

    /**
     * getFunctionD 함수는 사용 안하던데..? // 언제적 소스인지 모르겠음;
     */
    public double getFunctionD(double dSlope)
    {
        if (10 + this.dSlope < dSlope || this.dSlope - 10 > dSlope) { // 각도가 10도이상 차이나지 않으면 무의미한 직선으로 간주
            return Math.abs(this.dSlope + dSlope - 2 * SWDUtil.sdMapDegree) // |a + b -2c| 논문 참조
                    * (this.dSlope + dSlope);
        }
        return 0;
    }

    /**
     * 내부 변수들에 대한 설명 : 서로다른 CompareableLine과 데이터를 비교하는 함수
     * 설명 : D와 S로 구성.
     */
    public double getFunctionS(ComparableLine comparableLine)
    {
        if (getFunctionD(comparableLine.getSlope()) == 0) // 사전 판별
            return -10000000; // 똥값

        return SWDUtil.cdParameter_beta*(Math.pow(this.iLine_size + comparableLine.getLine_size(),2)
                - SWDUtil.sdAvgCubeLineSize
                - getFunctionD(comparableLine.getSlope()))
                + getFunctionD(comparableLine.getSlope()); // 수식은 논문 참조,
    }



    public Vec4i getPoint(){return vPoint;}
    public int getLine_size(){return iLine_size;}
    public double getSlope(){ return dSlope;}
    public boolean calParams(){return true;}
    
}
