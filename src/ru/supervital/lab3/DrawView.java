package ru.supervital.lab3;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewDebug.HierarchyTraceType;

public class DrawView extends View {
    Paint paint = new Paint();
    Matrix matrix = new Matrix();
    
    protected int height;
    protected int width;
    
    public DrawView(Context context, AttributeSet attrs) {
    	super(context, attrs);
    }
   
    public DrawView(Context context) {
        this(context, null);
    }

    @Override
    protected void onMeasure(int widthSpecId, int heightSpecId)
    {
        this.height = View.MeasureSpec.getSize(heightSpecId);
        this.width = View.MeasureSpec.getSize(widthSpecId);
        
        setMeasuredDimension(width, height);
    }
    
    
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
    }
    
    public void convertCoord(ArrayList<CurrDynam> aList, int aMax) {
        for (int i=0; i < aList.size(); i++){
        	aList.get(i).Rate = aMax - aList.get(i).Rate;
        }
    }
    
 
    

}