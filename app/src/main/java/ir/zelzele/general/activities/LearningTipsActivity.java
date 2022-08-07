package ir.zelzele.general.activities;

import android.os.Bundle;
import androidx.core.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ir.zelzele.R;
import ir.zelzele.customview.CustomTextView;
import ir.zelzele.general.adapters.LearnsExpandableListAdapter;
import ir.zelzele.models.LearningTips;
import ir.zelzele.utils.AppInfoProvider;
import ir.zelzele.utils.Tools;
import ir.zelzele.webservices.ApiMethodCaller;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LearningTipsActivity extends AppCompatActivity {
    LearnsExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    static List<String> listDataHeader;
    static HashMap<String, List<String>> listDataChild;
    AVLoadingIndicatorView prg_learns;
    CustomTextView txt_nothing;
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_tips_actiity);
        getSupportActionBar().setTitle(R.string.LearningTips);

        expListView = (ExpandableListView) findViewById(R.id.lvExp_learns);
        prg_learns = (AVLoadingIndicatorView) findViewById(R.id.prg_learns);
        txt_nothing=(CustomTextView) findViewById(R.id.txt_nothing);
        // preparing list data
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLearns_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    prepareListData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {
                //      mSwipeRefreshLayout.setRefreshing(true);
                try {
                    prepareListData();
                } catch (Exception e) {
                    e.printStackTrace();
                    txt_nothing.setVisibility(View.VISIBLE);
                }
            }
        });

        txt_nothing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    prepareListData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void prepareListData() {
        prg_learns.show();
        if(!Tools.internetConnect()){
            expListView.setVisibility(View.GONE);
            txt_nothing.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setRefreshing(false);
            Toast.makeText(this, R.string.internet_Fail, Toast.LENGTH_SHORT).show();
        }
        else {
             ApiMethodCaller.getInstance(true, getApplicationContext()).getLearningTips(AppInfoProvider.getDeviceName(), "android", new
                    Callback<List<LearningTips>>() {
                        @Override
                        public void onResponse(Call<List<LearningTips>> call, Response<List<LearningTips>> response) {
                            prg_learns.hide();
                            listDataHeader = new ArrayList<String>();
                            listDataChild = new HashMap<String, List<String>>();
                            final List<LearningTips> learningTips = response.body();
                            int i = 1;
                            for (LearningTips tips : learningTips) {
                                String title = Tools.convertEnglishNumbersToPersian
                                        (String.valueOf(i++)) + ") " + tips.getTittle();
                                listDataHeader.add(title);
                                List<String> names = new ArrayList<String>();
                                names.add(tips.getMessage());
                                listDataChild.put(
                                        title,
                                        names);
                            }
                            expListView.setVisibility(View.VISIBLE);
                            txt_nothing.setVisibility(View.GONE);

                            listAdapter = new LearnsExpandableListAdapter(getApplicationContext(),
                                    listDataHeader, listDataChild);

                            // setting list adapter

                            expListView.setAdapter(listAdapter);

                            expListView.expandGroup(0);
                            expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
                                @Override
                                public void onGroupCollapse(int groupPosition) {
                                }
                            });
                            expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                                @Override
                                public boolean onGroupClick(ExpandableListView parent, View v,
                                                            int groupPosition, long id) {
                                    return false;
                                }
                            });

                            mSwipeRefreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void onFailure(Call<List<LearningTips>> call, Throwable t) {
                            prg_learns.hide();
                            Toast.makeText(LearningTipsActivity.this, R.string.fail, Toast.LENGTH_SHORT).show();
                            txt_nothing.setVisibility(View.VISIBLE);
                            expListView.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    });
        }
    }
}
