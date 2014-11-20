package com.example.uploadfiletoserverwithlocation;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.util.Log;

public class MyHttpConntection {
	private static String delimiter = "--";
	private static String boundary = "SwA"
			+ Long.toString(System.currentTimeMillis()) + "SwA";
	private static OutputStream os;
	private static FileInputStream fileInputStream;
	static int bytesRead, bytesAvailable, bufferSize;
	static byte[] buffer;
	static int maxBufferSize = 1 * 1024 * 1024;

	public static String uploadFile(String path) {
		HttpURLConnection connection = null;
		DataOutputStream outputStream = null;
		String pathToOurFile = path;
		String urlServer = "http://54.173.51.136/db/getMP3File.php";
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		try {
			FileInputStream fileInputStream = new FileInputStream(new File(
					pathToOurFile));
			URL url = new URL(urlServer);
			connection = (HttpURLConnection) url.openConnection();
			// Allow Inputs & Outputs
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			// Enable POST method
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream
					.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
							+ pathToOurFile + "\"" + lineEnd);
			outputStream.writeBytes(lineEnd);
			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			while (bytesRead > 0) {
				outputStream.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}
			outputStream.writeBytes(lineEnd);
			outputStream.writeBytes(twoHyphens + boundary + twoHyphens
					+ lineEnd);
			int serverResponseCode = connection.getResponseCode();
			Log.d("getResponseCode", serverResponseCode + "");
			String serverResponseMessage = connection.getResponseMessage();
			Log.d("serverReponseMessage", serverResponseMessage + "");
			InputStream in = connection.getInputStream();
			InputStreamReader reader = new InputStreamReader(in);
			BufferedReader bufferedReader = new BufferedReader(reader);
			StringBuilder builder = new StringBuilder();
			String line = bufferedReader.readLine();
			while (line != null) {
				builder.append(line);
				line = bufferedReader.readLine();
			}
			Log.d("builder", builder.toString());
			fileInputStream.close();
			outputStream.flush();
			outputStream.close();
			return builder.toString();
		} catch (Exception ex) {
			return null;
		}
	}

	public static String UploadViaHttpClient(String path) {
		File f = new File(path);
		// PostMethod filePost = new PostMethod("http://host/some_path");
		return path;

	}

	public static String UploadFileAndParams(UploadParams params) {

		HttpURLConnection con = null;
		try {
			con = (HttpURLConnection) (new URL(params.getURI()))
					.openConnection();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			con.setRequestMethod("POST");
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Content-Type", "multipart/form-data; boundary="
				+ boundary);
		try {
			con.connect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			os = con.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			addFormPart("lat", params.getParams().get("lat"));
			addFormPart("lng", params.getParams().get("lng"));
			addFormPart("rad", params.getParams().get("rad"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File file = new File(params.getParams().get("path"));
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(new File(params.getParams()
					.get("path")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		try {
			addFilePart("mp3File", params.getParams().get("path"),
					fileInputStream);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fileInputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Log.i("response from prams and file", con.getResponseMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputStream in = null;
		try {
			in = con.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputStreamReader reader = new InputStreamReader(in);
		BufferedReader bufferedReader = new BufferedReader(reader);
		StringBuilder builder = new StringBuilder();
		String line = null;
		try {
			line = bufferedReader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (line != null) {
			builder.append(line);
			try {
				line = bufferedReader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Log.d("builder in params and file", builder.toString());
		return "";

	}

	public static void addFormPart(String paramName, String value)
			throws Exception {
		writeParamData(paramName, value);
	}

	private static void writeParamData(String paramName, String value)
			throws Exception {
		os.write((delimiter + boundary + "\r\n").getBytes());
		os.write("Content-Type: text/plain\r\n".getBytes());
		os.write(("Content-Disposition: form-data; name=\"" + paramName + "\"\r\n")
				.getBytes());
		os.write(("\r\n" + value + "\r\n").getBytes());

	}

	public static void addFilePart(String paramName, String fileName,
			FileInputStream input) throws Exception {
		os.write((delimiter + boundary + "\r\n").getBytes());
		os.write(("Content-Disposition: form-data; name=\"" + paramName
				+ "\"; filename=\"" + fileName + "\"\r\n").getBytes());
		os.write(("Content-Type: application/octet-stream\r\n").getBytes());
		os.write(("Content-Transfer-Encoding: binary\r\n").getBytes());
		os.write("\r\n".getBytes());
		bytesAvailable = input.available();
		bufferSize = Math.min(bytesAvailable, maxBufferSize);
		buffer = new byte[bufferSize];
		bytesRead = input.read(buffer, 0, bufferSize);
		while (bytesRead > 0) {
			os.write(buffer, 0, bufferSize);
			bytesAvailable = input.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			bytesRead = input.read(buffer, 0, bufferSize);
		}
		os.write("\r\n".getBytes());
	}

}
