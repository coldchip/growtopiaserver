package ru.ColdChip.GrowtopiaServer.World;

import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;

public class PutWorldData {
	public boolean updateTile(String name, int tile, int x, int y) throws IOException {
			try {
				System.out.println("OK");
				File file = new File(Paths.get("").toAbsolutePath().toString() + "/Lib/CoreData.txt");
				FileReader fileReader = new FileReader(file);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				StringBuffer stringBuffer = new StringBuffer();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					try {
						int id = Integer.parseInt(line.split("\\|")[0]);
						String type = line.split("\\|")[4];
						if((type.equals("Foreground_Block") || type.equals("Bedrock") || type.equals("Toggleable_Foreground")) && tile == id) {
							int index = ((100 * y) + x) * 8;

							RandomAccessFile f = new RandomAccessFile("Database/Worlds/" + name, "rw");

							f.seek(index);

							f.write(Short2Byte((short)tile), 0, 2);
							return true;
						}
						if(type.equals("Fist") && tile == 18) {

							int index = ((100 * y) + x) * 8;

							RandomAccessFile f = new RandomAccessFile("Database/Worlds/" + name, "rw");

							f.seek(index);

							f.write(Short2Byte((short)0), 0, 2);

							return true;
						}
					} catch(Exception e) {

					}
				}
				fileReader.close();
			} catch (IOException e) {

			} catch(Exception e) {

			} 
			return false;
			
	}
	private byte[] Short2Byte(short val) {
		byte[] ret = new byte[2];
		ret[0] = (byte)(val & 0xff);
		ret[1] = (byte)((val >> 8) & 0xff);
		return ret;
	}
}