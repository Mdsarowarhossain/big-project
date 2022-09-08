package com.shuvo.workweb;

import android.content.Context;
import android.provider.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.shuvo.workweb.referhistoryAdapterrcyle.viewHolder;
import java.util.ArrayList;

public class referhistoryAdapterrcyle extends RecyclerView.Adapter<referhistoryAdapterrcyle.viewHolder> {
	Context context;
	ArrayList<referhistorycontractore> arrayList;

	public referhistoryAdapterrcyle(Context context, ArrayList arrayList) {
		this.context = context;
		this.arrayList = arrayList;
	}

	@Override
	public viewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		View inflate = LayoutInflater.from(context).inflate(R.layout.referhistorylayout, arg0, false);

		viewHolder viewHolder = new viewHolder(inflate);

		return viewHolder;
	}

	@Override
	public void onBindViewHolder(viewHolder arg0, int arg1) {
		arg0.name.setText(arrayList.get(arg1).Name);
		arg0.statusAndpoint.setText(arrayList.get(arg1).status + "\n" + arrayList.get(arg1).point);
		arg0.date.setText(arrayList.get(arg1).date+ "\n" + arrayList.get(arg1).Reasom);

	}

	@Override
	public int getItemCount() {
		return arrayList.size();

	}

	public class viewHolder extends RecyclerView.ViewHolder {
		TextView name, statusAndpoint, date;

		public viewHolder(@NonNull View view) {
			super(view);
			name = view.findViewById(R.id.refer_name);
			statusAndpoint = view.findViewById(R.id.refer_PointAndstatus);
			date = view.findViewById(R.id.refer_date);

		}

	}
}