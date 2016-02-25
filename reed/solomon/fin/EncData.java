package reed.solomon.fin;
//参考文献http://downloads.bbc.co.uk/rd/pubs/whp/whp-pdf-files/WHP031.pdf
public class EncData {
	PolyAlg genX;
	EncData()
	{
		Galois.init_exp_table();
	}
	
	/**
	 * 此时，每一个需要传输的数据的长度为11，冗余码的长度为4
	 * 此时的t为2，编码之后的数据的长度为15
	 * 
	 * 经过这段编码之后的数据的就错能力是长度为15中任意出现不大于2位的错误，检测的范围是4位出错
	 * */
	String Data_16(String str)
	{
		//生成16的产生多项式
		genX=new PolyAlg(new int[]{1});
		for(int i=1;i<=4;i++)
		{
			genX=genX.PolyMul(new PolyAlg(new int[]{1,Galois.gexp[i]}));
		}
		
		//把要发送的数据转换成多项式表示的形式
		PolyAlg EnStr=new PolyAlg(str);
	
		//生成16的乘子多项式
		PolyAlg MultData=new PolyAlg(new int[]{1,0,0,0,0});
		//按照编码公式对数据进行编码
		PolyAlg AftEnStrPoly=EnStr.PolyMul(MultData).PolyMod(genX).PolyAdd(EnStr.PolyMul(MultData));
		
		//把要传输的多项式转换成String
		String result="";
		for(int i=0;i<AftEnStrPoly.poly.size();i++)
		{
			result+=(char)(int)AftEnStrPoly.poly.get(i);
		}
		
		return result;
	}
	
	String Data_32(String str)
	{
		//生成16的产生多项式
		genX=new PolyAlg(new int[]{1});
		for(int i=1;i<=5;i++)
		{
			genX=genX.PolyMul(new PolyAlg(new int[]{1,Galois.gexp[i]}));
		}
		
		//把要发送的数据转换成多项式表示的形式
		PolyAlg EnStr=new PolyAlg(str);
		
		//生成16的乘子多项式
		PolyAlg MultData=new PolyAlg(new int[]{1,0,0,0,0,0});
		//按照编码公式对数据进行编码
		PolyAlg AftEnStrPoly=EnStr.PolyMul(MultData).PolyMod(genX).PolyAdd(EnStr.PolyMul(MultData));
		//把要传输的多项式转换成String
		String result="";
		for(int i=0;i<AftEnStrPoly.poly.size();i++)
		{
			result+=(char)(int)AftEnStrPoly.poly.get(i);
		}
		
		return result;
	}
	
	String Data_64(String str)
	{
		//生成16的产生多项式
		genX=new PolyAlg(new int[]{1});
		for(int i=1;i<=6;i++)
		{
			genX=genX.PolyMul(new PolyAlg(new int[]{1,Galois.gexp[i]}));
		}
		
		//把要发送的数据转换成多项式表示的形式
		PolyAlg EnStr=new PolyAlg(str);
		
		//生成16的乘子多项式
		PolyAlg MultData=new PolyAlg(new int[]{1,0,0,0,0,0,0});
		//按照编码公式对数据进行编码
		PolyAlg AftEnStrPoly=EnStr.PolyMul(MultData).PolyMod(genX).PolyAdd(EnStr.PolyMul(MultData));
		//把要传输的多项式转换成String
		String result="";
		for(int i=0;i<AftEnStrPoly.poly.size();i++)
		{
			result+=(char)(int)AftEnStrPoly.poly.get(i);
		}
		
		return result;
	}

	String Data_128(String str)
	{
		//生成16的产生多项式
		genX=new PolyAlg(new int[]{1});
		for(int i=1;i<=7;i++)
		{
			genX=genX.PolyMul(new PolyAlg(new int[]{1,Galois.gexp[i]}));
		}
		
		//把要发送的数据转换成多项式表示的形式
		PolyAlg EnStr=new PolyAlg(str);
		
		//生成16的乘子多项式
		PolyAlg MultData=new PolyAlg(new int[]{1,0,0,0,0,0,0,0});
		//按照编码公式对数据进行编码
		PolyAlg AftEnStrPoly=EnStr.PolyMul(MultData).PolyMod(genX).PolyAdd(EnStr.PolyMul(MultData));
		//把要传输的多项式转换成String
		String result="";
		for(int i=0;i<AftEnStrPoly.poly.size();i++)
		{
			result+=(char)(int)AftEnStrPoly.poly.get(i);
		}
		
		return result;
	}
	
	String Data_256(String str)
	{
		//生成256的产生多项式
		genX=new PolyAlg(new int[]{1});
		for(int i=1;i<=8;i++)
		{
			genX=genX.PolyMul(new PolyAlg(new int[]{1,Galois.gexp[i]}));
		}
		
		//把要发送的数据转换成多项式表示的形式
		PolyAlg EnStr=new PolyAlg(str);
		
		//生成16的乘子多项式
		PolyAlg MultData=new PolyAlg(new int[]{1,0,0,0,0,0,0,0,0});
		//按照编码公式对数据进行编码
		PolyAlg AftEnStrPoly=EnStr.PolyMul(MultData).PolyMod(genX).PolyAdd(EnStr.PolyMul(MultData));
		//把要传输的多项式转换成String
		String result="";
		for(int i=0;i<AftEnStrPoly.poly.size();i++)
		{
			result+=(char)(int)AftEnStrPoly.poly.get(i);
		}
		
		return result;
	}
}
