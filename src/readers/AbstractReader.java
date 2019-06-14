package readers;

import java.io.IOException;
import java.io.RandomAccessFile;


public abstract class AbstractReader {
	

	RandomAccessFile file;
	
	
	public AbstractReader(String fileName) throws IOException {
		super();
		this.file = new RandomAccessFile(fileName, "r");
	}


	public abstract String getNextIpMask() ;


	public void closeFile() throws IOException {

		file.close();
	}
	
}
