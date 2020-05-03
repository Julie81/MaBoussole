package fr.gleizes.maboussole;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CompasView extends View {

    private float northOrientation = 0;

    //initialisation des pinceaux
    private Paint circlePaint;
    private Paint northPaint;
    private Paint southPaint;

    private Path trianglePath;

    public CompasView(Context context) {
        super(context);
        initView ();
    }

    public CompasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView ();
    }

    public CompasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView ();
    }

    public float getNorthOrientation() {
        return northOrientation;
    }

    public void setNorthOrientation(float rotation) {
        //si l'orientation change
        if (rotation != this.northOrientation )
        {
            this.northOrientation = rotation;
            //on met à jour l'affichage de la boussole
            this.invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //on récupère les dimensions de l'écran
        int measuredWidth = measure(widthMeasureSpec);
        int measuredHeight = measure(heightMeasureSpec);

        //on définit les dimensions du carré dans lequel on dessinera
        int d= Math.min(measuredHeight, measuredWidth);
        setMeasuredDimension(d,d);
    }

    private int measure (int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode== MeasureSpec.UNSPECIFIED) {
            //si la taille n'est pas définit, on initialise à 150
            result = 150;
        }
        else {
            //si elle est définie, on la récupère
            result=specSize;
        }

        //on retourne le résultat
        return result;
    }


    @SuppressLint("ResourceAsColor")
    private  void  initView () {

        //crayon pour l'arrière-plan de la boussole
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int circleColor = this.getResources().getColor(R.color.compassCircle);
        circlePaint.setColor(circleColor);

        //crayon pour l'aiguille nord
        northPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int northColor = this.getResources().getColor(R.color.northPointer);
        northPaint.setColor(northColor);

        //crayon pour l'aiguille sud
        southPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int southColor = this.getResources().getColor(R.color.southPointer);
        southPaint.setColor(southColor);

        //Path pour dessiner les aiguilles
        trianglePath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //on détermine le point au centre de notre vue
        int centerX = getMeasuredWidth()/2;
        int centerY = getMeasuredHeight()/2;

        //on détermine le diamètre du cercle à l'arrière-plan de la boussole
        int radius=Math.min(centerX, centerY);

        //on dessine un cercle avec le pinceau CirclePaint
        canvas.drawCircle(centerX,centerY,radius,circlePaint);

        //on sauvegarde la position initiale du canvas
        canvas.save();

        //On tourne le canvas pour que le nord pointe vers le haut
        canvas.rotate(-northOrientation, centerX, centerY);

        //On crée l'aiguille du Nord
        trianglePath.reset();
        trianglePath.moveTo(centerX, 10);
        trianglePath.lineTo(centerX-10, centerY);
        trianglePath.lineTo(centerX+10, centerY);

        canvas.drawPath(trianglePath, northPaint);

        //on tourne notre vue de 180° et on dessine l'aiguille Sud
        canvas.rotate(180, centerX, centerY);
        canvas.drawPath(trianglePath,southPaint);

        //on restore la position initiale
        canvas.restore();
    }
}
