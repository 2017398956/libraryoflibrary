package com.nfl.libraryoflibrary.view.CustomCrop;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.nfl.libraryoflibrary.R;

public class ShowImageActivity extends Activity {
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        mImageView = (ImageView) findViewById(R.id.id_showImage);
        byte[] b = getIntent().getByteArrayExtra("bitmap");
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        if (bitmap != null) {
            mImageView.setImageBitmap(bitmap);
        }
    }
}
