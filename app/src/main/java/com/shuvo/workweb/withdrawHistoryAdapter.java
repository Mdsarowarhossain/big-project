package com.shuvo.workweb;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import com.shuvo.workweb.withdrawHistoryAdapter.viewHolder;

public class withdrawHistoryAdapter extends RecyclerView.Adapter<withdrawHistoryAdapter.viewHolder> {

	Context context;
	ArrayList<withdrawHistoryContractore> arrayList;

	public withdrawHistoryAdapter(Context context, ArrayList <withdrawHistoryContractore> arrayList) {
		this.context = context;
		this.arrayList = arrayList;
	}

	@Override
	public viewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {

		View inflate = LayoutInflater.from(context).inflate(R.layout.withdraw_history_sample_layout, arg0, false);

		viewHolder viewHolder = new viewHolder(inflate);

		return viewHolder;
	}

	@Override
	public void onBindViewHolder(viewHolder arg0, int arg1) {
		int payid = Integer.parseInt(arrayList.get(arg1).id)+1000;
		arg0.id.setText("#" +payid+ "\n" + arrayList.get(arg1).date);
		arg0.number_method_date.setText(arrayList.get(arg1).phone + "\n" + arrayList.get(arg1).method);
		arg0.amount_status.setText(arrayList.get(arg1).amount + "\n" + arrayList.get(arg1).status);

	}

	@Override
	public int getItemCount() {
		return arrayList.size();
	}

	public class viewHolder extends RecyclerView.ViewHolder {
		TextView id, number_method_date, amount_status;

		public viewHolder(@NonNull View view) {
			super(view);
			id = view.findViewById(R.id.withdraw_id);
			number_method_date = view.findViewById(R.id.withdraw__Date_number_method);
			amount_status = view.findViewById(R.id.withdraw_Amount_status);

		}
	}
}