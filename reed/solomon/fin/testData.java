package reed.solomon.fin;

public class testData {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Galois.init_exp_table();
		System.out.println("TestData\n");
		DecData tt=new DecData();
		EncData aa=new EncData();
		String result1=aa.Data_16("234567890");
		System.out.println("result = "+result1);
		String result3="2315678902äùè";
		String result2=tt.Data_16(result3);
		System.out.println("result 2 = "+result2);
		
		System.out.println(Galois.gmult(79, 111));
	}

}
