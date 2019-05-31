package entities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MigraEntities {

	public MigraEntities() {
		super();
	}

	public List<String> listFoldersBetter(String strPath) throws IOException {
		@SuppressWarnings("resource")
		Stream<Path> walk = Files.walk(Paths.get(strPath));
		List<String> result = walk.filter(Files::isDirectory).map(x -> x.toString()).collect(Collectors.toList());
		return result;
	}

	public String[] GetStringArray(List<String> arr) {

		String str[] = new String[arr.size()];
		for (int j = 0; j < arr.size(); j++) {
			str[j] = arr.get(j);
		}
		return str;
	}

	@SuppressWarnings("resource")
	public void executeSqlScript(Connection conn, File inputFile) {

		// Delimiter
		String delimiter = ";";

		// Create scanner
		Scanner scanner;
		try {
			scanner = new Scanner(inputFile).useDelimiter(delimiter);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return;
		}

		// Loop through the SQL file statements
		Statement currentStatement = null;
		while (scanner.hasNext()) {

			// Get statement
			String rawStatement = scanner.next() + delimiter;

			try {
				// Execute statement
				if (!rawStatement.equals("\n\n;")) {
					currentStatement = conn.createStatement();
					currentStatement.execute(rawStatement);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				// Release resources
				if (currentStatement != null) {
					try {
						currentStatement.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				currentStatement = null;
			}
		}
	}

	public static double taxaIR(double dias) {

		double taxa = 0.0;

		if (dias <= 180) {
			taxa = 22.5;
		} else if (dias <= 360) {
			taxa = 20.0;
		} else if (dias <= 720) {
			taxa = 17.5;
		} else {
			taxa = 15.0;
		}
		return taxa;
	}

}
