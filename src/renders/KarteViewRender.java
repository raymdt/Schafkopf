package renders;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import forms.Karte;
import forms.Player;

public class KarteViewRender extends GLSurfaceView implements Renderer {

	//private Karte karte;	
	private Karte selectedKarte = null;

	 private final float TOUCH_SCALE_FACTOR = 500.f/320;
	private Context context;
	
	private GL10 currentGl;


	private float z = 0.0f;			
	private float x = 0.0f;			
	private float y = 0.0f;					
	
	private float xscale=1f;
	private float yscale=1f;
	private float zscale=1f;


	private float mPreviousX;
	private float mpreviousY;
	
	Player testplayer;
	List<Karte>playerKarten = new ArrayList<Karte>();
	List<Karte> statpel = new ArrayList<Karte>();
	
	
	public KarteViewRender(Context context) {
		super(context);
		this.setRenderer(this);
		this.requestFocus();
		this.setFocusableInTouchMode(true);

		this.context = context;		
	//	karte = new Karte(1000);
		
		testplayer = new Player("charly");
		for(int i=0;i<8;i++) {
			playerKarten.add(new Karte(i));
		}
		

	}


	public void onSurfaceCreated(GL10 gl, EGLConfig config) {		


		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glShadeModel(GL10.GL_SMOOTH); 			
		gl.glClearColor(0.f, 0.f, 0.f, 1.0f); 	
		gl.glClearDepthf(1.0f); 					
		gl.glEnable(GL10.GL_DEPTH_TEST); 			
		gl.glDepthFunc(GL10.GL_LEQUAL); 	
		
		
		

	}


	public void onSurfaceChanged(GL10 gl, int width, int height) {
			
	}


	public void onDrawFrame(GL10 gl) {


		currentGl = gl;
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);	
		gl.glViewport(0, 0, this.getWidth(), this.getHeight()); 
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0,this.getWidth(),0,this.getHeight(), 1, -1);


		
		for(int j =0; j <playerKarten.size();j++) {
			gl.glPushMatrix();
			Karte temp = playerKarten.get(j);			
			gl.glTranslatef(72*j,0, 0);
			gl.glScalef(temp.getScaleX(), temp.getScaleY(),1);
			temp.setPosX(72*j);
			temp.draw(gl, this.context,"lasto.jpg");
			Log.d("LES POSITIONS de "+j, temp.getPosX()+"   "+temp.getPosY());
			gl.glPopMatrix();
		
			
		}

		

	setRenderMode(RENDERMODE_WHEN_DIRTY);

	}


	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		//
		if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {

			}		 
		requestRender();

		return true;
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		
		float x = event.getX();
		float y = this.getHeight()-event.getY();	
		
		
		Log.d("ON A CLICKOOO", x+"  "+y);
		
		
		for(int i=0;i<playerKarten.size();i++) {
		
			Karte karte = playerKarten.get(i);
		if(karte.isPointInclude(x,y)) {
		
		 switch (event.getAction()) {
	
		   
			 case  MotionEvent.ACTION_UP:{
				 
				 if(karte.isSelect()) {
				 karte.setScaleX(1.f);
				 karte.setScaleY(1.f);
				 karte.setSizeX(karte.getSizeX()/1.2f);
				 karte.setSizeY(karte.getSizeY()/1.2f);

				 karte.setSelect(false);
			 }
				 else {
				 karte.setScaleX(1.3f);
				 karte.setScaleY(1.3f);
				 karte.setSizeX(karte.getSizeX()*1.2f);
				 karte.setSizeY(karte.getSizeY()*1.2f);
				 karte.setSelect(true);
				 this.selectedKarte= karte;
				 
				 }
				 requestRender();
			break;
			 }
		 

		 
		 
		 
		 
		 
	}
		

}
		else {
			Log.d("BOOOOO","Nicht select");
		}
	}
		return true;
	}
}







//case MotionEvent.ACTION_MOVE:
//{
//
//float dx= x - mPreviousX;
//float dy= y - mpreviousY;
//
//
//karte.setPosX(karte.getPosX()+dx);
//karte.setPosY(karte.getPosY()+dy);
//requestRender();
//	
//
//mPreviousX=x;
//mpreviousY=y;
//
//break;
//}