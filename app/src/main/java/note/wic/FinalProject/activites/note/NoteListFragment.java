package note.wic.FinalProject.activites.note;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import note.wic.FinalProject.R;
import note.wic.FinalProject.activites.dashboard.DashBoardActivity;
import note.wic.FinalProject.model.Folder;
import note.wic.FinalProject.utils.ShadowLayout;


public class NoteListFragment extends Fragment  {
    public static final String FOLDER = "FOLDER";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.zero_notes_view)
    View zeroNotesView;
    NoteAdapter adapter;
    Folder folder;
    @BindView(R.id.mdw_view_tv)
    TextView mdwViewTv;
    @BindView(R.id.arrow_down_iv0)
    ImageView arrowDownIv0;
    @BindView(R.id.spinner_parent_rl1)
    RelativeLayout spinnerParentRl1;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.arrow_down_iv)
    ImageView arrowDownIv;
    @BindView(R.id.spinner_parent_rl)
    RelativeLayout spinnerParentRl;
    @BindView(R.id.spinner_sl)
    ShadowLayout spinnerSl;
    @BindView(R.id.etSearch)
    SearchView etSearch;
    @BindView(R.id.dash_parent_rl)
    RelativeLayout dashParentRl;
    @BindView(R.id.categories_rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.add_event_iv)
    ImageView addEventIv;
    @BindView(R.id.rel_ll)
    RelativeLayout relLl;
    @BindView(R.id.no_event_iv)
    ImageView noEventIv;
    @BindView(R.id.no_event_tv)
    TextView noEventTv;
    @BindView(R.id.can_add_event_tv)
    TextView canAddEventTv;
    private int clickCount = 0;
    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_notelist, container, false);
        ButterKnife.bind(this, view);
        folder = getArguments() == null ? null : (Folder) getArguments().getParcelable(NoteListFragment.FOLDER);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (folder != null) mToolbar.setTitle(folder.getName());
        mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((DashBoardActivity) getActivity()).mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        StaggeredGridLayoutManager slm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        slm.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mRecyclerView.setLayoutManager(slm);
        adapter = new NoteAdapter(zeroNotesView, folder);
        mRecyclerView.setAdapter(adapter);
        adapter.loadFromDatabase();

        etSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String s = newText;
                adapter.getFilter().filter(newText);
                return false;
            }
        });


    }

    @OnClick(R.id.add_event_iv)
    void clickNewNoteButton() {
        Intent intent = new NoteActivityIntentBuilder().build(getContext());
        this.startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.registerEventBus();
    }
    @OnClick(R.id.spinner_sl)
    public void onViewClicked() {

        clickCount++;
        if (clickCount == 0) {
            setupOrderByDate();
        } else if (clickCount == 1) {
            setupOrderByTitle();
        } else if (clickCount == 2) {
            setupOrderByDesc();
            clickCount = -1;

        }
    }
    @Override
    public void onStop() {
        super.onStop();
        adapter.unregisterEventBus();
    }

    private void setupOrderByDesc() {
        spinnerParentRl1.setBackgroundResource(R.drawable.custom_daily_view_background);
        spinnerSl.setShadowColor(getResources().getColor(R.color.daily_view_shadow));
        mdwViewTv.setText("BY DESC");
        adapter.getByDesc();

    }

    private void setupOrderByTitle() {
        spinnerParentRl1.setBackgroundResource(R.drawable.custom_daily_view_background);
        spinnerSl.setShadowColor(getResources().getColor(R.color.daily_view_shadow));
        mdwViewTv.setText("BY TITLE");
        adapter.getByTitle();
    }

    private void setupOrderByDate() {
        spinnerParentRl1.setBackgroundResource(R.drawable.custom_daily_view_background);
        spinnerSl.setShadowColor(getResources().getColor(R.color.daily_view_shadow));
        mdwViewTv.setText("BY DATE");
        adapter.getByDate();
    }


}
