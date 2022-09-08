package com.shuvo.workweb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.shuvo.workweb.topuserrcycle.viewHolder;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;
import java.util.ArrayList;

public class topuserrcycle extends RecyclerView.Adapter<topuserrcycle.viewHolder> {
	Context context;
	ArrayList<topUserContractor> arrayList;

	public topuserrcycle(Context context, ArrayList arrayList) {
		this.context = context;
		this.arrayList = arrayList;
	}

	@Override
	public viewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		View inflate = LayoutInflater.from(context).inflate(R.layout.topuser_rycylesmple, arg0, false);

		viewHolder viewHolder = new viewHolder(inflate);

		return viewHolder;
	}

	@Override
	public void onBindViewHolder(viewHolder arg0, int arg1) {
		
		arg0.position.setText((arg1+1)+"");
		arg0.name.setText(arrayList.get(arg1).Name);
		arg0.earnings.setText(arrayList.get(arg1).Earnings);
		arg0.refer.setText(arrayList.get(arg1).Refer);
		
	}

	@Override
	public int getItemCount() {
		return arrayList.size();
	}

	public class viewHolder extends RecyclerView.ViewHolder {
		TextView position, name, earnings, refer;

		public viewHolder(@NonNull View view) {
			super(view);
			position = view.findViewById(R.id.top_position);

			name = view.findViewById(R.id.top_name);
			earnings = view.findViewById(R.id.top_earnings);
			refer = view.findViewById(R.id.top_refer);

		}

	}

}