package com.codertimo.sidewalkdetection; // ��Ű�� ������Ʈ

import android.support.v7.app.ActionBarActivity; // do import
import android.os.Bundle; // do import
import android.util.Log; // do import
import android.view.Menu; // do import
import android.view.MenuItem; // do import

import com.codertimo.sidewalkdetection.algorithm.LineMap; // do import

import org.opencv.core.Mat; // do import


public class MainActivity extends ActionBarActivity { // class


    private LineMap frame = null; // �ϳ��� �̹����� �����ϴ� ������ �����ϴ� Ŭ������ LineMap�� frame�̶�� �����ϰ�, �װ��� �ʱ�ȭ�Ѵ�.

    @Override // �Ƴ����̽þ��
    protected void onCreate(Bundle savedInstanceState) { // ũ��������
        super.onCreate(savedInstanceState); // ����ũ��������
        setContentView(R.layout.activity_main); // ��źƮ��
    }

    private void SWDProcess(Mat mat) // ������������ ó�� ��Ʈ
    {
        frame = new LineMap(mat); // �����ڸ� �̿��Ͽ� Mat������ LineMap�� �ѱ��, �������� ���� ��ü ����
        frame.setLine(); // ���� Mat ������ ���� �������� ���͸� �����Ͽ� ���� ���
        frame.compareCurrent(); // ���� ������ �̿��Ͽ� ���������� ������ ���⼺ ��
        frame.sendProtocol(); // ������� ����
    }


    void sendProtocal(int iAngleProtocol){ // �������Ŀ��f ����Ÿ��
        if(iAngleProtocol == 0) // ����Ÿ���� 0�̸�(�����Ȳ)
            Log.i("SWD","walking.."); // �α�..
        else
            Log.i("SWD","receive : "+iAngleProtocol); // �α�
    }


}
