#include "CompareableLine.h"

CompareableLine::CompareableLine() {}
CompareableLine::~CompareableLine() {}

CompareableLine::CompareableLine(Vec4i vPoint) {
	this->vPoint = vPoint;
	iLine_size = (int)pow(vPoint[0] - vPoint[2], 2) + (int)pow(vPoint[1] - vPoint[3], 2);
	if (vPoint[2] == vPoint[0]) dSlope = 90;
	dSlope = atan(-1 * (double)(vPoint[3] - vPoint[1]) / (vPoint[2] - vPoint[0])) * 180.0 / CV_PI;
	if (dSlope < 0) dSlope += 180;
}

Vec4i CompareableLine::getPoint() {
	return vPoint;
}

int CompareableLine::getLine_size() {
	return iLine_size;
}

double CompareableLine::getFunctionS(CompareableLine* ll) {
	if (getFunctionD(ll->getSlope()) == 0) return -10000000;
	return cdParameter_beta*(pow(this->iLine_size + ll->getLine_size(),2) - sdAvgCubeLineSize - getFunctionD(ll->getSlope())) + getFunctionD(ll->getSlope()); // represent loss function of line which involves probablity of loss.
}

double CompareableLine::getFunctionD(double dSlope) {
	if (10 + this->dSlope < dSlope || this->dSlope - 10 > dSlope) return abs(this->dSlope + dSlope - 2 * sdMapDegree)*(this->dSlope + dSlope);
	return 0;
}

bool CompareableLine::calParams() {
	return true;
}

double CompareableLine::getSlope() {
	return dSlope;
}

void CompareableLine::setAvgLineSize(double AvgLineSize) {
	sdAvgLineSize = AvgLineSize;
}

double CompareableLine::getAvgLineSize() {
	return sdAvgLineSize;
}

void CompareableLine::setAvgCubeLineSize(double AvgCubeLineSize) {
	sdAvgCubeLineSize = AvgCubeLineSize;
}

CompareableLine CompareableLine::operator=(const CompareableLine& p) {
	return p;
}

void CompareableLine::setNowDegree(double dNowDegree) {
	sdMapDegree = dNowDegree;
}

void CompareableLine::setParam(double val)
{
	CompareableLine::cdParameter_beta = val;
}

double CompareableLine::sdAvgLineSize;
double CompareableLine::sdAvgCubeLineSize;
double CompareableLine::sdMapDegree;
double CompareableLine::cdParameter_beta;