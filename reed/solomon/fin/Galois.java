package reed.solomon.fin;
//参考文献http://downloads.bbc.co.uk/rd/pubs/whp/whp-pdf-files/WHP031.pdf
public class Galois {
	//定义初始化的表,和数据的大小
		static final int GF=256;
		static int gexp[]=new int[2*GF];
		static int glog[]=new int[GF];
		
		static void init_exp_table() {
			// TODO Auto-generated method stub
			int i,z;
			int pinit,p1,p2,p3,p4,p5,p6,p7,p8;
			
			pinit=p2=p3=p4=p5=p6=p7=p8=0;
			p1=1;
			
			gexp[0]=0;
			gexp[255]=gexp[0];
			glog[0]=0;
			/* shouldn't log[0] be an error? */
			
			//构造在大小为GF里面的域表
			for(i=1;i<256;i++)
			{
				gexp[i]=p1 + p2*2 + p3*4 + p4*8 + p5*16 + p6*32 + p7*64 + p8*128;
				pinit=p8;
				p8=p7;
				p7=p6;
				p6=p5;
				p5=p4^pinit;
				p4=p3^pinit;
				p3=p2^pinit;
				p2=p1;
				p1=pinit;			
				gexp[i+GF-1] = gexp[i];
			}		
			//构造对应的指数表
			for (i = 1; i < GF; i++) {
			    for (z = 0; z < GF; z++) {
			      if (gexp[z] == i) {
			    	  glog[i] = z;
			    	  break;
			    	}
			    }
			  }		
		}
		
		/**
		 * 定义：域上的加法运算，这里的加法运算就是异或运算
		 * */
		static int gadd(int a,int b)
		{
			return a^b;
		}
	
		/**
		 * 定义：域上的乘法预算
		 * */
		static int gmult(int a, int b)
		{
		  int i,j;
		  if (a==0 || b == 0) return (0);
		  i = glog[a];
		  //System.out.println("i = "+i);
		  //i为a的幂次加1
		  j = glog[b];
		  //System.out.println("j = "+j);
		  //j为a的幂次加1
		  return (gexp[i+j-1]);
		}
		
		/**
		 * 定义：域上的求幂运算
		 * */
		static int gpow(int elt,int n)
		{
			int result=1;
			for(int i=0;i<n;i++)
			{
				result=gmult(result,elt);
			}
			return result;
		}
		
		/**
		 * 定义：域上的求逆元预算
		 * */
		static int ginv (int elt)
		{ 
			if(elt==0)
			{
				//如果是0的话是没有逆元的
				System.err.println("0 does't have Inv! ");
				return 0;
			}
			else
			{
				//glog[elt]-1表示这个数字是生成元的多少次幂
				//255-(glog[elt]-1)得到逆元的幂次，然后再加1得到位置,即255-(glog[elt]-1)+1
				return (gexp[GF+1-glog[elt]]);
			}
				
		}
}
