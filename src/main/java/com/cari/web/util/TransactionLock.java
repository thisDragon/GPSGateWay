package com.cari.web.util;

/**
 * linliangyi @ team of miracle
 * 2006 | Aug 17, 2006  | 12:30:13 AM
 * com.cari.util.concurrent.TransactionLock 
 * 
 * 工具类：并发事务锁
 * 
 * PS:这个类的程序很简短，却因汇聚了智慧，显得如此有灵性
 * */
public class TransactionLock {

	//并发事务锁对象，根据lockOwner，判定相应线程能否获得事务锁
	private Thread lockOwner = null;
	
	//锁深度，在同一synchronized事务内，事务锁被获取的次数
	private int lockDeep = 0;
		
	public TransactionLock() {
		super();
	}

	public synchronized void getLock(){
		while(tryGetLock() == false){
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private synchronized boolean tryGetLock(){
		boolean rtval = false;
		
		if(this.lockOwner == null){
			this.lockOwner = Thread.currentThread();
			this.lockDeep = 1;
			rtval = true;
		}else if(this.lockOwner == Thread.currentThread()){
			this.lockDeep += 1;
			rtval = true;
		}
		
		return rtval;
	}
	
	
	public synchronized void freeLock(){
		if(this.lockOwner == Thread.currentThread()){
			this.lockDeep -= 1;
			if(this.lockDeep == 0){
				this.lockOwner = null;
				this.notify();
			}
		}
	}
	
	public Thread getLockOwner(){
		return this.lockOwner;
	}

	
}
