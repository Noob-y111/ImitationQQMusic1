package com.example.imitationqqmusic.model.tools;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class Rotate3DAnimation extends Animation {

    private float fromDegress;
    private float toDegress;
    private float centerX, centerY, centerZ;
    private Camera camera;
    private boolean isReverse;

    public Rotate3DAnimation(float fromDegress, float toDegress, float centerX, float centerY, float centerZ, boolean isReverse){
        this.fromDegress = fromDegress;
        this.toDegress = toDegress;
        this.centerX = centerX;
        this.centerY = centerY;
        this.centerZ = centerZ;
        this.isReverse = isReverse;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight){
        super.initialize(width, height, parentWidth, parentHeight);
        this.camera = new Camera();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation transformation){
        float newFromDegress = fromDegress;
        float centerDegress = newFromDegress + ((toDegress - fromDegress) * interpolatedTime);
        float nCenterX = centerX;
        float nCenterY = centerY;
        Camera nCamera = camera;
        Matrix matrix = transformation.getMatrix();
        nCamera.save();

        if (isReverse){
            nCamera.translate(0.0f, 0.0f, centerZ * interpolatedTime);
        }else {
            nCamera.translate(0.0f, 0.0f, centerZ * (1.0f - interpolatedTime));
        }


        nCamera.rotateY(centerDegress);
        nCamera.getMatrix(matrix);
        nCamera.restore();
        matrix.preTranslate(-nCenterX, -nCenterY);
        matrix.postTranslate(nCenterX, nCenterY);
    }
}

