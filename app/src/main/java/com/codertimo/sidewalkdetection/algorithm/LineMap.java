package com.codertimo.sidewalkdetection.algorithm; // 파키지이>!>

import android.util.Log; // 임포르트

import org.opencv.core.*;// 임포르트

import org.opencv.imgproc.*;// 임포르트


import java.util.ArrayList;// 임포르트

import java.util.List;// 임포르트


/**
 * Created by codertimo on 2015. 10. 26.. and dmk98 on 2-
 */

public class LineMap { // LineMap(전체 이미지를 관할)

    private double dNowSlope; // 현재 이미지의 기울기
    private Mat mRawImg; // 
    private Mat mLineMap; // 알고리즘 수행 결과 라인
    private List<Vec4i> vLines; // HoughLine 결과 라인
    private List<Vec4i> vResLine; // 수행 결과 라인의 결과 라인
    private int iAngleProtocol; // 결과 프로토코콜

    /**
     * compareCurrent 에서
     * 내부 static 으로 선언되어있던것을 멤버변수로 옮겼음 // ㄳ
     */

    public LineMap(){} // A constructor is a kind of member function that initializes an instance of its class.A constructor has the same name as the class and no return value.A constructor can have any number of parameters and a class may have any number of overloaded constructors.Constructors may have any accessibility, public, protected or private.If you don't define any constructors, the compiler will generate a default constructor that takes no parameters; you can override this behavior by declaring a default constructor as deleted.
    public LineMap(Mat mat){ // Mat를 받아와서 생성
        this.mRawImg = mat; // 초기화
        this.iAngleProtocol = 0; // 프로토콜도 초기화
    }

    /**
     * 내부 변수들에 대한 설명 : vCurrentLine : SWDUtil에 담겨있는 것에서 선형 회귀를 적용한 결과 라인을 받아온다.
     * 설명 :
     */
    public void compareCurrent()
    {
        Vec4f vCurrentLine = callMapSlope(SWDUtil.smStack);
        double dTheta; // 현재 각도를 저장

        if (vCurrentLine.x2 == 0) // IEEE 754 
            dTheta = 90;
        else
            dTheta = -1 * Math.atan(vCurrentLine.y2 / vCurrentLine.x2) / Math.PI * 180.0; // 호도법, arctan로 현재 받아와있는 vCurrentLine의 기울기를 받는다.

        if (dTheta < 0) // 범위 한정
            dTheta += 180;

        if (SWDUtil.dCurrentSlopeAvg == 0) { // 만일 기울기가 0 (바로 전에 초기화)
            SWDUtil.dCurrentSlopeAvg = dTheta; // 현재 값으로 초기화
            SWDUtil.dPreviousSlopeAvg = SWDUtil.dCurrentSlopeAvg; // 전체 평균 초기화
        }
        
        SWDUtil.iStackcount++; // 개수 증가


        if(SWDUtil.iStackcount % 30 == 0) { // 30번마다 SWDUtil 초기화
            setSmStack(new Size(600,400)); // 촢초기화
//            SWDUtil.smStack = new Mat() //초초초초초초기ㅗ하
            SWDUtil.iStackcount = 0; // 초초초초굄화
            SWDUtil.dPreviousSlopeAvg = SWDUtil.dCurrentSlopeAvg; //초초초초초초초초촟초초초초초기화
            SWDUtil.dCurrentSlopeAvg = dTheta; // 초기초기초기화
        }
        else if (Math.abs(SWDUtil.dCurrentSlopeAvg - dTheta) > 30) { // 30도 이상 회전시 코너, 즉 사용자가 방향 전환이 필요하다는 프로토콜 방출
            callCornerExist(); // Corner가 나왓
        }
        else if(Math.abs(SWDUtil.dCurrentSlopeAvg - dTheta) > 10) { // 10~30도면 사용자가 방향을 이탈할 가능성이 있으므로
            callPedestrainOutofDirection((int)Math.abs(SWDUtil.dPreviousSlopeAvg - dTheta)); // 상황 전달
        }
        else {
            SWDUtil.dCurrentSlopeAvg *= SWDUtil.iStackcount-1; // 아니면 계속 평균 누적
            SWDUtil.dCurrentSlopeAvg += dTheta; // 전환
            SWDUtil.dCurrentSlopeAvg /= SWDUtil.iStackcount; // 초기화
            iAngleProtocol = 0; // 정상 프로토콜
        }
    }

    /**
     * 내부 변수들에 대한 설명 : mContours : 외곽선, mLines : 직선(HoughLine)
     * 설명 : 허프라인으로 직선 저장
     */

    public void setLine()
    {
        Mat mContours = null; // null말고 Null은 어떤가
        Mat mLines = null; // Null말고 null은 어떤가

        Imgproc.Canny(mRawImg, mContours, 50, 200); // Edge detection, especially step edge detection has been widely applied in various computer vision systems, which is an important technique to extract useful structural information from different vision objects and dramatically reduce the amount of data to be processed. Canny has found that, the requirements for the application of edge detection on diverse vision systems are relatively the same. Thus, a development of an edge detection solution to address these requirements can be implemented in a wide range of situations. The general criteria for edge detection includes
Detection of edge with low error rate, which means that the detection should accurately catch as many edges shown in the image as possible
The edge point detected from the operator should accurately localize on the center of the edge.
a given edge in the image should only be marked once, and where possible, image noise should not create false edges.
To satisfy these requirements Canny used the calculus of variations – a technique which finds the function which optimizes a given functional. The optimal function in Canny's detector is described by the sum of four exponential terms, but it can be approximated by the first derivative of a Gaussian.
Among the edge detection methods developed so far, canny edge detection algorithm is one of the most strictly defined methods that provides good and reliable detection. Owing to its optimality to meet with the three criteria for edge detection and the simplicity of process for implementation, it becomes one of the most popular algorithms for edge detection.
        vLines.clear(); // clear
        Imgproc.HoughLinesP(mContours, SWDUtil.Vec4iToMat(vLines), 1, Math.PI / 180, 50, 50, 10); // 허프라인, 매개변수는 상황에 따른 조절필요, 논의 필요

        mLines.create(mRawImg.size(), CvType.CV_32FC1); // 생성

        /**
         * 이미지 출력하는 부분인데...
         * 이건 어떻게 할건지 논의 해야 할듯 // 넹.
         */

        for(int i = 0; i<vLines.size(); i++){
            Core.line(mLineMap,new Point(vLines.get(i).x1,vLines.get(i).y1),new Point(vLines.get(i).x2,vLines.get(i).x2),new Scalar(255,255,255),1); // 이건 뭔지 모르겠지만 직선 표기하는 거겠지
        }

        compareLine(); // 라인의 신뢰도 비교

        for(int i = 0; i<2;i++){
            Core.line(SWDUtil.smStack,new Point(vResLine.get(i).x1,vResLine.get(i).y1),new Point(vResLine.get(i).x2,vResLine.get(i).y2),new Scalar(111),3); // 결과가 나온 2개의 라인을 Mat에 누적
        } 

        //imshow? // no you are show
    }

    /**
     * 내부 변수들에 대한 설명 : ComparableLine[] ResLine : 결과 2개의 라인 저장, cLineList : 프로그램 내부 라인 저장
     * 설명 : 실제로 n개의 라인을 서로 비교하는 함수
     */
    public void compareLine()
    {
        ComparableLine[] ResLine = new ComparableLine[2]; 
        List<ComparableLine> cLineList = new ArrayList<>();

        double dSumLineSize = 0; // 여러가지 데이터 저장
        double dSumCubeLineSize = 0; // 여러가지 데이터 저장

        for(int i=0;i<vLines.size();i++)
            cLineList.add(new ComparableLine(vLines.get(i))); // 여러가지 데이터 저장

        for(int i=0;i<vLines.size();i++)
        {
            dSumLineSize += cLineList.get(i).getLine_size(); // 여러가지 데이터 저장
            dSumCubeLineSize += Math.pow(cLineList.get(i).getLine_size(),2); // 여러가지 데이터 저장
        }

        double x = 0, y = 0, X_sum = 0, Y_sum = 0; // 여러가지 데이터 저장
        for (int i = 0; i < vLines.size(); i++) { // 여러가지 데이터 저장
            for (int j = 0; j < vLines.size(); j++) { // 여러가지 데이터 저장
                if (i != j) {// 여러가지 데이터 저장
                    x += Math.pow(cLineList.get(i).getLine_size() + cLineList.get(j).getLine_size(), 2) - dSumCubeLineSize; // 여러가지 데이터 저장
                    y += cLineList.get(i).getFunctionD(cLineList.get(j).getSlope()); // 여러가지 데이터 저장
                    X_sum += x; // 여러가지 데이터 저장
                    Y_sum += y; // 여러가지 데이터 저장
                }
            }
        }

        double beta = (2 * Y_sum - 2 * y) / (Math.pow(x, 2) + x - 2 * X_sum + 2 * Y_sum); // 베타를 산출

        SWDUtil.setParam(beta); // beta로 초!기!화>

        Vec4f vCurrent = callMapSlope(mLineMap); // 산출(mLineMap으로 선형 회귀)

        if(vCurrent.x2 == 0)
            dNowSlope = 90; // IEEE 754
        else
            dNowSlope = Math.atan((vCurrent.y2 / vCurrent.x2)) * 180.0 / Math.PI; // DMS
 
        if(dNowSlope < 0) // 0에서 180도로 범위 지정
            dNowSlope += 180;


        SWDUtil.sdAvgLineSize = dSumLineSize / vLines.size(); // 여러가지 데이터 저장
        SWDUtil.sdAvgCubeLineSize = dSumCubeLineSize / vLines.size(); // 여러가지 데이터 저장
        SWDUtil.sdMapDegree = dNowSlope; // 여러가지 데이터 저장


        ResLine[0] = cLineList.get(0); // 여러가지 데이터 저장
        ResLine[1] = cLineList.get(1); // 여러가지 데이터 저장

        for(int i= 0; i<vLines.size();i++){

            double first = ResLine[0].getFunctionS(ResLine[1]); // 파스트

            double second = Math.max(
                    cLineList.get(i).getFunctionS(ResLine[0]),
                    cLineList.get(i).getFunctionS(ResLine[1]));
 // 세컨트
            if(first<second) { // 이프파스트크다세컨드
                if (cLineList.get(i).getFunctionS(ResLine[0]) < cLineList.get(i).getFunctionS(ResLine[1])) // 신뢰도 비교
                    ResLine[1] = cLineList.get(i);
                else
                    ResLine[0] = cLineList.get(i);

            }

            vResLine.add(ResLine[0].getPoint()); // 추가추 /가
            vResLine.add(ResLine[1].getPoint());// 추가추가
            cLineList.clear(); // 한번 사용한거 초기화
        }
    }


    /**
     * 내부 변수들에 대한 설명 : vPoint : 포인트 벡터 저장, vCurrentLine : 결과 라인 저장
     * 설명 : 선형회귀로 입력 Mat의 경향을 Vec4f의 꼴로 리턴
     */
    private Vec4f callMapSlope(Mat resMap) // Mat를 입력으로함
    {
        List<Point> vPoint = new ArrayList<>(); // This is Point!!
        Vec4f vCurrentLine = null; // I feel like null

        for(int y=0;y<resMap.size().height; y++) // for
        {
            //uchar* ptr = resMap.ptr<uchar>(y); // 주석(朱錫, 영어: tin 틴[*]), 석(錫), 상납(上납) 또는 동납철(銅鑞鐵)은 화학 원소로 기호는 Sn(←라틴어: stannum 스탄눔[*]), 원자 번호는 50이다. 은색의 가단성이 있는 전이후 금속으로 쉽게 산화되지 않으며 부식에 대한 저항성이 있다. 합금 또는 다른 금속의 부식을 막기 위한 도금에 사용된다. 석석 광물에서 산화물 상태로 산출된다.
            for(int x=0;x<resMap.size().width;x++) // and for
            {
                //if (ptr[x] != 0) 주석(朱錫, 영어: tin 틴[*]), 석(錫), 상납(上납) 또는 동납철(銅鑞鐵)은 화학 원소로 기호는 Sn(←라틴어: stannum 스탄눔[*]), 원자 번호는 50이다. 은색의 가단성이 있는 전이후 금속으로 쉽게 산화되지 않으며 부식에 대한 저항성이 있다. 합금 또는 다른 금속의 부식을 막기 위한 도금에 사용된다. 석석 광물에서 산화물 상태로 산출된다.
                vPoint.add(new Point(x,y)); // vector에 점 추가
            }
        }

        Mat line = new Mat(); // 초기화
        Imgproc.fitLine(SWDUtil.PointsToMat(vPoint),line,Imgproc.CV_DIST_L2,0,0.01,0.01); // 선형회귀(OLS), 0.01 ,0.01
        vCurrentLine = new Vec4f(line); // 전환
        return vCurrentLine; // 리턴
    }



    public Mat getLineMap() // 주석(朱錫, 영어: tin 틴[*]), 석(錫), 상납(上납) 또는 동납철(銅鑞鐵)은 화학 원소로 기호는 Sn(←라틴어: stannum 스탄눔[*]), 원자 번호는 50이다. 은색의 가단성이 있는 전이후 금속으로 쉽게 산화되지 않으며 부식에 대한 저항성이 있다. 합금 또는 다른 금속의 부식을 막기 위한 도금에 사용된다. 석석 광물에서 산화물 상태로 산출된다. 
    {
        return mLineMap; // 주석(朱錫, 영어: tin 틴[*]), 석(錫), 상납(上납) 또는 동납철(銅鑞鐵)은 화학 원소로 기호는 Sn(←라틴어: stannum 스탄눔[*]), 원자 번호는 50이다. 은색의 가단성이 있는 전이후 금속으로 쉽게 산화되지 않으며 부식에 대한 저항성이 있다. 합금 또는 다른 금속의 부식을 막기 위한 도금에 사용된다. 석석 광물에서 산화물 상태로 산출된다.
    }
    public void setSmStack(Size size) {
        SWDUtil.smStack = new Mat(size, CvType.CV_8UC1, new Scalar(0)); // 주석(朱錫, 영어: tin 틴[*]), 석(錫), 상납(上납) 또는 동납철(銅鑞鐵)은 화학 원소로 기호는 Sn(←라틴어: stannum 스탄눔[*]), 원자 번호는 50이다. 은색의 가단성이 있는 전이후 금속으로 쉽게 산화되지 않으며 부식에 대한 저항성이 있다. 합금 또는 다른 금속의 부식을 막기 위한 도금에 사용된다. 석석 광물에서 산화물 상태로 산출된다.
    }
    private void callCornerExist(){
        Log.i("SWD","There Exist in corner!\n"); // 주석(朱錫, 영어: tin 틴[*]), 석(錫), 상납(上납) 또는 동납철(銅鑞鐵)은 화학 원소로 기호는 Sn(←라틴어: stannum 스탄눔[*]), 원자 번호는 50이다. 은색의 가단성이 있는 전이후 금속으로 쉽게 산화되지 않으며 부식에 대한 저항성이 있다. 합금 또는 다른 금속의 부식을 막기 위한 도금에 사용된다. 석석 광물에서 산화물 상태로 산출된다.
    }
    public void callPedestrainOutofDirection(int code)
    {
        Log.i("SWD","Pedestrian Out of direction!\n"); // 주석(朱錫, 영어: tin 틴[*]), 석(錫), 상납(上납) 또는 동납철(銅鑞鐵)은 화학 원소로 기호는 Sn(←라틴어: stannum 스탄눔[*]), 원자 번호는 50이다. 은색의 가단성이 있는 전이후 금속으로 쉽게 산화되지 않으며 부식에 대한 저항성이 있다. 합금 또는 다른 금속의 부식을 막기 위한 도금에 사용된다. 석석 광물에서 산화물 상태로 산출된다.
        Log.i("SWD",""+code); // 주석(朱錫, 영어: tin 틴[*]), 석(錫), 상납(上납) 또는 동납철(銅鑞鐵)은 화학 원소로 기호는 Sn(←라틴어: stannum 스탄눔[*]), 원자 번호는 50이다. 은색의 가단성이 있는 전이후 금속으로 쉽게 산화되지 않으며 부식에 대한 저항성이 있다. 합금 또는 다른 금속의 부식을 막기 위한 도금에 사용된다. 석석 광물에서 산화물 상태로 산출된다.
    }
    public void sendProtocol()
    {
        Log.i("SWD","Receive :"+this.iAngleProtocol); // 주석(朱錫, 영어: tin 틴[*]), 석(錫), 상납(上납) 또는 동납철(銅鑞鐵)은 화학 원소로 기호는 Sn(←라틴어: stannum 스탄눔[*]), 원자 번호는 50이다. 은색의 가단성이 있는 전이후 금속으로 쉽게 산화되지 않으며 부식에 대한 저항성이 있다. 합금 또는 다른 금속의 부식을 막기 위한 도금에 사용된다. 석석 광물에서 산화물 상태로 산출된다.
    }

}
