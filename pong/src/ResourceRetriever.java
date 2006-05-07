import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Utility class that allows transparent reading of files from
 * the current working directory or from the classpath.
 * @author Pepijn Van Eeckhoudt
 * @author Christoph Schwering
 */
public class ResourceRetriever {
	public static URL getResource(final String filename) throws IOException {
		// Try to load resource from jar
		URL url = ResourceRetriever.class.getClassLoader().getResource(filename);
		// If not found in jar, then load from disk
		if (url == null) {
			return new URL("file", "localhost", filename);
		} else {
			return url;
		}
	}
	
	public static InputStream getResourceAsStream(String filename) throws IOException {
		// Try to load resource from jar
		InputStream stream = ResourceRetriever.class.getClassLoader().getResourceAsStream(filename);
		// If not found in jar, then load from disk
		if (stream == null) {
			return new FileInputStream(filename);
		} else {
			return stream;
		}
	}
}
