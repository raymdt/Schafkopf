package forms;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.opengl.GLUtils;


public class KarteBack {
	


		
	private FloatBuffer vertexBuffer;
	private FloatBuffer textureBuffer;
	private FloatBuffer normalBuffer;

	private int[] textures = new int[3];
	

	private int[] imageFieldIds = {
	1,2,3,4,5
		
	};
	
	private Bitmap[] bitmaps = new Bitmap[6];
	
	
	private float vertices[] = { 
						-0.6f, -0.7f, 0.0f, 	//Links unten
						0.6f, -0.7f, 0.0f, 		//recht unten
						-0.6f, 0.7f, 0.0f, 		//link Oben
						0.6f, 0.7f, 0.0f 		//recht oben
										};
	
	
	private float normals[] = {
			// Normals
			0.0f, 0.0f, 1.0f, 						
			0.0f, 0.0f, -1.0f, 
			0.0f, 1.0f, 0.0f, 
			0.0f, -1.0f, 0.0f
	};
	
    private float texture[] = {    		
    		0.0f, 1.0f,     
    		1.0f, 1.0f,		
    		0.0f, 0.0f,		
    		1.0f, 0.0f };	
	

	public KarteBack() {
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		
		byteBuf = ByteBuffer.allocateDirect(normals.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		normalBuffer = byteBuf.asFloatBuffer();
		normalBuffer.put(normals);
		normalBuffer.position(0);
		
		
		byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);
	}


	 
	public void draw(GL10 gl,int filter) {	
		
		//Drehung Seite
		gl.glFrontFace(GL10.GL_CW);
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[filter]);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);

		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);


		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);
		
//		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
//		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

	}
	
	
	
	public void loadGLTexture(GL10 gl, Context context, int textureNummer) {

		InputStream is = null;
		Bitmap bitmap = null;
		try {
			is= context.getAssets().open("bri.JPG");
			
			bitmap = BitmapFactory.decodeStream(is);
			char carac='a';
			for(int p=0;carac <='e'&& p<imageFieldIds.length;p++,carac++) {
				bitmaps[p] = BitmapFactory.decodeStream(context.getAssets().open("dra/"+String.valueOf(carac)+".jpg"));
				
				
			}

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

		gl.glGenTextures(3, textures, 0);

		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmaps[textureNummer], 0);

		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[1]);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmaps[textureNummer], 0);
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[2]);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR_MIPMAP_NEAREST);
		
		if(gl instanceof GL11) {
			gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmaps[textureNummer], 0);
			
		//
		} else {
			buildMipmap(gl, bitmaps[textureNummer]);
		}		
		
		for(int m=0;m<imageFieldIds.length;m++) {
			bitmaps[m].recycle();
		}
	}
	
	
	public void unloadText(GL10 gl) {
		gl.glDeleteTextures(3, textures,0);
		
	}

	private void buildMipmap(GL10 gl, Bitmap bitmap) {
		//
		int level = 0;
		//
		int height = bitmap.getHeight();
		int width = bitmap.getWidth();

		//
		while(height >= 1 || width >= 1) {
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, level, bitmap, 0);
			
			//
			if(height == 1 || width == 1) {
				break;
			}

			level++;

			//
			height /= 2;
			width /= 2;
			Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap, width, height, true);
			
			bitmap.recycle();
			bitmap = bitmap2;
		}
	}
		
	


	
	
		public static int newTextureID(GL10 gl) {
			int[] temp = new int[1];
			gl.glGenTextures(1, temp,0);
			return temp[0];
		}
	
	
		private int loadTexture(GL10 gl, Context context, int resource) {
		    
		    
		    int id = newTextureID(gl);
		    
		    
		    Matrix flip = new Matrix();
		    flip.postScale(1f, -1f);
		    
		  //nicht auf device Pixel skalieren
		    BitmapFactory.Options opts = new BitmapFactory.Options();
		    opts.inScaled = false;
		    
		    //texture laden
		    Bitmap temp = BitmapFactory.decodeResource(context.getResources(), resource, opts);
		    Bitmap bmp = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), flip, true);
		    temp.recycle();
		    
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
		
}
