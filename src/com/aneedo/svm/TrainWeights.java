package com.aneedo.svm;


public class TrainWeights {
/*
	public static void train(){
		//Training ******
		// s one class:2, t :kernel type, 2
		String[] args = new String[]{"-s", "2", "-t", "2", SVMConstants.SVM_TRAIN,
				SVMConstants.SVM_MODEL};
		// FileConstants.SVM_TRAIN - Where to pick training file
		// FileConstants.SVM_MODEL - Where to store model file
		try
		{
			System.out.println("hello");
			svm_train train = new svm_train();
			
			train.run(args);
		}
		catch(Exception e){
			System.out.println("In catch");
			e.printStackTrace();
		}
	}

	public static void predict(){


		//	Here it writes it to a file but we can return label. Need to look at
		//svm_predict.predict ()
		try{
			BufferedReader input = new BufferedReader(new
					FileReader(SVMConstants.SVM_TRAIN));
			DataOutputStream output = new DataOutputStream(new
					BufferedOutputStream(new
							FileOutputStream(SVMConstants.SVM_PREDICT)));
			svm_model model = svm.svm_load_model(SVMConstants.SVM_MODEL);
			svm_predict.predict(input,output,model,0);
			input.close();
			output.close();
		}
		catch(Exception e){

		}

	}
	
	public static void main(String[] args) {
//		TrainWeights.train();
		TrainWeights.predict();
	}
*/}
