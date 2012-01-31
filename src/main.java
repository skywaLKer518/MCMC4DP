import java.io.IOException;

public class main{
	public static void main(String args[]) throws IOException{
		Environment c = new Environment();
//		for (int i = 0; i < 10; i ++){
//			System.out.println(c.sampleStandardNormal());
//		}
		MCMC m = new MCMC(18);
		m.algorithm5();
		m.printDataStateTheta();
		return;
	}
}