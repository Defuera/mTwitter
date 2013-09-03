package com.example.mtwitter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MyPoolExecutor implements ExecutorService {

	private static final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(150);
	private int maxThread;
	private static ArrayList<Thread> list = new ArrayList<Thread>();

	@Override
	public void execute(Runnable arg0) {
	}

	public MyPoolExecutor(int maxThread) {
		this.maxThread = maxThread;
	}

	@Override
	public Future<?> submit(Runnable task) {

		if (!queue.contains(task))
			try {
				queue.put(task);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		else
			return null;

		while (!threadIsAvailable()) { // wait --  notify yield

		}
		try {
			Thread thread = new Thread(queue.remove()); // take  no need in notify.
			thread.start();
			list.add(thread);

			System.out.println("list size "+list.size());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private boolean threadIsAvailable() {
		int count = 0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).isAlive()) {
				count++;
			} else {
				list.get(i).interrupt();
				System.out.println(list.get(i).getState());
				list.remove(i);
			}
		}
		return count < maxThread;
	}

	@Override
	public <T> Future<T> submit(Runnable arg0, T arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean awaitTermination(long arg0, TimeUnit arg1)
			throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> arg0)
			throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<Future<T>> invokeAll(
			Collection<? extends Callable<T>> arg0, long arg1, TimeUnit arg2)
			throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> arg0)
			throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> arg0, long arg1,
			TimeUnit arg2) throws InterruptedException, ExecutionException,
			TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isShutdown() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTerminated() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Runnable> shutdownNow() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> Future<T> submit(Callable<T> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
