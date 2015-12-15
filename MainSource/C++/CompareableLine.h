#pragma once
#include"header.h"

class CompareableLine
{
private:
	const double sdAllowedMinGap = 0.9;
	const double sdAllowedMaxGap = 1.1;
	const double sdDeniedMinGap = 0.8;
	const double sdDeniedMaxGap = 1.2;
	int iLine_size;
	double dSlope;
	static double sdAvgLineSize;
	static double sdAvgCubeLineSize;
	static double sdMapDegree;
	static double cdParameter_beta;
	Vec4i vPoint;
public:
	CompareableLine();
	CompareableLine(Vec4i);
	CompareableLine operator=(const CompareableLine&);
	~CompareableLine();
	
	Vec4i getPoint();
	int getLine_size();
	double getFunctionD(double);
	double getFunctionS(CompareableLine*);
	double getSlope();
	void static setAvgLineSize(double);
	void static setAvgCubeLineSize(double);
	void static setNowDegree(double);
	void static setParam(double);
	double static getAvgLineSize();
	bool calParams();
};