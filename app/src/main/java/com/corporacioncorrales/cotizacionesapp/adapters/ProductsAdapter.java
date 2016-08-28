package com.corporacioncorrales.cotizacionesapp.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.corporacioncorrales.cotizacionesapp.R;
import com.corporacioncorrales.cotizacionesapp.fragments.ProductsFragment;
import com.corporacioncorrales.cotizacionesapp.model.ProductsResponse;
import com.corporacioncorrales.cotizacionesapp.utils.Common;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by victor on 8/20/16.
 */
public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {

    ArrayList<ProductsResponse> productsList;
    ArrayList<ProductsResponse> productsSelectedList;
    RecyclerView rvQuotation;
    QuotationAdapter quotationAdapter;
    Context mContext;
    CheckBox chb;

    public ProductsAdapter(Context mContext, ArrayList<ProductsResponse> productsList, QuotationAdapter quotationAdapter) {
        this.mContext = mContext;
        this.productsList = productsList;
        this.quotationAdapter = quotationAdapter;
        productsSelectedList = new ArrayList<>();
    }

    @Override
    public ProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card_row_item, parent, false);
        ProductsViewHolder productsViewHolder = new ProductsViewHolder(view);
        return productsViewHolder;
    }

    @Override
    public void onBindViewHolder(final ProductsViewHolder holder, int position) {
        final ProductsResponse product = productsList.get(position);
        holder.tvId.setText(product.getId());
        holder.tvCantidad.setText("Stock: " + product.getCantidad());

        if(!product.getFoto().isEmpty()) {
            Picasso.with(mContext)
                    .load(product.getFoto())
                    .placeholder(R.drawable.package_96_gray)
                    .error(R.drawable.package_96_gray)
                    .centerInside()
                    .fit()
                    .into(holder.ivProduct);
        } else {
            holder.ivProduct.setImageResource(R.drawable.package_96_gray);
        }

        //chb = holder.chbAddProduct;
        holder.chbAddProduct.setEnabled(false);
        holder.chbAddProduct.setChecked(product.getSelected());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Common.showToastMessage(mContext, "Articulo > " + product.getNombre());

                if(product.getSelected()) {
                    product.setSelected(false);
                    holder.chbAddProduct.setChecked(false);
                    productsSelectedList.remove(product);

                    quotationAdapter.removeItem(product);

                } else {
                    product.setSelected(true);
                    holder.chbAddProduct.setChecked(true);
                    productsSelectedList.add(product);

                    quotationAdapter.addItem(0, product);
                }

                //quotationAdapter.refreshQuotation(productsSelectedList);
                //quotationAdapter.addItem(productsSelectedList.size()-1, product);

                Log.d(mContext.getResources().getString(R.string.log_arrow), String.valueOf(productsSelectedList.size()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public static class ProductsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvId, tvNombre, tvPrecio, tvCantidad;
        ImageView ivProduct;
        CheckBox chbAddProduct;
        Context ctx;

        public ProductsViewHolder(View view) {
            super(view);
            ivProduct = (ImageView)view.findViewById(R.id.iv_Product);
            tvId = (TextView)view.findViewById(R.id.tv_id);
            tvCantidad = (TextView)view.findViewById(R.id.tv_cantidad);
            chbAddProduct = (CheckBox) view.findViewById(R.id.checkBox);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public void refreshItem(ProductsResponse product, Boolean willCheck) {
        //refrescar producto de la lista
        //quitar check de producto
        //chb.setChecked(willCheck);
        int index = Arrays.asList(productsList).indexOf(product);
        notifyItemChanged(index);
    }

    private void showCustomizeDialog(final Context context, final ProductsResponse productSelected, final CheckBox checkBox) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_product_to_quotation);

        Button btnAccept1 = (Button)dialog.findViewById(R.id.btnAccept);
        btnAccept1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productSelected.setSelected(true);
                checkBox.setChecked(true);

                //checkBox.setChecked(productSelected.getSelected());

                productsSelectedList.add(productSelected);
                ProductsFragment.productsSelectedList.add(productSelected);
                Log.v(mContext.getString(R.string.log_arrow) + "CHECKED", productsSelectedList.toString());
                Common.showToastMessage(context, "Productos en Cotizacion: " + productsSelectedList.toString());
                dialog.dismiss();
            }
        });
        Button btnCloseTinDIalog = (Button)dialog.findViewById(R.id.btnCloseTinDIalog);
        btnCloseTinDIalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productSelected.setSelected(false);
                checkBox.setChecked(false);

                if(productsSelectedList.contains(productSelected)) {
                    productsSelectedList.remove(productSelected);
                    ProductsFragment.productsSelectedList.remove(productSelected);
                }

                dialog.dismiss();
            }
        });
        Button btnCancel1 = (Button)dialog.findViewById(R.id.btnClose);
        btnCancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productSelected.setSelected(false);
                checkBox.setChecked(false);

                if(productsSelectedList.contains(productSelected)) {
                    productsSelectedList.remove(productSelected);
                    ProductsFragment.productsSelectedList.remove(productSelected);
                }

                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

}
