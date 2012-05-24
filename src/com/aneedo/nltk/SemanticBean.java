package com.aneedo.nltk;

import java.util.ArrayList;
import java.util.List;

public class SemanticBean {
	List<BeanInfo> beanList=new ArrayList<BeanInfo>();
	
	public List<BeanInfo> getBeanList() {
		return beanList;
	}

	public void setBeanList(List<BeanInfo> beanList) {
		this.beanList = beanList;
	}

	public class BeanInfo{
		private String className;
		public String getClassName() {
			return className;
		}
		public void setClassName(String className) {
			this.className = className;
		}
		public List<String> getInstanceList() {
			return instanceList;
		}
		public void setInstanceList(List<String> instanceList) {
			this.instanceList = instanceList;
		}
		private List<String> instanceList=new ArrayList<String>();
	}
	

}
