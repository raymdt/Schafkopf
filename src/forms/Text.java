package forms;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.opengl.GLUtils;

public class Text {

	private FloatBuffer vertexBuffer;
	private FloatBuffer textureBuffer;



	public int texid;		
	private float vertices[] = { 
			-0.6f, -0.7f, 0.0f, 	//Links unten
			0.6f, -0.7f, 0.0f, 		//recht unten
			-0.6f, 0.7f, 0.0f, 		//link Oben
			0.6f, 0.7f, 0.0f 		//recht oben
	};



	private float texture[] = {    		
			0.0f, 1.0f,     
			1.0f, 1.0f,		
			0.0f, 0.0f,		
			1.0f, 0.0f };	


	public Text() {


		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);



		byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);

	}



	public void draw(GL10 gl,Context context,String text) {	

		
		gl.glFrontFace(GL10.GL_CW);

		
	int id = loadtext(gl, context, text);


		//////////////////SCHRIFT/////////////////////////////////////
		
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);


		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, id);
	
		

	}

	
public int loadtext(GL10 gl , Context context, String text) {
		
		
		int id = newTextureID(gl);

		Matrix flip = new Matrix();
		flip.postScale(1f, -1f);

	
		BitmapFactory.Options opts = new BitmapFactory.Options();
		 opts.inScaled = false;
		
		 Bitmap bitmap = Bitmap.createBitmap(64,32, Bitmap.Config.RGB_565);
		 bitmap.eraseColor(Color.GRAY); 
		 Canvas canvas = new Canvas(bitmap);
		 Paint textPaint = new Paint(); 
		 textPaint.setTextSize(8); 
		 textPaint.setTextAlign(Align.CENTER);
		 textPaint.setAntiAlias(true);
		// textPaint.setARGB(255, 255, 255, 255); 
		 textPaint.setColor(Color.BLUE);
		 canvas.drawText(text, 90, 20, textPaint); 
		
			gl.glBindTexture(GL10.GL_TEXTURE_2D, id);

			// Set all of our texture parameters:
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR_MIPMAP_NEAREST);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR_MIPMAP_NEAREST);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);

			//mipmap generieren und laden
			for(int level=0, height = bitmap.getHeight(), width = bitmap.getWidth(); true; level++) {
				// bitmap in GPU
				GLUtils.texImage2D(GL10.GL_TEXTURE_2D, level, bitmap, 0);

				//Aufhören wenn die Texture 1x1 ist
				if(height==1 && width==1) break;

				// Dimensionen neu anpassen
				width >>= 1; height >>= 1;
				if(width<1)  width = 1;
				if(height<1) height = 1;

				Bitmap bmp2 = Bitmap.createScaledBitmap(bitmap, width, height, true);
				bitmap.recycle();
				bitmap = bmp2;
			}

			bitmap.recycle();

			return id;

		 
	}
	
	
	
	

	public static int newTextureID(GL10 gl) {
		int[] temp = new int[1];
		gl.glGenTextures(1, temp,0);
		return temp[0];
	}

	
	
	
	
	
	
	
}
