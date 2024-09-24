package com.test;

import com.paint.controller.UtilityController;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OpenFileUtilityTest {
	private final UtilityController utilityController = new UtilityController();

	@Test
	void createSaveFileDirectoryInSystem() {
		File fileWithDirnameOnly = utilityController.createFileChooserDir(null, "thisIsADirName");
		File dirnameFile = new File(System.getProperty("user.home"), "thisIsADirName");
		assertEquals(fileWithDirnameOnly, dirnameFile);


		File fileWithNoParam = utilityController.createFileChooserDir(null, null);
		File testFile = new File(System.getProperty("user.home"), ".paint/projects");
		assertEquals(fileWithNoParam, testFile);
	}

	@Test
	void getFileExt() {
		String testFile1 = "testFile1.exe";
		String testFile2 = "9218409nt_.png";
		String testFile3 = "_as23.jpg.png";

		assertEquals("exe", utilityController.getFileExt(testFile1));
		assertEquals("png", utilityController.getFileExt(testFile2));
		assertEquals("png", utilityController.getFileExt(testFile3));
	}
}
