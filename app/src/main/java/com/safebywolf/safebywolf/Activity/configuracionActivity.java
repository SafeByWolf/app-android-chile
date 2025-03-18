package com.safebywolf.safebywolf.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.safebywolf.safebywolf.R;
import com.safebywolf.safebywolf.Activity.TensorFlow.DetectorActivity;

public class configuracionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        /*
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://worldtimeapi.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call callTime = jsonPlaceHolderApi.getTime();
        callTime.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (!response.isSuccessful()) {
                    Log.v("dattimecs",""+response.code());

                    return;
                }
                Gson gson = new Gson();
                String jsonString = gson.toJson(response.body());
                Log.v("dattimecs",jsonString);
                try {
                    JSONObject objeto = new JSONObject(jsonString);
                    //JSONArray data = new JSONArray(jsonString);
                    //JSONObject obj = data.getJSONObject(0);

                    String datetime = objeto.getString("datetime");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    Date date=dateFormat.parse(datetime);
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.v("dattimecs","eror al obtener fecha");
                return;
            }
        });*/

//        Long date = getIntent().getExtras().getLong("date");
        EditText editTextFrames=findViewById(R.id.frames);
        EditText editTextResolucionX=findViewById(R.id.resolucionX);
        EditText editTextResolucionY=findViewById(R.id.resolucionY);
        CheckBox checkBoxFoco=findViewById(R.id.checkBoxFoco);
        EditText editTextZoom=findViewById(R.id.zoom);
        EditText editTextBound=findViewById(R.id.bounds);
        Button buttonAceptar=findViewById(R.id.aceptar);
        buttonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(configuracionActivity.this, DetectorActivity.class);
                if(editTextFrames!=null && !editTextFrames.getText().toString().isEmpty()){
                    intent.putExtra("frames",editTextFrames.getText().toString());
                } else{
                    editTextFrames.setText("30");
                    intent.putExtra("frames",editTextFrames.getText().toString());
                }
                if(editTextResolucionX!=null && !editTextResolucionX.getText().toString().isEmpty()){
                    intent.putExtra("resolucionX",editTextResolucionX.getText().toString());

                } else{
                    editTextResolucionX.setText("1080");
                    intent.putExtra("resolucionX",editTextResolucionX.getText().toString());
                }
                if(editTextResolucionY!=null && !editTextResolucionY.getText().toString().isEmpty()){
                    intent.putExtra("resolucionY",editTextResolucionY.getText().toString());
                } else{
                    editTextResolucionY.setText("720");
                    intent.putExtra("resolucionY",editTextResolucionY.getText().toString());
                }

                intent.putExtra("foco",checkBoxFoco.isChecked());

                if(editTextZoom!=null && !editTextZoom.getText().toString().isEmpty()){
                    intent.putExtra("zoom",editTextZoom.getText().toString());
                } else{
                    editTextZoom.setText("0");
                    intent.putExtra("zoom",editTextZoom.getText().toString());
                }

                if(editTextBound!=null && !editTextBound.getText().toString().isEmpty()){
                    intent.putExtra("bound",editTextBound.getText().toString());
                } else{
                    editTextBound.setText("0");
                    intent.putExtra("bound",editTextBound.getText().toString());
                }
                //intent.putExtra("date",date);
                startActivity(intent);
            }
        });
    }



}
