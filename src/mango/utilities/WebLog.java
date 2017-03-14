package mango.utilities;

import java.io.File;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.util.Date;

public class WebLog {

	public static void log(String data) {
		try (PrintWriter out = new PrintWriter(new FileOutputStream(new File("mylog.txt"), true))) {
			out.println(new Date().toString() + ": " + data);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void truncate() {
		try (PrintWriter out = new PrintWriter(new FileOutputStream(new File("mylog.txt"), false))) {
			out.print("");
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
