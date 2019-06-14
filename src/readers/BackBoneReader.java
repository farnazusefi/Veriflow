package readers;

import java.io.IOException;

public class BackBoneReader extends AbstractReader {

	public BackBoneReader(String fileName) throws IOException {
		super(fileName);
		file.readLine();

	}

	@Override
	public String getNextIpMask() {

		try {
			String currentLine;
			do {
				currentLine = file.readLine();
				if (currentLine == null)
					return null;
			} while (currentLine.startsWith(" "));
			String[] splitLine = currentLine.split("\\s+");
			return splitLine[0];
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
