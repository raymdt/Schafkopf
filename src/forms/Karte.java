package forms;


import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.opengl.GLUtils;


public class Karte {

	private FloatBuffer vertexBuffer;
	private FloatBuffer textureBuffer;
	public String imageUrl;
	private float posX =0;
	private float posY =0;
	private float sizeX=56;
	private float sizeY=112;
	private float scaleX = 1;
	private float scaleY = 1;
	private boolean isSelect;
	public int karteId;
	






	public int texid;		


	/*
	 * 3   4
	 * 1   2
	 */
	
	private float vertices[] = { 
			posX, posY, 0.0f, 	//Links unten
			posX+sizeX, posY, 0.0f, 		//recht unten
			posX, posY+sizeY, 0.0f, 		//link Oben
			posX+sizeX,posY+sizeY, 0.0f 		//recht oben
	};



	//	private float vertices[] = { 
	//			-0.1f, -0.3f, 0.0f, 	//Links unten
	//			0.1f, -0.3f, 0.0f, 		//recht unten
	//			-0.1f, 0.3f, 0.0f, 		//link Oben
	//			0.1f, 0.3f, 0.0f 		//recht oben
	//	};

	/*
	 * 1   2
	 * 3   4
	 */
	//	private float texture[] = {    		
	//			1.0f, 0.0f,
	//			0.0f, 0.0f,		
	//			1.0f, 1.0f,		
	//			0.0f, 1.0f,     
	//			
	//	
	//	};



	private float texture[] = {    		
			0.0f, 1.0f,     
			1.0f, 1.0f,		
			0.0f, 0.0f,		
			1.0f, 0.0f };	


	public Karte(int id) {


		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		this.karteId = id;


		byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);
		isSelect= false;
	}


	public boolean isPointInclude(float x, float y) {
		
		
		if(x>=(posX) && x<=(posX+sizeX)){
			if(y>=(posY)&& y<=(posY+sizeY)) {
				return true;
			}
		}
		return false;
	}


	public void draw(GL10 gl,Context context,String url) {	


		gl.glFrontFace(GL10.GL_CW);


		int id = loadTexture(gl, context, url);


		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);


		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, id);


		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);		



	}







	public static int newTextureID(GL10 gl) {
		int[] temp = new int[1];
		gl.glGenTextures(1, temp,0);
		return temp[0];
	}


	public int loadTexture(GL10 gl, Context context,String url) {


		int id = newTextureID(gl);


		Matrix flip = new Matrix();
		flip.postScale(1f, -1f);

		//nicht auf device Pixel skalieren
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inScaled = true;



		InputStream is = null;
		Bitmap bmp = null;
		try {
			is= context.getAssets().open(url);

			bmp = BitmapFactory.decodeStream(is);



		} 
		catch(IOException e) {
			e.printStackTrace();
		}

		finally {
			try {
				is.close();
				is = null;
			} catch (IOException e) {
			}
		}





		gl.glBindTexture(GL10.GL_TEXTURE_2D, id);

		// Set all of our texture parameters:
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR_MIPMAP_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR_MIPMAP_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);

		//mipmap generieren und laden
		for(int level=0, height = bmp.getHeight(), width = bmp.getWidth(); true; level++) {
			// bitmap in GPU
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, level, bmp, 0);

			//Aufhören wenn die Texture 1x1 ist
			if(height==1 && width==1) break;

			// Dimensionen neu anpassen
			width >>= 1; height >>= 1;
			if(width<1)  width = 1;
			if(height<1) height = 1;

			Bitmap bmp2 = Bitmap.createScaledBitmap(bmp, width, height, true);
			bmp.recycle();
			bmp = bmp2;
		}

		bmp.recycle();

		return id;
	}




	public FloatBuffer getVertexBuffer() {
		return vertexBuffer;
	}



	public void setVertexBuffer(FloatBuffer vertexBuffer) {
		this.vertexBuffer = vertexBuffer;
	}



	public FloatBuffer getTextureBuffer() {
		return textureBuffer;
	}



	public void setTextureBuffer(FloatBuffer textureBuffer) {
		this.textureBuffer = textureBuffer;
	}



	public String getImageUrl() {
		return imageUrl;
	}



	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}



	public float getPosX() {
		return posX;
	}



	public void setPosX(float posX) {
		this.posX = posX;
	}



	public float getPosY() {
		return posY;
	}



	public void setPosY(float posY) {
		this.posY = posY;
	}



	public float getSizeX() {
		return sizeX;
	}



	public void setSizeX(int sizeX) {
		this.sizeX = sizeX;
	}



	public float getSizeY() {
		return sizeY;
	}



	public void setSizeY(int sizeY) {
		this.sizeY = sizeY;
	}



	public int getTexid() {
		return texid;
	}



	public void setTexid(int texid) {
		this.texid = texid;
	}



	public float[] getVertices() {
		return vertices;
	}



	public void setVertices(float[] vertices) {
		this.vertices = vertices;
	}



	public float[] getTexture() {
		return texture;
	}



	public void setTexture(float[] texture) {
		this.texture = texture;
	}


	public float getScaleX() {
		return scaleX;
	}


	public void setScaleX(float previousX) {
		this.scaleX = previousX;
	}


	public float getScaleY() {
		return scaleY;
	}


	public void setScaleY(float previousY) {
		this.scaleY = previousY;
	}


	public void setSizeX(float sizeX) {
		this.sizeX = sizeX;
	}


	public void setSizeY(float sizeY) {
		this.sizeY = sizeY;
	}


	public boolean isSelect() {
		return isSelect;
	}


	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}







}
