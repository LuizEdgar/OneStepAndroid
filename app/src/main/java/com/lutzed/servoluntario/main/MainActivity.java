package com.lutzed.servoluntario.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.lutzed.servoluntario.R;
import com.lutzed.servoluntario.api.Api;
import com.lutzed.servoluntario.models.Opportunity;
import com.lutzed.servoluntario.opportunity.OpportunityActivity;
import com.lutzed.servoluntario.util.AuthHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    Intent intent = new Intent(MainActivity.this, OpportunityActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_dashboard:
                    Api.getClient(AuthHelper.getInstance(MainActivity.this).getUser()).getOpportunity(11l).enqueue(new Callback<Opportunity>() {
                        @Override
                        public void onResponse(Call<Opportunity> call, Response<Opportunity> response) {
                            Intent intent = new Intent(MainActivity.this, OpportunityActivity.class);
                            intent.putExtra(OpportunityActivity.EXTRA_OPPORTUNITY, response.body());
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<Opportunity> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });

                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    AuthHelper.getInstance(getApplicationContext()).signout();
                    finish();
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
