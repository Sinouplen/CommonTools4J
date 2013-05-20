/**
 * 
 */
package org.sinouplen.tools.thread;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.sinouplen.tools.logger.LoggerLevel;
import org.sinouplen.tools.stream.ShowInputStream;
import org.sinouplen.tools.thread.exception.GenericRunnableCMDException;

/**
 * @author Sinouplen
 * 
 */
public class GenericRunnableCMD implements Runnable {

	private static final Logger LOGGER = Logger
			.getLogger(GenericRunnableCMD.class);

	private String command;
	private String[] environnementParameters;
	private File executeDirectory;

	/**
	 * @param command
	 * @param environnementParameters
	 * @param executeDirectory
	 * @throws GenericRunnableCMDException
	 */
	public GenericRunnableCMD(String command, String[] environnementParameters,
			File executeDirectory) throws GenericRunnableCMDException {
		this.command = command;
		this.environnementParameters = environnementParameters.clone();
		this.executeDirectory = executeDirectory;

		if (this.command == null || this.command.isEmpty()) {
			throw new GenericRunnableCMDException(
					"The command parameters must be set");
		}

		if (this.executeDirectory == null || !this.executeDirectory.exists()
				|| !this.executeDirectory.isDirectory()) {
			throw new GenericRunnableCMDException(
					"The executeDirectory parameters must be set");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		String preparedCommandeMessage;
		if (this.command != null && !this.command.isEmpty()) {
			preparedCommandeMessage = this.command
					+ " into this executed directory " + this.executeDirectory
					+ " ";
			if (this.environnementParameters != null
					&& this.environnementParameters.length > 0) {
				preparedCommandeMessage = "with this environnement variable "
						+ Arrays.toString(this.environnementParameters);
			}
			try {
				LOGGER.debug("Executing commande ligne "
						+ preparedCommandeMessage + "...");

				Process process = Runtime.getRuntime().exec(this.command,
						this.environnementParameters, this.executeDirectory);

				ShowInputStream streamOutput = new ShowInputStream(
						process.getInputStream(), LoggerLevel.INFO);
				ShowInputStream streamError = new ShowInputStream(
						process.getErrorStream(), LoggerLevel.ERROR);

				new Thread(streamOutput).start();
				new Thread(streamError).start();

				process.waitFor();

				LOGGER.debug("Executed commande ligne "
						+ preparedCommandeMessage + ".");
			} catch (IOException e) {
				LOGGER.error("Impossible to execute this commande ligne "
						+ preparedCommandeMessage);
			} catch (InterruptedException e) {
				LOGGER.error(e);
			}
		}
	}
}
