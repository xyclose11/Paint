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
		String testFile1 = "testFile1.exe";
		String testFile2 = "9218409nt_.png";
		String testFile3 = "_as23.jpg.png";


		assertEquals("exe", utilityController.getFileExt(testFile1));
		assertEquals("png", utilityController.getFileExt(testFile2));
		assertEquals("png", utilityController.getFileExt(testFile3));
	}

	@Test
	void getFileExtEdgeCases() {
		String testFile4 = "fileWithoutAnExtension";
		String testFile5 = "this.file.has.many.dots";
		String testFile6 = "thisFileEndsWithA.";

		assertEquals("", utilityController.getFileExt(testFile4));
		assertEquals("dots", utilityController.getFileExt(testFile5));
		assertEquals("", utilityController.getFileExt(testFile6));
	}

}
