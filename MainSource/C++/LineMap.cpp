#include "LineMap.h"
#include "CompareableLine.h"

LineMap::LineMap(){}
LineMap::~LineMap(){
	vLines.clear();
	vResLine.clear();
}

LineMap::LineMap(Mat mImg) {
	this->mRawImg = mImg;
	this->iAngleProtocol = 0;
}

void LineMap::setSmStack(Size sSize) {
	smStack = Mat(sSize, CV_8UC1, Scalar(0));
}

void LineMap::setLine() {
	Mat mContours, mLines;
	
	Canny(mRawImg, mContours, 50, 200);
	vLines.clear();
	HoughLinesP(mContours, vLines, 1, CV_PI / 180, 50, 50, 10);
	
	mLineMap.create(mRawImg.size(), CV_32FC1);
	
	for (int i = 0; i < vLines.size(); i++) line(mLineMap, Point(vLines[i][0], vLines[i][1]), Point(vLines[i][2], vLines[i][3]), Scalar(255,255,255), 1);
	compareLine();
	for (int i = 0; i < 2; i++) line(smStack, Point(vResLine[i][0], vResLine[i][1]), Point(vResLine[i][2], vResLine[i][3]), Scalar(111), 3, 8);
	imshow("TT", smStack);
}

void LineMap::compareCurrent() {
	Vec4f vCurrentLine = calMapSlope(smStack);
	double dTheta;
	static int iStackcount = 1;
	static double dCurrentSlopeAvg, dPreviousSlopeAvg;

	if (vCurrentLine[2] == 0) dTheta = 90;
	else dTheta = -1 * atan(vCurrentLine[3] / vCurrentLine[2]) / CV_PI * 180.0;
	if (dTheta < 0) dTheta += 180;
	
	if (dCurrentSlopeAvg == 0) {
		dCurrentSlopeAvg = dTheta;
		dPreviousSlopeAvg = dCurrentSlopeAvg;
	}

	iStackcount++;
	if (iStackcount % 30 == 0) {
		setSmStack(Size(600, 400));
		smStack = Scalar(0);
		iStackcount = 0;
		dPreviousSlopeAvg = dCurrentSlopeAvg;
		dCurrentSlopeAvg = dTheta;
	}
	else if (abs(dPreviousSlopeAvg - dTheta) > 30) {
		waitKey();
		callCornerExist();
	}
	else if (abs(dPreviousSlopeAvg - dTheta) > 10) {
		callPedestrianOutofdirection((int)abs(dPreviousSlopeAvg - dTheta));
	}
	else {
		dCurrentSlopeAvg *= iStackcount-1;
		dCurrentSlopeAvg += dTheta;
		dCurrentSlopeAvg /= iStackcount;
		iAngleProtocol = 0;
	}
}

void LineMap::sendProtocol() {
	sendProc(iAngleProtocol);
}

void LineMap::callCornerExist(){
	printf("There Exist in corner!\n");
}

void LineMap::callPedestrianOutofdirection(int code) {
	printf("Pedestrian Out of direction!\n");
	printf("%d\n", code);
}

Vec4f LineMap::calMapSlope(Mat resMap) {
	std::vector<Point> vPoint;
	Vec4f vCurrentLine;

	for (int y = 0; y < resMap.size().height; y++) {
		uchar* ptr = resMap.ptr<uchar>(y);
		for (int x = 0; x < resMap.size().width; x++)
			if (ptr[x] != 0) vPoint.push_back(Point(x, y));
	}

	fitLine(Mat(vPoint), vCurrentLine, CV_DIST_L2, 0, 0.01, 0.01);

	vPoint.clear();
	return vCurrentLine;
}

void LineMap::compareLine() {	
	CompareableLine* ResLine[2];
	vector<CompareableLine*> cLineList;
	
	double dSumLineSize = 0, dSumCubeLineSize = 0;

	for (size_t i = 0; i < vLines.size(); i++) cLineList.push_back(new CompareableLine(vLines[i]));
	
	for (size_t i = 0; i < vLines.size(); i++) {
		dSumLineSize += (double)cLineList[i]->getLine_size();
		dSumCubeLineSize += pow(cLineList[i]->getLine_size(), 2);
	}

	double x = 0, y = 0, X_sum = 0, Y_sum = 0;
	for (size_t i = 0; i < vLines.size(); i++) {
		for (size_t j = 0; j < vLines.size(); j++) {
			if (i != j) {
				x += pow(cLineList[i]->getLine_size() + cLineList[j]->getLine_size(), 2) - dSumCubeLineSize;
				y += cLineList[i]->getFunctionD(cLineList[j]->getSlope());
				X_sum += x;
				Y_sum += y;
			}
		}
	}

	double beta = (2 * Y_sum - 2 * y) / (pow(x, 2) + x - 2 * X_sum + 2 * Y_sum);
	CompareableLine::setParam(beta);

//	printf("this is 1e20 * beta!! : %.10f\n",beta * 1e20);
	
	Vec4f vCurrent = calMapSlope(mLineMap);

	if (vCurrent[2] == 0) dNowSlope = 90;
	else dNowSlope = atan(-1 * (double)(vCurrent[3] / vCurrent[2])) * 180.0 / CV_PI;
	if (dNowSlope < 0) dNowSlope += 180;
	
	CompareableLine::setAvgLineSize(dSumLineSize / vLines.size());
	CompareableLine::setAvgCubeLineSize(dSumCubeLineSize / vLines.size());
	CompareableLine::setNowDegree(dNowSlope);
	
	ResLine[0] = cLineList[0];
	ResLine[1] = cLineList[1];
	
	for (size_t i = 2; i < vLines.size(); i++) {
		if (ResLine[0]->getFunctionS(ResLine[1]) < max(cLineList[i]->getFunctionS(ResLine[0]), cLineList[i]->getFunctionS(ResLine[1]))) { // if the line's loss function is larger in case of change occur
			bool flag = (cLineList[i]->getFunctionS(ResLine[0]) < cLineList[i]->getFunctionS(ResLine[1])); // show the larger index (As we want less value of loss func)
			ResLine[flag] = cLineList[i];
		}
	}
	
	vResLine.push_back(ResLine[0]->getPoint());
	vResLine.push_back(ResLine[1]->getPoint());
	cLineList.clear();
}

Mat LineMap::getLineMap() {
	return mLineMap;
} 

Mat LineMap::smStack;