package com.facerec.util;

import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_COLOR;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgcodecs.imwrite;
import static org.bytedeco.javacpp.opencv_imgproc.COLOR_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.cvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.equalizeHist;
import static org.bytedeco.javacpp.opencv_imgproc.resize;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_core.RectVector;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.opencv_objdetect.CascadeClassifier;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class ImageUtil {

	private static final String XML_FILE = "D:/OpenCV/opencv/sources/data/haarcascades/haarcascade_frontalface_default.xml";
	private static final Gson gson = new Gson();

	private static final CascadeClassifier face_cascade = new CascadeClassifier(XML_FILE);

	public static boolean dealSampleFaceImage(Mat src, String fileUrl) {
		Mat srcGray = new Mat();
		cvtColor(src, srcGray, COLOR_BGR2GRAY);
		equalizeHist(srcGray, srcGray);
		
		RectVector faces = new RectVector();
		face_cascade.detectMultiScale(srcGray, faces);
		
		int total_Faces = (int) faces.size();
		if (total_Faces == 0)
			return false;
		
		Mat imgTemp = src.clone();
		for (int i = 0; i < total_Faces; i++) {
			Rect r = faces.get(i);
			Rect cr = new Rect(r.x(), r.y(), 200, 200);
			Mat faceROI = new Mat(imgTemp, cr);
			resize(faceROI, faceROI, new Size(200, 200));
		}
		return imwrite(fileUrl, imgTemp);
	}
	
	public static boolean dealSampleFaceImage(String imagePath, String fileUrl) {
		Mat src = imread(imagePath, IMREAD_COLOR);
		if (src.empty()) {
			return false;
		}
		return dealSampleFaceImage(src, fileUrl);
	}
	
	@SuppressWarnings("resource")
	public static void saveFileJson(ImageFile imageFile) {
		String filesContent = gson.toJson(imageFile);
		File file = new File("files.json");
		System.out.println(imageFile.toString());
		try {
			FileOutputStream fileOutput = new FileOutputStream(file);
			fileOutput.write(filesContent.getBytes());
		} catch(IOException e) {}
	}
	
	@SuppressWarnings("finally")
	public static ImageFile getImageFile() {
		ImageFile file = null;
		try {
			file =  gson.fromJson(new FileReader("files.json"), ImageFile.class);
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			System.out.println("files.json 文件加载失败！没有找到！");
			file = new ImageFile();			
		} finally {
			return file;
		}
	}
	
	public static String getNameInNumber(String nb) {
		return "老王";
	}
}
