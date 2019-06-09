/**
 * 
 */
package com.diageo.mras.webservices.init;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * This class is used for reading the property file.
 * 
 * @author Infosys.
 * 
 */
public class PropertyReader {

	private static Properties properties = new Properties();
	static {
		try {
			loadPropertyFile("Mras.properties");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static final Logger logger = Logger.getLogger(PropertyReader.class
			.getName());

	/**
	 * This method loads all the property files.The property files needs to be
	 * in classpath.
	 * 
	 * @param fileNames
	 *            List of property file that needs to be loaded.
	 * @throws IOException
	 *             IOException
	 */
	public static void loadPropertyFile(List<String> fileNames)
			throws IOException {

		try {
			properties = new Properties();

			// iterating over the file names and loading each property file
			// name..
			if (null != fileNames && !fileNames.isEmpty()) {
				for (String fileName : fileNames) {

					// get the classloader and load the property files one by
					// one.
					ClassLoader loader = Thread.currentThread()
							.getContextClassLoader();
					InputStream in = loader.getResourceAsStream(fileName);
					properties.load(loader.getResourceAsStream(fileName));
					in.close();
				}
			}
		} catch (IOException e) {
			logger.error("Error while loading Property file.", e);
			throw e;
		}
	}

	public static void loadPropertyFile(String fileName) throws IOException {

		try {

			// iterating over the file names and loading each property file
			// name..
			if (null != fileName && !fileName.isEmpty()) {

				// get the classloader and load the property files one by one.
				ClassLoader loader = Thread.currentThread()
						.getContextClassLoader();
				InputStream in = loader.getResourceAsStream(fileName);
				properties.load(loader.getResourceAsStream(fileName));
				in.close();

			}
		} catch (IOException e) {
			logger.error("Error while loading Property file.", e);
			throw e;
		}
	}

	/**
	 * Get the property value using property name.
	 * 
	 * @param propertyName
	 *            String.
	 * @return propertyValue.
	 */
	public static String getPropertyValue(String propertyName) {
		String propertyValue = null;
		if (null != propertyName) {
			propertyValue = properties.getProperty(propertyName);
		}
		return propertyValue;
	}

}
