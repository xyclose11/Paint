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
}
