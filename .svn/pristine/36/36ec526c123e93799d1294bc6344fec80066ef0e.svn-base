package com.aneedo.multithreading.util;

import java.util.Collection;
import java.util.LinkedList;

import org.apache.lucene.document.Document;


public class PipelineDataQueue {
    LinkedList<Document> queue = new LinkedList<Document>();
    
    //For quick check, set only at the end
    private boolean isDone = false;
    
    // For last 100, maintain thread progress
    private boolean[] inProgress = new boolean[8];
    
    private PipelineDataQueue() {
		//single ton
	}
    
    public void setInProgress(int i, boolean isInprogress) {
    	synchronized (this) {
    		inProgress[i] = isInprogress;
		}
    	
    }
    public boolean isDone() {
		if(isDone) {
			// When all finished return true;
			for(int i=0;i<inProgress.length;i++) {
				if(!inProgress[i]) {
					return inProgress[i] && queue.isEmpty();
				}
			}
		}
		return isDone && queue.isEmpty();
	}


	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}

	private static PipelineDataQueue dataQueue = new PipelineDataQueue();
    
    public static PipelineDataQueue getPipelineDataQueue() {
    	return dataQueue;
    }
    
    // Add categories to queue
    public  void addDataObjects(Collection<Document> o) {
    	synchronized (this) {
    	queue.addAll(o);
    	}
    }

    public Document getDataObject() {
        if (!queue.isEmpty()) {
        
        return queue.removeFirst();
        }
        return null;
    }
    
    
}

class MyBean {
	int i = 0;
	
}
