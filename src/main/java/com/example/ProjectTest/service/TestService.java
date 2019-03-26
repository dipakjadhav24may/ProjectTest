package com.example.ProjectTest.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.ProjectTest.entity.HomeWork;
import com.example.ProjectTest.entity.Student;
import com.example.ProjectTest.util.HeaderRequestInterceptor;
;


@Component
public class TestService {
	
	public String testService() {
		return "Hello world";
	}

	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";

	// CSV file header
	private static final String FILE_HEADER = "id,firstName,lastName,gender,age";

	public static void writeCsvFile(String fileName) {

		// Create new students objects
		Student student1 = new Student(1, "Ahmed", "Mohamed", "M", 25);
		Student student2 = new Student(2, "Sara", "Said", "F", 23);
		Student student3 = new Student(3, "Ali", "Hassan", "M", 24);
		Student student4 = new Student(4, "Sama", "Karim", "F", 20);
		Student student5 = new Student(5, "Khaled", "Mohamed", "M", 22);
		Student student6 = new Student(6, "Ghada", "Sarhan", "F", 21);

		// Create a new list of student objects
		List<Student> students = new ArrayList<Student>();
		students.add(student1);
		students.add(student2);
		students.add(student3);
		students.add(student4);
		students.add(student5);
		students.add(student6);

		FileWriter fileWriter = null;

		try {
			fileWriter = new FileWriter(fileName);

			// Write the CSV file header
			fileWriter.append(FILE_HEADER.toString());

			// Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);

			// Write a new student object list to the CSV file
			for (Student student : students) {
				fileWriter.append(String.valueOf(student.getId()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(student.getFirstName());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(student.getLastName());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(student.getGender());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(student.getAge()));
				fileWriter.append(NEW_LINE_SEPARATOR);
			}

			System.out.println("CSV file was created successfully !!!");

		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {

			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
				e.printStackTrace();
			}

		}
	}
	
	private static final String FILE_HEADER_Home = "BatchId,ClassId,SubjectId,Folder,Name,Topic";

	
	
	public static void writeCsvFileHomeWork(String fileName) {

		FileWriter fileWriter = null;

		try {
			fileWriter = new FileWriter(fileName);

			// Write the CSV file header
			fileWriter.append(FILE_HEADER_Home.toString());

			// Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);

			// Write a new student object list to the CSV file
			for (int i=1;i<=100;i++) {
				HomeWork homeWork=new HomeWork();
				homeWork.setBatch_id(i);
				homeWork.setClass_id(i);
				homeWork.setSubject_id(i);				
				homeWork.setFolder_name("Folder "+i);
				homeWork.setHomework_topic("Topic "+i);
				homeWork.setHomework_name("Name "+i);
				homeWork.setEnd_date(new Date());
				homeWork.setStart_date(new Date());				
				
				
				fileWriter.append(String.valueOf(homeWork.getBatch_id()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(homeWork.getClass_id()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(homeWork.getSubject_id()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(homeWork.getFolder_name());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(homeWork.getHomework_name());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(homeWork.getHomework_topic());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(NEW_LINE_SEPARATOR);
			}

			System.out.println("CSV file was created successfully !!!");

		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {

			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
				e.printStackTrace();
			}

		}
	}

	// Student attributes index
	private static final int STUDENT_ID_IDX = 0;
	private static final int STUDENT_FNAME_IDX = 1;
	private static final int STUDENT_LNAME_IDX = 2;
	private static final int STUDENT_GENDER = 3;
	private static final int STUDENT_AGE = 4;

	public static void readCsvFile(String fileName) {

		BufferedReader fileReader = null;

		try {

			// Create a new list of student to be filled by CSV file data
			List<Student> students = new ArrayList<Student>();

			String line = "";

			// Create the file reader
			fileReader = new BufferedReader(new FileReader(fileName));

			// Read the CSV file header to skip it
			fileReader.readLine();

			// Read the file line by line starting from the second line
			while ((line = fileReader.readLine()) != null) {
				// Get all tokens available in line
				String[] tokens = line.split(COMMA_DELIMITER);
				if (tokens.length > 0) {
					// Create a new student object and fill his data
					Student student = new Student(Long.parseLong(tokens[STUDENT_ID_IDX]), tokens[STUDENT_FNAME_IDX],
							tokens[STUDENT_LNAME_IDX], tokens[STUDENT_GENDER], Integer.parseInt(tokens[STUDENT_AGE]));
					students.add(student);
				}
			}

			// Print the new student list
			for (Student student : students) {
				System.out.println(student.toString());
			}
		} catch (Exception e) {
			System.out.println("Error in CsvFileReader !!!");
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
			} catch (IOException e) {
				System.out.println("Error while closing fileReader !!!");
				e.printStackTrace();
			}
		}

	}

	public static void readXLSFile(String fileName) {
		try {
//			FileInputStream file = new FileInputStream(new File("/opt/howtodoinjava_demo.xlsx"));
//
//			// Create Workbook instance holding reference to .xlsx file
//			XSSFWorkbook workbook = new XSSFWorkbook(file);
//
//			// Get first/desired sheet from the workbook
//			XSSFSheet sheet = workbook.getSheetAt(0);
//
//			// Iterate through each rows one by one
//			Iterator<Row> rowIterator = sheet.iterator();
//			while (rowIterator.hasNext()) {
//				Row row = rowIterator.next();
//				// For each row, iterate through all the columns
//				Iterator<Cell> cellIterator = row.cellIterator();
//
//				while (cellIterator.hasNext()) {
//					Cell cell = cellIterator.next();
//					// Check the cell type and format accordingly
//					switch (cell.getCellType()) {
//					case Cell.CELL_TYPE_NUMERIC:
//						System.out.print(cell.getNumericCellValue() + "t");
//						break;
//					case Cell.CELL_TYPE_STRING:
//						System.out.print(cell.getStringCellValue() + "t");
//						break;
//					}
//				}
//				System.out.println("");
	//		}
	//		file.close();
			
			Workbook workbook = WorkbookFactory.create(new File(fileName));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeXLSFile() {
		// Blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();

		// Create a blank sheet
		XSSFSheet sheet = workbook.createSheet("Employee Data");

		// This data needs to be written (Object[])
		Map<String, Object[]> data = new TreeMap<String, Object[]>();
		data.put("1", new Object[] { "ID", "NAME", "LASTNAME" });
		data.put("2", new Object[] { 1, "Amit", "Shukla" });
		data.put("3", new Object[] { 2, "Lokesh", "Gupta" });
		data.put("4", new Object[] { 3, "John", "Adwards" });
		data.put("5", new Object[] { 4, "Brian", "Schultz" });

		// Iterate over data and write to sheet
		Set<String> keyset = data.keySet();
		int rownum = 0;
		for (String key : keyset) {
			Row row = sheet.createRow(rownum++);
			Object[] objArr = data.get(key);
			int cellnum = 0;
			for (Object obj : objArr) {
				Cell cell = row.createCell(cellnum++);
				if (obj instanceof String)
					cell.setCellValue((String) obj);
				else if (obj instanceof Integer)
					cell.setCellValue((Integer) obj);
			}
		}
		try {
			// Write the workbook in file system
			FileOutputStream out = new FileOutputStream(new File("/opt/howtodoinjava_demo.xlsx"));
			workbook.write(out);
			out.close();
			System.out.println("howtodoinjava_demo.xlsx written successfully on disk.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private static final String FIREBASE_SERVER_KEY = "AAAAI28FKxE:APA91bEzCJkAJhh56cXOz4D-2KLtvTh8GBieseoMoKQ_lJnd-1voMy1e85UQFr4BTtAp5rrO276KtnyV359XC2iRPr2yD1_2OfhVuqwhwds0R1wNvi7X6THNj73yacInVqXOG-1RBBEX";
	private static final String FIREBASE_API_URL = "https://fcm.googleapis.com/fcm/send";

	public CompletableFuture<String> send(HttpEntity<String> entity) {

		RestTemplate restTemplate = new RestTemplate();

		ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + FIREBASE_SERVER_KEY));
		interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
		restTemplate.setInterceptors(interceptors);

		String firebaseResponse = restTemplate.postForObject(FIREBASE_API_URL, entity, String.class);

		return CompletableFuture.completedFuture(firebaseResponse);
	}
	
	
	public String httpPostRequest(String key, String payload) {

		URL url;
		StringBuffer result = new StringBuffer();
		HttpURLConnection httpURLConnection = null;

		try {
			url = new URL("https://beta.livehealth.solutions/labLogin/");

			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestProperty("Authorization","bearer "+key);
			httpURLConnection.setRequestProperty("Content-Type", "application/json");
			httpURLConnection.setRequestProperty("Accept", "application/json");
			httpURLConnection.setRequestMethod("POST");

			if (payload != null) {
				// set the content length of the body
				httpURLConnection.setRequestProperty("Content-length", payload.getBytes().length + "");
				httpURLConnection.setDoInput(true);
				httpURLConnection.setDoOutput(true);
				httpURLConnection.setUseCaches(false);

				// send the json as body of the request
				OutputStream outputStream = httpURLConnection.getOutputStream();
				outputStream.write(payload.getBytes("UTF-8"));
				outputStream.close();
			}

			// Connect to the server
			httpURLConnection.connect();

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));

			String inputReader;

			while ((inputReader = reader.readLine()) != null) {

				result.append(inputReader);

			}
		} catch (Exception e) {

			e.printStackTrace();

		}

		return result.toString();
	}
	

}
