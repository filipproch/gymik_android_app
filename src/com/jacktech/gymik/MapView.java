package com.jacktech.gymik;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class MapView extends View {

private static final int INVALID_POINTER_ID = -1;

    private float mPosX;
    private float mPosY;

    private float mLastTouchX;
    private float mLastTouchY;
    private int mActivePointerId = INVALID_POINTER_ID;
    private boolean showColors = true;
    
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;
    
    private int currentLevel = 0;
    private JSONParser parser = new JSONParser();
    private JSONObject map;
    private Paint paint;
    private int coordsScaleFactor = 20;
    
    private DataWorker dataWorker;
    
    private ArrayList<Line> drawLines;
    private ArrayList<Text> drawTexts;
    private ArrayList<Room> drawRooms;
    
    public MapView(Context context) {
        this(context, null, 0);
    }

    public MapView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        dataWorker = new DataWorker(context);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Style.STROKE);
        paint.setAntiAlias(true);
        readMap();
        loadMap();
    }
    
    public void updateLevel(int level){
    	Log.i("DEBUG", "changing map floor : "+level);
    	this.currentLevel = level;
    	readMap();
    	loadMap();
    	super.invalidate();
    }
    
    public void setShowColors(boolean show){
    	this.showColors = show;
    }
    
    private void readMap(){
    	map = dataWorker.getMap(currentLevel);
		Log.i("DEBUG", "Map loaded");
    }
    
    private void loadMap(){
    	if(map != null){
	    	drawLines = new ArrayList<MapView.Line>();
	    	drawRooms = new ArrayList<MapView.Room>();
	    	drawTexts = new ArrayList<MapView.Text>();
	    	JSONArray lines = (JSONArray) map.get("lines");
	    	for(Object lineO : lines){
	    		JSONArray line = ((JSONArray)lineO);
	    		drawLines.add(new Line(((Number)line.get(0)).floatValue(), ((Number)line.get(1)).floatValue(), ((Number)line.get(2)).floatValue(), ((Number)line.get(3)).floatValue()));
	    	}
	    	
	    	JSONArray rooms = (JSONArray) map.get("rooms");
	    	if(rooms != null){
		    	for(Object roomO : rooms){
		    		JSONArray line = ((JSONArray)roomO);
		    		drawRooms.add(new Room(((Number)line.get(0)).floatValue(), ((Number)line.get(1)).floatValue(), ((Number)line.get(2)).floatValue(), ((Number)line.get(3)).floatValue(), Integer.toHexString(((Long)line.get(4)).intValue()), Integer.toHexString(((Long)line.get(5)).intValue()), Integer.toHexString(((Long)line.get(6)).intValue())));
		    	}
	    	}
	    	
	    	JSONArray texts = (JSONArray) map.get("names");
	    	if(texts != null){
		    	for(Object textO : texts){
		    		JSONObject room  = (JSONObject) textO;
		    		JSONArray pos = (JSONArray) room.get("position");
		    		drawTexts.add(new Text(((Number)pos.get(0)).floatValue(), ((Number)pos.get(1)).floatValue(), (String)room.get("text")));
		    	}
	    	}
    	}else{
    		Log.i("DEBUG","unable to read map data");
    	}
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //ScaleGestureDetector kontroluje udalosti
        mScaleDetector.onTouchEvent(ev);

        final int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_DOWN: {
            final float x = ev.getX();
            final float y = ev.getY();

            mLastTouchX = x;
            mLastTouchY = y;
            mActivePointerId = ev.getPointerId(0);
            break;
        }

        case MotionEvent.ACTION_MOVE: {
            final int pointerIndex = ev.findPointerIndex(mActivePointerId);
            final float x = ev.getX(pointerIndex);
            final float y = ev.getY(pointerIndex);

            // pohybujeme s mapou pouze pokud GestureDetector nedetekuje nejake gesto
            if (!mScaleDetector.isInProgress()) {
                final float dx = x - mLastTouchX;
                final float dy = y - mLastTouchY;

                mPosX += dx;
                mPosY += dy;

                invalidate();
            }

            mLastTouchX = x;
            mLastTouchY = y;

            break;
        }

        case MotionEvent.ACTION_UP: {
            mActivePointerId = INVALID_POINTER_ID;
            break;
        }

        case MotionEvent.ACTION_CANCEL: {
            mActivePointerId = INVALID_POINTER_ID;
            break;
        }

        case MotionEvent.ACTION_POINTER_UP: {
            final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) 
                    >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
            final int pointerId = ev.getPointerId(pointerIndex);
            if (pointerId == mActivePointerId) {
                final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                mLastTouchX = ev.getX(newPointerIndex);
                mLastTouchY = ev.getY(newPointerIndex);
                mActivePointerId = ev.getPointerId(newPointerIndex);
            }
            break;
        }
        }

        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate(mPosX, mPosY);
        canvas.scale(mScaleFactor, mScaleFactor);
        
        paint.setTextSize(12/10*coordsScaleFactor);
        paint.setFlags(Paint.LINEAR_TEXT_FLAG);
        
        for(Line l : drawLines){
        	canvas.drawLine(l.a.x*coordsScaleFactor, l.a.y*coordsScaleFactor, l.b.x*coordsScaleFactor, l.b.y*coordsScaleFactor, paint);
        }
        
        if(showColors){
	        for(Room r : drawRooms){
	        	paint.setColor(Color.parseColor("#ff"+r.r+r.g+r.b));
	        	paint.setStyle(Style.FILL);
	        	canvas.drawRect(r.p.x*coordsScaleFactor,r.p.y*coordsScaleFactor,r.p.x*coordsScaleFactor+r.w*coordsScaleFactor,r.p.y*coordsScaleFactor+r.h*coordsScaleFactor, paint);
	        }
        }
        paint.setStyle(Style.STROKE);
        paint.setColor(Color.BLACK);
        
        for(Text t : drawTexts){
        	canvas.drawText(t.name, t.p.x*coordsScaleFactor, t.p.y*coordsScaleFactor, paint);
        }
        
        canvas.restore();
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            //Omezeni na priblizeni/oddaleni
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));

            invalidate();
            return true;
        }
    }
    
    class Position2D {
    	public float x,y;
    	public Position2D(float x,float y){
    		this.x = x;
    		this.y = y;
    	}
    }
    
    class Line{
    	public Position2D a;
    	public Position2D b;
    	public Line(float x1, float y1,float x2, float y2){
    		a = new Position2D(x1, y1);
    		b = new Position2D(x2,y2);
    	}
    }
    
    class Room{
    	public Position2D p;
    	public float w,h;
    	public String r,g,b;
    	public Room(float x1, float y1,float w,float h,String r,String g,String b){
    		p = new Position2D(x1,y1);
    		this.w = w;
    		this.h = h;
    		this.r = r;
    		this.g = g;
    		this.b = b;
    		repairColors();
    	}
    	
    	private void repairColors(){
    		if(r.length() == 1)
    			r = "0"+r;
    		if(g.length() == 1)
    			g = "0"+g;
    		if(b.length() == 1)
    			b = "0"+b;
    	}
    }
    
    class Text{
    	public Position2D p;
    	public String name;
    	public Text(float x, float y,String name){
    		p = new Position2D(x,y);
    		this.name = name;
    	}
    }

}