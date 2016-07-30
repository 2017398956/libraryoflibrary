package com.nfl.libraryoflibrary.view.CustomCrop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.nfl.libraryoflibrary.R;

import java.io.ByteArrayOutputStream;

public class CustomCropActivity extends Activity {
    private ClipImageLayout mClipImageLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_crop);
        mClipImageLayout = (ClipImageLayout) findViewById(R.id.id_clipImageLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.crop, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId() ;
        if(itemId == R.id.id_action_clip){
            Bitmap bitmap = mClipImageLayout.clip();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] datas = baos.toByteArray();

            Intent intent = new Intent(this, ShowImageActivity.class);
            intent.putExtra("bitmap", datas);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
