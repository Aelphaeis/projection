package com.cruat.tools.projection.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
	private ConflictResolution resolutionStrategy;
	

	public DirectoryProjector(String source, String target) {
		this(source, target, ConflictResolution.OVERWRITE);
	}

	public DirectoryProjector(String s, String t, ConflictResolution r) {
		this(new File(s), new File(t), r);
	}
	
	public DirectoryProjector(File s, File t) {
		this(s, t, ConflictResolution.OVERWRITE);
	}

	public DirectoryProjector(File s, File t, ConflictResolution r) {
		this.source = s;
		this.target = t;
		resolutionStrategy = r;
		validateSource();
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

	@Override
	public ConflictResolution getResolution() {
		return resolutionStrategy;
	}
	
	private boolean moveDirectory() {
        Path sourceParentFolder = getSource().toPath();
        try (Stream<Path> movables = Files.walk(sourceParentFolder)){
            movables.forEach(move);

        } catch(FileAlreadyExistsException e) {
            //file already exists and unable to copy
        	//TODO figure this out
        } catch (IOException e) {
        	//probably permission issue
        	//TODO figure out what we will do here
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
			targetBase = this.target.toPath().toString().replaceAll(Pattern.quote("\\"), "/");
			
			isFilesMoved = false;
		}

		@Override
		public void accept(Path source) {
			String sPath = source.toString();
			String targetLocation = sPath.replaceAll(Pattern.quote(sourceBase), targetBase);
			
			try {
				Files.copy(source, Paths.get(targetLocation));
				isFilesMoved = true;
			}
			catch(FileAlreadyExistsException e) {
				//TODO figure out a way for caller to configure this
			}
			catch(IOException e) {
				//I don't really expect this to happen.
				//TODO figure out a way for caller to configure this
				String err = "Unexpected exception occurred";
	        	logger.error(err, e);
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
