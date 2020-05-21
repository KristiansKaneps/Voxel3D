package lv.kaneps.voxel3d.client.engine.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class GraphicsUtils
{
	private GraphicsUtils() { }

	public static String loadResource(String fileName) throws Exception
	{
		System.out.println("Loading " + fileName);
		String result;
		try (InputStream in = asInputStream(fileName); Scanner scanner = new Scanner(in, StandardCharsets.UTF_8.name()))
		{
			result = scanner.useDelimiter("\\A").next();
		}
		return result;
	}

	public static String loadVS(String fileName) throws Exception
	{
		return loadResource("shaders/vertex/" + fileName);
	}

	public static String loadFS(String fileName) throws Exception
	{
		return loadResource("shaders/fragment/" + fileName);
	}

	public static String loadGS(String fileName) throws Exception
	{
		return loadResource("shaders/geometry/" + fileName);
	}

	public static List<String> readAllLines(String fileName) throws Exception
	{
		List<String> list = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(asInputStream(fileName))))
		{
			String line;
			while ((line = br.readLine()) != null)
				list.add(line);
		}
		return list;
	}

	public static InputStream asInputStream(String fileName)
	{
		return GraphicsUtils.class.getResourceAsStream(fileName.startsWith("/") ? fileName : "/" + fileName);
	}

	public static float[] listToArray(List<Float> list)
	{
		int size = list != null ? list.size() : 0;
		float[] floatArr = new float[size];
		for (int i = 0; i < size; i++)
			floatArr[i] = list.get(i);
		return floatArr;
	}
}
