import java.util.ArrayList;

public class PercolationStats  {
	ArrayList<Integer> data= new ArrayList<Integer>();
	int n;
	public PercolationStats(int n, int trials){
		// perform trials independent experiments on an n-by-n grid
		this.n= n;
		for(int a=0; a<trials; a++){
			Percolation objc = new Percolation(n);
			boolean b=true;
			while(b){
				int p= (int)((n)*Math.random()+1);
				int q= (int)((n)*Math.random()+1);
				//System.out.println("test: p="+p+", q="+q);
				if(objc.isFull(p, q)){
					objc.open(p,q);
					objc.check(p,q);
					if(objc.percolates()){
						b= false;
					}
				}


			}
			
			data.add(objc.numberOfOpenSites());
			
		}
		
	}

	public double mean(){
		// sample mean of percolation threshold
		int sum=0;
		for(int i=0; i<data.size(); i++){
			sum+=data.get(i);
		}
		return ((int)(sum*10000.0/data.size())/n/n)/100.0;
	}
	public double stddev()  {
		// sample standard deviation of percolation threshold
		//System.out.println("d: "+d);
		double sum=0;
		for(int i=0; i<data.size(); i++){
			sum+=((data.get(i)-mean()*n*n/100)*(data.get(i)-mean()*n*n/100));
		}
		return Math.sqrt(sum/(data.size()-1))/n/n;
	}
	public double confidenceLo(){
		// low  endpoint of 95% confidence interval
		return mean()/100.0-stddev()*1.96/Math.sqrt(data.size());
	}
	public double confidenceHi(){
		return mean()/100.0+stddev()*1.96/Math.sqrt(data.size());
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int n = 200;
		int trials = 100;
		
		PercolationStats stats = new PercolationStats(n, trials);
		double mean = stats.mean();
		System.out.println("mean                    = "+mean);
		System.out.println("stddev                  = "+stats.stddev());
		System.out.println("95% confidence interval = ["+stats.confidenceLo()+", "+stats.confidenceHi()+"]");

	}

}
