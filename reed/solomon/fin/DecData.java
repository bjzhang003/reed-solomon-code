package reed.solomon.fin;
//参考文献http://downloads.bbc.co.uk/rd/pubs/whp/whp-pdf-files/WHP031.pdf
import java.util.Vector;


public class DecData {
	
	/**
	 * 根据得到的数据，来构造一个Si的值
	 * 
	 * @param str
	 * @param n
	 * @return
	 */
	
	Vector<Integer> DecSi(String str,int n)
	{
		//根据传进来的字符串构造一个接受到的多项式
		PolyAlg Rev=new PolyAlg(str);
		//新建一个Vector用来存放Si的数据，这里的Si数据是到寻存放的，即S0存放在result.size()-1的位置
		Vector<Integer> result=new Vector<Integer>();
		for(int i=1;i<=n;i++)
		{
			result.add(Rev.PolyCal(Galois.gexp[i]));
		}
		return result;
	}
	
	/** 
	 * 计算Reed Solomon解码所需要的Lambda函数
	 * 
	 * @param SS
	 * @param level
	 * @return
	 */
	PolyAlg CalcLamX(Vector<Integer> SS,int level)
	{
		//初始化数据
		int K=1,L=0,Er=0;
		PolyAlg LamX=new PolyAlg(new int[]{1});
		PolyAlg Cx=new PolyAlg(new int[]{1,0});
		
		//实用这个i来记号计算的每一轮，这里实用的是Berlekamp's Algorithm
		int i=0;
		while(K<=2*level)
		{
			//每一轮开始的时候对e（也就是这里的Er）进行初始化的过程
			Er=0;
			Er=Galois.gadd(Er, SS.get(i));
			
			//按照公式计算出我们想要的Er的值
			for(int j=1;j<=L;j++)
			{
				Er=Galois.gadd(Er,Galois.gmult(SS.get(K-1-j), LamX.poly.get(LamX.poly.size()-1-j)));
			}
			
			//更新Lambda函数表达式
			PolyAlg neLamX=LamX.PolyAdd(Cx.PolyMul(new PolyAlg(new int[]{Er})));
			//当满足这个条件的时候，对L进行更新
			if(2*L<K&&Er>0)
			{				
				L=K-L;
				Cx=LamX.PolyMul(new PolyAlg(new int[]{Galois.ginv(Er)}));
			}
			//更新Cx
			Cx=Cx.PolyMul(new PolyAlg(new int[]{1,0}));
			LamX=neLamX;
			
			//更新K和i的值
			K++;
			i++;
		}
		return LamX;
	}

	/**
	 * 计算Reed Solomon解码所需要的Omega函数
	 * 
	 * @param SS
	 * @param LamX
	 * @return
	 */
	PolyAlg CalcOmega(Vector<Integer> SS,PolyAlg LamX)
	{
		PolyAlg result=new PolyAlg();
		//构造S(x)
		for(int i=SS.size()-1;i>=0;i--)
			result.poly.add(SS.get(i));
		PolyAlg ReTemp=result.PolyMul(LamX);
		
		result=ReTemp.PolyMod(new PolyAlg(new int[]{1,0,0,0,0}));
		
		return result;
	}
	
	/**
	 * 计算解码所需要的LamX'函数式子
	 * @param LamX
	 * @return
	 */
	PolyAlg CalcNeLamX(PolyAlg LamX)
	{
		//新建一个多项式
		PolyAlg neLamXTemp=new PolyAlg();
		//取出Lambda中的奇数相的系数
		for(int j=LamX.poly.size()-1;j>=0;j--)
		{
			if((LamX.poly.size()-1-j)%2!=0)
			{
				neLamXTemp.poly.add(LamX.poly.get(j));
			}
		}
		
		//根据前面取出的数据构造一个新的Lamx函数式子
		PolyAlg neLamX=new PolyAlg();
		for(int i=neLamXTemp.poly.size()-1;i>=0;i--)
		{
			if(i==0)
				neLamX.poly.add(neLamXTemp.poly.get(i));
			else
			{
				neLamX.poly.add(neLamXTemp.poly.get(i));
				neLamX.poly.add(0);
			}
				
		}
		
		return neLamX;
	}
	
	/**
	 * 计算得到的纠错码
	 * 
	 * @param LamX
	 * @param neLamX
	 * @param OmegaX
	 * @param Rev
	 * @return
	 */
	PolyAlg CorErr(PolyAlg LamX,PolyAlg neLamX,PolyAlg OmegaX,PolyAlg Rev)
	{
		Vector<Integer> result=new Vector<Integer>();
		PolyAlg CorErrorTemp =new PolyAlg();
		Galois.init_exp_table();
		for(int i=1;i<=Rev.poly.size();i++)
		{
			int tempData=LamX.PolyCal(Galois.ginv(Galois.gexp[i]));
			result.add(tempData);
			if(tempData>0)
			{
				CorErrorTemp.poly.add(0);
			}
			else
			{
				int InvTemp=Galois.ginv(Galois.gexp[i]);
				CorErrorTemp.poly.add(Galois.gmult(Galois.gmult(Galois.gexp[i],OmegaX.PolyCal(InvTemp)),
						Galois.ginv(neLamX.PolyCal(InvTemp))));
			}
		}		
		PolyAlg CorError =new PolyAlg();
		for(int i=CorErrorTemp.poly.size()-1;i>=0;i--)
		{
			CorError.poly.add(CorErrorTemp.poly.get(i));
		}
		
		return CorError;
	}
	
	
	String Data_16(String str)
	{
		Vector<Integer> Si=this.DecSi(str, 4);
		PolyAlg LamX=this.CalcLamX(Si, 2);
		PolyAlg OmegaX=this.CalcOmega(Si, LamX);	
		PolyAlg NeLamX=this.CalcNeLamX(LamX);
		PolyAlg Rev=new PolyAlg(str);
		PolyAlg CorError=this.CorErr(LamX, NeLamX, OmegaX, Rev);
		PolyAlg CorResult=Rev.PolyAdd(CorError);
		
		String result="";
		for(int i=0;i<CorResult.poly.size()-4;i++)
		{
			result+=(char)(int)CorResult.poly.get(i);
		}
		System.out.println("result = "+result);
		return result;
	}
	
	String Data_64(String str)
	{
		Vector<Integer> Si=this.DecSi(str, 6);
		PolyAlg LamX=this.CalcLamX(Si, 3);
		PolyAlg OmegaX=this.CalcOmega(Si, LamX);	
		PolyAlg NeLamX=this.CalcNeLamX(LamX);
		PolyAlg Rev=new PolyAlg(str);
		PolyAlg CorError=this.CorErr(LamX, NeLamX, OmegaX, Rev);
		PolyAlg CorResult=Rev.PolyAdd(CorError);
		
		String result="";
		for(int i=0;i<CorResult.poly.size()-6;i++)
		{
			result+=(char)(int)CorResult.poly.get(i);
		}
		System.out.println("result = "+result);
		return result;
	}
	
	String Data_256(String str)
	{
		Vector<Integer> Si=this.DecSi(str, 8);
		PolyAlg LamX=this.CalcLamX(Si, 4);
		PolyAlg OmegaX=this.CalcOmega(Si, LamX);	
		PolyAlg NeLamX=this.CalcNeLamX(LamX);
		PolyAlg Rev=new PolyAlg(str);
		PolyAlg CorError=this.CorErr(LamX, NeLamX, OmegaX, Rev);
		PolyAlg CorResult=Rev.PolyAdd(CorError);
		
		String result="";
		for(int i=0;i<CorResult.poly.size()-8;i++)
		{
			result+=(char)(int)CorResult.poly.get(i);
		}
		System.out.println("result = "+result);
		return result;
	}

}
