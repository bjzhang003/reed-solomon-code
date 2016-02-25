package reed.solomon.fin;
//参考文献http://downloads.bbc.co.uk/rd/pubs/whp/whp-pdf-files/WHP031.pdf
import java.util.Vector;

public class PolyAlg {
	/**
	 * 实用Vector来表示多项式,幂次高的在前面,幂次低的相在后面
	 * 这里的poly里面记录的是多项式的系数，如果没有这个相的话，也仍然需要给出0来表示
	 * */
	Vector<Integer> poly=null;
	
	//默认构造函数
	PolyAlg()
	{
		Galois.init_exp_table();
		poly=new Vector<Integer>();
	}
	//实用数组的构造函数
	PolyAlg(int a[])
	{
		Galois.init_exp_table();
		poly=new Vector<Integer>();
		for(int i=0;i<a.length;i++)
		{
			poly.add(a[i]);
		}
	}	
	//实用String的构造函数
	PolyAlg(String str)
	{
		Galois.init_exp_table();
		poly=new Vector<Integer>();
		for(int i=0;i<str.length();i++)
		{
			poly.add((int)str.charAt(i));
		}		
	}
	
	/**
	 * 判断多项式是不是全部系数都为0
	 * 如果是系数全部为0的多项式的话，则返回true，否则返回false
	 * */
	static boolean IsZero(PolyAlg pol)
	{
		boolean result=true;
		for(int i=0;i<pol.poly.size();i++)
		{
			if(pol.poly.get(i)>0)
			{
				result=false;
				break;
			}			
		}
		return result;
	}
	
	/**
	 * 多项式的加法计算方式，这里的加法计算是在前面的类文件Galois中进行的域上的加法
	 * 加法是异或运算
	 * 如果向改变域的大小的话，就需要直接修改前面的GF的大小即可
	 * 
	 * this多项式加上p1多项式的结果
	 * */
	PolyAlg PolyAdd(PolyAlg p1)
	{
		PolyAlg result=new PolyAlg();
		Vector<Integer> tempData=new Vector<Integer>();
		/**
		 * 这里的加法是按照从最低位到最高位进行加法计算的方法
		 * */
		for(int i=poly.size()-1,j=p1.poly.size()-1;i>=0||j>=0;j--,i--)
		{
			if(i>=0&&j>=0)
			{
				//如果这一位是两个多项式都有的话，那么直接相加
				tempData.add(Galois.gadd(this.poly.get(i), p1.poly.get(j)));
			}
			else if(i>=0&&j<0)
			{
				tempData.add(Galois.gadd(this.poly.get(i),0));
			}
			else if(j>=0&&i<0)
			{
				tempData.add(Galois.gadd(p1.poly.get(j),0));
			}
		}
		//把数据以一个以前前面所说的方式返回回来
		for(int j=tempData.size()-1;j>=0;j--)
		{
			result.poly.add(tempData.get(j));
		}
		return result;
	}
	
	/**
	 * 多项式的乘法运算，这里的乘法也是在前面提到的域Galois上进行的乘法运算 
	 * 
	 * this多项式乘以p1多项式的结果
	 * */
	PolyAlg PolyMul(PolyAlg p1)
	{
		PolyAlg result=new PolyAlg();
		//如果有一个多项式为0的话，则返回空多项式
		if(this.poly.size()==0||PolyAlg.IsZero(this))
		{
			//直接返回空多项式
			System.out.println("this is 0");
		}
		else if(p1.poly.size()==0||PolyAlg.IsZero(p1))
		{
			System.err.println("zero cannot be a devidor! ");
		}
		else
		{
			for(int i=this.poly.size()-1;i>=0;i--)
			{
				PolyAlg tempData=new PolyAlg();
				for(int j=0;j<p1.poly.size();j++)
				{
					//实用p1中的每一项乘this多项式，这一步进行的是每一个的系数的相乘
					tempData.poly.add(Galois.gmult(this.poly.get(i),p1.poly.get(j)));
				}
				//把多项式的相数给添加进去
				for(int k=0;k<this.poly.size()-i-1;k++)
				{
					tempData.poly.add(0);
				}
				//不停地进行加法运算直到结束
				result=result.PolyAdd(tempData);
			}
		}
		
		return result;
	}
	
	/**
	 * 多项式求余数的运算，this多项式除以p1多项式得到的余数
	 * 如果整除的话，也返回一个多项式，只是这个多项式的每一个系数都是0
	 * 如果存在余数的话，则返回的是这个的余数
	 * 
	 * 这里不要传入系数都是0的多项式
	 * */
	PolyAlg PolyMod(PolyAlg p1)
	{
		PolyAlg result=new PolyAlg();
		//如果this多项式是一个空多项式的话，则返回一个空多项式
		
		if(this.poly.size()==0||PolyAlg.IsZero(this))
		{
			return result;
		}
		else
			if(p1.poly.size()==0||PolyAlg.IsZero(p1))
			{
				//除数多项式不能是空多项式
				System.err.println("The devide is zero!!!");
				return result;
			}
			else if(this.poly.size()<p1.poly.size())
			{
				//如果被除多项式的系数小于除数多项式的系数的话，则认为得到了结果
				result=this;
				return result;
			}
			else
			{
				PolyAlg tempData=new PolyAlg();
				
				int t1=this.poly.size();
				int t2=p1.poly.size();
				
				//计算出乘数系数来，这个成熟的大小等于被除数除以除数
				int dev=Galois.gmult(this.poly.get(0),Galois.ginv(p1.poly.get(0)));
				PolyAlg devPoly=new PolyAlg();
				devPoly.poly.add(dev);
				//根据得到的除数多项式的系数建立这样地一个除数多项式
				for(int i=0;i<t1-t2;i++)
					devPoly.poly.add(0);
				tempData=this.PolyAdd(p1.PolyMul(devPoly));
				int notzero=0;
				for(int i=0;i<tempData.poly.size();i++)
				{
					if(tempData.poly.get(i)==0&&i<=notzero)
					{
						notzero=i;
					}
					else 
						result.poly.add(tempData.poly.get(i));
				}
				
				return result.PolyMod(p1);
			}
		
	}
		
	/**
	 * 在域上，计算这个多项式的函数值，传入的参数是num
	 * */
	int PolyCal(int num)
	{
		int result=0;
		for(int i=0;i<poly.size();i++)
		{
			result=Galois.gadd(result,Galois.gmult(poly.get(i),Galois.gpow(num, poly.size()-i-1)));
		}
		return result;
	}
	
	/**
	 * 输出这个多项式
	 * */
	String PolyToString()
	{
		String result="";
		if(this.poly.size()==0||PolyAlg.IsZero(this))
		{
			result=result+"0";
		}
		else if(poly.size()>0)
		{
			for(int j=0;j<poly.size()-1;j++)
				result+=poly.get(j)+"x^"+(poly.size()-j-1)+"+";
			result+=poly.get(poly.size()-1);
		}
		return result;
	}

}
