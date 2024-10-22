package com.test;

import com.paint.controller.UtilityController;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OpenFileUtilityTest {
	private final UtilityController utilityController = new UtilityController();

	@Test
	void testCreateSaveFileDirectoryWithNoParams() {
		File fileWithNoParam = utilityController.createFileChooserDir(null, null);
		File testFile = new File(System.getProperty("user.home"), ".paint/projects");
		assertEquals(fileWithNoParam, testFile);
	}

	@Test
	void testCreateSaveFileDirectoryWithDirNameOnly() {
		File fileWithDirnameOnly = utilityController.createFileChooserDir(null, "thisIsADirName");
		File dirnameFile = new File(System.getProperty("user.home"), "thisIsADirName");
		assertEquals(fileWithDirnameOnly, dirnameFile);
	}

	@Test
	void getFileExtBaseCases() {
		File testFile1 = new File("testFile1.exe");
		File testFile2 = new File("9218409nt_.png");
		File testFile3 = new File("_as23.jpg.png");

		assertEquals("exe", utilityController.getFileExt(testFile1));
		assertEquals("png", utilityController.getFileExt(testFile2));
		assertEquals("png", utilityController.getFileExt(testFile3));
	}

	@Test
	void getFileExtEdgeCases() {
		File testFile4 = new File("fileWithoutAnExtension");
		File testFile5 = new File("this.file.has.many.dots");
		File testFile6 = new File("thisFileEndsWithA.");

		assertEquals("", utilityController.getFileExt(testFile4));
		assertEquals("dots", utilityController.getFileExt(testFile5));
		assertEquals("", utilityController.getFileExt(testFile6));
	}

}
