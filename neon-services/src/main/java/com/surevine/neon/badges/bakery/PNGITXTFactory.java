package com.surevine.neon.badges.bakery;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ar.com.hjg.pngj.ImageLineInt;
import ar.com.hjg.pngj.PngReader;
import ar.com.hjg.pngj.PngWriter;
import ar.com.hjg.pngj.chunks.ChunkCopyBehaviour;
import ar.com.hjg.pngj.chunks.ChunksList;
import ar.com.hjg.pngj.chunks.PngChunkITXT;

public class PNGITXTFactory {

	public void copyAndAddITXT(InputStream in, String key, String value, OutputStream os) {
		PngReader reader = new PngReader(in);
		PngWriter writer = new PngWriter(os, reader.imgInfo);
		ChunksList chunks = reader.getChunksList();
		
		PngChunkITXT newChunk = new PngChunkITXT(reader.imgInfo);		
		newChunk.setKeyVal(key, value);
		
		writer.copyChunksFrom(chunks, ChunkCopyBehaviour.COPY_ALL_SAFE);
		writer.getChunksList().queue(newChunk.cloneForWrite(reader.imgInfo));
		
		for (int row = 0; row < reader.imgInfo.rows; row++) {
			ImageLineInt line = (ImageLineInt) reader.readRow(row);
			writer.writeRow(line, row);
		}
		reader.end();
		writer.end();
	}
	
	public static void main (String arg[]) throws IOException {
		File in = new File("/tmp/baboon.png");
		File out = new File("/tmp/baboon_out.png");
		System.out.println("Copying "+in.getPath());
		out.delete();
		FileOutputStream fos = new FileOutputStream(out);
		new PNGITXTFactory().copyAndAddITXT(new FileInputStream(in), "test", "wibble", fos);
		fos.close();
	}

}
