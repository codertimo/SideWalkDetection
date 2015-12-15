package com.codertimo.sidewalkdetection; // 파키지 임포르트

import android.support.v7.app.ActionBarActivity; // do import
import android.os.Bundle; // do import
import android.util.Log; // do import
import android.view.Menu; // do import
import android.view.MenuItem; // do import

import com.codertimo.sidewalkdetection.algorithm.LineMap; // do import

import org.opencv.core.Mat; // do import


public class MainActivity extends ActionBarActivity { // class


    private LineMap frame = null; // 하나의 이미지에 존재하는 라인을 저장하는 클래스인 LineMap을 frame이라고 정의하고, 그것을 초기화한다.

    @Override // 아노테이시어언
    protected void onCreate(Bundle savedInstanceState) { // 크리에이투
        super.onCreate(savedInstanceState); // 오느크리에이투
        setContentView(R.layout.activity_main); // 콘탄트뷰
    }

    private void SWDProcess(Mat mat) // 에스따블유디 처리 마트
    {
        frame = new LineMap(mat); // 생성자를 이용하여 Mat정보를 LineMap에 넘기고, 그정보를 토대로 객체 생성
        frame.setLine(); // 받은 Mat 정보를 토대로 여러가지 필터를 적용하여 직선 계산
        frame.compareCurrent(); // 계산된 직선을 이용하여 이전까지의 직선과 경향성 비교
        frame.sendProtocol(); // 결과값을 전달
    }


    void sendProtocal(int iAngleProtocol){ // 세에에ㅔ엥늗 프로타콜
        if(iAngleProtocol == 0) // 프로타콜이 0이면(정상상황)
            Log.i("SWD","walking.."); // 로깅..
        else
            Log.i("SWD","receive : "+iAngleProtocol); // 로깅
    }


}
