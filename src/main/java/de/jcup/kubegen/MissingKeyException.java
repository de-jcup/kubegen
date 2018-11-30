package de.jcup.kubegen;

import java.io.File;

public class MissingKeyException extends RuntimeException{

	private static final long serialVersionUID = -7558788044793985611L;

	public MissingKeyException(String key, File file) {
		super("Key:"+key+" is missing in "+file);
	}
	
	

}
