package reed.solomon.fin;
//参考文献http://downloads.bbc.co.uk/rd/pubs/whp/whp-pdf-files/WHP031.pdf
import java.util.Vector;

public class Galois256_test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		EncData tt=new EncData();
		String SenData=tt.Data_16("1234");
		System.out.println(SenData);
		String outpu="1234f8";
		for(int i=6;i<SenData.length();i++)
			outpu+=SenData.charAt(i);
		System.out.println(outpu);
		
		DecData rr=new DecData();
		rr.Data_16(outpu);
			

	}

}
