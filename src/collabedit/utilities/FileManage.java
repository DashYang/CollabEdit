package collabedit.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import collabedit.operation.Operation;
import collabedit.operation.OperationType;

public class FileManage {
	private static ArrayList<String> getOperationlines(String filename) {
		ArrayList<String> operationlines = new ArrayList<String>();
		File file = new File(filename);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                operationlines.add(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return operationlines;
	}
	
	private static Operation extractOperation(String line) {
		System.out.println(line);
		String[] part1 = line.split("\\[");
		String type = part1[0];
		String[] part2 = part1[1].split(",");
		String position = part2[0];
		OperationType ot = OperationType.valueOf(type);
		String character = "";
		if(ot == OperationType.INSERT)
			character = part2[1].substring(0,part2[1].length() - 1);
		return new Operation(ot , character , Integer.parseInt(position));
	}
	
	public static Queue<Operation> getOperations(String filename) {
		ArrayList<String> lines = getOperationlines(filename);
		Queue<Operation> ops = new LinkedList<Operation>();
		for(String line : lines) {
			Operation op = extractOperation(line);
			ops.add(op);
		}
		return ops;
	}
}
