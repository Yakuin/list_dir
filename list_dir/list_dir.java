package list_dir;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class list_dir
{
	private static int recursivelevel;
	private static boolean fullpath;
	private static int maxdepth;
	private static List<Integer> lastfiles;
	
	public static void errorMessage(int errorID)
	{
		if (errorID == 0)
			System.out.println("Unable to access directory or directory doesn't exist.");
		if (errorID == 1)
			System.out.println("Invalid arguments.");
		System.out.println("Usage: java -jar list_dir.jar directory [[R][F]]");
		System.exit(1);
	}

	public static void getDirList(File dir, List<Integer> lastfiles)
	{
		File[] dirlist;

		dirlist = dir.listFiles();
		printDirList(dirlist);
	}

	public static void printTree(int i, File[] dirlist)
	{
		for (int j = 0; j < recursivelevel; j++)
		{
			if (lastfiles.get(j) == 1)
				System.out.print("    ");
			else
				System.out.print("|   ");
		}
		if (i == dirlist.length - 1)
			System.out.print("\\-- ");
		else
			System.out.print("|-- ");
	}
	
	public static void printDirList(File[] dirlist)
	{
		int i;
		
		for (i = 0; i < dirlist.length; i++)
		{
			if (i == dirlist.length - 1 && recursivelevel >= 0)
				lastfiles.set(recursivelevel, 1);
			printTree(i, dirlist);

			if (fullpath)
				System.out.print(dirlist[i] + "\n");
			else
				System.out.print(dirlist[i].getName() + "\n");
			
			if (dirlist[i].isDirectory() && recursivelevel != -1)
			{
				recursivelevel++;
				if (recursivelevel > maxdepth)
				{
					maxdepth = recursivelevel;
					lastfiles.add(0);
				}
				getDirList(dirlist[i], lastfiles);
			}
		}
		if (recursivelevel >= 0)
			lastfiles.set(recursivelevel, 0);
		recursivelevel--;
	}

	public static boolean checkValidArgs(String arglist[])
	{
		int length;

		length = arglist.length;
		if (length > 2 || length < 1)
		{
			if (length == 0)
				errorMessage(-1);
			errorMessage(1);
		}
		if (length == 2)
		{
			if (arglist[1].length() <= 2)
			{
				if (arglist[1].contains("R") || arglist[1].contains("F"))
				{
					recursivelevel = arglist[1].contains("R") ? 0 : -1;
					fullpath = arglist[1].contains("F") ? true : false;
				}
				else
					errorMessage(1);
			}
		}
		return true;
	}

	public static void main(String argv[])
	{
		File dir;

		recursivelevel = -1;
		fullpath = false;
		maxdepth = 0;
		lastfiles = new ArrayList<Integer>();
		lastfiles.add(0);

		if (!checkValidArgs(argv))
			return ;
		dir = new File (argv[0]);
		if (!dir.exists())
			errorMessage(0);
		getDirList(dir, lastfiles);
	}
}