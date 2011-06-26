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
import forms.Text;

public class KarteViewRender extends GLSurfaceView implements Renderer {

	//private Karte karte;	
	private Karte selectedKarte = null;
	private int stapelzeiger=0;

	 private final float TOUCH_SCALE_FACTOR = 500.f/320;
	private Context context;
	
	private GL10 currentGl;


	private float z = 0.0f;			
	private float x = 0.0f;			
	private float y = 0.0f;					
	



	private float mPreviousX;
	private float mpreviousY;
	
	Player testplayer;
	List<Karte>playerKarten = new ArrayList<Karte>();
	List<Karte> stapel = new ArrayList<Karte>();
	List<Text> spielerNamen = new ArrayList<Text>();
	
	
	public KarteViewRender(Context context) {
		super(context);
		this.setRenderer(this);
		this.requestFocus();
		this.setFocusableInTouchMode(true);

		this.context = context;		
	//	karte = new Karte(1000);
		
		testplayer = new Player("charly");
		for(int i=0;i<8;i++) {
			playerKarten.add(new Karte(i,"bilpng.png"));
			
		}
		
		for(int j=0;j<4;j++) {
			stapel.add(new Karte(20-j,"back.png"));
		}
		
		for(int k=0;k<4;k++) {
			spielerNamen.add(new Text());
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
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);


		//Main Karten zeichnen
		for(int j =0; j <playerKarten.size();j++) {
			gl.glPushMatrix();
			Karte temp = playerKarten.get(j);			
			gl.glTranslatef(70*j,0, 0);
			gl.glScalef(temp.getScaleX(), temp.getScaleY(),1);
			temp.setPosX(70*j);
			temp.draw(gl, this.context);
			Log.d("LES POSITIONS de "+j, temp.getPosX()+"   "+temp.getPosY());
			gl.glPopMatrix();
			
		}
		
		
		//Stapel zeichnen
		
		float interval= 140;
		for(int i=0;i<stapel.size();i++) {
			gl.glPushMatrix();
			Karte temp2 = stapel.get(i);
			gl.glTranslatef(interval,140.f, 0);
			temp2.setPosX(70*i);
			temp2.setPosY(140.f);
			temp2.draw(gl, this.context);		
			gl.glPopMatrix();
			interval+=70;
		}
		
		
		
		for(int i=0;i<spielerNamen.size();i++) {
			gl.glPushMatrix();
			Text t = spielerNamen.get(i);
			gl.glTranslatef(157*i,270.f, 0);
		
			t.draw(gl, this.context,"PLAYER "+i);		
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
				 stapel.set(stapelzeiger, karte);
				 playerKarten.set(i, new Karte(i,"back.png"));
				 
				 this.selectedKarte=null;
				 stapelzeiger = (stapelzeiger+1)%4;
			 }
				 else {
				 karte.setScaleX(1.2f);
				 karte.setScaleY(1.2f);
				 karte.setSizeX(karte.getSizeX()*1.2f);
				 karte.setSizeY(karte.getSizeY()*1.2f);
				 karte.setSelect(true);
				 
				 
				 if(this.selectedKarte!=null) {
					 this.selectedKarte.setScaleX(1.f);
					 this.selectedKarte.setScaleY(1.f);
					 this.selectedKarte.setSizeX(this.selectedKarte.getSizeX()/1.2f);
					 this.selectedKarte.setSizeY(this.selectedKarte.getSizeY()/1.2f);

					 this.selectedKarte.setSelect(false);
				 }
				 
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