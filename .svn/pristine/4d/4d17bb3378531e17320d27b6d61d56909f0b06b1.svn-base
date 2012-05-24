package com.aneedo.search.ranking.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import edu.duke.cs.smlr.learn.properties.KFoldProperty;
import edu.duke.cs.smlr.learn.properties.LearnProperty;
import edu.duke.cs.smlr.learn.properties.ModifyProperty;
import edu.duke.cs.smlr.learn.properties.TestProperty;
import edu.duke.cs.smlr.util.ArgumentFormatException;
import edu.duke.cs.smlr.util.Constants;
import edu.duke.cs.smlr.util.Core;
import edu.duke.cs.smlr.util.Messages;
import edu.duke.cs.smlr.util.OutputController;

public class LogisticRegressionUtil {
	private static LogisticRegressionUtil instance = null;
	private LogisticRegressionUtil() {
		
	}
	
	public static LogisticRegressionUtil getInstance() {
		if(instance == null) {
			instance = new LogisticRegressionUtil();
		}
		return instance;
	}
	
	public static void main(String[] args) {
		LogisticRegressionUtil lrParamLearn = LogisticRegressionUtil.getInstance();
		lrParamLearn.train();
	}
	private static void doSettings(String filePath, HashSet mainOptionsSet,
			ArrayList mainOptions) {
		ArrayList al = new ArrayList();
		try {
		    BufferedReader input = new BufferedReader(new FileReader(filePath));		    
			String s = "";
			while (s != null) {
				s = input.readLine();
				if (s != null)
					al.add(s);
			}
		} catch (FileNotFoundException e) {
			throw new ArgumentFormatException(Messages.ERROR_FILE_NOT_FOUND);
		} catch (IOException e) {
			throw new ArgumentFormatException(Messages.ERROR_READING_FILE);
		}
		int count = al.size();
		String[] sArray = new String[count];
		for (int i = 0; i < count; i++)
			sArray[i] = (String) al.get(i);
		Core.doSettings(sArray, mainOptionsSet, mainOptions);
	}
	
	public void train() {

		
		HashSet optionsSet = new HashSet();
		optionsSet.add(LRConstants.LR_TRAIN_OPTION);
		optionsSet.add(LRConstants.LR_OUTPUT_OPTION);
		optionsSet.add(LRConstants.LR_DISPLAY_WEIGHT);
//		optionsSet.add(LRConstants.LR_DELETE_LABEL_COLUMN);
		//optionsSet.add(LRConstants.LR_KERNEL_LABEL);
//		optionsSet.add(LRConstants.LR_BIAS);
		
		ArrayList options = new ArrayList();
		options.add(LRConstants.LR_SUMMARY_FOLDER);
		
		options.add(LRConstants.LR_TRAIN_OPTION);
		options.add(LRConstants.TRAIN_FILE);
		
		options.add(LRConstants.LR_OUTPUT_OPTION);
		options.add(LRConstants.MODEL_FOLDER);
		
		options.add(LRConstants.LR_DISPLAY_WEIGHT);
		options.add("true");
		
//		options.add(LRConstants.LR_KERNEL_LABEL);
//		options.add(LRConstants.LR_KERNEL);
		
//		options.add(LRConstants.LR_BIAS);
//		options.add("false");
		
//		options.add(LRConstants.LR_DELETE_LABEL_COLUMN);
//		options.add("0");
//		options.add("-");

		ModifyProperty modifyprop[] = new ModifyProperty[3]; for (int i = 0; i < 3; i++) modifyprop[i] = new ModifyProperty();
		LearnProperty learnprop = new LearnProperty();
		
		TestProperty testprop = new TestProperty();
		KFoldProperty kfoldprop = new KFoldProperty();
		int[] testoption = new int[1];
		int[] columnChoice = new int[1];
		OutputController output = new OutputController();
		
		try {
			if (optionsSet.contains("-settings")) {
				doSettings(Core.parseFileName(options, options.indexOf("-settings") + 1), optionsSet, options);
			} else {
				testoption[0] = (Core.setDefaults(modifyprop, learnprop,
						testprop, kfoldprop, output, columnChoice));
				String outFile = Core.parseArgs(optionsSet, options, modifyprop, learnprop, testprop,
						kfoldprop, output, testoption, columnChoice);
				learnprop.setFileName(LRConstants.TRAIN_FILE);
				learnprop.setMethod(Constants.ALGORITHM_NON_COMPONENT); 
				
				System.out.println("Ouptut file : " + outFile);
				if (outFile == null) {
					outFile = LRConstants.MODEL_FILE;
				}
				Core.process(modifyprop, learnprop, testprop, kfoldprop, Constants.TEST_LOOCV, output, columnChoice[0], outFile, "", "",LRConstants.MODEL_FILE);
			}
		} catch (ArgumentFormatException e) {
			if (!e.getMessage().equals("HELP"))
				System.out.println(e.getMessage()
						+ "\nUse -help to view parameter options\n");
			else {
				
			}
		}
	}
	

}

