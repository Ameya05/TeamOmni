package org.team.omni;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.sql.DataSource;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.ContentDisposition;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class OrchestrationEngineUtils {

	private OrchestrationEngineUtils() {
	}

	/**
	 * Create HikariCP Data Source from he hikariCP configuration properties
	 * 
	 * @param hikariCPConfiguration
	 * @return
	 */
	public static DataSource createDataSource(Properties hikariCPConfiguration) {
		return new HikariDataSource(new HikariConfig(hikariCPConfiguration));
	}

	/**
	 * 
	 * @param in
	 *            input stream from which data is obtained
	 * @param fileName
	 *            the file name of the output file containing the data
	 * @return File object containing the name of the file in which the data has
	 *         been saved
	 * @throws IOException
	 */
	public static File saveFile(InputStream in, String fileName) throws IOException {
		File file = new File(fileName);
		try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file)); BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));) {
			String output = null;
			while ((output = bufferedReader.readLine()) != null) {
				bufferedWriter.write(output + "\n");
				bufferedWriter.flush();
			}
		}
		return file;
	}

	/**
	 * 
	 * @param response
	 *            Response from which the data has to be saved from the details
	 *            obtained from Content-Disposition header
	 * @return the File object for which the data has been saved
	 * @throws IOException
	 */
	public static File saveFileFromResposne(Response response, String folder) throws IOException {
		InputStream in = (InputStream) response.getEntity();
		ContentDisposition contentDisposition = (ContentDisposition) response.getHeaders().getFirst("Content-Disposition");
		return saveFile(in, folder + "/" + contentDisposition.getFileName());
	}

}
