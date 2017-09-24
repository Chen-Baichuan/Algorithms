import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Percolation {
	private boolean[][] bl;
	private int[][] sz;
	private int[][] id;
	private int open_site= 0;
	private int n;
	public Percolation(int n){
		// create n-by-n grid, with all sites blocked
		this.n = n;
		bl = new boolean[n][n];
		sz = new int[n][n];
		id = new int[n][n];
		for(int i=0; i<n; i++){
			for(int j=0; j<n; j++){
				bl[i][j]=false;
				sz[i][j]=1;
				if(i==0){
					id[i][j]=-1*(i*n+j);
				}
				else{
					id[i][j]=i*n+j;
				}
			}
		}
	}
	public void open(int row, int col){
		// open site (row, col) if it is not open already
		bl[row-1][col-1]= true;
		if(row==1&&col==1){
			id[0][0]-=1;
		}
		open_site++;

	}
	public int root(int idn){
		//System.out.println("idn: "+idn);
		if(idn<0){
			return idn;
		}
		//int num=0;
		while(idn!=id[idn/n][idn%n]){
			//num++;
			//System.out.println("num: "+num+", idn: "+idn +", last root: "+ id[idn/n][idn%n]);
			if(id[idn/n][idn%n]<0){
				return id[idn/n][idn%n];
			}
			id[idn/n][idn%n]= id[id[idn/n][idn%n]/n][id[idn/n][idn%n]%n];
			idn=id[idn/n][idn%n];
			if(idn<0){
				return idn;
			}
			//System.out.println(idn);

		}
		return idn;
	}
	public int idn_generate(int a, int b){
		return (id[a-1][b-1]);
	}
	public int get_row(int idn){
		return idn/n;
	}
	public int get_column(int idn){
		return idn%n;
	}
	public void union(int p, int q){
		int i = root(p);
		int j = root(q);
		//System.out.println("i: "+i+", j: "+j);
		if(i==j){
			return;
		}
		if(i<0&&j<0){
			if(sz[get_row(-i)][get_column(-i)]>sz[get_row(-j)][get_column(-j)]){
				id[get_row(-j)][get_column(-j)]=i;
				sz[get_row(-i)][get_column(-i)]+=sz[get_row(-j)][get_column(-j)];
			}
			else{
				id[get_row(-i)][get_column(-i)]=j;
				sz[get_row(-j)][get_column(-j)]+=sz[get_row(-i)][get_column(-i)];}
		}
		else if(i<0){

			id[get_row(j)][get_column(j)]=i;
			sz[get_row(-i)][get_column(-i)]+=sz[get_row(j)][get_column(j)];
		}
		else if(j<0){
			id[get_row(i)][get_column(i)]=j;
			sz[get_row(-j)][get_column(-j)]+=sz[get_row(i)][get_column(i)];
		}
		else if(sz[get_row(i)][get_column(i)]>sz[get_row(j)][get_column(j)]){
			id[get_row(j)][get_column(j)]=i;
			sz[get_row(i)][get_column(i)]+=sz[get_row(j)][get_column(j)];
		}
		else{
			id[get_row(i)][get_column(i)]=j;
			sz[get_row(j)][get_column(j)]+=sz[get_row(i)][get_column(i)];}
	}
	public boolean conneted(int p, int q){
		return (root(p)==root(q));
	}
	public int find(int i){
		return root(i);
	}
	public int count(){
		ArrayList<Integer> al= new ArrayList<Integer>();
		int num=0;
		for(int i=0; i<n; i++){
			for(int j=0; j<n; j++){
				if(al.get(root(idn_generate(i,j)))==-1){
					al.add(root(idn_generate(i,j)));
					num++;
				}
			}
		}
		return num;
	}
	public boolean isOpen(int row, int col){
		// is site (row, col) open?
		return bl[row-1][col-1];
	}
	public boolean isFull(int row, int col){
		// is site (row, col) full?
		return !bl[row-1][col-1];
	}
	public int numberOfOpenSites(){
		// number of open sites
		return open_site;
	}
	public boolean percolates(){
		// does the system percolate?
		for(int i=0;i<n;i++){
			//System.out.println("root: "+(root(idn_generate(n,i+1))));
			if(root(idn_generate(n,i+1))<0){
				return true;
			}
		}
		return false;
	}

	public void check(int p, int q){
	
		int minx= (p-1<1?1:p-1);
		int maxx= (p+1>n?n:p+1);
		int miny= (q-1<1?1:q-1);
		int maxy= (q+1>n?n:q+1);
		for(int i=minx;i<=maxx;i++){
			for(int j=miny;j<=maxy;j++){
				if(!(Math.abs(i-p)==1&&Math.abs(j-q)==1)){
					if(isOpen(i,j)){
						union(idn_generate(i,j),idn_generate(p,q));
					}
				}
			}
		}
	}

	public static void main(String[] args) throws FileNotFoundException{
		Scanner sc = new Scanner(new File("wayne98.txt"));
		Percolation objc = new Percolation(sc.nextInt());
		while(sc.hasNextInt()){
			int a = sc.nextInt();
			int b = sc.nextInt();
			objc.open(a,b);
			//System.out.println("opened"+a+","+b);
			objc.check(a,b);
		}
		sc.close();
		System.out.println("numbers open: "+ objc.numberOfOpenSites());
		System.out.println("percolates? "+objc.percolates());
		//System.out.println("id: "+objc.id[0][0]);
	}
}
