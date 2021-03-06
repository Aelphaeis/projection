package com.cruat.tools.projection.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DirectoryProjector implements Projector<File> {
	
	private static final Logger logger = LogManager.getLogger();
	
	private final File source;
	private final File target;
	private final FileMover move;
	private final boolean overwrite;
	
	public DirectoryProjector(String s, String t) {
		this(new File(s), new File(t));
	}
	
	public DirectoryProjector(String s, String t, boolean overwrite) {
		this(new File(s), new File(t), overwrite);
	}
	
	public DirectoryProjector(File s, File t) {
		this(s, t, true);
	}
	
	public DirectoryProjector(File s, File t, boolean overwrite) {
		this.source = s;
		this.target = t;
		validateSource();
		
		this.overwrite = overwrite;
		move = new FileMover(getSource(), getTarget());
	}
	
	@Override
	public boolean project() {
		return moveDirectory();
	}

	@Override
	public File getSource() {
		return source;
	}

	@Override
	public File getTarget() {
		return target;
	}

	private boolean moveDirectory() {
        Path sourceParentFolder = getSource().toPath();
        try (Stream<Path> movables = Files.walk(sourceParentFolder)){
            movables.forEach(move);
        } catch (IOException e) {
        	//probably permission issue
        	String err = "Unexpected exception occurred";
        	logger.error(err, e);
        }
        try {
            return move.isFilesMoved();
        }
        finally {
        	move.reset();
        }
    }

	private void validateSource() {
		if (!getSource().exists()) {
			String err = "source must exist";
			throw new IllegalArgumentException(err);
		}
		if (!getSource().isDirectory()) {
			String err = "source must be a file";
			throw new IllegalArgumentException(err);
		}
	}
	
	private class FileMover implements Consumer<Path> {
		
		private final File target;
		private final File source;
		
		private final String sourceBase;
		private final String targetBase;
		
		private boolean isFilesMoved;
		
		public FileMover(File sDir, File tDir ) {
			this.target = tDir;
			this.source = sDir;
			
			sourceBase = this.source.toPath().toString();
			String tBase = this.target.toPath().toString();
			targetBase = tBase.replaceAll(Pattern.quote("\\"), "/");
			
			isFilesMoved = false;
		}

		@Override
		public void accept(Path source) {
			accept(source, new CopyOption[0]);
		}
		
		public void accept(Path source, CopyOption... opts) {
			String sPath = source.toString();
			String quoted = Pattern.quote(sourceBase);
			String targetLocation = sPath.replaceAll(quoted, targetBase);
			Path tPath = Paths.get(targetLocation);
			
			logger.info("Moving [{}] to [{}]", sPath, tPath);
			
			try {
				//make parent directories.
				tPath.toFile().getParentFile().mkdirs();
				Files.copy(source, tPath, opts);
				isFilesMoved = true;
			}
			catch(FileAlreadyExistsException e) {
				//NB : e can be null, but really it should ever happen.
				File existing = new File(e.getFile());
				if(!existing.isDirectory() && overwrite) {
					logger.info("[{}] already exists. Replacing.", tPath);
					accept(source, StandardCopyOption.REPLACE_EXISTING);
				}
				else {
					logger.info("[{}] already exists. Skipping", tPath);
				}
			}
			catch(IOException e) {
	        	logger.error("Unexpected exception", e);
			}
		}
		
		public void reset() {
			isFilesMoved = false;
		}

		public boolean isFilesMoved() {
			return isFilesMoved;
		}
	}
}
