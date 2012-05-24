package com.aneedo.multithreading.util;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

public class AsynchronousIndexWriter implements Runnable {

	private BlockingQueue documents;

	private IndexWriter writer;

	private Thread writerThread;

	private boolean keepRunning = true;

	private boolean isRunning = true;

	private long sleepMilisecondOnEmpty = 100;

	public void addDocument(Document doc) throws InterruptedException {

		documents.put(doc);

	}

	public void startWriting() {

		writerThread = new Thread(this, "AsynchronousIndexWriter");

		writerThread.start();

	}

	public AsynchronousIndexWriter(IndexWriter w) {

		this(w, 100, 100);

	}

	public AsynchronousIndexWriter(IndexWriter w, int queueSize) {

		this(w, queueSize, 100);

	}

	public AsynchronousIndexWriter(IndexWriter w, int queueSize,

	long sleepMilisecondOnEmpty) {

		this(w, new ArrayBlockingQueue(queueSize), sleepMilisecondOnEmpty);

	}

	public AsynchronousIndexWriter(IndexWriter w, BlockingQueue queue,

	long sleepMilisecondOnEmpty) {

		writer = w;

		documents = queue;

		this.sleepMilisecondOnEmpty = sleepMilisecondOnEmpty;

		startWriting();

	}

	public void run() {

		while (keepRunning || !documents.isEmpty()) {

			Document d = (Document) documents.poll();

			try {

				if (d != null) {

					writer.addDocument(d);

				} else {

					Thread.sleep(sleepMilisecondOnEmpty);

				}

			} catch (ClassCastException e) {

				e.printStackTrace();

				throw new RuntimeException(e);

			} catch (InterruptedException e) {

				e.printStackTrace();

				throw new RuntimeException(e);

			} catch (CorruptIndexException e) {

				e.printStackTrace();

				throw new RuntimeException(e);

			} catch (IOException e) {

				e.printStackTrace();

				throw new RuntimeException(e);

			}

		}

		isRunning = false;

	}

	private void stopWriting() {

		this.keepRunning = false;

		try {

			while (isRunning) {

				Thread.sleep(sleepMilisecondOnEmpty);

			}

		} catch (InterruptedException e) {

			e.printStackTrace();

		}

	}

	public void optimize() throws CorruptIndexException, IOException {

		writer.optimize();

	}

	public void close() throws CorruptIndexException, IOException {

		stopWriting();

		writer.close();

	}

	private static final String path = "/home/ambha/aneedo/indexing/dmozindexinglatestxx";

	public static void main(String[] args) throws Exception {

		try {
			Analyzer analyzer = new SimpleAnalyzer(Version.LUCENE_30);

			File file = new File(path);

			Directory directory = new SimpleFSDirectory(file);

			IndexWriter iwriter = new IndexWriter(directory, analyzer, true,
					new IndexWriter.MaxFieldLength(Integer.MAX_VALUE));
			iwriter.setMergeFactor(1000);
			iwriter.setMaxMergeDocs(Integer.MAX_VALUE);

			AsynchronousIndexWriter writer = new AsynchronousIndexWriter(
					iwriter);

			addDocumentsInMultipleThreads(writer);

			writer.optimize();

			writer.close();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	private static void addDocumentsInMultipleThreads(

	AsynchronousIndexWriter writer) throws InterruptedException {

		Document doc = new Document();

		doc.add(new Field("content", "My Content", Field.Store.YES,
				Field.Index.NOT_ANALYZED));

		writer.addDocument(new Document());

	}

}