#pragma once
#include"header.h"
#include"CompareableLine.h"

class LineMap
{
private:
	double dNowSlope;
	Mat mRawImg;
	Mat mLineMap;
	vector<Vec4i> vLines;
	vector<Vec4i> vResLine;
	int iAngleProtocol;
	static Mat smStack;
public:
	LineMap();
	~LineMap();
	LineMap(Mat);
	void setLine();
	void compareCurrent();
	void sendProtocol();
	void callCornerExist();
	void callPedestrianOutofdirection(int);
	void compareLine();
	void static setSmStack(Size);
	Vec4f calMapSlope(Mat);
	Mat getLineMap();
};