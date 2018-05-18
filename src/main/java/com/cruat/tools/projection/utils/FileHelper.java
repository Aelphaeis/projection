package com.cruat.tools.projection.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

public class FileHelper {

	public static Set<File> listFileTree(File dir) {
		return listFileTree(dir, true);
	}

	/**
	 * Returns 
	 * @param dir
	 * @param recursive
	 * @return
	 */
	public static Set<File> listFileTree(File dir, boolean recursive) {
		Set<File> fileTree = new HashSet<>();
		if (dir == null || !dir.exists()) {
			return fileTree;
		}
		File[] children = dir.listFiles();
		if (children == null) {
			fileTree.add(dir);
		} else {
			for (File entry : children) {
				if (entry.isFile())
					fileTree.add(entry);
				else if (recursive)
					fileTree.addAll(listFileTree(entry, recursive));
			}
		}
		return fileTree;
	}

	/**
	 * Deletes the contents of a folder without deleting the folder.
	 * 
	 * @param folder
	 * @return true if anything was deleted, otherwise false
	 */
	public static boolean deleteContents(File folder) {
		File[] files = folder.listFiles();
		boolean deleted = false;
		if(files == null) {
			return deleted;
		}
		try {
			for (File f : files) {
				if (f.isDirectory()) {
					deleted |= deleteContents(f);
				} else {
					deleted |= Files.deleteIfExists(f.toPath());
				}
			}
		}
		catch(IOException e) {
			throw new IllegalArgumentException(e);
		}
		return deleted;
	}
	private FileHelper() {
		// utility class
	}
}
