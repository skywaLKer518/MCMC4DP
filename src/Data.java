public class Data{
	static public int dataNumber = 18;
	double y[];
	Data(){
		y = new double[dataNumber];
		//setting 1, as in the paper
//		y[0] = -1.48;
//		y[1] = -1.40;
//		y[2] = -1.16;
//		y[3] = -1.08;
//		y[4] = -1.02;
//		y[5] = +0.14;
//		y[6] = +0.51;
//		y[7] = +0.53;
//		y[8] = +0.78;
		
		//setting 2, y[5] -> 3.5
		// Problem: at the beginning, the posterior probability is too small so that sum goes to zero!! TODO
//		y[0] = -1.48;
//		y[1] = -1.40;
//		y[2] = -1.16;
//		y[3] = -1.08;
//		y[4] = -1.02;
//		y[5] = 3.5;
//		y[6] = +0.51;
//		y[7] = +0.53;
//		y[8] = +0.78;
//		
		//setting 3, y[5] ->3.5 y[4] -> 3.4
		y[0] = -1.48;
		y[1] = -1.40;
		y[2] = -1.16;
		y[3] = -1.08;
		y[4] = 3.4;
		y[5] = 3.5;
		y[6] = +0.51;
		y[7] = +0.53;
		y[8] = +0.78;
		
		//setting 4 double setting 3
		y[0] = -1.48;
		y[1] = -1.40;
		y[2] = -1.16;
		y[3] = -1.08;
		y[4] = 3.4;
		y[5] = 3.5;
		y[6] = +0.51;
		y[7] = +0.53;
		y[8] = +0.78;
		y[9] = -1.48;
		y[10] = -1.40;
		y[11] = -1.16;
		y[12] = -1.08;
		y[13] = 3.4;
		y[14] = 3.5;
		y[15] = +0.51;
		y[16] = +0.53;
		y[17] = +0.78;
	}
	
//	public double next(int i){
//		if (i == 8){
//			return y[0];
//		}
//		else return y[i+1];
//	}
}