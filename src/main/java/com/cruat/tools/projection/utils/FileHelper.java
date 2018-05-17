package com.cruat.tools.projection.utils;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class FileHelper {

	public static Set<File> listFileTree(File dir) {
		return listFileTree(dir, true);
	}

	public static Set<File> listFileTree(File dir, boolean recursive) {
		Set<File> fileTree = new HashSet<>();
		if (dir == null || !dir.exists()) {
			return fileTree;
		}
		File[] children = dir.listFiles();
		if (children == null) {
			fileTree.add(dir);
		} 
		else {
			for (File entry : children) {
				if (entry.isFile())
					fileTree.add(entry);
				else if(recursive)
					fileTree.addAll(listFileTree(entry, recursive));
			}
		}
		return fileTree;
	}
	
	private FileHelper() {
		//utility class
	}
}
