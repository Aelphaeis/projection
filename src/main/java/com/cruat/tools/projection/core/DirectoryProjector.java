package com.cruat.tools.projection.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class DirectoryProjector implements Projector<File> {

	private final File source;
	private final File target;
	ConflictResolution resolutionStrategy;

	public DirectoryProjector(String source, String target) {
		this(source, target, ConflictResolution.OVERWRITE);
	}

	public DirectoryProjector(String source, String target, ConflictResolution res) {
		this(new File(source), new File(target), res);
	}
	
	public DirectoryProjector(File source, File target) {
		this(source, target, ConflictResolution.OVERWRITE);
	}

	public DirectoryProjector(File source, File target, ConflictResolution res) {
		this.source = source;
		this.target = target;
		resolutionStrategy = res;
		validateSource();
	}
	
	@Override
	public boolean project() throws ProjectionException {
		// TODO Auto-generated method stub
		return false;
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
	public ConflictResolution getConflictResolutionStrategy() {
		return resolutionStrategy;
	}
	
	private static void moveDirectory(File source, File target) {
        Path sourceParentFolder = source.toPath();
        Path destinationParentFolder = target.toPath();

        try {
            Stream<Path> allFilesPathStream = Files.walk(sourceParentFolder);
            Consumer<? super Path> action = new Consumer<Path>(){

                @Override
                public void accept(Path t) {
                    try {
                        String destinationPath = t.toString().replaceAll(sourceParentFolder.toString(), destinationParentFolder.toString());
                        Files.copy(t, Paths.get(destinationPath));
                    } 
                    catch(FileAlreadyExistsException e){
                        //TODO do acc to business needs
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            };
            allFilesPathStream.forEach(action );

        } catch(FileAlreadyExistsException e) {
            //file already exists and unable to copy
        } catch (IOException e) {
            //permission issue
            e.printStackTrace();
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
}
