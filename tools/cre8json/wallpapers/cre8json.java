package cre8json;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import javax.imageio.ImageIO;

public class cre8json {

	private static String jsonFileName = "wallpapers.json",
			authorName = "Jahir Fiquitiva", linkPrefix = "http://www.jahirfiquitiva.net/wallpapers/",
			copyright = "Copyright © 2016 Jahir Fiquitiva. All rights reserved.";

	private static boolean withJPGFiles = false;

	private static File[] images;
	private static String dir;
	private static StringBuilder json;

	public static void main(String[] args) {

		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);

		System.out.println("\n\nType the name of the json file you want to get");
		jsonFileName = scan.nextLine() + ".json";

		System.out.println(
				"\nType the name of the author or designer or creator of all the wallpapers.\n(If some wallpapers are created by someone else, you will have to modify their info manually).");
		authorName = scan.nextLine();

		System.out.println(
				"\nType the link prefix for the wallpapers.\n(For example: \'http://www.jahirfiquitiva.net/wallpapers/\').");
		linkPrefix = scan.nextLine();

		System.out.println(
				"\nType the copyright notice for the wallpapers.\n(For example: \'Copyright © 2016 Jahir Fiquitiva. All rights reserved.\').");
		copyright = scan.nextLine();

		boolean correcto = false;

		while (!correcto) {
			System.out.println("\nDo you have JPG or JPEG files in the folders?\nType \'Y\' for yes, or \'N\' for no.");
			String ans = scan.nextLine();
			ans = ans.toLowerCase();

			if (ans.equals("y")) {
				correcto = true;
				withJPGFiles = true;
			} else if (ans.equals("n")) {
				correcto = true;
				withJPGFiles = false;
			} else {
				System.out.println("\nJust type \'Y\' for yes, or \'N\' for no.");
			}
		}

		dir = new File(WallpapersJSON.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();

		dir = dir.replace("%20", " ");

		System.out.println("\nDirectory: " + dir + "\nScanning...");

		images = scan4Images(dir);

		writeJSON(dir);
		saveFiles();
		System.out.print("\nFinished!\n\n");

	}

	private static File[] scan4Images(String folder) {
		File directory = new File(folder);

		return directory.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String filename) {
				boolean acceptable = false;
				if (withJPGFiles) {
					if (filename.endsWith(".png") || filename.endsWith(".PNG") || filename.endsWith(".jpg")
							|| (filename.endsWith(".JPG") || filename.endsWith(".jpeg"))
							|| filename.endsWith(".JPEG")) {
						acceptable = true;
					} else {
						acceptable = false;
					}
				} else {
					if (filename.endsWith(".png") || filename.endsWith(".PNG")) {
						acceptable = true;
					} else {
						acceptable = false;

					}

				}
				return acceptable;
			}
		});
	}

	private static void writeJSON(String folder) {
		json = new StringBuilder();

		System.out.println("\nScanning images in directory: " + folder + " ...");
		if (images.length > 0) {
			System.out.println("Found " + images.length + " images");
			System.out.println("Writing wallpapers data...");
			writeJSONContent(json, folder);
		} else {
			System.out.print("Could not find any .png image files. :(\n");
		}

	}

	private static void writeJSONContent(StringBuilder json, String folder) {

		json.append("{\n");
		json.append("\t\"wallpapers\": [\n");
		for (int i = 0; i < images.length; i++) {

			BufferedImage bimg;
			int width = 0;
			int height = 0;
			try {
				bimg = ImageIO.read(images[i]);
				width = bimg.getWidth();
				height = bimg.getHeight();
			} catch (IOException e) {
				e.printStackTrace();
			}

			json.append("\t\t{\n");
			if (withJPGFiles) {
				json.append("\t\t\t\"name\": \"" + images[i].getName().replace(".png", "").replace(".PNG", "")
						.replace(".jpg", "").replace(".JPG", "").replace(".jpeg", "").replace(".JPEG", "") + "\",\n");
			} else {
				json.append("\t\t\t\"name\": \"" + images[i].getName().replace(".png", "").replace(".PNG", "") + "\",\n");
			}
			json.append("\t\t\t\"author\": \"" + authorName + "\",\n");
			json.append("\t\t\t\"url\": \"" + linkPrefix + images[i].getName() + "\",\n");
			// json.append(" \"url\": \"" + linkPrefix + folder.replaceAll(" ",
			// "") + "/" + images[i].getName() + "\",\n");
			json.append("\t\t\t\"dimensions\": \"" + String.valueOf(width) + " x " + String.valueOf(height) + "\",\n");
			json.append("\t\t\t\"copyright\": \"" + copyright + "\"\n");
			if (i == images.length - 1) {
				json.append("\t\t}\n");
			} else {
				json.append("\t\t},\n");
			}
		}
		json.append("\t]\n");
		
		json.append("}");

		images = null;

	}

	private static void saveFiles() {
		try {
			File jsonFile = new File(dir, jsonFileName);
			OutputStreamWriter f = new OutputStreamWriter(new FileOutputStream(jsonFile), "UTF-8");

			try {
				f.write(json.toString());
			} finally {
				f.close();
			}
		} catch (Exception e) {
			System.out.println("Oops, an error occured while writing json file");
		}
	}

	final class WallpapersJSON implements FilenameFilter {
		public boolean accept(File dir, String filename) {
			boolean acceptable = false;
			if (withJPGFiles) {
				if (filename.endsWith(".png") || filename.endsWith(".PNG") || filename.endsWith(".jpg")
						|| (filename.endsWith(".JPG") || filename.endsWith(".jpeg")) || filename.endsWith(".JPEG")) {
					acceptable = true;
				} else {
					acceptable = false;
				}
			} else {
				if (filename.endsWith(".png") || filename.endsWith(".PNG")) {
					acceptable = true;
				} else {
					acceptable = false;

				}

			}
			return acceptable;
		}
	}

}