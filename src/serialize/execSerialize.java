package serialize;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class execSerialize {
    
	public static void main(String[] args) {
		OAuthConfiguration consumer = new OAuthConfiguration(); 
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(
					new BufferedOutputStream(new FileOutputStream("src/serialize/OAuth")));
			out.writeObject(consumer);
			out.flush();
			System.out.println("execute");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}