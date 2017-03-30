package trace.traceapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class StartScreenActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        //moves to main activity on click
        Button enterName = (Button) findViewById(R.id.btn_Enter);
        enterName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent Start = new Intent(StartScreenActivity.this, MainActivity.class);

                startActivity(Start);
            }


        });

    }


}
