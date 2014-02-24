package ru.supervital.lab3;

import java.util.ArrayList;

import ru.supervital.lab3.R;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RateArrayAdapter extends ArrayAdapter <Rate> {
    private final Activity context;
    private final ArrayList<Rate> rates;

    public RateArrayAdapter(Activity context, ArrayList<Rate> rates) {
        super(context, R.layout.rowlayout, rates);
        this.context = context;
        this.rates = rates;
    }

	// Класс для сохранения во внешний класс и для ограничения доступа
    // из потомков класса
    static class ViewHolder {
        public ImageView imageView;
        public TextView txtCode;
        public TextView txtRate;
        public TextView txtNominal;
        public TextView txtName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // ViewHolder буферизирует оценку различных полей шаблона элемента

        ViewHolder holder;
        // Очищает сущетсвующий шаблон, если параметр задан
        // Работает только если базовый шаблон для всех классов один и тот же
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.rowlayout, null, true);
            holder = new ViewHolder();
            holder.imageView =  (ImageView) rowView.findViewById(R.id.icon);
            holder.txtCode =    (TextView) rowView.findViewById(R.id.txtCode);
            holder.txtRate =    (TextView) rowView.findViewById(R.id.txtRate);
            holder.txtNominal = (TextView) rowView.findViewById(R.id.txtNominal);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }
        holder.txtCode.setText((CharSequence) rates.get(position).Code);
        holder.txtRate.setText((CharSequence) String.valueOf(rates.get(position).Rate));
        holder.txtNominal.setText((CharSequence) rates.get(position).Nominal);
//        holder.txtName.setText((CharSequence) rates.get(position).ValName);        
/*        
        // Изменение иконки для Windows и iPhone
        String s = names[position];
        if (s.startsWith("Windows7") || s.startsWith("iPhone")
                || s.startsWith("Solaris")) {

            holder.imageView.setImageResource(R.drawable.no);
        } else {
            holder.imageView.setImageResource(R.drawable.ok);
        }
*/
        return rowView;
    }
}
