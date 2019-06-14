package readers;

import java.io.IOException;

public class RouteViewReader extends AbstractReader {

	public RouteViewReader(String fileName) throws IOException {
		super(fileName);
		file.readLine();

	}

	@Override
	public String getNextIpMask() {

		try {
			String currentLine = file.readLine();
			String[] splitLine = currentLine.split("\\|");
			if (!splitLine[1].equals("E"))
				return splitLine[7];

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
