package demo.project.landmark.adapter;

import android.content.Context;
import android.view.View;

import java.util.List;

import b.laixuantam.myaarlibrary.widgets.superadapter.SuperAdapter;
import b.laixuantam.myaarlibrary.widgets.superadapter.SuperViewHolder;
import demo.project.landmark.R;
import demo.project.landmark.database.table_note.NoteSaved;
import demo.project.landmark.model.NotesModel;

public class ListNoteAdapter extends SuperAdapter<NoteSaved> {

    public interface ListNoteAdapterListener {
        void onItemClick(NoteSaved item);
    }

    private ListNoteAdapterListener listener;

    public void setListener(ListNoteAdapterListener listener) {
        this.listener = listener;
    }

    public ListNoteAdapter(Context context, List<NoteSaved> items) {
        super(context, items, R.layout.row_item_note_saved);
    }

    @Override
    public void onBind(SuperViewHolder superViewHolder, int viewType, int layoutPosition, NoteSaved item) {
        superViewHolder.setText(R.id.tvNoteTitle, item.getTitle());
        superViewHolder.setText(R.id.tvNoteDes, item.getDescription());

        View btnGetDetailFeedback = superViewHolder.findViewById(R.id.rowItem);
        btnGetDetailFeedback.setOnClickListener(view -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }
}
