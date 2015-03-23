package serialize;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Deserialize {
	/**
	 * OAuth認証に必要な情報を保持したクラスをデシリアライズ
	 * 
	 * 
	 */
	public static OAuthConfiguration deserializationOAuthConfiguration() {
		OAuthConfiguration conf = null;
		System.out.println("DeserializationConsumer");
	    ObjectInputStream in = null;
	    try {
	    	in = new ObjectInputStream(
	    			new BufferedInputStream(ClassLoader.getSystemResourceAsStream("serialize/OAuth")));
	    	conf = (OAuthConfiguration)in.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	    return conf;
	}
}
