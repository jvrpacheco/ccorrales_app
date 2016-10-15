package com.corporacioncorrales.cotizacionesapp.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.corporacioncorrales.cotizacionesapp.R;
import com.corporacioncorrales.cotizacionesapp.fragments.ProductsFragment;
import com.corporacioncorrales.cotizacionesapp.model.ProductsResponse;
import com.corporacioncorrales.cotizacionesapp.networking.ProductsApi;
import com.corporacioncorrales.cotizacionesapp.utils.Common;
import com.corporacioncorrales.cotizacionesapp.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by victor on 8/20/16.
 */
public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {

    ArrayList<ProductsResponse> productsList;
    ArrayList<ProductsResponse> productsSelectedList;
    QuotationAdapter quotationAdapter;
    Context mContext;
    private ProgressBar progressBar;

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
    public void onBindViewHolder(final ProductsViewHolder holder, final int position) {

        final ProductsResponse product = productsList.get(position);

        holder.tvId.setText(product.getId());
        holder.tvCantidad.setText("Stock: " + product.getCantidad());

        //*************************
        if(product.getNuevoPrecio()==null) {
            product.setNuevoPrecio(product.getPrecio());
        }

        if(product.getCantidadSolicitada()==null) {
            /*if(Integer.parseInt(product.getCantidad()) > 0) {
                product.setCantidadSolicitada("1");
            } else {
                product.setCantidadSolicitada("0");
            }*/
            product.setCantidadSolicitada("0");
        }
        //*************************

        if(!product.getFoto().isEmpty()) {
            Picasso.with(mContext)
                    .load(product.getFoto())
                    .placeholder(R.drawable.marca2)
                    .error(R.drawable.marca2)
                    .centerInside()
                    .fit()
                    .into(holder.ivProduct);
        } else {
            holder.ivProduct.setImageResource(R.drawable.marca2);
        }

        if(product.getSelected()) {
            holder.ivCheck.setVisibility(View.VISIBLE);
            //quotationAdapter.addItem(quotationAdapter.getItemCount(), product);
        } else {
            holder.ivCheck.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(product.getSelected()) {
                    product.setSelected(false);
                    holder.ivCheck.setVisibility(View.GONE);
                    productsSelectedList.remove(product);
                    quotationAdapter.removeItem(product);
                } else {
                    product.setSelected(true);
                    holder.ivCheck.setVisibility(View.VISIBLE);
                    productsSelectedList.add(product);
                    //quotationAdapter.addItem(0, product);
                    quotationAdapter.addItem(quotationAdapter.getItemCount(), product);
                }

            Log.d(Constants.log_arrow, String.valueOf(productsSelectedList.size()));
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showZoomProductImage(product);
                return true; // si esta en false, luego de long click ejecutara onclick
            }
        });

    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public ArrayList<ProductsResponse> getProductsList() {
        return productsList;
    }

    public static class ProductsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvId, tvCantidad;
        ImageView ivProduct, ivCheck;
        Context ctx;

        public ProductsViewHolder(View view) {
            super(view);
            ivProduct = (ImageView)view.findViewById(R.id.iv_Product);
            tvId = (TextView)view.findViewById(R.id.tv_id);
            tvCantidad = (TextView)view.findViewById(R.id.tv_cantidad);
            ivCheck = (ImageView) view.findViewById(R.id.ivCheck);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public void refreshItem(ProductsResponse product, Boolean willCheck) {

        int index = -1;
        if(productsList.contains(product)) {
            index = productsList.indexOf(product);

            if(index != -1) {
                //notifyItemChanged(index, product); //when remove from right the last element, this dissapear from left list
                notifyDataSetChanged();
            }
        }
    }

    private void showZoomProductImage(ProductsResponse product) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_product_zoom);

        final ImageView ivClose = (ImageView)dialog.findViewById(R.id.ivClose);
        final ImageView ivProductZoom = (ImageView)dialog.findViewById(R.id.ivProductZoom);
        final TextView tvDescripcion = (TextView)dialog.findViewById(R.id.tvDescripcionDialog);
        final TextView tvZoomDialogTitle = (TextView)dialog.findViewById(R.id.tvZoomDialogTitle);
        progressBar = (ProgressBar)dialog.findViewById(R.id.newProgressBar);

        tvZoomDialogTitle.setText(product.getId());
        tvDescripcion.setText(product.getNombre());
        if(Common.isOnline(mContext)) {
            getZoomProductImage(product.getId(), ivProductZoom);
        }

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void getZoomProductImage(String idProduct, final ImageView imageView) {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.url_server)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProductsApi request = retrofit.create(ProductsApi.class);
        Call<ProductsResponse> call = request.getProductImageZoom(idProduct);  //product.getId()   //00000172

        call.enqueue(new Callback<ProductsResponse>() {
            @Override
            public void onResponse(Call<ProductsResponse> call, Response<ProductsResponse> response) {
                if(response != null) {
                    ProductsResponse product = response.body();

                    if(!product.getFoto().isEmpty()) {
                        Picasso.with(mContext)
                                .load(product.getFoto())
                                .placeholder(R.drawable.marca3_grande1)
                                .error(R.drawable.marca3_grande1)
                                .centerInside()
                                .fit()
                                .into(imageView);
                    } else {
                        imageView.setImageResource(R.drawable.marca3_grande1);
                    }
                    progressBar.setVisibility(View.GONE);

                } else {
                    Log.d(Constants.log_arrow_response, "response null");
                    Common.showToastMessage(mContext, "Error en el servidor");
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ProductsResponse> call, Throwable t) {
                Log.d(Constants.log_arrow_failure, t.toString());
                progressBar.setVisibility(View.GONE);
                Common.showToastMessage(mContext, "Error en el servidor");
                //Common.showToastMessage(mContext, t.getMessage());
            }
        });

    }

}
