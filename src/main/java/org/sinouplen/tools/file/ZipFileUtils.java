package org.sinouplen.tools.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 * @author Sinouplen
 * 
 */
public final class ZipFileUtils {

	private final static Logger LOGGER = Logger.getLogger(ZipFileUtils.class);

	private final static String ERROR_MESSAGE_SRC_ZIP_FILE_DOESNT_EXISTS = "The source zip file doesn't exists";

	/**
	 * Hide Utility Class Constructor
	 */
	private ZipFileUtils() {

	}

	public static void extract(File srcZipfile, File destinationDir)
			throws ZipException, IOException {

		if (!srcZipfile.exists()) {
			String errorMessage = ERROR_MESSAGE_SRC_ZIP_FILE_DOESNT_EXISTS
					+ " : " + srcZipfile;
			LOGGER.error(errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}

		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(srcZipfile);

			Enumeration<? extends ZipEntry> enumZipFile = zipFile.entries();
			ZipEntry entry = null;

			while (enumZipFile.hasMoreElements()) {
				entry = enumZipFile.nextElement();

				FileUtils.forceMkdir(destinationDir);

				if (!entry.isDirectory()) {

					String outFilename = entry.getName();
					File destinationFile = new File(destinationDir, outFilename);

					FileUtils.copyInputStreamToFile(
							zipFile.getInputStream(entry), destinationFile);
				}
			}

			LOGGER.info("Zip file extracted successfully...");
		} catch (ZipException e) {
			LOGGER.error("Error during extrat the zip file : " + srcZipfile, e);
			throw e;
		} catch (IOException e) {
			LOGGER.error("Error during extrat the zip file : " + srcZipfile, e);
			throw e;
		} finally {
			if (zipFile != null) {
				try {
					zipFile.close();
				} catch (IOException e) {
					throw e;
				}
			}
		}
	}

	public static void compress(File src, File destinationZipFile)
			throws IOException {

		if (src.isDirectory()) {
			compressDirectory(src, destinationZipFile);
		} else if (src.isFile()) {
			compressFile(src, destinationZipFile);
		}

	}

	public static void compressDirectory(File srcDir, File destinationZipFile)
			throws IOException {

		if (!srcDir.exists()) {
			String errorMessage = srcDir.getAbsolutePath() + " doesn't exists";
			LOGGER.error(errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}

		if (!srcDir.isDirectory()) {
			String errorMessage = srcDir.getAbsolutePath()
					+ " is not a directory";
			LOGGER.error(errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}

		compressAllFiles(srcDir.listFiles(), destinationZipFile);
	}

	public static void compressFile(File srcFile, File destinationZipFile)
			throws IOException {

		if (!srcFile.exists()) {
			String errorMessage = srcFile.getAbsolutePath() + " doesn't exists";
			LOGGER.error(errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}

		if (!srcFile.isFile()) {
			String errorMessage = srcFile.getAbsolutePath() + " is not a file";
			LOGGER.error(errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}

		File[] file = new File[1];
		file[0] = srcFile;

		compressAllFiles(file, destinationZipFile);
	}

	/**
	 * @param srcDir
	 * @param destinationZipFile
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void compressAllFiles(File[] listFile,
			File destinationZipFile) throws FileNotFoundException, IOException {
		ZipOutputStream zipOutPutStream = null;

		File destinationZipFileLock = new File(
				destinationZipFile.getAbsolutePath() + ".lock");

		try {
			zipOutPutStream = new ZipOutputStream(new FileOutputStream(
					destinationZipFileLock));

			for (File currentFileTozip : listFile) {
				if (currentFileTozip.isDirectory()) {
					continue;
				}

				int bytesRead;

				FileInputStream fileInputStream = null;
				try {
					fileInputStream = new FileInputStream(currentFileTozip);
					ZipEntry zipEntry = new ZipEntry(currentFileTozip.getName());
					zipOutPutStream.putNextEntry(zipEntry);
					while ((bytesRead = fileInputStream.read()) != -1) {
						zipOutPutStream.write(bytesRead);
					}
				} catch (FileNotFoundException e) {
					LOGGER.error("Not possible to find this file : "
							+ currentFileTozip.getAbsolutePath(), e);
					throw e;
				} catch (IOException e) {
					LOGGER.error("Error during to zip file : "
							+ currentFileTozip.getAbsolutePath(), e);
					throw e;
				} finally {

					if (fileInputStream != null) {
						try {
							fileInputStream.close();
						} catch (IOException e) {
							LOGGER.error(
									"Impossible to close this file input stream : "
											+ currentFileTozip
													.getAbsolutePath(), e);
							throw e;
						}
					}
				}
			}
		} finally {
			if (zipOutPutStream != null) {
				try {
					zipOutPutStream.close();
				} catch (IOException e) {
					LOGGER.error(
							"Impossible to close this zip output stream : "
									+ destinationZipFileLock.getAbsolutePath(),
							e);
					throw e;
				}
			}
		}

		if (destinationZipFile.exists()) {
			FileUtils.forceDelete(destinationZipFile);
		}

		FileUtils.moveFile(destinationZipFileLock, destinationZipFile);
	}

}
