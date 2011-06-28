package renders;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import MobileAnwendungen.Schafkopf.GameConnection;
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
	private Text infoBox = null;
	private int stapelzeiger=0;
	private Karte table = new Karte("tableh.png");
	 private final float TOUCH_SCALE_FACTOR = 500.f/320;
	private Context context;
	private List<Text> spielInfo = new ArrayList<Text>();
	private GameConnection gameConnection;
	private GL10 currentGl;


	private float z = 0.0f;			
	private float x = 0.0f;			
	private float y = 0.0f;					
	


	

	private float mPreviousX;
	private float mpreviousY;
	
	private Player thisPlayer;
	private List<Karte> playerKarten = new ArrayList<Karte>();
	private List<Karte> stapel = new ArrayList<Karte>();
	private List<Text> spielerNamen = new ArrayList<Text>();
	private List<Text> spielerScores = new ArrayList<Text>();
	
	
	
	public KarteViewRender(Context context,GameConnection game ,String[] mycards,String[] spielerNamen,int[] spielerScores) {
		super(context);
		this.setRenderer(this);
		this.requestFocus();
		this.setFocusableInTouchMode(true);

		this.context = context;		
		
		this.gameConnection = game;
		thisPlayer = new Player("charly");

		setMyCards(mycards);
		setNames(spielerNamen);
		setPoint(spielerScores);
		
		for(int j=0;j<4;j++) {
			stapel.add(new Karte("back.png"));
			spielInfo.add(new Text());
		}

	}

	
	public void setGameTyp(String typ) {
		
		
			Text t1 = new Text();
			t1.setAnzeigeText("SpielTyp");
			spielInfo.add(t1);
			
			Text t2 = new Text();
			t2.setAnzeigeText(typ);
			spielInfo.add(t2);
			
			Text t3 = new Text();
			t3.setAnzeigeText("Runde Nr:");
			
			spielInfo.add(t3);
		
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

		
		
		//Table
			gl.glPushMatrix();
			gl.glTranslatef(0,0, 0);
			gl.glScalef(11,3,1);
		//	table.draw(gl, this.context);
			gl.glPopMatrix();
			
		
		
		

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
		
		float interval= 50;
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
		
	
		//spielInformationen Zeichnen
		float interval2= 140;
		for(int i=0;i<spielInfo.size();i++) {
			gl.glPushMatrix();
			Text t1 = spielInfo.get(i);
			gl.glTranslatef(370,interval2, 0);
			gl.glScalef(1.8f, 1f, 1f);
			t1.draw(gl, this.context,t1.getAnzeigeText(),15);
			gl.glPopMatrix();
			interval2+=25.5;
		}
		
		
		//SpielerNamenZeichnen
		
		for(int i=0;i<spielerNamen.size();i++) {
			gl.glPushMatrix();
			Text t = spielerNamen.get(i);
			gl.glTranslatef(157*i,295.f, 0);
		
			t.draw(gl, this.context,t.getAnzeigeText(),15);		
			gl.glPopMatrix();
		}
		
		
		//SpielerScore Zeichnen
		for(int i=0;i<spielerScores.size();i++) {
			gl.glPushMatrix();
			Text t = spielerScores.get(i);
			gl.glTranslatef(157*i,268.f, 0);
			
			t.draw(gl, this.context,t.getAnzeigeText(),15);
			//t.draw(gl, this.context,new Random().nextInt(15)+"",13);
			
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
				 playerKarten.set(i, new Karte("back.png"));
				 gameConnection.sendCard(karte.getUrl(), 2+"");
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
			Log.d("BOOOOO",this.getWidth()+"  "+this.getHeight());
		}
	}
		return true;
	}
	
	
	
	public void setMyCards(String[] cards) {
		for(int i=0;i<cards.length;i++) {
			playerKarten.add(new Karte(cards[i]));
		}
	}
	
	
	public void setNames(String[] namen) {
		for(int i=0;i<namen.length;i++) {
			Text temp = new Text();
			String merkmal = i==0?"":" (du)";
			temp.setAnzeigeText(namen[i]+merkmal);
			spielerNamen.add(temp);
		}
	}
	
	public void setPoint(int[] points) {
		
		for(int i=0;i<points.length;i++) {
			Text tmp = new Text();
			
			tmp.setAnzeigeText(points[i]+"");
			spielerScores.add(tmp);
		}
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