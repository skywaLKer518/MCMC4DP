import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MCMC extends MarkovChain{
	private int state[]; // c, which indicates the category y belongs to. (1,2,3,4,...; 0 means no category)
	                     // also respond to y_i
	private int number[]; // record n_-i,c.  number[MaxCateNumber]
	private int cateNumber = 0; // number of category
	private int cateIndexMax = 0;
	
	private int Observed = 0;
	private int minN = 1;
//	private double prior[];
	private double theta[]; // parameters. for each c?
	
//	private List<Double> thetaAlive = new LinkedList<Double>(); // store all cate which is associated with at least one data sample
	private List<Integer> cateAlive = new LinkedList<Integer>(); // store all cate which is associated with at least one data sample
	
	MCMC(int stateN) {
		super(stateN);
		state = new int[stateN];
		for (int i = 0; i < stateN; i++){
			state[i] = 0;
		}
		number = new int[Environment.MaxCateNumber+1];
		for (int i = 0; i <= Environment.MaxCateNumber; i++){
			number[i] = 0;
		}
		cateNumber = 0;
		theta = new double[Environment.MaxCateNumber];
	}
	
	public void algorithm5(){
		for (int i = 0; i < Environment.Times; i ++){
			step1();
//			printDataStateTheta();
			step2();
		}
		return;
	}
	
	public void step1(){
		Data data = new Data();
		int c;
		double accept,r;
		for (int i = 0; i < stateNumber; i++){
			for (int j = 0; j < Environment.R; j++){
				c = drawC();
				if (state[i] == c)
					continue;
				if (number[c] == 0){ // if c* is not in {c1,c2,...,cn}, draw theta from G_0, for c*
					drawTheta(c);
				}
				// compute the acceptance probability
				double dis1 = Math.abs(theta[state[i]] - data.y[i]);
				double dis2 = Math.abs(theta[c] - data.y[i]);
				if ( dis2 < dis1 )
					accept = 1;
				else
					accept = ratio(dis1,dis2);
				r = Math.random();
				if (r <= accept){
					updateCate(i,c);
//					printDataStateTheta();
				}
				
			}
			if (Observed < Data.dataNumber)
				Observed ++;
		}
	}
	
	public void step2(){
		for (int i = 1; i <= cateIndexMax; i++){
			if (number[i]<=0)
				continue;		
			theta[i] = updateTheta(i);
		}
	}
	
	// wrong!!: use c to update state[i] and all other state with the same state
	// correct: use c to update state[i] with c
	private void updateCate(int i, int c){
		int previous = state[i];
		// case 0: state[i] == 0;
		//          change state[i] to c; if (number[c]>0), else cateNumber++ ;number[c]++; if (c > cateIndexMax) cateIndexMax = c; MinN++ until MinN not used;
		//          if cateAlive contains c , else cateAlive add c;
		if (previous == 0){				// state
			state[i] = c;
			if (number[c] <= 0){		// cateNumber,number[]
				cateNumber++;
				number[c]++;
			}
			else{
				number[c]++;
			}
			if (c > cateIndexMax)			// cateIndexMax
				cateIndexMax = c;
			if (number[minN]!=0){
				do{ // minN
					minN ++;
				}while (number[minN]!=0);
			}
			if (!cateAlive.contains(c)){  // cateAlive
				cateAlive.add(c);
			}
		}
		else {
			// case 1: state i exists, so does c
			//		    change all state with the same state[i] to be c;number[c] += number[i],number[i] = 0;cateNumber--;if (state[i] == cateIndexMax) 
			//          cateIndexMax -- until number[cateIndexMax - 1]>0; MinN: i < MinN then MinN = i;  if cateAlive contains state[i], remove it.
			// case 2: state i exists, but not c  : new category
			//			change all state with state[i] to c; number[c] = number[i],number[i] = 0;if (state[i] == cateIndexMax)cateIndexMax -- until number[cateIndexMax - 1]>0;
			//          MinN: if i < MinN then MinN = i; if cateAlive contains state[i], remove it. cateAlive adds c;
			
			if (number[c] > 0) {// case 1
				if (number[previous]==1){
					cateNumber--;
					if (previous < minN) minN = previous;
					if (cateIndexMax == previous){
						do {
							cateIndexMax--;
						}while(number[cateIndexMax] == 0);
					}
				}
			}
			else{
				cateNumber++;
				if (number[previous]==1){
					cateNumber--;
					if (previous < c){
						minN = previous;
					}
					else{// previous > c. at this time c = minN for sure.
						do { // minN
							minN ++;
						}while( number[minN]!=0 && minN != previous);
					}	
				}
			}
			state[i] = c;
			number[c] ++;
			number[previous] --;
			while (number[minN]!=0){
				minN++;
			}
			if (c > cateIndexMax){  // TODO
				cateIndexMax = c;
			}
			if (number[previous] == 0 && cateAlive.contains(previous)){
				int index = cateAlive.indexOf(previous);
				cateAlive.remove(index);
			}
			if (!cateAlive.contains(c)){
				cateAlive.add(c);
			}
			
		}
	}

	// use c to update state[i]	
//	private void updateC(int i, int c) {
//		int previous = state[i];
//		if (previous > 0)
//			number[previous] --;
//		number[c] ++;
//		if ( c > cateIndexMax){
//			cateIndexMax = c;
//		}
//		state[i] = c;
//		if (number[c] == 1){  // c is a new one; that is , c use the previous minN; add c to cateAlive
//			while (number[minN + 1] != 0){
//				minN ++;
//			}
//			minN++;
//			cateAlive.add(c);
//		}
//		if (previous == 0){			// simply added c, cateNumber++
//			if (number[c] == 1){
//				cateNumber++;
//			}
//			else;
//		}
//		else{
//			if (number[previous] == 0)		// one category vanished: update cateNumber, minN
//			{
//				cateAlive.remove(previous);// not correct here TODO
//				if (previous < minN){
//					minN = previous;
//				}
//				if (number[c]== 1);
//				else
//					cateNumber--;
//			}
//			else{
//				if (number[c]== 1)
//					cateNumber++;
//				else;
//			}
//		}
//	}


	/*
	 * draw theta from G_0, for category c
	 */
	private void drawTheta(int c) {
		double r =Environment.sampleStandardNormal();
		if (r == Environment.Error){
			System.out.println("Error in sampling from standard Normal");
		}
		theta[c] = r; 
	}
	
	private double ratio(double dis1,double dis2){
		return Math.pow(Math.E, -0.5 * ((dis2 * dis2) - (dis1 * dis1)) / Environment.F_sigma / Environment.F_sigma);
	}

	/*
	 * draw a candidate c*, from the conditional prior by (5.4)
	 */
	public int drawC(){
		int n;
		if (cateAlive.isEmpty()){  // no category at present, draw a new one
			return drawNewC();
		}
		else{
			double p;
			double F = 0;
			double r = Math.random();
			Iterator<Integer> it = cateAlive.iterator();
			while (it.hasNext()){
				n = (Integer)it.next();             // the nth category
				p = number[n] * 1.0 / ((Observed * 1.0) + DP.alpha - 1);
				F += p;
				if (r < F)
					return n;
			}
			// a new category
			return drawNewC();
		}
	}
	 
	/*
	 * draw a new value from the posterior distribution of theta (based on the prior G_0 and all the data currently associated with c
	 * given c, 
	 */
	private double updateTheta(int c){
		// get vector of y (data), that are associated with category c 
		Data data = new Data();
		double[] f = new double[10];
		int size = 0;
		for (int i = 0; i < Data.dataNumber; i ++){
			if (state[i] != c) continue;
			f[size] = data.y[i];
			size++;
		}
		// given y, sample a theta from posterior
		return Environment.drawPosterior(f,size);
	}
	
	
	private int drawNewC() {
		return minN;
	}
	
	public void printState(){
		for (int i = 0; i < stateNumber; i++){
			System.out.println(state[i]);
		}
	}
	
	public void printStateTheta(){
		for (int i = 0; i < stateNumber; i++){
			System.out.println("state: "+state[i]+"  theta: "+theta[state[i]]);
		}
	}
	public void printDataStateTheta(){
		Data data = new Data();
		for (int i = 0; i < stateNumber; i++){
			System.out.println("Data: "+data.y[i]+"  state: "+state[i]+"  theta: "+theta[state[i]]);
		}
		System.out.println("Total category: "+cateNumber);
		System.out.println("Max category index: "+cateIndexMax);
		System.out.println("minN: "+minN);
	}
	
}

