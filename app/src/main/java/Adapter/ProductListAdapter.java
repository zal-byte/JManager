package Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.zadev.jmanage.R;

import java.util.ArrayList;

import Client.Client;
import Model.Product;
import SharedPref.UserSession;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {
    Activity activity;
    ArrayList<Product> data;
    Client client;
    UserSession userSession;

    public ProductListAdapter(Activity activity, ArrayList<Product> data) {
        this.activity = activity;
        this.data = data;
        this.client = new Client();
        this.userSession = new UserSession(activity);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.row_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder h, int i) {

        if (data != null) {
            if (data.size() > 0) {
                h.ProdQuantity_view.setText(data.get(i).ProdQuantity);
                h.ProdName_view.setText(data.get(i).ProdName);
                h.ProdLove_view.setText(data.get(i).ProdLove);
                h.ProdComm_view.setText(data.get(i).ProdComm);
                h.ProdPrice_view.setText(data.get(i).ProdPrice);
                Picasso.get().load(client.imgProdSrc + data.get(i).ProdPict).into(h.ProdPict_view);

                h.card_row_product.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog dialog = new Dialog(activity, android.R.style.Theme_Material_Dialog_Alert);
                        dialog.setContentView(R.layout.view_product);
                        dialog.setCanceledOnTouchOutside(false);

                        FloatingActionButton fab_delete = dialog.findViewById(R.id.fab_delete);
                        FloatingActionButton fab_edit = dialog.findViewById(R.id.fab_edit);

                        dialog.show();
                    }
                });
            } else {
                Toast.makeText(activity, "Tidak ada produk", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(activity, "Tidak ada produk", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ProdPict_view;
        TextView ProdName_view, ProdPrice_view, ProdQuantity_view, ProdLove_view, ProdComm_view;
        CardView card_row_product;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ProdPict_view = (ImageView) itemView.findViewById(R.id.ProdPict_view);
            ProdName_view = (TextView) itemView.findViewById(R.id.ProdName_view);
            ProdPrice_view = (TextView) itemView.findViewById(R.id.ProdPrice_view);
            ProdLove_view = (TextView) itemView.findViewById(R.id.ProdLove_view);
            ProdComm_view = (TextView) itemView.findViewById(R.id.ProdComm_view);
            ProdQuantity_view = (TextView) itemView.findViewById(R.id.ProdQuantity_view);
            card_row_product = (CardView) itemView.findViewById(R.id.card_row_product);
        }
    }
}
