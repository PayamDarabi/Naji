package ir.zelzele.general.adapters;

import android.content.Context;
import androidx.appcompat.widget.CardView;
import androidx.appcompat.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import java.util.Date;
import java.util.List;

import ir.zelzele.R;
import ir.zelzele.customview.CustomTextView;
import ir.zelzele.models.Target;
import ir.zelzele.utils.JalaliCalendar;
import ir.zelzele.utils.Tools;

/**
 * Created by Payam on 12/24/2017.
 */

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder>
        implements AdapterView.OnItemLongClickListener {

    private List<Target> targetList;

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        //  targetList.get(i).get();
        targetList.get(i);
        return false;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView txt_state, txt_date, txt_size, txt_depth; //txt_updateDate;
        public ImageView img_time, img_loc, img_show;
        public CardView card_view_item;
        public Context context;

        public MyViewHolder(View view) {
            super(view);
            try {
                context = view.getContext();
                txt_state = (CustomTextView) view.findViewById(R.id.txt_city);
                txt_date = (CustomTextView) view.findViewById(R.id.txt_Date);
                txt_size = (CustomTextView) view.findViewById(R.id.txt_size);
                txt_depth = (CustomTextView) view.findViewById(R.id.txt_depth);
                card_view_item = (CardView) view.findViewById(R.id.card_view_events_item);
                img_loc = (ImageView) view.findViewById(R.id.imgv_loc);
                img_time = (ImageView) view.findViewById(R.id.imgv_time);
               // img_show = (ImageView) view.findViewById(R.id.img_showDetails);
                //  txt_updateDate=(TextView)view.findViewById(R.id.txt_updateDate);

                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        getAdapterPosition();
                        return true;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    public ItemsAdapter(List<Target> targetList) {
        this.targetList = targetList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            Target target = targetList.get(position);
            Date date = Tools.convertToDate(target.getDate());

            String engDate = Tools.converNumberPersianToEnglish(target.getDate());
            String[] dateArray = engDate.split(" ");

            String[] myDate = dateArray[0].split("-");
            String myTime = dateArray[1];
            JalaliCalendar jalaliCalendar = new JalaliCalendar(Integer.parseInt(myDate[0]), Integer.parseInt(myDate[1]), Integer.parseInt(myDate[2]));
            String lbl_data = jalaliCalendar.getDayOfWeekDayMonthString() + " " + myDate[0] + " ساعت: " + myTime;
            lbl_data = Tools.convertEnglishNumbersToPersian(lbl_data);
            holder.txt_state.setText(target.getState());
            holder.txt_date.setText(lbl_data);
            holder.txt_size.setText(target.getSize() + " ریشتر");
            holder.txt_depth.setText(target.getDepth() + " کیلومتر");

            if (Float.valueOf(Tools.converNumberPersianToEnglish(target.getSize())) < 2.0) {
                holder.card_view_item.setBackgroundColor(holder.context.getResources().getColor(R.color.emGreen1));
                holder.txt_date.setTextColor(holder.context.getResources().getColor(R.color.emGreentextColor));
                holder.txt_size.setTextColor(holder.context.getResources().getColor(R.color.emGreentextColor));
                holder.txt_state.setTextColor(holder.context.getResources().getColor(R.color.emGreentextColor));
                holder.txt_depth.setTextColor(holder.context.getResources().getColor(R.color.emGreentextColor));
                holder.img_loc.setImageDrawable(holder.context.getResources().getDrawable(R.drawable.ic_location_searching));
                holder.img_time.setImageDrawable(holder.context.getResources().getDrawable(R.drawable.ic_access_time_black_24dp));
       //         holder.img_show.setImageDrawable(holder.context.getResources().getDrawable(R.drawable.ic_keyboard_arrow_left));
            } else if ((Float.valueOf(Tools.converNumberPersianToEnglish(target.getSize())) >= 2.0)
                    && Float.valueOf(Tools.converNumberPersianToEnglish(target.getSize())) < 3.0) {
                holder.card_view_item.setBackgroundColor(holder.context.getResources().getColor(R.color.emGreen2));
                holder.txt_date.setTextColor(holder.context.getResources().getColor(R.color.emGreentextColor));
                holder.txt_size.setTextColor(holder.context.getResources().getColor(R.color.emGreentextColor));
                holder.txt_state.setTextColor(holder.context.getResources().getColor(R.color.emGreentextColor));
                holder.txt_depth.setTextColor(holder.context.getResources().getColor(R.color.emGreentextColor));
                holder.img_loc.setImageDrawable(holder.context.getResources().getDrawable(R.drawable.ic_location_searching));
                holder.img_time.setImageDrawable(holder.context.getResources().getDrawable(R.drawable.ic_access_time_black_24dp));
      //          holder.img_show.setImageDrawable(holder.context.getResources().getDrawable(R.drawable.ic_keyboard_arrow_left));
            } else if ((Float.valueOf(Tools.converNumberPersianToEnglish(target.getSize())) >= 3.0)
                    && Float.valueOf(Tools.converNumberPersianToEnglish(target.getSize())) < 3.7) {
                holder.card_view_item.setBackgroundColor(holder.context.getResources().getColor(R.color.emYellow1));
                holder.txt_date.setTextColor(holder.context.getResources().getColor(R.color.emYellowtextColor));
                holder.txt_size.setTextColor(holder.context.getResources().getColor(R.color.emYellowtextColor));
                holder.txt_state.setTextColor(holder.context.getResources().getColor(R.color.emYellowtextColor));
                holder.txt_depth.setTextColor(holder.context.getResources().getColor(R.color.emYellowtextColor));
                holder.img_loc.setImageDrawable(holder.context.getResources().getDrawable(R.drawable.ic_location_searching));
                holder.img_time.setImageDrawable(holder.context.getResources().getDrawable(R.drawable.ic_access_time_black_24dp));
    //            holder.img_show.setImageDrawable(holder.context.getResources().getDrawable(R.drawable.ic_keyboard_arrow_left));
            } else if ((Float.valueOf(Tools.converNumberPersianToEnglish(target.getSize())) >= 3.7)
                    && Float.valueOf(Tools.converNumberPersianToEnglish(target.getSize())) < 4.5) {
                holder.card_view_item.setBackgroundColor(holder.context.getResources().getColor(R.color.emYellow2));
                holder.txt_date.setTextColor(holder.context.getResources().getColor(R.color.emYellowtextColor));
                holder.txt_size.setTextColor(holder.context.getResources().getColor(R.color.emYellowtextColor));
                holder.txt_state.setTextColor(holder.context.getResources().getColor(R.color.emYellowtextColor));
                holder.txt_depth.setTextColor(holder.context.getResources().getColor(R.color.emYellowtextColor));
                holder.img_loc.setImageDrawable(holder.context.getResources().getDrawable(R.drawable.ic_location_searching));
                holder.img_time.setImageDrawable(holder.context.getResources().getDrawable(R.drawable.ic_access_time_black_24dp));
    //            holder.img_show.setImageDrawable(holder.context.getResources().getDrawable(R.drawable.ic_keyboard_arrow_left));
            } else if (Float.valueOf(Tools.converNumberPersianToEnglish(target.getSize())) >= 4.5) {
                holder.card_view_item.setBackgroundColor(holder.context.getResources().getColor(R.color.emRed));
                holder.txt_date.setTextColor(holder.context.getResources().getColor(R.color.emRedtextColor));
                holder.txt_size.setTextColor(holder.context.getResources().getColor(R.color.emRedtextColor));
                holder.txt_state.setTextColor(holder.context.getResources().getColor(R.color.emRedtextColor));
                holder.txt_depth.setTextColor(holder.context.getResources().getColor(R.color.emRedtextColor));
                holder.img_loc.setImageDrawable(holder.context.getResources().getDrawable(R.drawable.ic_location_searching_white));
                holder.img_time.setImageDrawable(holder.context.getResources().getDrawable(R.drawable.ic_access_time_white_24dp));
    //            holder.img_show.setImageDrawable(holder.context.getResources().getDrawable(R.drawable.ic_keyboard_arrow_left));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return targetList.size();
    }
}