package com.aneedo.nltk;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import com.aneedo.nltk.SemanticBean.BeanInfo;

public class MeroHyperExtractor {

	static SemanticBean getSemanticBean(String query){
		SemanticBean bean=new SemanticBean();
		List<BeanInfo> beanList=bean.getBeanList();
		try
		{
			Runtime r = Runtime.getRuntime();
			Process p = r.exec("python /home/ambha/Patterns/meronyms_hypernyms.py "+query);
			p.waitFor();
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			br.readLine();
			SemanticBean.BeanInfo beanInfo = bean.new BeanInfo();	
			beanInfo.setClassName("meronyms");
			while (!(line = br.readLine()).equals("=hypernyms=")) {
				beanInfo.getInstanceList().add(line);
//				System.out.println(line);
			}
			beanList.add(beanInfo);
			beanInfo = bean.new BeanInfo();	
			beanInfo.setClassName("hypernyms");
			while ((line = br.readLine())!=null) {
				beanInfo.getInstanceList().add(line);
//				System.out.println(line);
			}
			beanList.add(beanInfo);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return bean;

	}
	public static void main(String[] args) {
		SemanticBean bean=getSemanticBean("car");
		List<BeanInfo> beanList=bean.getBeanList();
		Iterator<BeanInfo> beanListIterator=beanList.iterator();
		while(beanListIterator.hasNext()){
			BeanInfo beanInfo=beanListIterator.next();
			System.out.println("className:"+beanInfo.getClassName());
			List<String> instanceList=beanInfo.getInstanceList();
			Iterator instanceListIterator = instanceList.iterator();
			while(instanceListIterator.hasNext()){
				System.out.println(instanceListIterator.next());
			}
				
		}
	}

}
